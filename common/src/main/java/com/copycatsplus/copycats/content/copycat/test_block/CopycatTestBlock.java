package com.copycatsplus.copycats.content.copycat.test_block;

import com.copycatsplus.copycats.content.copycat.base.multistate.MultiStateCopycatBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.SlabType;

import java.util.Set;

public class CopycatTestBlock extends MultiStateCopycatBlock {

    public static final EnumProperty<SlabType> SLAB_TYPE = BlockStateProperties.SLAB_TYPE;
    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.AXIS;

    public CopycatTestBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState()
                .setValue(AXIS, Direction.Axis.Y)
                .setValue(SLAB_TYPE, SlabType.BOTTOM));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(SLAB_TYPE).add(AXIS));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState stateForPlacement = super.getStateForPlacement(context);
        assert stateForPlacement != null;
        BlockPos blockPos = context.getClickedPos();
        BlockState state = context.getLevel().getBlockState(blockPos);
        if (state.is(this)) {
            return state
                    .setValue(SLAB_TYPE, SlabType.DOUBLE);
        } else {
            Direction.Axis axis = context.getNearestLookingDirection().getAxis();
            boolean flag = switch (axis) {
                case X -> context.getClickLocation().x - (double) blockPos.getX() > 0.5D;
                case Y -> context.getClickLocation().y - (double) blockPos.getY() > 0.5D;
                case Z -> context.getClickLocation().z - (double) blockPos.getZ() > 0.5D;
            };
            Direction clickedFace = context.getClickedFace();
            return stateForPlacement
                    .setValue(AXIS, axis)
                    .setValue(SLAB_TYPE, clickedFace == Direction.fromAxisAndDirection(axis, Direction.AxisDirection.POSITIVE) || clickedFace.getAxis() != axis && !flag ? SlabType.BOTTOM : SlabType.TOP);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean canBeReplaced(BlockState pState, BlockPlaceContext pUseContext) {
        ItemStack itemstack = pUseContext.getItemInHand();
        SlabType slabtype = pState.getValue(SLAB_TYPE);
        Direction.Axis axis = pState.getValue(AXIS);
        if (slabtype != SlabType.DOUBLE && itemstack.is(this.asItem())) {
            boolean flag = switch (axis) {
                case X -> pUseContext.getClickLocation().x - (double) pUseContext.getClickedPos().getX() > 0.5D;
                case Y -> pUseContext.getClickLocation().y - (double) pUseContext.getClickedPos().getY() > 0.5D;
                case Z -> pUseContext.getClickLocation().z - (double) pUseContext.getClickedPos().getZ() > 0.5D;
            };
            Direction direction = pUseContext.getClickedFace();
            if (slabtype == SlabType.BOTTOM) {
                return direction == Direction.fromAxisAndDirection(axis, Direction.AxisDirection.POSITIVE) || flag;
            } else {
                return direction == Direction.fromAxisAndDirection(axis, Direction.AxisDirection.NEGATIVE) || !flag;
            }
        } else {
            return false;
        }
    }

    @Override
    public int maxMaterials() {
        return 2;
    }

    @Override
    public Set<String> storageProperties() {
        return Set.of(SlabType.TOP.getSerializedName(), SlabType.BOTTOM.getSerializedName());
    }

    @Override
    public String getPropertyFromInteraction(BlockState state, BlockPos hitLocation, BlockPos blockPos) {
        if (state.getValue(SLAB_TYPE) == SlabType.DOUBLE) {
            return switch (state.getValue(AXIS)) {
                case X -> {
                    if (hitLocation.getX() == 1) {
                        yield (SlabType.TOP.getSerializedName());
                    } else {
                        yield SlabType.BOTTOM.getSerializedName();
                    }
                }
                case Y -> {
                    if (hitLocation.getY() == 1) {
                        yield SlabType.TOP.getSerializedName();
                    } else {
                        yield SlabType.BOTTOM.getSerializedName();
                    }
                }
                case Z -> {
                    if (hitLocation.getZ() == 1) {
                        yield SlabType.TOP.getSerializedName();
                    } else {
                        yield SlabType.BOTTOM.getSerializedName();
                    }
                }
            };
        } else {
            return state.getValue(SLAB_TYPE).getSerializedName();
        }
    }

    @Override
    public boolean canConnectTexturesToward(BlockAndTintGetter reader, BlockPos fromPos, BlockPos toPos, BlockState state) {
        return true;
    }
}
