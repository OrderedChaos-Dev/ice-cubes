package dev.orderedchaos.icecubes;

import com.mojang.logging.LogUtils;
import dev.orderedchaos.icecubes.core.IceCubesConfig;
import dev.orderedchaos.icecubes.core.registry.*;
import dev.orderedchaos.icecubes.data.IceCubeTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.component.DataComponentPredicate;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import net.neoforged.neoforge.event.village.WandererTradesEvent;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Mod(IceCubes.MOD_ID)
public class IceCubes {
  public static final String MOD_ID = "icecubes";
  public static final Logger LOGGER = LogUtils.getLogger();

  public IceCubes(IEventBus modEventBus, ModContainer modContainer) {
    modEventBus.addListener(this::commonSetup);
    EntityRegistry.ENTITY_TYPES.register(modEventBus);
    ItemRegistry.ITEMS.register(modEventBus);
    EnchantmentRegistry.ENCHANT_EFFECTS.register(modEventBus);
    MobEffectRegistry.MOB_EFFECTS.register(modEventBus);
    PotionRegistry.POTIONS.register(modEventBus);
    RecipeSerializerRegistry.RECIPE_SERIALIZERS.register(modEventBus);
    ParticleTypeRegistry.PARTICLE_TYPES.register(modEventBus);

    modEventBus.addListener(this::addCreative);
    modContainer.registerConfig(ModConfig.Type.COMMON, IceCubesConfig.SPEC);

    NeoForge.EVENT_BUS.register(this);
  }

  private void commonSetup(final FMLCommonSetupEvent event) {

  }

  private void addCreative(BuildCreativeModeTabContentsEvent event) {
    if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
      event.accept(ItemRegistry.ICE_CUBE_SPAWN_EGG);
    } else if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
      event.accept(ItemRegistry.FROST_ESSENCE);
    }
  }

  @SubscribeEvent
  private void addVillagerTrades(VillagerTradesEvent event) {
    if (IceCubesConfig.enableVillagerTrades.get()) {
      final int JOURNEYMAN = 3;
      if (event.getType() == VillagerProfession.CLERIC) {
//      event.getTrades().get(JOURNEYMAN).add(new BasicItemListing(new ItemStack(ItemRegistry.FROST_ESSENCE.get(), 1), new ItemStack(Items.EMERALD, 2), 7, 15, 1));
        event.getTrades().get(JOURNEYMAN).add(new VillagerTrades.ItemListing() {
          @Nullable
          @Override
          public MerchantOffer getOffer(Entity pTrader, RandomSource pRandom) {
            ItemStack stack = new ItemStack(ItemRegistry.FROST_ESSENCE.get(), 1);
            ItemCost cost = new ItemCost(stack.getItemHolder(), stack.getCount(), DataComponentPredicate.EMPTY, stack);
            return new MerchantOffer(cost, Optional.empty(), new ItemStack(Items.EMERALD, 2), 6, 15, 1);
          }
        });
      }
    }
  }

  @SubscribeEvent
  private void addWandererTrades(WandererTradesEvent event) {
    if (IceCubesConfig.enableWanderingTrades.get()) {
      //    event.getGenericTrades().add(1, new BasicItemListing(7, new ItemStack(ItemRegistry.FROST_ESSENCE.get(), 1), 2, 3));
      event.getGenericTrades().add(new VillagerTrades.ItemListing() {
        @Nullable
        @Override
        public MerchantOffer getOffer(Entity pTrader, RandomSource pRandom) {
          ItemStack stack = new ItemStack(Items.EMERALD, 7);
          ItemCost cost = new ItemCost(stack.getItemHolder(), stack.getCount(), DataComponentPredicate.EMPTY, stack);
          return new MerchantOffer(cost, Optional.empty(), new ItemStack(ItemRegistry.FROST_ESSENCE.get(), 1), 2, 3, 1);
        }
      });
    }
  }

  @EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD)
  public static class Data {
    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
        .add(Registries.ENCHANTMENT, EnchantmentRegistry::bootstrap);

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
      DataGenerator generator = event.getGenerator();
      PackOutput packOutput = event.getGenerator().getPackOutput();
      CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
      ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

      generator.addProvider(event.includeServer(), new DatapackBuiltinEntriesProvider(packOutput, lookupProvider, BUILDER, Set.of(IceCubes.MOD_ID)));
      generator.addProvider(event.includeServer(), new IceCubeTags.IceCubesBiomeTagsProvider(packOutput, lookupProvider, existingFileHelper));
    }
  }
}
