package dev.orderedchaos.icecubes.data;

import dev.orderedchaos.icecubes.IceCubes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class IceCubeTags {
  public static final TagKey<Biome> ICE_CUBE_MELTS = createBiomeTag("ice_cube_melts");
  public static final TagKey<Biome> ALLOWS_SURFACE_ICE_CUBE_SPAWNS = createBiomeTag("allows_surface_ice_cube_spawns");

  private static TagKey<Biome> createBiomeTag(final String location) {
    return TagKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(IceCubes.MOD_ID, location));
  }

  public static class IceCubesBiomeTagsProvider extends BiomeTagsProvider {
      public IceCubesBiomeTagsProvider(PackOutput pOutput, CompletableFuture< HolderLookup.Provider> pProvider, @Nullable ExistingFileHelper existingFileHelper) {
      super(pOutput, pProvider, IceCubes.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
      this.tag(ICE_CUBE_MELTS).addTag(BiomeTags.SNOW_GOLEM_MELTS);
      this.tag(ALLOWS_SURFACE_ICE_CUBE_SPAWNS).add(Biomes.ICE_SPIKES, Biomes.FROZEN_PEAKS);
    }
  }
}
