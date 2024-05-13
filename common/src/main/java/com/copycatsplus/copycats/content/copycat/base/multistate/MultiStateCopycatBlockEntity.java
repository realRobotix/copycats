package com.copycatsplus.copycats.content.copycat.base.multistate;

import com.copycatsplus.copycats.Copycats;
import com.simibubi.create.content.redstone.RoseQuartzLampBlock;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.Set;

public abstract class MultiStateCopycatBlockEntity extends SmartBlockEntity {

    private final MaterialItemStorage materialItemStorage;

    public MultiStateCopycatBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        if (getBlockState().getBlock() instanceof MultiStateCopycatBlock mscb) {
            materialItemStorage = MaterialItemStorage.create(mscb.maxMaterials(), mscb.storageProperties());
        } else {
            materialItemStorage = MaterialItemStorage.create(1, Set.of("block"));
        }
    }

    public boolean cycleMaterial(String property) {
        BlockState material = getMaterialItemStorage().getMaterialItem(property).material();
        if (material.hasProperty(TrapDoorBlock.HALF) && material.getOptionalValue(TrapDoorBlock.OPEN)
                .orElse(false))
            setMaterial(property, material.cycle(TrapDoorBlock.HALF));
        else if (material.hasProperty(BlockStateProperties.FACING))
            setMaterial(property, material.cycle(BlockStateProperties.FACING));
        else if (material.hasProperty(BlockStateProperties.HORIZONTAL_FACING))
            setMaterial(property, material.setValue(BlockStateProperties.HORIZONTAL_FACING,
                    material.getValue(BlockStateProperties.HORIZONTAL_FACING)
                            .getClockWise()));
        else if (material.hasProperty(BlockStateProperties.AXIS))
            setMaterial(property, material.cycle(BlockStateProperties.AXIS));
        else if (material.hasProperty(BlockStateProperties.HORIZONTAL_AXIS))
            setMaterial(property, material.cycle(BlockStateProperties.HORIZONTAL_AXIS));
        else if (material.hasProperty(BlockStateProperties.LIT))
            setMaterial(property, material.cycle(BlockStateProperties.LIT));
        else if (material.hasProperty(RoseQuartzLampBlock.POWERING))
            setMaterial(property, material.cycle(RoseQuartzLampBlock.POWERING));
        else
            return false;

        return true;
    }

    public MaterialItemStorage getMaterialItemStorage() {
        return materialItemStorage;
    }

    public void setMaterial(String property, BlockState blockState) {
        BlockState wrapperState = getBlockState();

        BlockState finalMaterial = blockState;
        if (!getMaterialItemStorage().getMaterialItem(property).material().is(finalMaterial.getBlock()))
            for (Direction side : Iterate.directions) {
                BlockPos neighbour = worldPosition.relative(side);
                BlockState neighbourState = level.getBlockState(neighbour);
                if (neighbourState != wrapperState)
                    continue;
                if (!(level.getBlockEntity(neighbour) instanceof MultiStateCopycatBlockEntity cbe))
                    continue;
                BlockState otherMaterial = cbe.getMaterialItemStorage().getMaterialItem(property).material();
                if (!otherMaterial.is(blockState.getBlock()))
                    continue;
                blockState = otherMaterial;
                break;
            }
        MaterialItemStorage.MaterialItem materialItem = getMaterialItemStorage().getMaterialItem(property);
        materialItem.setMaterial(blockState);
        getMaterialItemStorage().storeMaterialItem(property, materialItem);
        if (!level.isClientSide()) {
            notifyUpdate();
            return;
        }
        redraw();
    }

    public void setConsumedItem(String property, ItemStack itemStack) {
        MaterialItemStorage.MaterialItem materialItem = getMaterialItemStorage().getMaterialItem(property);
        getMaterialItemStorage().getMaterialItem(property).setConsumedItem(itemStack);
        setChanged();

    }

    private void redraw() {
        if (!isVirtual())
            requestModelUpdate();
        if (hasLevel()) {
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 16);
            level.getChunkSource()
                    .getLightEngine()
                    .checkBlock(worldPosition);
        }
    }

    public abstract void requestModelUpdate();

    @Override
    public void writeSafe(CompoundTag tag) {
        super.writeSafe(tag);

        tag.put("material_data", materialItemStorage.serializeSafe());
    }

    @Override
    protected void write(CompoundTag tag, boolean clientPacket) {
        super.write(tag, clientPacket);

        tag.put("material_data", materialItemStorage.serialize());
    }

    @Override
    protected void read(CompoundTag tag, boolean clientPacket) {
        super.read(tag, clientPacket);
        if (getBlockState().getBlock() instanceof MultiStateCopycatBlock mscb) {
            boolean anyUpdated = materialItemStorage.deserialize(tag.getCompound("material_data"));

            if (clientPacket && anyUpdated)
                redraw();
        }
    }
}
