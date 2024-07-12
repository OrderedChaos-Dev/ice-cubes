package dev.orderedchaos.icecubes.client;

import dev.orderedchaos.icecubes.client.particles.SnowflakeParticle;
import dev.orderedchaos.icecubes.IceCubes;
import dev.orderedchaos.icecubes.client.renderers.IceCubeRenderer;
import dev.orderedchaos.icecubes.core.registry.EntityRegistry;
import dev.orderedchaos.icecubes.core.registry.ParticleTypeRegistry;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

@EventBusSubscriber(modid = IceCubes.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class ClientEvents {

  @SubscribeEvent
  public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
    event.registerEntityRenderer(EntityRegistry.ICE_CUBE.get(), IceCubeRenderer::new);
  }

  @SubscribeEvent
  public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
    event.registerSpriteSet(ParticleTypeRegistry.SNOWFLAKE.get(), SnowflakeParticle.Provider::new);
  }
}
