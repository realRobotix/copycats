package com.copycatsplus.copycats.mixin.copycat.base.multistate;

import com.copycatsplus.copycats.content.copycat.base.IShimCopycatBlock;
import com.copycatsplus.copycats.content.copycat.base.multistate.MultiStateCopycatBlock;
import com.copycatsplus.copycats.content.copycat.base.multistate.ScaledBlockAndTintGetter;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.simibubi.create.content.decoration.copycat.CopycatBlock;
import com.simibubi.create.foundation.block.connected.ConnectedTextureBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ConnectedTextureBehaviour.class)
public class ConnectedTextureBehaviourMixin {
    @Inject(
            method = "getCTBlockState",
            at = @At("HEAD"),
            cancellable = true
    )
    private void multiStateCT(BlockAndTintGetter reader, BlockState reference, Direction face, BlockPos fromPos, BlockPos toPos, CallbackInfoReturnable<BlockState> cir) {
        BlockState blockState = reader.getBlockState(toPos);

        if (blockState.getBlock() instanceof MultiStateCopycatBlock ufb) {
            BlockState connectiveMaterial = ufb.getConnectiveMaterial(reader, reference, face, fromPos, toPos);
            cir.setReturnValue(connectiveMaterial == null ? blockState : connectiveMaterial);
        }
    }

    @Inject(
            method = "testConnection(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;Lnet/minecraft/core/Direction;Lnet/minecraft/core/Direction;II)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private void multiStateCT(BlockAndTintGetter reader, BlockPos pos, BlockState state, Direction face,
                              final Direction horizontal, final Direction vertical, int sh, int sv,
                              CallbackInfoReturnable<Boolean> cir) {
        BlockPos p = pos.relative(horizontal, sh)
                .relative(vertical, sv);
        BlockState blockState = reader.getBlockState(pos);

        if (blockState.getBlock() instanceof MultiStateCopycatBlock ufb && reader instanceof ScaledBlockAndTintGetter scaledReader) {
            Vec3i fromInner = scaledReader.getInner(pos);
            String property = ufb.getPropertyFromInteraction(blockState, reader, fromInner, pos, face, Vec3.atCenterOf(fromInner));
            if (!ufb.canConnectTexturesToward(property, reader, pos, p, blockState))
                cir.setReturnValue(false);
        }
    }

    @WrapOperation(
            method = "testConnection(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;Lnet/minecraft/core/Direction;Lnet/minecraft/core/Direction;II)Z",
            at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/decoration/copycat/CopycatBlock;isIgnoredConnectivitySide(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/BlockPos;)Z")
    )
    private boolean bypassIfShim(CopycatBlock instance, BlockAndTintGetter reader, BlockState state, Direction face, BlockPos fromPos, BlockPos toPos, Operation<Boolean> original) {
        if (instance instanceof IShimCopycatBlock shim) {
            return !shim.canConnectTexturesToward(reader, fromPos, toPos, state);
        }

        return original.call(instance, reader, state, face, fromPos, toPos);
    }
}
