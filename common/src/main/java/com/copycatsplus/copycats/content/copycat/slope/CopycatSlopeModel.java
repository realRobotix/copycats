package com.copycatsplus.copycats.content.copycat.slope;

import com.copycatsplus.copycats.content.copycat.base.model.SimpleCopycatPart;
import com.copycatsplus.copycats.content.copycat.base.model.assembly.GlobalTransform;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;

import static com.copycatsplus.copycats.content.copycat.base.model.assembly.Assembler.*;
import static com.copycatsplus.copycats.content.copycat.base.model.assembly.MutableCullFace.*;
import static com.copycatsplus.copycats.content.copycat.base.model.assembly.MutableCullFace.SOUTH;
import static com.copycatsplus.copycats.content.copycat.base.model.assembly.quad.QuadSlope.map;

public class CopycatSlopeModel implements SimpleCopycatPart {

    private final boolean enhanced;

    public CopycatSlopeModel(boolean enhanced) {
        this.enhanced = enhanced;
    }

    @Override
    public void emitCopycatQuads(BlockState state, CopycatRenderContext<?, ?> context, BlockState material) {
        Direction facing = state.getValue(CopycatSlopeBlock.FACING);
        Half half = state.getValue(CopycatSlopeBlock.HALF);
        int rot = (int) facing.toYRot();
        boolean flipY = half == Half.TOP;
        GlobalTransform transform = t -> t.flipY(flipY).rotateY(rot);
        if (enhanced)
            assembleSlope(context, transform, 16, 0, 3);
        else
            assembleSlope(context, transform, 16, 0);
    }

    public static void assembleSlope(CopycatRenderContext<?, ?> context, GlobalTransform transform, double maxHeight, double minHeight) {
        assemblePiece(context,
                transform,
                vec3(0, 0, 0),
                aabb(16, 16, 16),
                cull(NORTH),
                updateUV(slope(Direction.UP, (a, b) -> map(0, 16, minHeight, maxHeight, b)))
        );
    }

    public static void assembleSlope(CopycatRenderContext<?, ?> context, GlobalTransform transform, double maxHeight, double minHeight, double margin) {
        final double angle = Math.atan2(maxHeight - minHeight, 16) / 2;
        final double marginAdj = margin / Math.tan(angle);
        final double midLength = Math.sqrt((maxHeight - minHeight) * (maxHeight - minHeight) + 16 * 16) - 2 * marginAdj;
        final double marginAdjExcess = marginAdj - Math.floor(marginAdj);
        final double alignedLength = ((int) Math.floor(midLength + 2 * marginAdjExcess)) % 2 == 0
                ? Math.floor(midLength + 2 * marginAdjExcess) - 2 * marginAdjExcess
                : Math.floor(midLength + 2 * marginAdjExcess) + 1 - 2 * marginAdjExcess;

        assemblePiece(context,
                transform,
                vec3(0, 0, 0),
                aabb(16, 16, marginAdj),
                cull(UP | NORTH | SOUTH),
                updateUV(slope(Direction.UP, (a, b) -> map(0, marginAdj, 0, margin, b)))
        );
        assemblePiece(context,
                transform,
                vec3(0, 0, marginAdj),
                aabb(16, 16, 16 - margin - marginAdj).move(0, 0, marginAdj),
                cull(UP | NORTH | SOUTH),
                updateUV(slope(Direction.UP, (a, b) -> map(marginAdj, 16 - margin, margin, 16 - marginAdj, b)))
        );
        assemblePiece(context,
                transform,
                vec3(0, 0, 16 - margin),
                aabb(16, 16, margin).move(0, 0, 16 - margin),
                cull(UP | NORTH),
                updateUV(slope(Direction.UP, (a, b) -> map(16 - margin, 16, 16 - marginAdj, 16, b)))
        );
        assemblePiece(context,
                transform,
                vec3(0, 0, 0),
                aabb(16, 16, marginAdj),
                cull(DOWN | NORTH | SOUTH),
                updateUV(slope(Direction.DOWN, (a, b) -> map(0, marginAdj, 0, margin, b))),
                translate(0, -16, 0),
                rotate(
                        pivot(0, 0, 0),
                        angle(-45, 0, 0)
                )
        );
        assemblePiece(context,
                transform,
                vec3(0, 16 - margin, 16 - alignedLength / 2),
                aabb(16, margin, alignedLength / 2).move(0, 16 - margin, marginAdj),
                cull(DOWN | NORTH | SOUTH),
                scale(
                        pivot(16, 16, 16),
                        scale(1, 1, midLength / alignedLength)
                ),
                translate(0, 0, -marginAdj - midLength / 2),
                rotate(
                        pivot(16, 16, 16),
                        angle(-45, 0, 0)
                )
        );
        assemblePiece(context,
                transform,
                vec3(0, 16 - margin, 16 - alignedLength / 2),
                aabb(16, margin, alignedLength / 2).move(0, 16 - margin, 16 - marginAdj - alignedLength / 2),
                cull(DOWN | NORTH | SOUTH),
                scale(
                        pivot(16, 16, 16),
                        scale(1, 1, midLength / alignedLength)
                ),
                translate(0, 0, -marginAdj),
                rotate(
                        pivot(16, 16, 16),
                        angle(-45, 0, 0)
                )
        );
        assemblePiece(context,
                transform,
                vec3(0, 0, 16 - marginAdj),
                aabb(16, 16, marginAdj).move(0, 0, 16 - marginAdj),
                cull(DOWN | NORTH | SOUTH),
                updateUV(slope(Direction.DOWN, (a, b) -> map(16 - marginAdj, 16, margin, 0, b))),
                rotate(
                        pivot(16, 16, 16),
                        angle(-45, 0, 0)
                )
        );
    }
}
