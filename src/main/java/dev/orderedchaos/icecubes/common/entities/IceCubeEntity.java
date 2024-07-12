package dev.orderedchaos.icecubes.common.entities;

import dev.orderedchaos.icecubes.core.IceCubesConfig;
import dev.orderedchaos.icecubes.core.registry.MobEffectRegistry;
import dev.orderedchaos.icecubes.core.registry.ParticleTypeRegistry;
import dev.orderedchaos.icecubes.data.IceCubeTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.MagmaCube;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.WorldgenRandom;

import javax.annotation.Nullable;

public class IceCubeEntity extends Slime {

  public static long ICE_CUBE_CHUNK_SALT = 274242277188L;

  public IceCubeEntity(EntityType<? extends Slime> pEntityType, Level pLevel) {
    super(pEntityType, pLevel);
  }

  public static AttributeSupplier.Builder createAttributes() {
    return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.2F);
  }

  public static boolean checkIceCubeSpawnRules(EntityType<IceCubeEntity> iceCube, LevelAccessor pLevel, MobSpawnType pSpawnType, BlockPos pPos, RandomSource pRandom) {
    if (MobSpawnType.isSpawner(pSpawnType)) {
      return checkMobSpawnRules(iceCube, pLevel, pSpawnType, pPos, pRandom);
    } else {
      if (pLevel.getDifficulty() != Difficulty.PEACEFUL) {
        if (pSpawnType == MobSpawnType.SPAWNER) {
          return checkMobSpawnRules(iceCube, pLevel, pSpawnType, pPos, pRandom);
        }

        if ((pLevel.getBiome(pPos).is(IceCubeTags.ALLOWS_SURFACE_ICE_CUBE_SPAWNS) || IceCubesConfig.enableNormalizeSpawns.get())
            && pPos.getY() > 50
            && pRandom.nextFloat() < 0.5F
            && pRandom.nextFloat() < pLevel.getMoonBrightness()
            && pLevel.getMaxLocalRawBrightness(pPos) <= pRandom.nextInt(8)) {
          return checkMobSpawnRules(iceCube, pLevel, pSpawnType, pPos, pRandom);
        }

        if (!(pLevel instanceof WorldGenLevel)) {
          return false;
        }

        if (!IceCubesConfig.enableNormalizeSpawns.get()) {
          ChunkPos chunkpos = new ChunkPos(pPos);
          boolean flag = WorldgenRandom.seedSlimeChunk(chunkpos.x, chunkpos.z, ((WorldGenLevel)pLevel).getSeed(), ICE_CUBE_CHUNK_SALT).nextInt(10) == 0;
          if (flag) {
            if (pRandom.nextInt(10) == 0 && flag && pPos.getY() <= 60) {
              return checkMobSpawnRules(iceCube, pLevel, pSpawnType, pPos, pRandom);
            } else if (pPos.getY() > 60) {
              return checkMobSpawnRules(iceCube, pLevel, pSpawnType, pPos, pRandom);
            }
          }
        }
      }

      return false;
    }
  }

  @Override
  public void tick() {
    super.tick();

    if (this.isMelting()) {
      if (this.level().getGameTime() % 10 == 0) {
        this.spawnMeltingParticles();
      }
      if (this.level().getGameTime() % 120 == 0) {
        int size = this.getSize();
        if (size == 1) {
          this.kill();
        } else {
          this.setSize(size - 1, false);
        }

        if (IceCubesConfig.enableMeltingWater.get()) {
          if (!net.neoforged.neoforge.event.EventHooks.canEntityGrief(this.level(), this)) {
            return;
          }

          BlockState blockstate = Blocks.WATER.defaultBlockState().setValue(LiquidBlock.LEVEL, 6);
          for (int i = 0; i < 1; i++) {
            int j = Mth.floor(this.getX() + (double)((float)(i % 2 * 2 - 1) * 0.25F));
            int k = Mth.floor(this.getY());
            int l = Mth.floor(this.getZ() + (double)((float)(i / 2 % 2 * 2 - 1) * 0.25F));
            BlockPos blockpos = new BlockPos(j, k, l);
            if (this.level().getBlockState(blockpos).isAir() && !this.level().getBlockState(blockpos.below()).isAir()) {
              this.level().setBlockAndUpdate(blockpos, blockstate);
              this.level().gameEvent(GameEvent.BLOCK_PLACE, blockpos, GameEvent.Context.of(this, blockstate));
            }
          }
        }
      }
    }
  }

  private void spawnMeltingParticles() {
    float f = this.getDimensions(this.getPose()).width() * 2.0F;
    float f1 = f / 2.0F;

    for (int i = 0; (float)i < f * 8.0F; i++) {
      float f2 = this.random.nextFloat() * (float) (Math.PI * 2);
      float f3 = this.random.nextFloat() * 0.5F + 0.5F;
      float f4 = Mth.sin(f2) * f1 * f3;
      float f5 = Mth.cos(f2) * f1 * f3;
      float yOffset = this.random.nextFloat() * this.getDimensions(this.getPose()).height();
      this.level().addParticle(ParticleTypes.FALLING_WATER, this.getX() + (double)f4, this.getY() + yOffset, this.getZ() + (double)f5, 0.0, 0.0, 0.0);
    }
  }

  @Nullable
  @Override
  public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pSpawnType, @Nullable SpawnGroupData pSpawnGroupData) {
    SpawnGroupData spawnGroupData = super.finalizeSpawn(pLevel, pDifficulty, pSpawnType, pSpawnGroupData);

    if (this.random.nextInt(1000) == 0) {
      this.setSize(15, true);
    }

    return spawnGroupData;
  }

  @Override
  protected void registerGoals() {
    super.registerGoals();
    this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, MagmaCube.class, true));
  }

  @Override
  public void setSize(int pSize, boolean pResetHealth) {
    super.setSize(pSize, pResetHealth);
    this.getAttribute(Attributes.ARMOR).setBaseValue((double) (pSize * 2));
  }

  @Override
  public void push(Entity pEntity) {
    super.push(pEntity);
    if (pEntity instanceof MagmaCube && this.isDealsDamage()) {
      this.dealDamage((LivingEntity) pEntity);
    }
  }

  @Override
  public boolean canFreeze() {
    return false;
  }

  @Override
  protected void dealDamage(LivingEntity pLivingEntity) {
    if (this.isAlive() && this.isWithinMeleeAttackRange(pLivingEntity) && this.hasLineOfSight(pLivingEntity)) {
      DamageSource damagesource = this.damageSources().mobAttack(this);
      if (pLivingEntity.hurt(damagesource, this.getAttackDamage())) {
        if (this.random.nextBoolean() && IceCubesConfig.enableIceCubeChillEffect.get()) {
          MobEffectInstance instance = pLivingEntity.getEffect(MobEffectRegistry.CHILLED);
          int levelToApply = instance == null ? 0 : instance.getAmplifier() + 1;
          pLivingEntity.addEffect(new MobEffectInstance(MobEffectRegistry.CHILLED, 200, levelToApply));
        }
        this.playSound(SoundEvents.GLASS_PLACE, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
        if (this.level() instanceof ServerLevel serverlevel) {
          EnchantmentHelper.doPostAttackEffects(serverlevel, pLivingEntity, damagesource);
        }
      }
    }
  }

  public boolean isMelting() {
    if (!IceCubesConfig.enableIceCubeMelting.get()) {
      return false;
    }
    Holder<Biome> biome = this.level().getBiome(this.blockPosition());
    return biome.is(IceCubeTags.ICE_CUBE_MELTS);
  }

  @Override
  protected boolean isDealsDamage() {
    return this.isEffectiveAi();
  }

  @Override
  protected int getJumpDelay() {
    return super.getJumpDelay() * 2;
  }

  @Override
  protected void decreaseSquish() {
    this.targetSquish *= 0;
  }

  @Override
  protected ParticleOptions getParticleType() {
    if (this.isOnFire() || this.isMelting()) {
      return ParticleTypes.SPLASH;
    }
    return ParticleTypeRegistry.SNOWFLAKE.get();
  }

  @Override
  protected SoundEvent getHurtSound(DamageSource pDamageSource) {
    return SoundEvents.GLASS_HIT;
  }

  @Override
  protected SoundEvent getDeathSound() {
    return SoundEvents.GLASS_BREAK;
  }

  @Override
  protected SoundEvent getSquishSound() {
    return SoundEvents.GLASS_STEP;
  }

  @Override
  protected SoundEvent getJumpSound() {
    return SoundEvents.GLASS_STEP;
  }
}
