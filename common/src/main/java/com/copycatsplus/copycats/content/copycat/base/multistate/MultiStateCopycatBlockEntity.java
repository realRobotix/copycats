package com.copycatsplus.copycats.content.copycat.base.multistate;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class MultiStateCopycatBlockEntity extends SmartBlockEntity {

    private final MaterialItemStorage materialItemStorage;

    public MultiStateCopycatBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        if (getBlockState().getBlock() instanceof MultiStateCopycatBlock mscb) {
            materialItemStorage = MaterialItemStorage.create(mscb.maxMaterials());
        } else {
            materialItemStorage = MaterialItemStorage.create(1);
        }
    }

    public MaterialItemStorage getMaterialItemStorage() {
        return materialItemStorage;
    }

    public void setMaterial(String property, BlockState blockState, ItemStack itemInHand) {
        BlockState wrapperState = getBlockState();

        BlockState finalMaterial = blockState;
        if (getMaterialItemStorage().getMaterialItem(property) != null && !getMaterialItemStorage().getMaterialItem(property).material().is(finalMaterial.getBlock()))
            for (Direction side : Iterate.directions) {
                BlockPos neighbour = worldPosition.relative(side);
                BlockState neighbourState = level.getBlockState(neighbour);
                if (neighbourState != wrapperState)
                    continue;
                if (!(level.getBlockEntity(neighbour) instanceof MultiStateCopycatBlockEntity cbe))
                    continue;
/*                BlockState otherMaterial = cbe.getMaterial();
                if (!otherMaterial.is(blockState.getBlock()))
                    continue;
                blockState = otherMaterial;*/
                break;
            }

        getMaterialItemStorage().storeMaterialItem(property, new MaterialItemStorage.MaterialItem(blockState, itemInHand));
        if (!level.isClientSide()) {
            notifyUpdate();
            return;
        }
        redraw();
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

        materialItemStorage.deserialize(tag.getCompound("material_data"));
    }
}
