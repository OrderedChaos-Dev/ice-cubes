package dev.orderedchaos.icecubes.common.enchantments;

import com.mojang.serialization.MapCodec;
import dev.orderedchaos.icecubes.core.registry.MobEffectRegistry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;

public record Chill() implements EnchantmentEntityEffect {

  public static final Chill INSTANCE = new Chill();
  public static final MapCodec<Chill> CODEC = MapCodec.unit(() -> INSTANCE);

  @Override
  public void apply(ServerLevel level, int amplifier, EnchantedItemInUse enchantedItemInUse, Entity entity, Vec3 vec3) {
    if (entity instanceof LivingEntity && entity.canFreeze()) {
      MobEffectInstance instance = ((LivingEntity) entity).getEffect(MobEffectRegistry.CHILLED);
      int levelToApply = instance == null ? 0 : instance.getAmplifier() + 1;
      ((LivingEntity) entity).addEffect(new MobEffectInstance(MobEffectRegistry.CHILLED, 200, levelToApply));
    }
  }

  @Override
  public MapCodec<? extends EnchantmentEntityEffect> codec() {
    return CODEC;
  }
}
