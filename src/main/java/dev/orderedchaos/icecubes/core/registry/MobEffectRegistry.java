package dev.orderedchaos.icecubes.core.registry;

import dev.orderedchaos.icecubes.common.mobeffects.ChilledEffect;
import dev.orderedchaos.icecubes.IceCubes;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class MobEffectRegistry {
  public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, IceCubes.MOD_ID);

  public static final DeferredHolder<MobEffect, MobEffect> CHILLED = register("chilled", () -> new ChilledEffect(0x2ce8e5));

  private static DeferredHolder<MobEffect, MobEffect> register(String name, Supplier<MobEffect> supplier) {
    return MOB_EFFECTS.register(name, supplier);
  }
}
