package com.copycatsplus.copycats.content.copycat.base.multistate;

import com.copycatsplus.copycats.CCBlockEntityTypes;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

import static net.minecraft.core.Direction.Axis;

public abstract class MultiStateCopycatBlock extends Block implements IBE<MultiStateCopycatBlockEntity>, IWrenchable {

    public MultiStateCopycatBlock(Properties properties) {
        super(properties);
    }

    public abstract int maxMaterials();

    @Nullable
    @Override
    public <S extends BlockEntity> BlockEntityTicker<S> getTicker(Level p_153212_, BlockState p_153213_, BlockEntityType<S> p_153214_) {
        return null;
    }

    @Override
    public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
        onWrenched(state, context);
        return IWrenchable.super.onSneakWrenched(state, context);
    }

    public abstract String getPropertyForInteraction(BlockState state, UseOnContext context);

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        return onBlockEntityUse(context.getLevel(), context.getClickedPos(), ufte -> {
            String property = getPropertyForInteraction(state, context);
            MaterialItemStorage.MaterialItem materialItem = ufte.getMaterialItemStorage().getMaterialItem(property);
            if (materialItem == null) return InteractionResult.PASS;
            ItemStack consumedItem = materialItem.consumedItem();
            Player player = context.getPlayer();
            if (!player.isCreative())
                player.getInventory()
                        .placeItemBackInInventory(consumedItem);
            context.getLevel()
                    .levelEvent(2001, context.getClickedPos(), Block.getId(ufte.getBlockState()));
            ufte.setMaterial(property, AllBlocks.COPYCAT_BASE.getDefaultState());
            ufte.getMaterialItemStorage().setConsumedItem(property, ItemStack.EMPTY);
            return InteractionResult.SUCCESS;
        });
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit) {
        Direction face = hit.getDirection();
        ItemStack itemInHand = player.getItemInHand(hand);
        BlockState materialIn = getAcceptedBlockState(level, pos, itemInHand, face);

        if (materialIn != null)
            materialIn = prepareMaterial(level, pos, state, player, hand, hit, materialIn);
        if (materialIn == null)
            return InteractionResult.PASS;

        BlockState material = materialIn;
        return onBlockEntityUse(level, pos, ufte -> {
            String property = getPropertyForInteraction(state, new UseOnContext(player, hand, hit));
            MaterialItemStorage.MaterialItem materialItem = ufte.getMaterialItemStorage().getMaterialItem(property);
            if (materialItem != null && materialItem.material().is(material.getBlock())) {
                if (!ufte.cycleMaterial(property))
                    return InteractionResult.PASS;
                ufte.getLevel()
                        .playSound(null, ufte.getBlockPos(), SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, .75f,
                                .95f);
                return InteractionResult.SUCCESS;
            }
            if (ufte.getMaterialItemStorage().hasCustomMaterial(property))
                return InteractionResult.PASS;

            // TODO: these two lines should be below isClientSide check. Find out why they can't be
            ufte.setMaterial(property, material);
            ufte.getMaterialItemStorage().setConsumedItem(property, itemInHand);

            if (level.isClientSide())
                return InteractionResult.SUCCESS;


            ufte.getLevel()
                    .playSound(null, ufte.getBlockPos(), material.getSoundType()
                            .getPlaceSound(), SoundSource.BLOCKS, 1, .75f);

            if (player.isCreative())
                return InteractionResult.SUCCESS;

            itemInHand.shrink(1);
            if (itemInHand.isEmpty())
                player.setItemInHand(hand, ItemStack.EMPTY);
            return InteractionResult.SUCCESS;
        });
    }

    //Copied from CopycatBlock
    @Nullable
    public BlockState getAcceptedBlockState(Level pLevel, BlockPos pPos, ItemStack item, Direction face) {
        if (!(item.getItem() instanceof BlockItem bi))
            return null;

        Block block = bi.getBlock();
        if (block instanceof MultiStateCopycatBlock)
            return null;

        BlockState appliedState = block.defaultBlockState();
        boolean hardCodedAllow = isAcceptedRegardless(appliedState);

        if (!AllTags.AllBlockTags.COPYCAT_ALLOW.matches(block) && !hardCodedAllow) {

            if (AllTags.AllBlockTags.COPYCAT_DENY.matches(block))
                return null;
            if (block instanceof EntityBlock)
                return null;
            if (block instanceof StairBlock)
                return null;

            if (pLevel != null) {
                VoxelShape shape = appliedState.getShape(pLevel, pPos);
                if (shape.isEmpty() || !shape.bounds()
                        .equals(Shapes.block()
                                .bounds()))
                    return null;

                VoxelShape collisionShape = appliedState.getCollisionShape(pLevel, pPos);
                if (collisionShape.isEmpty())
                    return null;
            }
        }

        if (face != null) {
            Axis axis = face.getAxis();

            if (appliedState.hasProperty(BlockStateProperties.FACING))
                appliedState = appliedState.setValue(BlockStateProperties.FACING, face);
            if (appliedState.hasProperty(BlockStateProperties.HORIZONTAL_FACING) && axis != Axis.Y)
                appliedState = appliedState.setValue(BlockStateProperties.HORIZONTAL_FACING, face);
            if (appliedState.hasProperty(BlockStateProperties.AXIS))
                appliedState = appliedState.setValue(BlockStateProperties.AXIS, axis);
            if (appliedState.hasProperty(BlockStateProperties.HORIZONTAL_AXIS) && axis != Axis.Y)
                appliedState = appliedState.setValue(BlockStateProperties.HORIZONTAL_AXIS, axis);
        }

        return appliedState;
    }

    public boolean isAcceptedRegardless(BlockState material) {
        return false;
    }

    public BlockState prepareMaterial(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer,
                                      InteractionHand pHand, BlockHitResult pHit, BlockState material) {
        return material;
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (!pState.hasBlockEntity() || pState.getBlock() == pNewState.getBlock())
            return;
        if (!pIsMoving)
            withBlockEntityDo(pLevel, pPos, ufte -> ufte.getMaterialItemStorage().getAllConsumedItems().forEach(stack -> Block.popResource(pLevel, pPos, stack)));
        pLevel.removeBlockEntity(pPos);
    }

    @Override
    public void playerWillDestroy(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
        super.playerWillDestroy(pLevel, pPos, pState, pPlayer);
        if (pPlayer.isCreative())
            withBlockEntityDo(pLevel, pPos, ufte -> ufte.getMaterialItemStorage().getAllProperties().forEach(key -> ufte.getMaterialItemStorage().setConsumedItem(key, ItemStack.EMPTY)));
    }

    @Override
    public Class<MultiStateCopycatBlockEntity> getBlockEntityClass() {
        return MultiStateCopycatBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends MultiStateCopycatBlockEntity> getBlockEntityType() {
        return CCBlockEntityTypes.MULTI_STATE_COPYCAT_BLOCK_ENTITY.get();
    }

    public boolean isIgnoredConnectivitySide(BlockAndTintGetter reader, BlockState state, Direction face,
                                             BlockPos fromPos, BlockPos toPos) {
        return false;
    }

    public abstract boolean canConnectTexturesToward(BlockAndTintGetter reader, BlockPos fromPos, BlockPos toPos,
                                                     BlockState state);

    public static BlockState getMaterial(BlockGetter reader, BlockPos targetPos, String property) {
        if (reader.getBlockEntity(targetPos) instanceof MultiStateCopycatBlockEntity cbe)
            return cbe.getMaterialItemStorage().getMaterial(property);
        return Blocks.AIR.defaultBlockState();
    }

    public boolean canFaceBeOccluded(BlockState state, Direction face) {
        return false;
    }

    public boolean shouldFaceAlwaysRender(BlockState state, Direction face) {
        return false;
    }

    // TODO: implement all other block overrides
}
