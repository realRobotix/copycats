package com.copycatsplus.copycats.content.copycat.base.multistate;

import com.copycatsplus.copycats.utility.ItemUtils;
import com.copycatsplus.copycats.utility.NBTUtils;
import com.simibubi.create.AllBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class MaterialItemStorage {

    private Map<String, MaterialItem> storage;
    private int maxStorage;

    private MaterialItemStorage(int maxStorage, Set<String> properties) {
        storage = new HashMap<>(maxStorage);
        this.maxStorage = maxStorage;
        for (String property : properties) {
            storage.put(property, new MaterialItem(AllBlocks.COPYCAT_BASE.getDefaultState(), ItemStack.EMPTY));
        }
    }

    public static MaterialItemStorage create(int maxStorage, Set<String> properties) {
        return new MaterialItemStorage(maxStorage, properties);
    }

    public void storeMaterialItem(String property, MaterialItem materialItem) {
        storage.put(property, materialItem);
    }

    public MaterialItem getMaterialItem(String property) {
        return storage.get(property);
    }

    public Set<String> getAllProperties() {
        return storage.keySet();
    }

    public Set<BlockState> getAllMaterials() {
        return storage.values().stream().map(MaterialItem::material).collect(Collectors.toSet());
    }

    public List<ItemStack> getAllConsumedItems() {
        return storage.values().stream().map(MaterialItem::consumedItem).collect(Collectors.toList());
    }

    public Map<String, BlockState> getMaterialMap() {
        return storage.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, s -> s.getValue().material));
    }

    public boolean hasCustomMaterial(String property) {
        return !storage.get(property).material().is(AllBlocks.COPYCAT_BASE.get());
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

    public boolean deserialize(CompoundTag tag) {
        AtomicBoolean anyUpdated = new AtomicBoolean(false);
        tag.getAllKeys().forEach(key -> {
            MaterialItem newVersion = MaterialItem.deserialize(tag.getCompound(key));
            if (newVersion.material() != storage.put(key, newVersion).material() && !anyUpdated.get()) {
                anyUpdated.set(true);
            }
            ;
        });
        return anyUpdated.get();
    }

    public static class MaterialItem {

        private BlockState material;
        private ItemStack consumedItem;

        public MaterialItem(BlockState material, ItemStack consumedItem) {
            this.material = material;
            this.consumedItem = consumedItem;
        }

        public void setConsumedItem(ItemStack stack) {
            consumedItem = ItemUtils.copyStackWithSize(stack, 1);
        }

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

        public BlockState material() {
            return material;
        }

        public ItemStack consumedItem() {
            return consumedItem;
        }

        public void setMaterial(BlockState material) {
            this.material = material;
        }
    }

}
