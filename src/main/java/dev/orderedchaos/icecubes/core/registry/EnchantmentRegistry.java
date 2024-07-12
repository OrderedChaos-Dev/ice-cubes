package dev.orderedchaos.icecubes.core.registry;

import com.mojang.serialization.MapCodec;
import dev.orderedchaos.icecubes.common.enchantments.Chill;
import dev.orderedchaos.icecubes.IceCubes;
import net.minecraft.advancements.critereon.DamageSourcePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.EntityTypePredicate;
import net.minecraft.advancements.critereon.TagPredicate;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentTarget;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.*;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.DamageSourceCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.EnchantmentLevelProvider;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class EnchantmentRegistry {

  public static final DeferredRegister<MapCodec<? extends EnchantmentEntityEffect>> ENCHANT_EFFECTS = DeferredRegister.create(Registries.ENCHANTMENT_ENTITY_EFFECT_TYPE, IceCubes.MOD_ID);

  public static final DeferredHolder<?, ?> CHILL = ENCHANT_EFFECTS.register("chill", () -> Chill.CODEC);

  public static final ResourceKey<Enchantment> ICE_ASPECT = key("ice_aspect");
  public static final ResourceKey<Enchantment> EVERFROST = key("everfrost");

  public static void bootstrap(BootstrapContext<Enchantment> context) {
    HolderGetter<Item> itemHolderGetter = context.lookup(Registries.ITEM);

    register(
      context,
      ICE_ASPECT,
      Enchantment.enchantment(
        Enchantment.definition(
          itemHolderGetter.getOrThrow(ItemTags.SWORD_ENCHANTABLE),
          itemHolderGetter.getOrThrow(ItemTags.SWORD_ENCHANTABLE),
          2,
          1,
          Enchantment.dynamicCost(10, 20),
          Enchantment.dynamicCost(60, 20),
          4,
          EquipmentSlotGroup.MAINHAND
        )
      )
      .withEffect(
        EnchantmentEffectComponents.POST_ATTACK,
        EnchantmentTarget.ATTACKER,
        EnchantmentTarget.VICTIM,
        new Chill(),
        DamageSourceCondition.hasDamageSource(DamageSourcePredicate.Builder.damageType().isDirect(true))
      )
      .withEffect(
        EnchantmentEffectComponents.DAMAGE,
        new AddValue(LevelBasedValue.perLevel(2.0F)),
        LootItemEntityPropertyCondition.hasProperties(
          LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().entityType(EntityTypePredicate.of(EntityTypeTags.FREEZE_HURTS_EXTRA_TYPES))
        )
      )
    );

    register(
      context,
      EVERFROST,
        Enchantment.enchantment(
          Enchantment.definition(
            itemHolderGetter.getOrThrow(ItemTags.ARMOR_ENCHANTABLE),
            itemHolderGetter.getOrThrow(ItemTags.CHEST_ARMOR_ENCHANTABLE),
            1,
            1,
            Enchantment.dynamicCost(10, 20),
            Enchantment.dynamicCost(60, 20),
            6,
            EquipmentSlotGroup.ANY
          )
        )
        .withEffect(
          EnchantmentEffectComponents.POST_ATTACK,
          EnchantmentTarget.VICTIM,
          EnchantmentTarget.ATTACKER,
          AllOf.entityEffects(
            new Chill(),
            new DamageItem(LevelBasedValue.constant(2.0F))
          ),
          LootItemRandomChanceCondition.randomChance(EnchantmentLevelProvider.forEnchantmentLevel(LevelBasedValue.perLevel(1.0F)))
        )
        .withEffect(
          EnchantmentEffectComponents.DAMAGE_IMMUNITY,
          DamageImmunity.INSTANCE,
          DamageSourceCondition.hasDamageSource(
            DamageSourcePredicate.Builder.damageType()
              .tag(TagPredicate.is(DamageTypeTags.IS_FREEZING))
              .tag(TagPredicate.isNot(DamageTypeTags.BYPASSES_INVULNERABILITY))
          )
        )
    );
  }

  private static void register(BootstrapContext<Enchantment> context, ResourceKey<Enchantment> key, Enchantment.Builder builder) {
    context.register(key, builder.build(key.location()));
  }

  private static ResourceKey<Enchantment> key(String name) {
    return ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(IceCubes.MOD_ID, name));
  }
}
