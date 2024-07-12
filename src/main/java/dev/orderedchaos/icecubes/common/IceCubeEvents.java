package dev.orderedchaos.icecubes.common;

import dev.orderedchaos.icecubes.common.entities.IceCubeEntity;
import dev.orderedchaos.icecubes.core.IceCubesConfig;
import dev.orderedchaos.icecubes.IceCubes;
import dev.orderedchaos.icecubes.core.registry.ItemRegistry;
import dev.orderedchaos.icecubes.core.registry.PotionRegistry;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.MagmaCube;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

@EventBusSubscriber(modid = IceCubes.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class IceCubeEvents {

  @SubscribeEvent
  public static void makeMagmaCubesAngry(EntityJoinLevelEvent event) {
    if (event.getEntity() instanceof MagmaCube magmaCube) {
      magmaCube.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(magmaCube, IceCubeEntity.class, true));
    }
  }

  @SubscribeEvent
  public static void registerBrewingRecipes(RegisterBrewingRecipesEvent event) {
    if (IceCubesConfig.enableBrewingRecipes.get()) {
      PotionBrewing.Builder builder = event.getBuilder();
      builder.addMix(Potions.AWKWARD, ItemRegistry.FROST_ESSENCE.get(), PotionRegistry.CHILLING);
      builder.addMix(PotionRegistry.CHILLING, Items.REDSTONE, PotionRegistry.LONG_CHILLING);
    }
  }
}
