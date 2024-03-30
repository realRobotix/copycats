package com.copycatsplus.copycats.content.copycat.vertical_stairs;

import com.copycatsplus.copycats.CCBlockStateProperties;
import com.copycatsplus.copycats.CCBlockStateProperties.VerticalStairShape;
import com.copycatsplus.copycats.CCShapes;
import com.copycatsplus.copycats.content.copycat.base.CTWaterloggedCopycatBlock;
import com.copycatsplus.copycats.content.copycat.stairs.CopycatStairsBlock;
import com.firemerald.additionalplacements.block.VerticalStairBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CopycatVerticalStairBlock extends CTWaterloggedCopycatBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<Half> HALF = BlockStateProperties.HALF;
    public static final EnumProperty<VerticalStairShape> VERTICAL_STAIR_SHAPE = CCBlockStateProperties.VERTICAL_STAIR_SHAPE;

    public CopycatVerticalStairBlock(Properties pProperties) {
        super(pProperties);
        registerDefaultState(defaultBlockState()
                .setValue(FACING, Direction.NORTH)
                .setValue(VERTICAL_STAIR_SHAPE, VerticalStairShape.STRAIGHT)
                .setValue(HALF, Half.BOTTOM));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(FACING, VERTICAL_STAIR_SHAPE, HALF));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction direction = context.getClickedFace();
        BlockPos blockPos = context.getClickedPos();
        FluidState fluidState = context.getLevel().getFluidState(blockPos);
        BlockState blockState = defaultBlockState().setValue(FACING, context.getHorizontalDirection()).setValue(HALF, direction == Direction.DOWN || direction != Direction.UP && context.getClickLocation().y - (double)blockPos.getY() > 0.5 ? Half.TOP : Half.BOTTOM).setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
        return blockState.setValue(VERTICAL_STAIR_SHAPE, getStairShape(blockState, context.getLevel(), blockPos));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction facing = state.getValue(FACING);
        boolean flip = state.getValue(HALF).equals(Half.BOTTOM);
        return switch (state.getValue(VERTICAL_STAIR_SHAPE)) {
            case STRAIGHT ->
                    (flip ? CCShapes.VERTICAL_STAIR_STRAIGHT.get(facing) : CCShapes.VERTICAL_STAIR_STRAIGHT_FLIP.get(facing));
            case OUTER_LEFT ->
                    (flip ? CCShapes.VERTICAL_STAIR_OUTER_LEFT.get(facing) : CCShapes.VERTICAL_STAIR_OUTER_LEFT_FLIP.get(facing));
            case OUTER_RIGHT ->
                    (flip ? CCShapes.VERTICAL_STAIR_OUTER_RIGHT.get(facing) : CCShapes.VERTICAL_STAIR_OUTER_RIGHT_FLIP.get(facing));
        };
    }

    private static VerticalStairShape getStairShape(BlockState state, BlockGetter level, BlockPos pos) {
        Direction direction2;
        Direction direction = state.getValue(FACING);
        BlockState blockState = level.getBlockState(pos.relative(direction));
        if (isStairs(blockState) && state.getValue(HALF) == blockState.getValue(HALF) && (direction2 = blockState.getValue(FACING)).getAxis() != state.getValue(FACING).getAxis() && canTakeShape(state, level, pos, direction2.getOpposite())) {
            if (direction2 == direction.getCounterClockWise()) {
                return VerticalStairShape.OUTER_LEFT;
            }
            return VerticalStairShape.OUTER_RIGHT;
        }
        return VerticalStairShape.STRAIGHT;
    }

    private static boolean canTakeShape(BlockState state, BlockGetter level, BlockPos pos, Direction face) {
        BlockState blockState = level.getBlockState(pos.relative(face));
        return !isStairs(blockState) || blockState.getValue(FACING) != state.getValue(FACING) || blockState.getValue(HALF) != state.getValue(HALF);
    }

    public static boolean isStairs(BlockState state) {
        return state.getBlock() instanceof StairBlock || state.getBlock() instanceof CopycatVerticalStairBlock || state.getBlock() instanceof CopycatStairsBlock;
    }


    @Override
    public boolean canConnectTexturesToward(BlockAndTintGetter reader, BlockPos fromPos, BlockPos toPos, BlockState state) {
        return state.is(this);
    }
}
