package de.lariel.qualityoflife.shopkeeper.utility;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;

public final class LarielItemStackFactory {

    private LarielItemStackFactory() {
    }

    public static ItemStack create(ResourceLocation itemId, JsonElement nbt) {
        var item = net.minecraft.core.registries.BuiltInRegistries.ITEM.get(itemId);
        if (item == Items.AIR) {
            throw new IllegalArgumentException("Unknown item: " + itemId);
        }

        var stack = new ItemStack(item);
        if (nbt != null && !nbt.isJsonNull()) {
            stack.set(DataComponents.CUSTOM_DATA, CustomData.of(parseNbt(nbt)));
        }
        return stack;
    }

    public static JsonElement serialize(ItemStack stack) {
        var customData = stack.get(DataComponents.CUSTOM_DATA);
        return customData == null ? null : NbtOps.INSTANCE.convertTo(JsonOps.INSTANCE, customData.copyTag());
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
