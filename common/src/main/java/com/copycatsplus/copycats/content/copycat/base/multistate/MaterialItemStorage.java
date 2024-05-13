package com.copycatsplus.copycats.content.copycat.base.multistate;

import com.copycatsplus.copycats.utility.NBTUtils;
import com.simibubi.create.content.redstone.RoseQuartzLampBlock;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class MaterialItemStorage {

    private Map<String, MaterialItem> storage;
    private int maxStorage;

    private MaterialItemStorage(int maxStorage) {
        storage = new HashMap<>(maxStorage);
        this.maxStorage = maxStorage;
    }

    public static MaterialItemStorage create(int maxStorage) {
        return new MaterialItemStorage(maxStorage);
    }

    public void storeMaterialItem(String property, MaterialItem materialItem) {
        storage.put(property, materialItem);
    }

    public @Nullable MaterialItem getMaterialItem(String property) {
        return storage.get(property);
    }

    public Set<String> getAllProperties() {
        return storage.keySet();
    }

    public Set<BlockState> getAllMaterials() {
        return storage.values().stream().map(MaterialItem::material).collect(Collectors.toSet());
    }

    public Set<ItemStack> getAllConsumedItems() {
        return storage.values().stream().map(MaterialItem::consumedItem).collect(Collectors.toSet());
    }

    public Map<String, BlockState> getMaterialMap() {
        return storage.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, s -> s.getValue().material));
    }

    public boolean hasCustomMaterial(String property) {
        return storage.get(property) != null;
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

    public static class MaterialItem {

        private BlockState material;
        private ItemStack consumedItem;

        public MaterialItem(BlockState material, ItemStack consumedItem) {
            this.material = material;
            this.consumedItem = consumedItem;
        }

        public boolean cycleMaterial() {
            if (material.hasProperty(TrapDoorBlock.HALF) && material.getOptionalValue(TrapDoorBlock.OPEN)
                    .orElse(false))
                setMaterial(material.cycle(TrapDoorBlock.HALF));
            else if (material.hasProperty(BlockStateProperties.FACING))
                setMaterial(material.cycle(BlockStateProperties.FACING));
            else if (material.hasProperty(BlockStateProperties.HORIZONTAL_FACING))
                setMaterial(material.setValue(BlockStateProperties.HORIZONTAL_FACING,
                        material.getValue(BlockStateProperties.HORIZONTAL_FACING)
                                .getClockWise()));
            else if (material.hasProperty(BlockStateProperties.AXIS))
                setMaterial(material.cycle(BlockStateProperties.AXIS));
            else if (material.hasProperty(BlockStateProperties.HORIZONTAL_AXIS))
                setMaterial(material.cycle(BlockStateProperties.HORIZONTAL_AXIS));
            else if (material.hasProperty(BlockStateProperties.LIT))
                setMaterial(material.cycle(BlockStateProperties.LIT));
            else if (material.hasProperty(RoseQuartzLampBlock.POWERING))
                setMaterial(material.cycle(RoseQuartzLampBlock.POWERING));
            else
                return false;

            return true;
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

        public void setConsumedItem(ItemStack stack) {
            this.consumedItem = stack;
        }
    }

}
