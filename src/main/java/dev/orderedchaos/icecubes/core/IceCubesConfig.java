package dev.orderedchaos.icecubes.core;

import dev.orderedchaos.icecubes.IceCubes;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = IceCubes.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class IceCubesConfig {
  private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

  public static ModConfigSpec.BooleanValue enableIceCubeMelting =
      BUILDER.comment("Enable ice cubes melting in hot biomes [default=true]").define("Enable Ice Cube Melting", true);
  public static ModConfigSpec.BooleanValue enableMeltingWater =
      BUILDER.comment("Enable ice cubes leaving water puddles when melting [default=true]").define("Enable Melting Water", true);
  public static ModConfigSpec.BooleanValue enableIceCubeChillEffect =
      BUILDER.comment("Enable ice cubes applying the chill effect on attack [default=true]").define("Enable Ice Cube Chill Attack", true);
  public static ModConfigSpec.BooleanValue enableNormalizeSpawns =
      BUILDER.comment("Enable ice cubes spawning in all cold biomes outside of ice cube spawning chunks [default=false]").define("Normalize Spawns", false);
  public static ModConfigSpec.BooleanValue enableVillagerTrades =
      BUILDER.comment("Enable trade to sell frost essences to cleric villagers [default=true]").define("Enable Villager Trades", true);
  public static ModConfigSpec.BooleanValue enableWanderingTrades =
      BUILDER.comment("Enable buying frost essences from wandering traders [default=true]").define("Enable Wandering Trades", true);
  public static ModConfigSpec.BooleanValue enableBrewingRecipes =
      BUILDER.comment("Enable brewing frost essences into chilling potions [default=true]").define("Enable Brewing Recipes", true);

  public static final ModConfigSpec SPEC = BUILDER.build();

  @SubscribeEvent
  static void onLoad(final ModConfigEvent event) {

  }
}
