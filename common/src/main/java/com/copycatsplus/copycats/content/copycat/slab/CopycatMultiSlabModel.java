package com.copycatsplus.copycats.content.copycat.slab;

import com.copycatsplus.copycats.content.copycat.base.model.QuadHelper.CopycatRenderContext;
import com.copycatsplus.copycats.content.copycat.base.model.multistate.SimpleMultiStateCopycatPart;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Objects;

import static com.copycatsplus.copycats.content.copycat.base.model.PlatformModelUtils.cullFacing;
import static net.minecraft.core.Direction.Axis;
import static net.minecraft.core.Direction.AxisDirection;

public class CopycatMultiSlabModel implements SimpleMultiStateCopycatPart {
    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        if (Objects.equals(key, SlabType.TOP.getSerializedName()) && state.getValue(CopycatSlabBlock.SLAB_TYPE) == SlabType.BOTTOM)
            return;
        if (Objects.equals(key, SlabType.BOTTOM.getSerializedName()) && state.getValue(CopycatSlabBlock.SLAB_TYPE) == SlabType.TOP)
            return;

        Axis axis = state.getValue(CopycatSlabBlock.AXIS);
        Direction facing = Direction.fromAxisAndDirection(axis, Objects.equals(key, SlabType.BOTTOM.getSerializedName()) ? AxisDirection.NEGATIVE : AxisDirection.POSITIVE);

        // 2 pieces
        for (boolean front : Iterate.trueAndFalse) {
            assemblePiece(facing, context, front, false, false);
        }
    }

    private void assemblePiece(Direction facing, CopycatRenderContext context, boolean front, boolean topSlab, boolean isDouble) {
        Vec3 normal = Vec3.atLowerCornerOf(facing.getNormal());
        Vec3 normalScaled12 = normal.scale(12 / 16f);
        Vec3 normalScaledN8 = topSlab ? normal.scale((front ? 0 : -8) / 16f) : normal.scale((front ? 8 : 0) / 16f);
        float contract = 12;
        AABB bb = CUBE_AABB.contract(normal.x * contract / 16, normal.y * contract / 16, normal.z * contract / 16);
        if (!front)
            bb = bb.move(normalScaled12);

        //Not sure on the name. But i needed to extract this to make it platform-agnostic
        cullFacing(facing, context, front, topSlab, isDouble, bb, normalScaledN8);
    }
}
