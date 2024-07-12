package dev.orderedchaos.icecubes.core.registry;

import dev.orderedchaos.icecubes.IceCubes;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class PotionRegistry {

  public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(Registries.POTION, IceCubes.MOD_ID);

  public static final DeferredHolder<Potion, Potion> CHILLING = POTIONS.register("chilling", () -> potion(MobEffectRegistry.CHILLED, 400));
  public static final DeferredHolder<Potion, Potion> LONG_CHILLING = POTIONS.register("long_chilling", () -> potion(MobEffectRegistry.CHILLED, 600));

  private static Potion potion(Holder<MobEffect> effect, int duration) {
    return new Potion(new MobEffectInstance(effect, duration));
  }
}
