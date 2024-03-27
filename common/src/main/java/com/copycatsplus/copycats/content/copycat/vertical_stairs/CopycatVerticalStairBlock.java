package com.copycatsplus.copycats.content.copycat.vertical_stairs;

import com.copycatsplus.copycats.CCShapes;
import com.copycatsplus.copycats.content.copycat.base.CTWaterloggedCopycatBlock;
import com.firemerald.additionalplacements.util.VoxelShapes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CopycatVerticalStairBlock extends CTWaterloggedCopycatBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<Half> HALF = BlockStateProperties.HALF;
    public static final EnumProperty<StairsShape> STAIRS_SHAPE = BlockStateProperties.STAIRS_SHAPE;

    public CopycatVerticalStairBlock(Properties pProperties) {
        super(pProperties);
        registerDefaultState(defaultBlockState()
                .setValue(FACING, Direction.NORTH)
                .setValue(STAIRS_SHAPE, StairsShape.STRAIGHT)
                .setValue(HALF, Half.BOTTOM));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(FACING, STAIRS_SHAPE, HALF));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState stateForPlacement = super.getStateForPlacement(context);
        assert stateForPlacement != null;
        Direction facing = context.getNearestLookingDirection().getOpposite();
        if (facing.getAxis().isHorizontal()) {
            return stateForPlacement.setValue(FACING, facing);
        } else {
            return stateForPlacement.setValue(FACING, Direction.NORTH);
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction facing = state.getValue(FACING);
        boolean flip = state.getValue(HALF).equals(Half.BOTTOM);
        return switch (state.getValue(STAIRS_SHAPE)) {
            case STRAIGHT -> (flip ? CCShapes.VERTICAL_STAIR_STRAIGHT_FLIP.get(facing) : CCShapes.VERTICAL_STAIR_STRAIGHT.get(facing));
            case OUTER_LEFT -> (flip ? CCShapes.VERTICAL_STAIR_OUTER_LEFT.get(facing) : CCShapes.VERTICAL_STAIR_OUTER_LEFT_FLIP.get(facing));
            case OUTER_RIGHT -> (flip ? CCShapes.VERTICAL_STAIR_OUTER_RIGHT.get(facing) : CCShapes.VERTICAL_STAIR_OUTER_RIGHT_FLIP.get(facing));
            default -> VoxelShapes.BLOCK;
        };
    }



    @Override
    public boolean canConnectTexturesToward(BlockAndTintGetter reader, BlockPos fromPos, BlockPos toPos, BlockState state) {
        return state.is(this);
    }
}
