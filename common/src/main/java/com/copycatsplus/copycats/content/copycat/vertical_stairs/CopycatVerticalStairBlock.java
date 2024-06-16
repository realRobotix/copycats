package com.copycatsplus.copycats.content.copycat.vertical_stairs;

import com.copycatsplus.copycats.CCBlockStateProperties;
import com.copycatsplus.copycats.CCBlockStateProperties.Side;
import com.copycatsplus.copycats.CCBlockStateProperties.VerticalStairShape;
import com.copycatsplus.copycats.CCShapes;
import com.copycatsplus.copycats.content.copycat.base.CTWaterloggedCopycatBlock;
import com.copycatsplus.copycats.content.copycat.stairs.CopycatStairsBlock;
import com.copycatsplus.copycats.content.copycat.stairs.WrappedStairsBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class CopycatVerticalStairBlock extends CTWaterloggedCopycatBlock {

    // Facing refers to the direction of the base of the stairs
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<Side> SIDE = CCBlockStateProperties.SIDE;
    public static final EnumProperty<VerticalStairShape> SHAPE = CCBlockStateProperties.VERTICAL_STAIR_SHAPE;

    public CopycatVerticalStairBlock(Properties pProperties) {
        super(pProperties);
        registerDefaultState(defaultBlockState()
                .setValue(FACING, Direction.NORTH)
                .setValue(SIDE, Side.LEFT)
                .setValue(SHAPE, VerticalStairShape.STRAIGHT));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(FACING, SIDE, SHAPE));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos blockPos = context.getClickedPos();
        Direction facing = context.getHorizontalDirection();
        Side side = context.getClickLocation().get(facing.getClockWise().getAxis()) - context.getClickedPos().get(facing.getClockWise().getAxis()) > 0.5
                ? Side.RIGHT : Side.LEFT;
        if (facing.getCounterClockWise().getAxisDirection() == Direction.AxisDirection.POSITIVE) {
            side = side.getOpposite();
        }
        BlockState blockState = defaultBlockState().setValue(FACING, facing).setValue(SIDE, side);
        return withWater(blockState.setValue(SHAPE, getStairsShape(blockState, context.getLevel(), blockPos)), context);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        Direction facing = state.getValue(FACING);
        boolean right = state.getValue(SIDE).isRight();
        return switch (state.getValue(SHAPE)) {
            case STRAIGHT ->
                    right ? CCShapes.VERTICAL_STAIR_STRAIGHT_RIGHT.get(facing) : CCShapes.VERTICAL_STAIR_STRAIGHT_LEFT.get(facing);
            case INNER_TOP ->
                    right ? CCShapes.VERTICAL_STAIR_INNER_TOP_RIGHT.get(facing) : CCShapes.VERTICAL_STAIR_INNER_TOP_LEFT.get(facing);
            case INNER_BOTTOM ->
                    right ? CCShapes.VERTICAL_STAIR_INNER_BOTTOM_RIGHT.get(facing) : CCShapes.VERTICAL_STAIR_INNER_BOTTOM_LEFT.get(facing);
            case OUTER_TOP ->
                    right ? CCShapes.VERTICAL_STAIR_OUTER_TOP_RIGHT.get(facing) : CCShapes.VERTICAL_STAIR_OUTER_TOP_LEFT.get(facing);
            case OUTER_BOTTOM ->
                    right ? CCShapes.VERTICAL_STAIR_OUTER_BOTTOM_RIGHT.get(facing) : CCShapes.VERTICAL_STAIR_OUTER_BOTTOM_LEFT.get(facing);
        };
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        BlockState newState = super.updateShape(state, direction, neighborState, level, pos, neighborPos);
        if (direction.getAxis() != newState.getValue(FACING).getAxis()) {
            return newState.setValue(SHAPE, getStairsShape(newState, level, pos));
        } else {
            return newState;
        }
    }

    @Override
    public boolean useShapeForLightOcclusion(@NotNull BlockState pState) {
        return true;
    }

    private static VerticalStairShape getStairsShape(BlockState state, BlockGetter level, BlockPos pos) {
        Direction facing = state.getValue(FACING);
        boolean side = state.getValue(SIDE).isRight();

        Direction verticalDirection = null;
        // true for right, false for left, null for vertical connections only
        Boolean horizontalConnection = null;

        boolean down = canConnect(state, level, pos, Direction.DOWN);
        boolean downParity = getVerticalParity(state, level, pos, Direction.DOWN);
        if (down && side == downParity) {
            verticalDirection = Direction.DOWN;
        } else {
            boolean up = canConnect(state, level, pos, Direction.UP);
            boolean upParity = getVerticalParity(state, level, pos, Direction.UP);
            if (up && side == upParity) {
                verticalDirection = Direction.UP;
            }
        }
        boolean left = canConnect(state, level, pos, facing.getCounterClockWise());
        boolean leftParity = getHorizontalParity(state, level, pos, facing.getCounterClockWise());
        if (left && (verticalDirection == null || verticalDirection == Direction.DOWN && side == leftParity || verticalDirection == Direction.UP && side != leftParity)) {
            horizontalConnection = false;
            if (verticalDirection == null) {
                verticalDirection = leftParity == side ? Direction.DOWN : Direction.UP;
            }
        } else {
            boolean right = canConnect(state, level, pos, facing.getClockWise());
            boolean rightParity = getHorizontalParity(state, level, pos, facing.getClockWise());
            if (right && (verticalDirection == null || verticalDirection == Direction.UP && side == rightParity || verticalDirection == Direction.DOWN && side != rightParity)) {
                horizontalConnection = true;
                if (verticalDirection == null) {
                    verticalDirection = rightParity == side ? Direction.UP : Direction.DOWN;
                }
            }
        }

        if (horizontalConnection == null) {
            return VerticalStairShape.STRAIGHT;
        } else if (!horizontalConnection) {
            if (verticalDirection == Direction.DOWN) {
                return side ? VerticalStairShape.INNER_TOP : VerticalStairShape.OUTER_BOTTOM;
            } else {
                return side ? VerticalStairShape.INNER_BOTTOM : VerticalStairShape.OUTER_TOP;
            }
        } else {
            if (verticalDirection == Direction.DOWN) {
                return side ? VerticalStairShape.OUTER_BOTTOM : VerticalStairShape.INNER_TOP;
            } else {
                return side ? VerticalStairShape.OUTER_TOP : VerticalStairShape.INNER_BOTTOM;
            }
        }
    }

    private static boolean getVerticalParity(BlockState state, BlockGetter level, BlockPos pos, Direction face) {
        BlockState blockState = level.getBlockState(pos.relative(face));
        if (!(blockState.getBlock() instanceof CopycatVerticalStairBlock)) return false;
        return blockState.getValue(SIDE).isRight();
    }

    private static boolean getHorizontalParity(BlockState state, BlockGetter level, BlockPos pos, Direction face) {
        BlockState blockState = level.getBlockState(pos.relative(face));
        if (!isStairs(blockState)) return false;
        if (blockState.getBlock() instanceof CopycatVerticalStairBlock) {
            return (state.getValue(FACING).getCounterClockWise() == face) == blockState.getValue(SIDE).isRight();
        } else {
            return blockState.getValue(StairBlock.HALF) == Half.TOP;
        }
    }

    private static boolean canConnect(BlockState state, BlockGetter level, BlockPos pos, Direction face) {
        BlockState blockState = level.getBlockState(pos.relative(face));
        if (!isStairs(blockState)) return false;
        if (face.getAxis().isVertical() && !(blockState.getBlock() instanceof CopycatVerticalStairBlock)) return false;
        Direction selfFacing = state.getValue(FACING);
        Direction otherFacing = blockState.getValue(FACING);
        if (selfFacing == otherFacing.getOpposite()) return false;
        if (selfFacing == otherFacing && face.getAxis() != selfFacing.getAxis()) return true;
        return selfFacing != otherFacing;
    }

    public static boolean isStairs(BlockState state) {
        return state.getBlock() instanceof StairBlock || state.getBlock() instanceof CopycatVerticalStairBlock || state.getBlock() instanceof WrappedStairsBlock || state.getBlock() instanceof CopycatStairsBlock;
    }

    @Override
    public boolean isIgnoredConnectivitySide(BlockAndTintGetter reader, BlockState state, Direction face, BlockPos fromPos, BlockPos toPos) {
        BlockState toState = reader.getBlockState(toPos);
        return !isStairs(toState);
    }

    @Override
    public boolean canConnectTexturesToward(BlockAndTintGetter reader, BlockPos fromPos, BlockPos toPos, BlockState state) {
        BlockState toState = reader.getBlockState(toPos);
        return isStairs(toState);
    }

    @Override
    public @NotNull BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public @NotNull BlockState mirror(@NotNull BlockState state, @NotNull Mirror mirror) {
        Direction facing = state.getValue(FACING);
        Side side = state.getValue(SIDE);
        Axis axis = switch (mirror) {
            case LEFT_RIGHT -> Axis.X;
            case FRONT_BACK -> Axis.Z;
            default -> Axis.Y;
        };
        if (axis == Axis.Y) return state;
        if (facing.getAxis() == axis) {
            return state.setValue(FACING, facing.getOpposite()).setValue(SIDE, side.getOpposite());
        } else {
            return state.setValue(SIDE, side.getOpposite());
        }
    }
}
