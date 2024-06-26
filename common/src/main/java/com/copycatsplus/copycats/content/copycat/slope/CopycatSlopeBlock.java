package com.copycatsplus.copycats.content.copycat.slope;

import com.copycatsplus.copycats.CCBlocks;
import com.copycatsplus.copycats.CCShapes;
import com.copycatsplus.copycats.content.copycat.base.CTWaterloggedCopycatBlock;
import com.copycatsplus.copycats.content.copycat.base.IStateType;
import com.copycatsplus.copycats.utility.shape.NoneVoxelShape;
import com.copycatsplus.copycats.utility.shape.VoxelCollection;
import com.copycatsplus.copycats.utility.shape.VoxelUtils;
import com.simibubi.create.content.equipment.extendoGrip.ExtendoGripItem;
import com.simibubi.create.foundation.placement.IPlacementHelper;
import com.simibubi.create.foundation.placement.PlacementHelpers;
import com.simibubi.create.foundation.placement.PlacementOffset;
import com.simibubi.create.foundation.placement.PoleHelper;
import com.simibubi.create.infrastructure.config.AllConfigs;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.*;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class CopycatSlopeBlock extends CTWaterloggedCopycatBlock implements IStateType {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<Half> HALF = BlockStateProperties.HALF;

    public CopycatSlopeBlock(Properties pProperties) {
        super(pProperties);
        registerDefaultState(defaultBlockState()
                .setValue(FACING, Direction.NORTH)
                .setValue(HALF, Half.BOTTOM)
        );
    }

    @Override
    public boolean isIgnoredConnectivitySide(BlockAndTintGetter reader, BlockState state, Direction face,
                                             BlockPos fromPos, BlockPos toPos) {
        Direction direction = state.getValue(FACING);
        Half half = state.getValue(HALF);
        BlockState toState = reader.getBlockState(toPos);

        if (toState.is(this)) {
            if (toState.getValue(FACING) == direction && toState.getValue(HALF)== half) return false;

            BlockPos diff = toPos.subtract(fromPos);
            if (diff.equals(Vec3i.ZERO)) {
                return false;
            }
            Direction connectFace = Direction.fromDelta(diff.getX(), diff.getY(), diff.getZ());
            if (connectFace == null) {
                return true;
            }

            return !(direction == connectFace && connectFace == toState.getValue(FACING).getOpposite());
        } else {
            return false;
        }
    }

    @Override
    public boolean canConnectTexturesToward(BlockAndTintGetter reader, BlockPos fromPos, BlockPos toPos,
                                            BlockState state) {
        BlockState toState = reader.getBlockState(toPos);
        if (!toState.is(this)) return false;
        Direction facing = state.getValue(FACING);
        Half half = state.getValue(HALF);

        BlockPos diff = toPos.subtract(fromPos);
        if (diff.equals(Vec3i.ZERO)) {
            return true;
        }
        Direction face = Direction.fromDelta(diff.getX(), diff.getY(), diff.getZ());
        if (face == null) {
            return false;
        }

        if (toState.is(this)) {
            try {
                return toState.getValue(FACING) == facing &&
                        toState.getValue(HALF) == half &&
                        face.getAxis().isHorizontal() && face.getAxis() != facing.getAxis() ||
                        face == facing && face == toState.getValue(FACING).getOpposite();
            } catch (IllegalStateException ignored) {
                return false;
            }
        } else {
            return false;
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isPathfindable(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull PathComputationType pType) {
        return false;
    }

    @Override
    public boolean canFaceBeOccluded(BlockState state, Direction face) {
        Direction facing = state.getValue(FACING);
        Half half = state.getValue(HALF);
        if (face == facing) return true;
        if (face == facing.getOpposite()) return false;
        if (half == Half.TOP) return face != Direction.DOWN;
        else return face != Direction.UP;
    }

    @Override
    public boolean shouldFaceAlwaysRender(BlockState state, Direction face) {
        return !canFaceBeOccluded(state, face);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState stateForPlacement = super.getStateForPlacement(context);
        assert stateForPlacement != null;
        Direction facing = context.getHorizontalDirection();
        Half half = context.getClickedFace() == Direction.DOWN
                ? Half.TOP
                : context.getClickedFace() == Direction.UP
                ? Half.BOTTOM
                : context.getClickLocation().y - context.getClickedPos().getY() > 0.5
                ? Half.TOP
                : Half.BOTTOM;
        return stateForPlacement.setValue(FACING, facing).setValue(HALF, half);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(FACING, HALF));
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull VoxelShape getShape(BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        Direction facing = pState.getValue(FACING);
        boolean isBottom = pState.getValue(HALF) == Half.BOTTOM;
        Vec3[] triangleVertices = switch (facing) {
            case WEST -> new Vec3[]{
                    new Vec3(0, (isBottom ? 0 : 1), 0),
                    new Vec3(0, (isBottom ? 0 : 1), 0),
                    new Vec3(1, (isBottom ? 0 : 1), 0),
                    new Vec3(0, (isBottom ? 1 : 0), 0),
                    new Vec3(0, (isBottom ? 0 : 1), 1),
                    new Vec3(0, (isBottom ? 0 : 1), 1),
                    new Vec3(1, (isBottom ? 0 : 1), 1),
                    new Vec3(0, (isBottom ? 1 : 0), 1)
            };
            case EAST -> new Vec3[]{
                    new Vec3(1, (isBottom ? 0 : 1), 1),
                    new Vec3(1, (isBottom ? 0 : 1), 1),
                    new Vec3(0, (isBottom ? 0 : 1), 1),
                    new Vec3(1, (isBottom ? 1 : 0), 1),
                    new Vec3(1, (isBottom ? 0 : 1), 0),
                    new Vec3(1, (isBottom ? 0 : 1), 0),
                    new Vec3(0, (isBottom ? 0 : 1), 0),
                    new Vec3(1, (isBottom ? 1 : 0), 0)
            };
            case NORTH -> new Vec3[]{
                    new Vec3(0, (isBottom ? 0 : 1), 0),
                    new Vec3(0, (isBottom ? 0 : 1), 0),
                    new Vec3(0, (isBottom ? 0 : 1), 1),
                    new Vec3(0, (isBottom ? 1 : 0), 0),
                    new Vec3(1, (isBottom ? 0 : 1), 0),
                    new Vec3(1, (isBottom ? 0 : 1), 0),
                    new Vec3(1, (isBottom ? 0 : 1), 1),
                    new Vec3(1, (isBottom ? 1 : 0), 0)
            };
            case SOUTH -> new Vec3[]{
                    new Vec3(1, (isBottom ? 0 : 1), 1),
                    new Vec3(1, (isBottom ? 0 : 1), 1),
                    new Vec3(1, (isBottom ? 0 : 1), 0),
                    new Vec3(1, (isBottom ? 1 : 0), 1),
                    new Vec3(0, (isBottom ? 0 : 1), 1),
                    new Vec3(0, (isBottom ? 0 : 1), 1),
                    new Vec3(0, (isBottom ? 0 : 1), 0),
                    new Vec3(0, (isBottom ? 1 : 0), 1)
            };
            default -> throw new AssertionError("Direction shouldn't appear as this is a horizontal facing block only");
        };

        Vec3[] shape = VoxelUtils.create12Edges(triangleVertices);

        //Accidentally made a corner version
/*        new Vec3(0.0, (isBottom ? 0 : 1), -0.0),
                new Vec3(0.0, (isBottom ? 0 : 1), -0.0),
                new Vec3(0.0, (isBottom ? 0 : 1), -1),
                new Vec3(1, (isBottom ? 1 : 0), 0),
                new Vec3(1, (isBottom ? 0 : 1), -0.0),
                new Vec3(1, (isBottom ? 0 : 1), -0.0),
                new Vec3(1, (isBottom ? 0 : 1), -1),
                new Vec3(1, (isBottom ? 1 : 0), 0)*/

        return new NoneVoxelShape((isBottom ? CCShapes.SLOPE_BOTTOM.get(facing) : CCShapes.SLOPE_TOP.get(facing)), shape);
    }


    public boolean supportsExternalFaceHiding(BlockState state) {
        return true;
    }


    public boolean hidesNeighborFace(BlockGetter level, BlockPos pos, BlockState state, BlockState neighborState,
                                     Direction dir) {
        if (state.is(this) == neighborState.is(this)) {
            if (getMaterial(level, pos).skipRendering(getMaterial(level, pos.relative(dir)), dir.getOpposite())) {
                Direction facing = state.getValue(FACING);
                Half half = state.getValue(HALF);
                return neighborState.getValue(FACING) == facing &&
                        neighborState.getValue(HALF) == half &&
                        dir.getAxis().isHorizontal() && dir.getAxis() != facing.getAxis();
            }
        }

        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull BlockState rotate(@NotNull BlockState pState, Rotation pRot) {
        return pState.setValue(FACING, pRot.rotate(pState.getValue(FACING)));
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull BlockState mirror(@NotNull BlockState pState, @NotNull Mirror pMirror) {
        return pState.setValue(FACING, pMirror.mirror(pState.getValue(FACING)));
    }
}
