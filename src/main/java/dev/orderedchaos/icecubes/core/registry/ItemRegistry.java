package dev.orderedchaos.icecubes.core.registry;

import dev.orderedchaos.icecubes.IceCubes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ItemRegistry {
  public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(IceCubes.MOD_ID);

  public static final DeferredItem<Item> FROST_ESSENCE = register("frost_essence", () -> new Item(new Item.Properties()));

  public static final DeferredItem<Item> ICE_CUBE_SPAWN_EGG = register(
      "ice_cube_spawn_egg", () -> egg(EntityRegistry.ICE_CUBE, 0x9eb8e8, 0xbad0f9)
  );

  public static DeferredItem<Item> register(String name, Supplier<Item> supplier) {
    return ITEMS.register(name, supplier);
  }

  private static DeferredSpawnEggItem egg(Supplier<? extends EntityType<? extends Mob>> type, int backgroundColor, int highlightColor) {
    return new DeferredSpawnEggItem(type, backgroundColor, highlightColor, new Item.Properties());
  }
}
