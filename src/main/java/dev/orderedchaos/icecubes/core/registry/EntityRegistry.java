package dev.orderedchaos.icecubes.core.registry;

import dev.orderedchaos.icecubes.IceCubes;
import dev.orderedchaos.icecubes.common.entities.IceCubeEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@EventBusSubscriber(modid = IceCubes.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class EntityRegistry {
  public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, IceCubes.MOD_ID);

  public static final DeferredHolder<EntityType<?>, EntityType<IceCubeEntity>> ICE_CUBE = ENTITY_TYPES.register(
      "ice_cube", () -> registerEntity(EntityType.Builder.of(IceCubeEntity::new, MobCategory.MONSTER).sized(0.52F, 0.52F).immuneTo(Blocks.POWDER_SNOW), "ice_cube"));

  public static <T extends LivingEntity> EntityType<T> registerEntity(EntityType.Builder<T> builder, String name) {
    return builder.build(name);
  }

  @SubscribeEvent
  public static void registerAttributes(EntityAttributeCreationEvent event) {
    event.put(ICE_CUBE.get(), IceCubeEntity.createAttributes().build());
  }

  @SubscribeEvent
  public static void registerAttributes(RegisterSpawnPlacementsEvent event) {
    event.register(ICE_CUBE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, IceCubeEntity::checkIceCubeSpawnRules, RegisterSpawnPlacementsEvent.Operation.OR);
  }
}
