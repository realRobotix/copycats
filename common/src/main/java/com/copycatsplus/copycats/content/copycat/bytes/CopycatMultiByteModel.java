package com.copycatsplus.copycats.content.copycat.bytes;

import com.copycatsplus.copycats.content.copycat.base.model.multistate.SimpleMultiStateCopycatPart;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Objects;

import static com.copycatsplus.copycats.content.copycat.base.model.QuadHelper.CopycatRenderContext;
import static com.copycatsplus.copycats.content.copycat.base.model.QuadHelper.MutableCullFace.*;
import static com.copycatsplus.copycats.content.copycat.base.model.QuadHelper.assemblePiece;
import static com.copycatsplus.copycats.content.copycat.bytes.CopycatByteBlock.*;

public class CopycatMultiByteModel implements SimpleMultiStateCopycatPart {

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        if (Objects.equals(key, TOP_NE.getName()) && !state.getValue(TOP_NE))
            return;
        if (Objects.equals(key, TOP_NW.getName()) && !state.getValue(TOP_NW))
            return;
        if (Objects.equals(key, TOP_SE.getName()) && !state.getValue(TOP_SE))
            return;
        if (Objects.equals(key, TOP_SW.getName()) && !state.getValue(TOP_SW))
            return;
        if (Objects.equals(key, BOTTOM_NE.getName()) && !state.getValue(BOTTOM_NE))
            return;
        if (Objects.equals(key, BOTTOM_NW.getName()) && !state.getValue(BOTTOM_NW))
            return;
        if (Objects.equals(key, BOTTOM_SE.getName()) && !state.getValue(BOTTOM_SE))
            return;
        if (Objects.equals(key, BOTTOM_SW.getName()) && !state.getValue(BOTTOM_SW))
            return;

        for (CopycatByteBlock.Byte bite : CopycatByteBlock.allBytes) {
            if (!state.getValue(CopycatByteBlock.byByte(bite))) continue;

            int offsetX = bite.x() ? 8 : 0;
            int offsetY = bite.y() ? 8 : 0;
            int offsetZ = bite.z() ? 8 : 0;

            assemblePiece(
                    context, 0, false,
                    vec3(offsetX, offsetY, offsetZ),
                    aabb(4, 4, 4),
                    cull(UP | EAST | SOUTH)
            );
            assemblePiece(
                    context, 0, false,
                    vec3(offsetX + 4, offsetY, offsetZ),
                    aabb(4, 4, 4).move(12, 0, 0),
                    cull(UP | WEST | SOUTH)
            );
            assemblePiece(
                    context, 0, false,
                    vec3(offsetX, offsetY, offsetZ + 4),
                    aabb(4, 4, 4).move(0, 0, 12),
                    cull(UP | EAST | NORTH)
            );
            assemblePiece(
                    context, 0, false,
                    vec3(offsetX + 4, offsetY, offsetZ + 4),
                    aabb(4, 4, 4).move(12, 0, 12),
                    cull(UP | WEST | NORTH)
            );
            assemblePiece(
                    context, 0, false,
                    vec3(offsetX, offsetY + 4, offsetZ),
                    aabb(4, 4, 4).move(0, 12, 0),
                    cull(DOWN | EAST | SOUTH)
            );
            assemblePiece(
                    context, 0, false,
                    vec3(offsetX + 4, offsetY + 4, offsetZ),
                    aabb(4, 4, 4).move(12, 12, 0),
                    cull(DOWN | WEST | SOUTH)
            );
            assemblePiece(
                    context, 0, false,
                    vec3(offsetX, offsetY + 4, offsetZ + 4),
                    aabb(4, 4, 4).move(0, 12, 12),
                    cull(DOWN | EAST | NORTH)
            );
            assemblePiece(
                    context, 0, false,
                    vec3(offsetX + 4, offsetY + 4, offsetZ + 4),
                    aabb(4, 4, 4).move(12, 12, 12),
                    cull(DOWN | WEST | NORTH)
            );
        }
    }

}
