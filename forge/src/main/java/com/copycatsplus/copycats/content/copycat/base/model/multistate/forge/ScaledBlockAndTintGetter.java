package com.copycatsplus.copycats.content.copycat.base.model.multistate.forge;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.client.model.data.ModelDataManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class ScaledBlockAndTintGetter implements BlockAndTintGetter {

    private final BlockAndTintGetter wrapped;
    private final BlockPos origin;
    private final Vec3i originInner;
    private final Vec3i scale;
    private final Predicate<BlockPos> filter;

    public ScaledBlockAndTintGetter(BlockAndTintGetter wrapped, BlockPos origin, Vec3i originInner, Vec3i scale, Predicate<BlockPos> filter) {
        this.wrapped = wrapped;
        this.origin = origin;
        this.originInner = originInner;
        this.scale = scale;
        this.filter = filter;
    }

    public BlockPos getTruePos(BlockPos pos) {
        return new BlockPos(
                origin.getX() + (int) Math.floor((pos.getX() + originInner.getX() - origin.getX()) / (double) scale.getX()),
                origin.getY() + (int) Math.floor((pos.getY() + originInner.getY() - origin.getY()) / (double) scale.getY()),
                origin.getZ() + (int) Math.floor((pos.getZ() + originInner.getZ() - origin.getZ()) / (double) scale.getZ())
        );
    }

    public Vec3i getInner(BlockPos pos) {
        int x = (pos.getX() - origin.getX() + originInner.getX()) % scale.getX();
        int y = (pos.getY() - origin.getY() + originInner.getY()) % scale.getY();
        int z = (pos.getZ() - origin.getZ() + originInner.getZ()) % scale.getZ();
        if (x < 0) x += scale.getX();
        if (y < 0) y += scale.getY();
        if (z < 0) z += scale.getZ();
        return new Vec3i(x, y, z);
    }

    @Override
    public BlockEntity getBlockEntity(@NotNull BlockPos pPos) {
        if (!filter.test(pPos)) return null;
        BlockPos truePos = getTruePos(pPos);
        return wrapped.getBlockEntity(truePos);
    }

    @Override
    public @NotNull BlockState getBlockState(@NotNull BlockPos pPos) {
        if (!filter.test(pPos)) return Blocks.AIR.defaultBlockState();
        BlockPos truePos = getTruePos(pPos);
        return wrapped.getBlockState(truePos);
    }

    @Override
    public @NotNull FluidState getFluidState(@NotNull BlockPos pPos) {
        if (!filter.test(pPos)) return Fluids.EMPTY.defaultFluidState();
        BlockPos truePos = getTruePos(pPos);
        return wrapped.getFluidState(truePos);
    }

    @Override
    public int getHeight() {
        return wrapped.getHeight();
    }

    @Override
    public int getMinBuildHeight() {
        return wrapped.getMinBuildHeight();
    }

    @Override
    public float getShade(@NotNull Direction pDirection, boolean pShade) {
        return wrapped.getShade(pDirection, pShade);
    }

    @Override
    public @NotNull LevelLightEngine getLightEngine() {
        return wrapped.getLightEngine();
    }

    @Override
    public int getBlockTint(@NotNull BlockPos pBlockPos, @NotNull ColorResolver pColorResolver) {
        return wrapped.getBlockTint(pBlockPos, pColorResolver);
    }

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public @Nullable ModelDataManager getModelDataManager() {
        return wrapped.getModelDataManager();
    }

}

