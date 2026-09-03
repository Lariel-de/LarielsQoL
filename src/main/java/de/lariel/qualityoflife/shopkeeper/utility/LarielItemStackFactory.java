package de.lariel.qualityoflife.shopkeeper.utility;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;

public final class LarielItemStackFactory {

    private static final HolderLookup.Provider REGISTRIES =
            RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);

    private LarielItemStackFactory() {
    }

    public static ItemStack create(ResourceLocation itemId, JsonElement nbt) {
        return create(itemId, nbt, REGISTRIES);
    }

    public static ItemStack create(ResourceLocation itemId, JsonElement nbt, HolderLookup.Provider registries) {
        var item = BuiltInRegistries.ITEM.get(itemId);
        if (item == Items.AIR) {
            throw new IllegalArgumentException("Unknown item: " + itemId);
        }
        if (nbt == null || nbt.isJsonNull()) {
            return new ItemStack(item);
        }

        var tag = parseNbt(nbt);
        if (tag.contains("components", Tag.TAG_COMPOUND)) {
            var itemStackTag = tag.copy();
            itemStackTag.putString("id", itemId.toString());
            return ItemStack.parse(registries, itemStackTag)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid item stack data: " + tag));
        }

        var stack = new ItemStack(item);
        applyLegacyNbt(stack, tag, registries);
        return stack;
    }

    public static JsonElement serialize(ItemStack stack, HolderLookup.Provider registries) {
        var tag = (CompoundTag) stack.save(registries);
        tag.remove("id");
        tag.remove("count");
        return tag.isEmpty() ? null : NbtOps.INSTANCE.convertTo(JsonOps.INSTANCE, tag);
    }

    private static void applyLegacyNbt(ItemStack stack, CompoundTag tag, HolderLookup.Provider registries) {
        applyLegacyEnchantments(stack, tag, registries);
        if (!tag.isEmpty()) {
            stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        }
    }

    private static void applyLegacyEnchantments(ItemStack stack, CompoundTag tag, HolderLookup.Provider registries) {
        if (!tag.contains("Enchantments", Tag.TAG_LIST)) {
            return;
        }

        var enchantmentList = tag.getList("Enchantments", Tag.TAG_COMPOUND);
        var enchantmentRegistry = registries.lookupOrThrow(Registries.ENCHANTMENT);
        for (int index = 0; index < enchantmentList.size(); index++) {
            var enchantmentTag = enchantmentList.getCompound(index);
            var enchantmentId = ResourceLocation.tryParse(enchantmentTag.getString("id"));
            if (enchantmentId == null) {
                throw new IllegalArgumentException("Invalid enchantment id: " + enchantmentTag.getString("id"));
            }

            var enchantment = enchantmentRegistry.get(ResourceKey.create(Registries.ENCHANTMENT, enchantmentId))
                    .orElseThrow(() -> new IllegalArgumentException("Unknown enchantment: " + enchantmentId));
            int level = enchantmentTag.getInt("lvl");
            if (level <= 0 || level > 255) {
                throw new IllegalArgumentException("Invalid enchantment level for " + enchantmentId + ": " + level);
            }
            stack.enchant(enchantment, level);
        }
        tag.remove("Enchantments");
    }

    private static CompoundTag parseNbt(JsonElement nbt) {
        var value = nbt.isJsonPrimitive() ? nbt.getAsString() : nbt.toString();
        try {
            return TagParser.parseTag(value);
        } catch (Exception exception) {
            throw new IllegalArgumentException("Invalid item NBT: " + value, exception);
        }
    }
}
