package com.copycatsplus.copycats.content.copycat.shaft;

import com.copycatsplus.copycats.CCBlockEntityTypes;
import com.copycatsplus.copycats.content.copycat.base.ICustomCTBlocking;
import com.copycatsplus.copycats.content.copycat.base.functional.IFunctionalCopycatBlock;
import com.simibubi.create.content.decoration.bracket.BracketBlock;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.simpleRelays.ShaftBlock;
import com.simibubi.create.foundation.placement.IPlacementHelper;
import com.simibubi.create.foundation.placement.PlacementHelpers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class CopycatShaftBlock extends ShaftBlock implements IFunctionalCopycatBlock, ICustomCTBlocking {

    public CopycatShaftBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
        InteractionResult result = IFunctionalCopycatBlock.super.onSneakWrenched(state, context);
        if (result.consumesAction()) {
            return result;
        }
        return super.onSneakWrenched(state, context);
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        InteractionResult result = IFunctionalCopycatBlock.super.onWrenched(state, context);
        if (result.consumesAction()) {
            return result;
        }
        return super.onWrenched(state, context);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult ray) {
        InteractionResult result = IFunctionalCopycatBlock.super.use(state, world, pos, player, hand, ray);
        if (result.consumesAction()) {
            return result;
        }
        if (!player.isShiftKeyDown() && player.mayBuild()) {
            ItemStack heldItem = player.getItemInHand(hand);
            IPlacementHelper placementHelper = PlacementHelpers.get(placementHelperId);
            if (placementHelper.matchesItem(heldItem)) {
                placementHelper.getOffset(player, world, state, pos, ray)
                        .placeInWorld(world, (BlockItem) heldItem.getItem(), player, hand, ray);
                return InteractionResult.SUCCESS;
            }
        }

        return super.use(state, world, pos, player, hand, ray);
    }

    @Nullable
    @Override
    public BlockState getAcceptedBlockState(Level pLevel, BlockPos pPos, ItemStack item, Direction face) {
        if (item.getItem() instanceof BlockItem bi) {
            if (bi.getBlock() instanceof BracketBlock) return null;
        }

        return IFunctionalCopycatBlock.super.getAcceptedBlockState(pLevel, pPos, item, face);
    }

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(worldIn, pos, state, placer, stack);
        IFunctionalCopycatBlock.super.setPlacedBy(worldIn, pos, state, placer, stack);
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        super.onRemove(state, world, pos, newState, isMoving);
        IFunctionalCopycatBlock.super.onRemove(state, world, pos, newState, isMoving);
    }

    @Override
    public void playerWillDestroy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Player player) {
        super.playerWillDestroy(level, pos, state, player);
        IFunctionalCopycatBlock.super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public BlockEntityType<? extends KineticBlockEntity> getBlockEntityType() {
        return CCBlockEntityTypes.COPYCAT_SHAFT.get();
    }

    @Override
    public boolean isIgnoredConnectivitySide(BlockAndTintGetter reader, BlockState state, Direction face, BlockPos fromPos, BlockPos toPos) {
        return true;
    }

    @Override
    public boolean canConnectTexturesToward(BlockAndTintGetter reader, BlockPos fromPos, BlockPos toPos, BlockState state) {
        Vec3i diff = toPos.subtract(fromPos);
        Direction face = Direction.fromDelta(diff.getX(), diff.getY(), diff.getZ());
        if (face == null) return false;
        return face.getAxis() == state.getValue(AXIS);
    }

    @Override
    public Optional<Boolean> blockCTTowards(BlockAndTintGetter reader, BlockState state, BlockPos pos, BlockPos ctPos, BlockPos connectingPos, Direction face) {
        return Optional.of(false);
    }

    @Override
    public Optional<Boolean> isCTBlocked(BlockAndTintGetter reader, BlockState state, BlockPos pos, BlockPos connectingPos, BlockPos blockingPos, Direction face) {
        return Optional.of(false);
    }
}
