package dev.orderedchaos.icecubes.common.mobeffects;

import dev.orderedchaos.icecubes.core.registry.MobEffectRegistry;
import dev.orderedchaos.icecubes.core.registry.ParticleTypeRegistry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Skeleton;

public class ChilledEffect extends MobEffect  {

  public ChilledEffect(int pColor) {
    super(MobEffectCategory.HARMFUL, pColor);
  }

  @Override
  public ParticleOptions createParticleOptions(MobEffectInstance pEffect) {
    return ParticleTypeRegistry.SNOWFLAKE.get();
  }

  @Override
  public boolean shouldApplyEffectTickThisTick(int pDuration, int pAmplifier) {
    return true;
  }

  @Override
  public boolean applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
    if (pLivingEntity.isOnFire() || (!pLivingEntity.canFreeze() && pLivingEntity.getType() != EntityType.SKELETON)) {
      pLivingEntity.removeEffect(MobEffectRegistry.CHILLED);
    }
    if (pAmplifier >= 2) {
      pLivingEntity.hurt(pLivingEntity.damageSources().freeze(), 3.0F);
      if (pLivingEntity.level() instanceof ServerLevel serverLevel) {
        pLivingEntity.setTicksFrozen(300);
        pLivingEntity.removeEffect(MobEffectRegistry.CHILLED);

        float f = pLivingEntity.getDimensions(pLivingEntity.getPose()).width() * 2.0F;
        float f1 = f / 2.0F;

        for (int i = 0; (float)i < f * 64.0F; i++) {
          float f2 = pLivingEntity.getRandom().nextFloat() * (float) (Math.PI * 2);
          float f3 = pLivingEntity.getRandom().nextFloat() * 0.5F + 0.5F;
          float f4 = Mth.sin(f2) * f1 * f3;
          float f5 = Mth.cos(f2) * f1 * f3;
          float yOffset = pLivingEntity.getRandom().nextFloat() * pLivingEntity.getDimensions(pLivingEntity.getPose()).height();
          serverLevel.sendParticles(ParticleTypeRegistry.SNOWFLAKE.get(), pLivingEntity.getX() + (double)f4, pLivingEntity.getY() + yOffset, pLivingEntity.getZ() + (double)f5, 1, 0.0, 0.0, 0.0, 0);
        }
      }
    } else {
      if (!pLivingEntity.level().isClientSide() && (pLivingEntity.canFreeze() || pLivingEntity.getType() == EntityType.SKELETON)) {
        int i = pLivingEntity.getTicksFrozen();
        pLivingEntity.setIsInPowderSnow(true);
        pLivingEntity.setTicksFrozen(Math.min(pLivingEntity.getTicksRequiredToFreeze(), i + 1));
      }
    }
    return true;
  }
}
