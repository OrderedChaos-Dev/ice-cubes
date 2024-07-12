package dev.orderedchaos.icecubes.core.registry;

import dev.orderedchaos.icecubes.IceCubes;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ParticleTypeRegistry {
  public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, IceCubes.MOD_ID);

  public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SNOWFLAKE = PARTICLE_TYPES.register("snowflake", () -> new SimpleParticleType(false));
}
