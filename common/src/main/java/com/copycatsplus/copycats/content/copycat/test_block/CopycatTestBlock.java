package com.copycatsplus.copycats.content.copycat.test_block;

import com.copycatsplus.copycats.content.copycat.base.multistate.MultiStateCopycatBlock;
import com.copycatsplus.copycats.content.copycat.base.multistate.ScaledBlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

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

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        VoxelShape shapeOverride = multiPlatformGetShape(state, level, pos, context);
        if (shapeOverride != null) return shapeOverride;
        return super.getShape(state, level, pos, context);
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
    public Vec3i vectorScale(BlockState state) {
        return switch (state.getValue(AXIS)) {
            case X -> new Vec3i(2, 1, 1);
            case Y -> new Vec3i(1, 2, 1);
            case Z -> new Vec3i(1, 1, 2);
        };
    }

    @Override
    public boolean partExists(BlockState state, String property) {
        SlabType slabType = state.getValue(SLAB_TYPE);
        if (property.equals(SlabType.BOTTOM.getSerializedName())) {
            return slabType == SlabType.DOUBLE || slabType == SlabType.BOTTOM;
        } else if (property.equals(SlabType.TOP.getSerializedName())) {
            return slabType == SlabType.DOUBLE || slabType == SlabType.TOP;
        }
        return false;
    }

    @Override
    public Set<String> storageProperties() {
        return Set.of(SlabType.TOP.getSerializedName(), SlabType.BOTTOM.getSerializedName());
    }

    @Override
    public String getPropertyFromInteraction(BlockState state, BlockGetter level, Vec3i hitLocation, BlockPos blockPos, Direction facing, Vec3 unscaledHit) {
        if (hitLocation.get(state.getValue(AXIS)) > 0) {
            return SlabType.TOP.getSerializedName();
        } else {
            return SlabType.BOTTOM.getSerializedName();
        }
    }

    @Override
    public Vec3i getVectorFromProperty(BlockState state, String property) {
        return switch (state.getValue(AXIS)) {
            case X -> property.equals(SlabType.TOP.getSerializedName()) ? new Vec3i(1, 0, 0) : new Vec3i(0, 0, 0);
            case Y -> property.equals(SlabType.TOP.getSerializedName()) ? new Vec3i(0, 1, 0) : new Vec3i(0, 0, 0);
            case Z -> property.equals(SlabType.TOP.getSerializedName()) ? new Vec3i(0, 0, 1) : new Vec3i(0, 0, 0);
        };
    }

    @Override
    public boolean isIgnoredConnectivitySide(String property, BlockAndTintGetter reader, BlockState state, Direction face, BlockPos fromPos, BlockPos toPos) {
        BlockState toState = reader.getBlockState(toPos);
        if (reader instanceof ScaledBlockAndTintGetter scaledReader) {
            BlockPos fromTruePos = scaledReader.getTruePos(fromPos);
            BlockPos toTruePos = scaledReader.getTruePos(toPos);
            return fromTruePos.equals(toTruePos);
        }
        return !toState.is(this);
    }

    @Override
    public boolean canConnectTexturesToward(String property, BlockAndTintGetter reader, BlockPos fromPos, BlockPos toPos, BlockState state) {
        BlockState toState = reader.getBlockState(toPos);
        if (reader instanceof ScaledBlockAndTintGetter scaledReader) {
            BlockPos fromTruePos = scaledReader.getTruePos(fromPos);
            BlockPos toTruePos = scaledReader.getTruePos(toPos);
            return !fromTruePos.equals(toTruePos) && toState.is(this);
        }
        return toState.is(this);
    }
}
