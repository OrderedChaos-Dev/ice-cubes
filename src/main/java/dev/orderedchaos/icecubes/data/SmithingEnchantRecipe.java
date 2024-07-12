package dev.orderedchaos.icecubes.data;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.orderedchaos.icecubes.core.registry.RecipeSerializerRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SmithingRecipe;
import net.minecraft.world.item.crafting.SmithingRecipeInput;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;

import java.util.stream.Stream;

public class SmithingEnchantRecipe implements SmithingRecipe {
  final Ingredient base;
  final Ingredient addition;
  final Holder<Enchantment> enchantment;

  public SmithingEnchantRecipe(Ingredient pBase, Ingredient pAddition, Holder<Enchantment> enchantment) {
    this.base = pBase;
    this.addition = pAddition;
    this.enchantment = enchantment;
  }

  @Override
  public boolean matches(SmithingRecipeInput input, Level level) {
    return this.base.test(input.base()) && this.addition.test(input.addition());
  }

  @Override
  public ItemStack assemble(SmithingRecipeInput input, HolderLookup.Provider lookupProvider) {
    ItemStack itemStack = input.base().copy();
    itemStack.enchant(enchantment, 1);
    return itemStack;
  }

  @Override
  public ItemStack getResultItem(HolderLookup.Provider pRegistries) {
    ItemStack itemstack = new ItemStack(Items.IRON_SWORD);
    itemstack.enchant(pRegistries.holderOrThrow(Enchantments.FIRE_ASPECT), 1);
    return itemstack;
  }

  @Override
  public boolean isTemplateIngredient(ItemStack pStack) {
    return pStack == ItemStack.EMPTY;
  }

  @Override
  public boolean isBaseIngredient(ItemStack pStack) {
    return this.base.test(pStack);
  }

  @Override
  public boolean isAdditionIngredient(ItemStack pStack) {
    return this.addition.test(pStack);
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return RecipeSerializerRegistry.SMITHING_ENCHANT.get();
  }

  @Override
  public boolean isIncomplete() {
    return Stream.of(this.base, this.addition).anyMatch(Ingredient::hasNoItems);
  }

  public static class Serializer implements RecipeSerializer<SmithingEnchantRecipe> {
    private static final MapCodec<SmithingEnchantRecipe> CODEC = RecordCodecBuilder.mapCodec(
        builder -> builder.group(
                Ingredient.CODEC.fieldOf("base").forGetter(recipe -> recipe.base),
                Ingredient.CODEC.fieldOf("addition").forGetter(recipe -> recipe.addition),
                Enchantment.CODEC.fieldOf("enchantment").forGetter(recipe -> recipe.enchantment)
            )
            .apply(builder, SmithingEnchantRecipe::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, SmithingEnchantRecipe> STREAM_CODEC = StreamCodec.of(
        SmithingEnchantRecipe.Serializer::toNetwork, SmithingEnchantRecipe.Serializer::fromNetwork
    );

    @Override
    public MapCodec<SmithingEnchantRecipe> codec() {
      return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, SmithingEnchantRecipe> streamCodec() {
      return STREAM_CODEC;
    }

    private static SmithingEnchantRecipe fromNetwork(RegistryFriendlyByteBuf byteBuf) {
      Ingredient ingredient1 = Ingredient.CONTENTS_STREAM_CODEC.decode(byteBuf);
      Ingredient ingredient2 = Ingredient.CONTENTS_STREAM_CODEC.decode(byteBuf);
      Holder<Enchantment> enchantment = Enchantment.STREAM_CODEC.decode(byteBuf);
      return new SmithingEnchantRecipe(ingredient1, ingredient2, enchantment);
    }

    private static void toNetwork(RegistryFriendlyByteBuf byteBuf, SmithingEnchantRecipe recipe) {
      Ingredient.CONTENTS_STREAM_CODEC.encode(byteBuf, recipe.base);
      Ingredient.CONTENTS_STREAM_CODEC.encode(byteBuf, recipe.addition);
      Enchantment.STREAM_CODEC.encode(byteBuf, recipe.enchantment);
    }
  }
}
