package com.copycatsplus.copycats.content.copycat.base.multistate;

import com.simibubi.create.content.contraptions.ITransformableBlockEntity;
import com.simibubi.create.content.contraptions.StructureTransform;
import com.simibubi.create.content.schematics.requirement.ISpecialBlockEntityItemRequirement;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.IPartialSafeNBT;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public abstract class MultiStateCopycatBlockEntity extends SmartBlockEntity
        implements ISpecialBlockEntityItemRequirement, ITransformableBlockEntity, IPartialSafeNBT {

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

    public void setMaterial(String property, BlockState blockState) {
        BlockState wrapperState = getBlockState();

        // TODO: optional logic to copy state from neighboring copycats of the same material

        getMaterialItemStorage().setMaterial(property, blockState);
        if (!level.isClientSide()) {
            notifyUpdate();
            return;
        }
        redraw();
    }

    public boolean cycleMaterial(String property) {
        boolean success = getMaterialItemStorage().getMaterialItem(property).cycleMaterial();
        if (!success) return false;
        if (!level.isClientSide()) {
            notifyUpdate();
            return true;
        }
        redraw();
        return true;
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

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
    }

    @Override
    public ItemRequirement getRequiredItems(BlockState state) {
        List<ItemStack> stacks = getMaterialItemStorage().getAllConsumedItems();
        if (stacks.isEmpty())
            return ItemRequirement.NONE;
        return new ItemRequirement(ItemRequirement.ItemUseType.CONSUME, stacks);
    }

    @Override
    public void transform(StructureTransform transform) {
        // TODO: probably need additional logic
        for (String key : getMaterialItemStorage().getAllProperties()) {
            getMaterialItemStorage().setMaterial(key, transform.apply(getMaterialItemStorage().getMaterial(key)));
        }
        notifyUpdate();
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

        boolean needRedraw = materialItemStorage.deserialize(tag.getCompound("material_data"));
        if (clientPacket && needRedraw)
            redraw();
    }
}
