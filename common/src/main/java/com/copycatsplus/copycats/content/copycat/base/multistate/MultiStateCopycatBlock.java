package com.copycatsplus.copycats.content.copycat.base.multistate;

import com.copycatsplus.copycats.CCBlockEntityTypes;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static net.minecraft.core.Direction.Axis;

public abstract class MultiStateCopycatBlock extends Block implements IBE<MultiStateCopycatBlockEntity>, IWrenchable {
    public MultiStateCopycatBlock(Properties properties) {
        super(properties);
    }

    public abstract int maxMaterials();

    public abstract float vectorScale();

    public abstract Set<String> storageProperties();

    @Override
    public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
        onWrenched(state, context);
        return IWrenchable.super.onSneakWrenched(state, context);
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        return onBlockEntityUse(context.getLevel(), context.getClickedPos(), ufte -> {
            String property = getProperty(state, context.getClickedPos(), context.getClickLocation(), context.getClickedFace());
            ItemStack consumedItem = ufte.getMaterialItemStorage().getMaterialItem(property).consumedItem();
            if (!ufte.getMaterialItemStorage().hasCustomMaterial(property))
                return InteractionResult.PASS;
            Player player = context.getPlayer();
            if (!player.isCreative())
                player.getInventory()
                        .placeItemBackInInventory(consumedItem);
            context.getLevel()
                    .levelEvent(2001, context.getClickedPos(), Block.getId(ufte.getBlockState()));
            ufte.setMaterial(property, AllBlocks.COPYCAT_BASE.getDefaultState());
            ufte.setConsumedItem(property, ItemStack.EMPTY);
            return InteractionResult.SUCCESS;
        });
    }

    public abstract String getPropertyFromInteraction(BlockState state, BlockPos hitLocation, BlockPos blockPos, Vec3 originalHitLocation, Direction facing);

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit) {
        if (player == null || !player.mayBuild() && !player.isSpectator())
            return InteractionResult.PASS;

        ItemStack itemInHand = player.getItemInHand(hand);
        Direction face = hit.getDirection();
        BlockState materialIn = getAcceptedBlockState(level, pos, itemInHand, face);

        if (materialIn != null)
            materialIn = prepareMaterial(level, pos, state, player, hand, hit, materialIn);
        if (materialIn == null)
            return InteractionResult.PASS;

        BlockState material = materialIn;
        return onBlockEntityUse(level, pos, ufte -> {
            String property = getProperty(state, pos, hit);
            if (ufte.getMaterialItemStorage().getMaterialItem(property).material().is(material.getBlock())) {
                if (!ufte.cycleMaterial(property))
                    return InteractionResult.PASS;
                ufte.getLevel()
                        .playSound(null, ufte.getBlockPos(), SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, .75f,
                                .95f);
                return InteractionResult.SUCCESS;
            }
            if (ufte.getMaterialItemStorage().hasCustomMaterial(property)
                    && !ufte.getMaterialItemStorage().getMaterialItem(property).material().is(AllBlocks.COPYCAT_BASE.get()))
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
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, LivingEntity pPlacer, ItemStack pStack) {
        //TODO: Not sure how to retrieve the property here as im unsure what to put as the context for the clipcontext.
        //Otherwise its all good to go
/*        if (pPlacer == null)
            return;
        ItemStack offhandItem = pPlacer.getItemInHand(InteractionHand.OFF_HAND);
        BlockState appliedState =
                getAcceptedBlockState(pLevel, pPos, offhandItem, Direction.orderedByNearest(pPlacer)[0]);

        if (appliedState == null)
            return;
        Vec3i vecPos = new Vec3i(pPos.getX(), pPos.getY(), pPos.getZ());
        String property = getProperty(pState, pPos, pLevel.clip(new ClipContext(VecHelper.getCenterOf(vecPos), new Vec3(vecPos.getX(), vecPos.getY(), vecPos.getZ()), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, pPlacer)));
        withBlockEntityDo(pLevel, pPos, ufte -> {
            if (ufte.getMaterialItemStorage().hasCustomMaterial(property))
                return;

            ufte.setMaterial(property, appliedState);
            ufte.setConsumedItem(property, offhandItem);

            if (pPlacer instanceof Player player && player.isCreative())
                return;
            offhandItem.shrink(1);
            if (offhandItem.isEmpty())
                pPlacer.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
        });*/
    }

    public String getProperty(@NotNull BlockState state, @NotNull BlockPos pos, @NotNull BlockHitResult hit) {
        Vec3 hitVec = hit.getLocation();
        return getProperty(state, pos, hitVec, hit.getDirection());
    }

    protected String getProperty(@NotNull BlockState state, @NotNull BlockPos pos, Vec3 hitVec, Direction face) {
        // Relativize the hit vector around the player position
        Vec3 unchanged = hitVec;
        hitVec = hitVec.add(-pos.getX(), -pos.getY(), -pos.getZ());
        hitVec = hitVec.scale(vectorScale());
        BlockPos location = new BlockPos((int) hitVec.x(), (int) hitVec.y(), (int) hitVec.z());
        return getPropertyFromInteraction(state, location, pos, unchanged, face);
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
            withBlockEntityDo(pLevel, pPos, ufte -> ufte.getMaterialItemStorage().getAllProperties().forEach(key -> ufte.getMaterialItemStorage().getMaterialItem(key).setConsumedItem(ItemStack.EMPTY)));
    }

    @Override
    public Class<MultiStateCopycatBlockEntity> getBlockEntityClass() {
        return MultiStateCopycatBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends MultiStateCopycatBlockEntity> getBlockEntityType() {
        return CCBlockEntityTypes.MULTI_STATE_COPYCAT_BLOCK_ENTITY.get();
    }

    @Nullable
    @Override
    public <S extends BlockEntity> BlockEntityTicker<S> getTicker(Level p_153212_, BlockState p_153213_, BlockEntityType<S> p_153214_) {
        return null;
    }



    public abstract boolean canConnectTexturesToward(BlockAndTintGetter reader, BlockPos fromPos, BlockPos toPos,
                                                     BlockState state);

    public boolean canFaceBeOccluded(BlockState state, Direction face) {
        return false;
    }

    public boolean shouldFaceAlwaysRender(BlockState state, Direction face) {
        return false;
    }

    // Wrapped properties
    //Copied From CopycatBlock and of course edited to work

    @Override
    public void fallOn(Level pLevel, BlockState pState, BlockPos pPos, Entity pEntity, float p_152430_) {
        String property = getProperty(pState, pPos, new BlockHitResult(Vec3.atCenterOf(pPos), Direction.UP, pPos, true));
        AtomicReference<BlockState> material = new AtomicReference<>(AllBlocks.COPYCAT_BASE.getDefaultState());
        withBlockEntityDo(pLevel, pPos, mscbe -> material.set(mscbe.getMaterialItemStorage().getMaterialItem(property).material()));
        material.get().getBlock()
                .fallOn(pLevel, material.get(), pPos, pEntity, p_152430_);
    }

    @Override
    public float getDestroyProgress(BlockState pState, Player pPlayer, BlockGetter pLevel, BlockPos pPos) {
        String property = getProperty(pState, pPos, new BlockHitResult(Vec3.atCenterOf(pPos), Direction.UP, pPos, true));
        AtomicReference<BlockState> material = new AtomicReference<>(AllBlocks.COPYCAT_BASE.getDefaultState());
        withBlockEntityDo(pLevel, pPos, mscbe -> material.set(mscbe.getMaterialItemStorage().getMaterialItem(property).material()));
        return material.get().getDestroyProgress(pPlayer, pLevel, pPos);
    }

    public SoundType getSoundType(BlockState state, LevelReader level, BlockPos pos, Entity entity) {
        AtomicReference<BlockState> blockState = new AtomicReference<>(AllBlocks.COPYCAT_BASE.getDefaultState());
        withBlockEntityDo(level, pos, mscbe -> blockState.set(mscbe.getMaterialItemStorage().getAllMaterials().stream().findFirst().get()));
        return blockState.get().getSoundType();
    }

    public float getFriction(BlockState state, LevelReader level, BlockPos pos, Entity entity) {
        AtomicReference<BlockState> blockState = new AtomicReference<>(AllBlocks.COPYCAT_BASE.getDefaultState());
        withBlockEntityDo(level, pos, mscbe -> blockState.set(mscbe.getMaterialItemStorage().getAllMaterials().stream().findFirst().get()));
        return blockState.get().getBlock().getFriction();
    }

    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        AtomicInteger light = new AtomicInteger(0);
        withBlockEntityDo(level, pos, mscbe -> mscbe.getMaterialItemStorage().getAllMaterials().forEach(bs -> light.addAndGet(bs.getLightEmission())));
        return Math.min(light.get(), 15);
    }

    public float getExplosionResistance(BlockState state, BlockGetter level, BlockPos pos, Explosion explosion) {
        AtomicReference<Float> explosionResistance = new AtomicReference<>(0.0f);
        withBlockEntityDo(level, pos, mscbe -> mscbe.getMaterialItemStorage().getAllMaterials().forEach(bs -> explosionResistance.set(explosionResistance.get() + bs.getBlock().getExplosionResistance())));
        return explosionResistance.get();
    }

    public boolean addLandingEffects(BlockState state1, ServerLevel level, BlockPos pos, BlockState state2, LivingEntity entity, int numberOfParticles) {
        return multiPlatformLandingEffects(state1, level, pos, state2, entity, numberOfParticles);
    }

    @ExpectPlatform
    public static boolean multiPlatformLandingEffects(BlockState state1, ServerLevel level, BlockPos pos, BlockState state2, LivingEntity entity, int numberOfParticles) {
        throw new AssertionError("This should never appear");
    }

    public boolean addRunningEffects(BlockState state, Level level, BlockPos pos, Entity entity) {
        return multiPlatformRunningEffects(state, level, pos, entity);
    }

    @ExpectPlatform
    public static boolean multiPlatformRunningEffects(BlockState state, Level level, BlockPos pos, Entity entity) {
        throw new AssertionError("This should never appear");
    }

    public float getEnchantPowerBonus(BlockState state, LevelReader level, BlockPos pos) {
        return multiPlatformEnchantPowerBonus(state, level, pos);
    }

    @ExpectPlatform
    public static float multiPlatformEnchantPowerBonus(BlockState state, LevelReader level, BlockPos pos) {
        throw new AssertionError("This should never appear!");
    }

    public boolean isValidSpawn(BlockState state, BlockGetter level, BlockPos pos, SpawnPlacements.Type type,
                                EntityType<?> entityType) {
        return false;
    }
}
