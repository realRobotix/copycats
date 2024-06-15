package com.copycatsplus.copycats.content.copycat.vertical_stairs;

import com.copycatsplus.copycats.CCBlockStateProperties.VerticalStairShape;
import com.copycatsplus.copycats.content.copycat.base.model.SimpleCopycatPart;
import com.copycatsplus.copycats.content.copycat.base.model.assembly.GlobalTransform;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;

import static com.copycatsplus.copycats.content.copycat.base.model.assembly.Assembler.*;
import static com.copycatsplus.copycats.content.copycat.base.model.assembly.MutableCullFace.*;

public class CopycatVerticalStairsModel implements SimpleCopycatPart {

    @Override
    public void emitCopycatQuads(BlockState state, CopycatRenderContext<?, ?> context, BlockState material) {
        int facing = (int) state.getValue(CopycatVerticalStairBlock.FACING).toYRot();
        boolean top = state.getValue(CopycatVerticalStairBlock.HALF) == Half.TOP;
        VerticalStairShape shape = state.getValue(CopycatVerticalStairBlock.SHAPE);
        GlobalTransform transform = t -> t.rotate(facing).flipY(top);

        switch (shape) {
            case STRAIGHT -> {
                assemblePiece(context,
                        transform,
                        vec3(0, 0, 0),
                        aabb(4, 8, 16),
                        cull(UP | SOUTH)
                );
                assemblePiece(context,
                        transform,
                        vec3(4, 0, 0),
                        aabb(4, 8, 16).move(12, 0, 0),
                        cull(DOWN | SOUTH)
                );
                assemblePiece(context,
                        transform,
                        vec3(0, 8, 0),
                        aabb(8, 8, 16).move(0, 8, 0),
                        cull(UP | NORTH)
                );
                assemblePiece(context,
                        transform,
                        vec3(8, 8, 0),
                        aabb(8, 4, 16).move(8, 0, 0),
                        cull(DOWN | SOUTH)
                );
                assemblePiece(context,
                        transform,
                        vec3(8, 12, 0),
                        aabb(8, 4, 16).move(8, 12, 0),
                        cull(DOWN | NORTH)
                );
            }
            case INNER_LEFT -> {
                assemblePiece(context,
                        transform,
                        vec3(0, 0, 0),
                        aabb(4, 8, 8),
                        cull(UP | SOUTH | EAST)
                );
                assemblePiece(context,
                        transform,
                        vec3(4, 0, 0),
                        aabb(4, 8, 8).move(12, 0, 0),
                        cull(DOWN | SOUTH | EAST)
                );
                assemblePiece(context,
                        transform,
                        vec3(0, 8, 0),
                        aabb(8, 8, 16).move(0, 8, 0),
                        cull(UP | NORTH)
                );
                assemblePiece(context,
                        transform,
                        vec3(8, 8, 8),
                        aabb(8, 8, 8).move(8, 8, 8),
                        cull(DOWN | NORTH | WEST)
                );
                assemblePiece(context,
                        transform,
                        vec3(8, 12, 0),
                        aabb(8, 4, 8).move(8, 12, 0),
                        cull(DOWN | NORTH | EAST)
                );
                assemblePiece(context,
                        transform,
                        vec3(8, 8, 0),
                        aabb(8, 4, 8).move(8, 0, 0),
                        cull(DOWN | SOUTH | EAST)
                );
                assemblePiece(context,
                        transform,
                        vec3(8, 0, 12),
                        aabb(8, 8, 4).move(8, 0, 12),
                        cull(DOWN | SOUTH | WEST)
                );
                assemblePiece(context,
                        transform,
                        vec3(8, 0, 8),
                        aabb(8, 8, 4).move(8, 0, 0),
                        cull(DOWN | SOUTH | EAST)
                );
                assemblePiece(context,
                        transform,
                        vec3(0, 0, 8),
                        aabb(8, 8, 8).move(0, 0, 8),
                        cull(UP | SOUTH | WEST)
                );
            }
            case INNER_RIGHT -> {
                assemblePiece(context,
                        transform,
                        vec3(0, 0, 8),
                        aabb(4, 8, 8).move(0, 0, 8),
                        cull(UP | SOUTH | WEST)
                );
                assemblePiece(context,
                        transform,
                        vec3(4, 0, 8),
                        aabb(4, 8, 8).move(12, 0, 8),
                        cull(DOWN | SOUTH | WEST)
                );
                assemblePiece(context,
                        transform,
                        vec3(0, 8, 0),
                        aabb(8, 8, 16).move(0, 8, 0),
                        cull(UP | NORTH)
                );
                assemblePiece(context,
                        transform,
                        vec3(8, 8, 0),
                        aabb(8, 8, 8).move(8, 8, 0),
                        cull(DOWN | NORTH | EAST)
                );
                assemblePiece(context,
                        transform,
                        vec3(8, 12, 8),
                        aabb(8, 4, 8).move(8, 12, 8),
                        cull(DOWN | NORTH | WEST)
                );
                assemblePiece(context,
                        transform,
                        vec3(8, 8, 8),
                        aabb(8, 4, 8).move(8, 0, 8),
                        cull(DOWN | SOUTH | WEST)
                );
                assemblePiece(context,
                        transform,
                        vec3(8, 0, 4),
                        aabb(8, 8, 4).move(8, 0, 12),
                        cull(DOWN | SOUTH | WEST)
                );
                assemblePiece(context,
                        transform,
                        vec3(8, 0, 0),
                        aabb(8, 8, 4).move(8, 0, 0),
                        cull(DOWN | SOUTH | EAST)
                );
                assemblePiece(context,
                        transform,
                        vec3(0, 0, 0),
                        aabb(8, 8, 8).move(0, 0, 0),
                        cull(UP | SOUTH | EAST)
                );
            }
            case OUTER_LEFT -> {
                assemblePiece(context,
                        transform,
                        vec3(0, 0, 0),
                        aabb(4, 16, 8).move(0, 0, 0),
                        cull(UP | EAST)
                );
                assemblePiece(context,
                        transform,
                        vec3(4, 0, 0),
                        aabb(4, 16, 8).move(12, 0, 0),
                        cull(DOWN | EAST)
                );
                assemblePiece(context,
                        transform,
                        vec3(0, 0, 8),
                        aabb(4, 8, 8).move(0, 0, 8),
                        cull(UP | SOUTH | WEST)
                );
                assemblePiece(context,
                        transform,
                        vec3(4, 0, 8),
                        aabb(4, 8, 8).move(12, 0, 8),
                        cull(DOWN | SOUTH | WEST)
                );
                assemblePiece(context,
                        transform,
                        vec3(0, 8, 8),
                        aabb(8, 8, 8).move(0, 8, 8),
                        cull(UP | NORTH | WEST)
                );
                assemblePiece(context,
                        transform,
                        vec3(8, 12, 12),
                        aabb(8, 4, 4).move(8, 12, 12),
                        cull(DOWN | NORTH | WEST)
                );
                assemblePiece(context,
                        transform,
                        vec3(8, 12, 8),
                        aabb(8, 4, 4).move(8, 12, 0),
                        cull(DOWN | NORTH | EAST)
                );
                assemblePiece(context,
                        transform,
                        vec3(8, 8, 12),
                        aabb(8, 4, 4).move(8, 0, 12),
                        cull(DOWN | SOUTH | WEST)
                );
                assemblePiece(context,
                        transform,
                        vec3(8, 8, 8),
                        aabb(8, 4, 4).move(8, 0, 0),
                        cull(DOWN | SOUTH | EAST)
                );
            }
            case OUTER_RIGHT -> {
                assemblePiece(context,
                        transform,
                        vec3(0, 0, 8),
                        aabb(4, 16, 8).move(0, 0, 8),
                        cull(UP | WEST)
                );
                assemblePiece(context,
                        transform,
                        vec3(4, 0, 8),
                        aabb(4, 16, 8).move(12, 0, 8),
                        cull(DOWN | WEST)
                );
                assemblePiece(context,
                        transform,
                        vec3(0, 0, 0),
                        aabb(4, 8, 8).move(0, 0, 0),
                        cull(UP | EAST)
                );
                assemblePiece(context,
                        transform,
                        vec3(4, 0, 0),
                        aabb(4, 8, 8).move(12, 0, 0),
                        cull(DOWN | EAST)
                );
                assemblePiece(context,
                        transform,
                        vec3(0, 8, 0),
                        aabb(8, 8, 8).move(0, 8, 0),
                        cull(UP | NORTH | EAST)
                );
                assemblePiece(context,
                        transform,
                        vec3(8, 12, 4),
                        aabb(8, 4, 4).move(8, 12, 12),
                        cull(DOWN | NORTH | WEST)
                );
                assemblePiece(context,
                        transform,
                        vec3(8, 12, 0),
                        aabb(8, 4, 4).move(8, 12, 0),
                        cull(DOWN | NORTH | EAST)
                );
                assemblePiece(context,
                        transform,
                        vec3(8, 8, 4),
                        aabb(8, 4, 4).move(8, 0, 12),
                        cull(DOWN | SOUTH | WEST)
                );
                assemblePiece(context,
                        transform,
                        vec3(8, 8, 0),
                        aabb(8, 4, 4).move(8, 0, 0),
                        cull(DOWN | SOUTH | EAST)
                );
            }
        }
    }
}
