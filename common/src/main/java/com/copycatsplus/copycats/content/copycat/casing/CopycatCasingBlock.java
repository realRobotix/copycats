package com.copycatsplus.copycats.content.copycat.casing;

import com.copycatsplus.copycats.CCBlocks;
import com.copycatsplus.copycats.content.copycat.base.multistate.MaterialItemStorage;
import com.copycatsplus.copycats.content.copycat.base.multistate.MultiStateCopycatBlock;
import com.copycatsplus.copycats.content.copycat.base.multistate.MultiStateCopycatBlockEntity;
import com.copycatsplus.copycats.content.copycat.base.multistate.ScaledBlockAndTintGetter;
import com.jozufozu.flywheel.util.Lazy;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;

public class CopycatCasingBlock extends MultiStateCopycatBlock {
    Lazy<Map<Item, BlockState>> ACCEPTED_CASINGS = Lazy.of(() -> Map.of(
            AllItems.ANDESITE_ALLOY.get(), CCBlocks.WRAPPED_ANDESITE_CASING.getDefaultState(),
            AllItems.BRASS_INGOT.get(), CCBlocks.WRAPPED_BRASS_CASING.getDefaultState(),
            Items.COPPER_INGOT, CCBlocks.WRAPPED_COPPER_CASING.getDefaultState(),
            AllItems.STURDY_SHEET.get(), CCBlocks.WRAPPED_RAILWAY_CASING.getDefaultState(),
            AllItems.REFINED_RADIANCE.get(), CCBlocks.WRAPPED_REFINED_RADIANCE_CASING.getDefaultState(),
            AllItems.SHADOW_STEEL.get(), CCBlocks.WRAPPED_SHADOW_STEEL_CASING.getDefaultState()
    ));

    public CopycatCasingBlock(Properties properties) {
        super(properties);
    }

    @Override
    public int maxMaterials() {
        return 2;
    }

    @Override
    public Vec3i vectorScale(BlockState state) {
        return new Vec3i(1, 1, 1);
    }

    @Override
    public Set<String> storageProperties() {
        return Set.of(Slot.INNER.getSerializedName(), Slot.OUTER.getSerializedName());
    }

    @Override
    public boolean partExists(BlockState state, String property) {
        return true;
    }

    @Override
    public String getPropertyFromInteraction(BlockState state, BlockGetter level, Vec3i hitLocation, BlockPos blockPos, Direction facing, Vec3 unscaledHit) {
        return Slot.INNER.getSerializedName();
    }

    @Override
    public String getPropertyFromRender(String renderingProperty, BlockState state, ScaledBlockAndTintGetter level, Vec3i vector, BlockPos blockPos, Direction side, BlockState queryState, BlockPos queryPos) {
        return renderingProperty;
    }

    @Override
    public Vec3i getVectorFromProperty(BlockState state, String property) {
        return new Vec3i(0, 0, 0);
    }

    @Override
    public void rotate(@NotNull BlockState state, @NotNull MultiStateCopycatBlockEntity be, Rotation rotation) {
        // do nothing
    }

    @Override
    public void mirror(@NotNull BlockState state, @NotNull MultiStateCopycatBlockEntity be, Mirror mirror) {
        // do nothing
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit) {
        InteractionResult result = super.use(state, level, pos, player, hand, hit);
        if (player.isShiftKeyDown() && player.getItemInHand(hand).equals(ItemStack.EMPTY)) {
            MultiStateCopycatBlockEntity be = getBlockEntity(level, pos);
            be.setEnableCT(Slot.OUTER.getSerializedName(), be.getMaterialItemStorage().getMaterialItem(Slot.INNER.getSerializedName()).enableCT());
            be.redraw();
        }
        if (result.consumesAction())
            return result;

        if (player == null || !player.mayBuild() && !player.isSpectator())
            return InteractionResult.PASS;

        ItemStack itemInHand = player.getItemInHand(hand);
        BlockState material = ACCEPTED_CASINGS.get().get(itemInHand.getItem());

        if (material == null)
            return InteractionResult.PASS;

        return onBlockEntityUse(level, pos, ufte -> {
            String property = Slot.OUTER.getSerializedName();
            if (ufte.getMaterialItemStorage().hasCustomMaterial(property))
                return InteractionResult.PASS;
            if (level.isClientSide())
                return InteractionResult.SUCCESS;

            ufte.setMaterial(property, material);
            ufte.setConsumedItem(property, itemInHand);
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

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        InteractionResult result = super.onWrenched(state, context);
        if (result.consumesAction())
            return result;

        return onBlockEntityUse(context.getLevel(), context.getClickedPos(), ufte -> {
            String property = Slot.OUTER.getSerializedName();
            MaterialItemStorage.MaterialItem material = ufte.getMaterialItemStorage().getMaterialItem(property);
            ItemStack consumedItem = material.consumedItem();
            if (!ufte.getMaterialItemStorage().hasCustomMaterial(property))
                return InteractionResult.PASS;
            Player player = context.getPlayer();
            if (!player.isCreative())
                player.getInventory()
                        .placeItemBackInInventory(consumedItem);
            context.getLevel()
                    .levelEvent(2001, context.getClickedPos(), Block.getId(material.material()));
            ufte.setMaterial(property, AllBlocks.COPYCAT_BASE.getDefaultState());
            ufte.setConsumedItem(property, ItemStack.EMPTY);
            return InteractionResult.SUCCESS;
        });
    }

    @Override
    public void setPlacedBy(@NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull BlockState pState, LivingEntity pPlacer, @NotNull ItemStack pStack) {
        if (pPlacer == null)
            return;
        ItemStack offhandItem = pPlacer.getItemInHand(InteractionHand.OFF_HAND);
        BlockState appliedState = ACCEPTED_CASINGS.get().get(offhandItem.getItem());

        if (appliedState == null)
            return;
        withBlockEntityDo(pLevel, pPos, ufte -> {
            String property = Slot.OUTER.getSerializedName();
            if (ufte.getMaterialItemStorage().hasCustomMaterial(property))
                return;

            ufte.setMaterial(property, appliedState);
            ufte.setConsumedItem(property, offhandItem);

            if (pPlacer instanceof Player player && player.isCreative())
                return;
            offhandItem.shrink(1);
            if (offhandItem.isEmpty()) {
                pPlacer.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
            }
        });
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return Shapes.block();
    }

    @Override
    public boolean isIgnoredConnectivitySide(String property, BlockAndTintGetter reader, BlockState state, Direction face, BlockPos fromPos, BlockPos toPos) {
        return false;
    }

    @Override
    public boolean canConnectTexturesToward(String property, BlockAndTintGetter reader, BlockPos fromPos, BlockPos toPos, BlockState state) {
        return true;
    }

    @Override
    public boolean canFaceBeOccluded(BlockState state, Direction face) {
        return true;
    }

    public boolean supportsExternalFaceHiding(BlockState state) {
        return true;
    }

    public boolean hidesNeighborFace(BlockGetter level, BlockPos pos, BlockState state, BlockState neighborState,
                                     Direction dir) {
        if (state.is(this) == neighborState.is(this)) {
            return getMaterial(level, pos).skipRendering(getMaterial(level, pos.relative(dir)), dir.getOpposite());
        }

        return false;
    }

    public static enum Slot implements StringRepresentable {
        OUTER,
        INNER;

        @Override
        public @NotNull String getSerializedName() {
            return this.name().toLowerCase();
        }
    }
}
