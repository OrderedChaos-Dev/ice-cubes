package dev.orderedchaos.icecubes.core.registry;

import dev.orderedchaos.icecubes.IceCubes;
import dev.orderedchaos.icecubes.data.SmithingEnchantRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class RecipeSerializerRegistry {

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, IceCubes.MOD_ID);

    public static DeferredHolder<RecipeSerializer<?>, RecipeSerializer<?>> SMITHING_ENCHANT = register("smithing_enchant", SmithingEnchantRecipe.Serializer::new) ;

    private static DeferredHolder<RecipeSerializer<?>, RecipeSerializer<?>> register(String name, Supplier<RecipeSerializer<?>> supplier) {
      return RECIPE_SERIALIZERS.register(name, supplier);
    }
}
