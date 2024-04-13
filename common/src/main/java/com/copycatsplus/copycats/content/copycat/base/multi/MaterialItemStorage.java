package com.copycatsplus.copycats.content.copycat.base.multi;

import com.copycatsplus.copycats.utility.NBTUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;
import java.util.stream.Collectors;

public class MaterialItemStorage {

    private static Map<String, MaterialItem> storage;
    private int maxStorage;

    private MaterialItemStorage(int maxStorage) {
        storage = new HashMap<>(maxStorage);
        this.maxStorage = maxStorage;
    }

    public static MaterialItemStorage create(int maxStorage) {
        return new MaterialItemStorage(maxStorage);
    }

    public void storeMaterialItem(String key, MaterialItem materialItem) {
        storage.put(key, materialItem);
    }

    public MaterialItem getMaterialItem(String key) {
        return storage.get(key);
    }

    public Set<String> getAllKeys() {
        return storage.keySet();
    }

    public Set<BlockState> getAllMaterials() {
        return storage.values().stream().map(MaterialItem::material).collect(Collectors.toSet());
    }

    public Set<ItemStack> getAllConsumedItems() {
        return storage.values().stream().map(MaterialItem::consumedItem).collect(Collectors.toSet());
    }

    public boolean hasCustomMaterial() {
        return (storage.size() == maxStorage || !storage.isEmpty());
    }

    public CompoundTag serialize() {
        CompoundTag root = new CompoundTag();
        storage.forEach((key, materialItem) -> root.put(key, materialItem.serialize()));
        return root;
    }

    public CompoundTag serializeSafe() {
        CompoundTag root = new CompoundTag();
        storage.forEach((key, materialItem) -> root.put(key, materialItem.serializeSafe()));
        return root;
    }

    public void deserialize(CompoundTag tag) {
        tag.getAllKeys().forEach(key -> {
            storage.put(key, MaterialItem.deserialize(tag.getCompound(key)));
        });
    }

    public record MaterialItem(BlockState material, ItemStack consumedItem) {

        public CompoundTag serialize() {
            CompoundTag root = new CompoundTag();
            root.put("material", NbtUtils.writeBlockState(material));
            root.put("consumedItem", NBTUtils.serializeStack(consumedItem));
            return root;
        }

        public CompoundTag serializeSafe() {
            CompoundTag root = new CompoundTag();
            root.put("material", NbtUtils.writeBlockState(material));
            ItemStack stackEmpty = consumedItem.copy();
            stackEmpty.setTag(null);
            root.put("consumedItem", NBTUtils.serializeStack(stackEmpty));
            return root;
        }

        public static MaterialItem deserialize(CompoundTag tag) {
            return new MaterialItem(NbtUtils.readBlockState(BuiltInRegistries.BLOCK.asLookup(), tag.getCompound("material")),
                    ItemStack.of(tag.getCompound("consumedItem")));
        }
    }

}
