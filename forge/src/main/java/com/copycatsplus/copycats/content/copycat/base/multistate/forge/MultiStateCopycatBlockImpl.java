package com.copycatsplus.copycats.content.copycat.base.multistate.forge;

import com.copycatsplus.copycats.content.copycat.base.multistate.MultiStateCopycatBlock;
import com.simibubi.create.AllBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.concurrent.atomic.AtomicReference;

public class MultiStateCopycatBlockImpl {

    public static boolean multiPlatformLandingEffects(BlockState state1, ServerLevel level, BlockPos pos, BlockState state2, LivingEntity entity, int numberOfParticles) {
        if (state1.getBlock() instanceof MultiStateCopycatBlock mscb) {
            String property = mscb.getProperty(state1, pos, new BlockHitResult(Vec3.atCenterOf(pos), Direction.UP, pos, true));
            AtomicReference<BlockState> mat = new AtomicReference<>(AllBlocks.COPYCAT_BASE.getDefaultState());
            mscb.withBlockEntityDo(level, pos, mscbe -> mat.set(mscbe.getMaterialItemStorage().getMaterialItem(property).material()));
            return mat.get().getBlock().addLandingEffects(state1, level, pos, state2, entity, numberOfParticles);
        }
        return false;
    }

    public static boolean multiPlatformRunningEffects(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (state.getBlock() instanceof MultiStateCopycatBlock mscb) {
            String property = mscb.getProperty(state, pos, new BlockHitResult(Vec3.atCenterOf(pos), Direction.UP, pos, true));
            AtomicReference<BlockState> mat = new AtomicReference<>(AllBlocks.COPYCAT_BASE.getDefaultState());
            mscb.withBlockEntityDo(level, pos, mscbe -> mat.set(mscbe.getMaterialItemStorage().getMaterialItem(property).material()));
            return mat.get().getBlock().addRunningEffects(state, level, pos, entity);
        }
        return false;
    }

    public static float multiPlatformEnchantPowerBonus(BlockState state, LevelReader level, BlockPos pos) {
        if (state.getBlock() instanceof MultiStateCopycatBlock mscb) {
            AtomicReference<Float> bonus = new AtomicReference<>(0f);
            mscb.withBlockEntityDo(level, pos, mscbe -> mscbe.getMaterialItemStorage().getAllMaterials().forEach(mat -> bonus.set(bonus.get() + mat.getEnchantPowerBonus(level, pos))));
            return bonus.get();
        }
        return 0f;
    }
}
