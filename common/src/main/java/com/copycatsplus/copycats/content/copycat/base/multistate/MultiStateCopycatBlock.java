package com.copycatsplus.copycats.content.copycat.base.multistate;

import com.copycatsplus.copycats.CCBlockEntityTypes;
import com.copycatsplus.copycats.content.copycat.base.IStateType;
import com.copycatsplus.copycats.content.copycat.base.StateType;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.schematics.requirement.ISpecialBlockEntityItemRequirement;
import com.simibubi.create.content.schematics.requirement.ISpecialBlockItemRequirement;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.simibubi.create.foundation.block.IBE;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
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
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static net.minecraft.core.Direction.Axis;

public abstract class MultiStateCopycatBlock extends Block implements IBE<MultiStateCopycatBlockEntity>, IWrenchable, ISpecialBlockItemRequirement, IStateType {

    public MultiStateCopycatBlock(Properties properties) {
        super(properties);
    }

    public abstract int maxMaterials();

    /**
     * Get the number of parts this block is divided into in each axis.
     */
    public abstract Vec3i vectorScale(BlockState state);

    public abstract Set<String> storageProperties();

    public abstract boolean partExists(BlockState state, String property);

    public abstract String getPropertyFromInteraction(BlockState state, Vec3i hitLocation, BlockPos blockPos, Direction facing, Vec3 unscaledHit);

    public abstract Vec3i getVectorFromProperty(BlockState state, String property);

    /**
     * @param targetingSolid Whether the interaction is targeting the solid part behind the block face or the air in front of the block face.
     */
    public String getProperty(@NotNull BlockState state, @NotNull BlockPos pos, @NotNull BlockHitResult hit, boolean targetingSolid) {
        Vec3 hitVec = hit.getLocation();
        return getProperty(state, pos, hitVec, hit.getDirection(), targetingSolid);
    }

    /**
     * @param targetingSolid Whether the interaction is targeting the solid part behind the block face or the air in front of the block face.
     */
    protected String getProperty(@NotNull BlockState state, @NotNull BlockPos pos, Vec3 hitVec, Direction face, boolean targetingSolid) {
        // Relativize the hit vector around the player position
        if (targetingSolid) {
            hitVec = hitVec.subtract(Vec3.atLowerCornerOf(face.getNormal()).scale(0.05));
        } else {
            hitVec = hitVec.add(Vec3.atLowerCornerOf(face.getNormal()).scale(0.05));
        }
        hitVec = hitVec.add(-pos.getX(), -pos.getY(), -pos.getZ());
        Vec3 unscaledHit = hitVec;
        Vec3i scale = vectorScale(state);
        hitVec = hitVec.multiply(scale.getX(), scale.getY(), scale.getZ());
        BlockPos location = new BlockPos((int) hitVec.x(), (int) hitVec.y(), (int) hitVec.z());
        return getPropertyFromInteraction(state, location, pos, face, unscaledHit);
    }

    @Nullable
    @Override
    public <S extends BlockEntity> BlockEntityTicker<S> getTicker(Level p_153212_, BlockState p_153213_, BlockEntityType<S> p_153214_) {
        return null;
    }

    @Override
    public ItemRequirement getRequiredItems(BlockState state, BlockEntity blockEntity) {
        if (state.getBlock() instanceof MultiStateCopycatBlock msb) {
            List<ItemStack> stacks = new ArrayList<>(msb.storageProperties().stream()
                    .filter(prop -> msb.partExists(state, prop))
                    .map(prop -> new ItemStack(state.getBlock().asItem())).toList());
            return new ItemRequirement(ItemRequirement.ItemUseType.CONSUME, stacks);
        }
        return ItemRequirement.INVALID;
    }

    @Override
    public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
        onWrenched(state, context);
        return IWrenchable.super.onSneakWrenched(state, context);
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        return onBlockEntityUse(context.getLevel(), context.getClickedPos(), ufte -> {
            String property = getProperty(state, context.getClickedPos(), context.getClickLocation(), context.getClickedFace(), true);
            if (!partExists(state, property)) return InteractionResult.PASS;
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

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit) {
        if (player == null || !player.mayBuild() && !player.isSpectator())
            return InteractionResult.PASS;

        Direction face = hit.getDirection();
        ItemStack itemInHand = player.getItemInHand(hand);
        BlockState materialIn = getAcceptedBlockState(level, pos, itemInHand, face);

        if (materialIn != null)
            materialIn = prepareMaterial(level, pos, state, player, hand, hit, materialIn);
        if (materialIn == null)
            return InteractionResult.PASS;

        BlockState material = materialIn;
        return onBlockEntityUse(level, pos, ufte -> {
            String property = getProperty(state, pos, hit, true);
            if (!partExists(state, property)) return InteractionResult.PASS;
            if (ufte.getMaterialItemStorage().getMaterialItem(property).material()
                    .is(material.getBlock())) {
                if (!ufte.cycleMaterial(property))
                    return InteractionResult.PASS;
                ufte.getLevel()
                        .playSound(null, ufte.getBlockPos(), SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, .75f,
                                .95f);
                return InteractionResult.SUCCESS;
            }
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
    public void setPlacedBy(@NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull BlockState pState, LivingEntity pPlacer, @NotNull ItemStack pStack) {
        if (pPlacer == null)
            return;
        ItemStack offhandItem = pPlacer.getItemInHand(InteractionHand.OFF_HAND);
        BlockState appliedState =
                getAcceptedBlockState(pLevel, pPos, offhandItem, Direction.orderedByNearest(pPlacer)[0]);

        if (appliedState == null)
            return;
        withBlockEntityDo(pLevel, pPos, ufte -> {
            for (String property : this.storageProperties()) {
                if (!partExists(pState, property))
                    continue;
                if (ufte.getMaterialItemStorage().hasCustomMaterial(property))
                    continue;

                ufte.setMaterial(property, appliedState);
                ufte.setConsumedItem(property, offhandItem);

                if (pPlacer instanceof Player player && player.isCreative())
                    continue;
                offhandItem.shrink(1);
                if (offhandItem.isEmpty()) {
                    pPlacer.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
                    break;
                }
            }
        });
    }

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
    public void onRemove(BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull BlockState pNewState, boolean pIsMoving) {
        if (!pState.hasBlockEntity() || pState.getBlock() == pNewState.getBlock())
            return;
        if (!pIsMoving)
            withBlockEntityDo(pLevel, pPos, ufte -> ufte.getMaterialItemStorage().getAllConsumedItems().forEach(stack -> Block.popResource(pLevel, pPos, stack)));
        pLevel.removeBlockEntity(pPos);
    }

    @Override
    public void playerWillDestroy(@NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull BlockState pState, @NotNull Player pPlayer) {
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

    @Override
    public StateType stateType() {
        return StateType.MULTI;
    }

    @ExpectPlatform
    public static BlockState multiPlatformGetAppearance(MultiStateCopycatBlock block, BlockState state, BlockAndTintGetter level, BlockPos pos, Direction side,
                                                        BlockState queryState, BlockPos queryPos) {
        throw new AssertionError("This should never appear");
    }

    @Environment(EnvType.CLIENT)
    public BlockState getAppearance(BlockState state, BlockAndTintGetter level, BlockPos pos, Direction side,
                                    BlockState queryState, BlockPos queryPos) {

        return multiPlatformGetAppearance(this, state, level, pos, side, queryState, queryPos);
    }

    public boolean isIgnoredConnectivitySide(String property, BlockAndTintGetter reader, BlockState state, Direction face,
                                             BlockPos fromPos, BlockPos toPos) {
        BlockState toState = reader.getBlockState(toPos);
        if (toState.getBlock() instanceof MultiStateCopycatBlock mscb) {
            return true;
        } else {
            return !toState.is(getMaterial(reader, fromPos).getBlock());
        }
    }

    public boolean canConnectTexturesToward(String property, BlockAndTintGetter reader, BlockPos fromPos, BlockPos toPos,
                                            BlockState state) {
        BlockState toState = reader.getBlockState(toPos);
        if (toState.getBlock() instanceof MultiStateCopycatBlock mscb) {
            if (mscb.partExists(toState, property)) {
                BlockState toMat = getMaterial(reader, toPos, property);
                return toMat.is(getMaterial(reader, fromPos, property).getBlock());
            } else {
                for (String prop : mscb.storageProperties()) {
                    return (mscb.partExists(toState, prop) && getMaterial(reader, toPos, prop).is(getMaterial(reader, fromPos, property).getBlock()));
                }
                return false;
            }
        } else {
            return toState.is(getMaterial(reader, fromPos, property).getBlock());
        }
    }

    /**
     * Get the first non-empty material in this multi-state copycat.
     * You should avoid using this method as much as possible and figure out which specific
     * material to get instead.
     */
    public static BlockState getMaterial(BlockGetter reader, BlockPos targetPos) {
        if (reader.getBlockEntity(targetPos) instanceof MultiStateCopycatBlockEntity cbe)
            return cbe.getMaterialItemStorage().getAllMaterials().stream()
                    .filter(s -> !s.is(AllBlocks.COPYCAT_BASE.get()))
                    .findFirst()
                    .orElse(Blocks.AIR.defaultBlockState());
        return Blocks.AIR.defaultBlockState();
    }

    public static BlockState getMaterial(BlockGetter reader, BlockPos targetPos, String property) {
        if (reader.getBlockEntity(targetPos) instanceof MultiStateCopycatBlockEntity cbe)
            return cbe.getMaterialItemStorage().getMaterialItem(property).material();
        return Blocks.AIR.defaultBlockState();
    }

    public boolean canFaceBeOccluded(BlockState state, Direction face) {
        return false;
    }

    public boolean shouldFaceAlwaysRender(BlockState state, Direction face) {
        return false;
    }

    // Wrapped properties
    //Copied From CopycatBlock and of course edited to work

    public SoundType getSoundType(BlockState state, LevelReader level, BlockPos pos, Entity entity) {
        return getMaterial(level, pos).getSoundType();
    }

    public float getFriction(BlockState state, LevelReader level, BlockPos pos, Entity entity) {
        return getMaterial(level, pos).getBlock().getFriction();
    }

    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        AtomicInteger light = new AtomicInteger(0);
        withBlockEntityDo(level, pos, mscbe -> {
            mscbe.getMaterialItemStorage().getAllMaterials().forEach(bs -> {
                light.accumulateAndGet(bs.getLightEmission(), Math::max);
            });
        });
        return light.get();
    }

    public float getExplosionResistance(BlockState state, BlockGetter level, BlockPos pos, Explosion explosion) {
        AtomicReference<Float> explosionResistance = new AtomicReference<>(0.0f);
        withBlockEntityDo(level, pos, mscbe -> {
            mscbe.getMaterialItemStorage().getAllMaterials().forEach(bs -> {
                explosionResistance.accumulateAndGet(bs.getBlock().getExplosionResistance(), Math::max);
            });
        });
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

    @ExpectPlatform
    @Nullable
    public static VoxelShape multiPlatformGetShape(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        // intentionally left empty so intellij doesn't complain about unreachable paths
        return null;
    }

    @Override
    public void fallOn(@NotNull Level pLevel, @NotNull BlockState pState, @NotNull BlockPos pPos, @NotNull Entity pEntity, float p_152430_) {
        String property = getProperty(pState, pPos, new BlockHitResult(Vec3.atCenterOf(pPos), Direction.UP, pPos, true), true);
        AtomicReference<BlockState> material = new AtomicReference<>(AllBlocks.COPYCAT_BASE.getDefaultState());
        withBlockEntityDo(pLevel, pPos, mscbe -> material.set(mscbe.getMaterialItemStorage().getMaterialItem(property).material()));
        material.get().getBlock()
                .fallOn(pLevel, material.get(), pPos, pEntity, p_152430_);
    }

    @Override
    public float getDestroyProgress(@NotNull BlockState pState, @NotNull Player pPlayer, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos) {
        String property = getProperty(pState, pPos, new BlockHitResult(Vec3.atCenterOf(pPos), Direction.UP, pPos, true), true);
        AtomicReference<BlockState> material = new AtomicReference<>(AllBlocks.COPYCAT_BASE.getDefaultState());
        withBlockEntityDo(pLevel, pPos, mscbe -> material.set(mscbe.getMaterialItemStorage().getMaterialItem(property).material()));
        return material.get().getDestroyProgress(pPlayer, pLevel, pPos);
    }

    @Environment(EnvType.CLIENT)
    public static BlockColor wrappedColor() {
        return new WrappedBlockColor();
    }

    @Environment(EnvType.CLIENT)
    public static class WrappedBlockColor implements BlockColor {
        @Override
        public int getColor(@NotNull BlockState pState, @Nullable BlockAndTintGetter pLevel, @Nullable BlockPos pPos,
                            int pTintIndex) {
            if (pLevel == null || pPos == null)
                return GrassColor.get(0.5D, 1.0D);
            return Minecraft.getInstance()
                    .getBlockColors()
                    .getColor(getMaterial(pLevel, pPos), pLevel, pPos, pTintIndex);
        }
    }
}
