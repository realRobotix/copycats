package com.copycatsplus.copycats.content.copycat.vertical_stairs;

import com.copycatsplus.copycats.CCBlockStateProperties.VerticalStairShape;
import com.copycatsplus.copycats.content.copycat.base.model.SimpleCopycatPart;
import com.copycatsplus.copycats.content.copycat.base.model.assembly.GlobalTransform;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.StairsShape;

import static com.copycatsplus.copycats.content.copycat.base.model.assembly.Assembler.*;
import static com.copycatsplus.copycats.content.copycat.base.model.assembly.MutableCullFace.*;

public class CopycatVerticalStairsEnhancedModel implements SimpleCopycatPart {

    @Override
    public void emitCopycatQuads(BlockState state, CopycatRenderContext<?, ?> context, BlockState material) {
        int facing = (int) state.getValue(CopycatVerticalStairBlock.FACING).toYRot();
        boolean top = state.getValue(CopycatVerticalStairBlock.HALF) == Half.TOP;
        VerticalStairShape shape = state.getValue(CopycatVerticalStairBlock.SHAPE);

        switch (shape) {
            case STRAIGHT -> {
                GlobalTransform transform = t -> t.rotate(facing).flipY(top);
                assemblePiece(
                        context,
                        transform,
                        vec3(4, 12, 0),
                        aabb(12, 4, 16).move(4, 12, 0),
                        cull(NORTH | DOWN)
                );
                assemblePiece(
                        context,
                        transform,
                        vec3(8, 8, 0),
                        aabb(8, 2, 16).move(8, 0, 0),
                        cull(SOUTH | DOWN)
                );
                assemblePiece(
                        context,
                        transform,
                        vec3(7, 8, 0),
                        aabb(1, 2, 16).move(7, 0, 0),
                        cull(NORTH | SOUTH | UP | DOWN)
                );
                assemblePiece(
                        context,
                        transform,
                        vec3(5, 10, 0),
                        aabb(11, 2, 16).move(5, 2, 0),
                        cull(NORTH | SOUTH | DOWN)
                );
                assemblePiece(
                        context,
                        transform,
                        vec3(0, 0, 0),
                        aabb(4, 16, 16).move(0, 0, 0),
                        cull(UP)
                );
                assemblePiece(
                        context,
                        transform,
                        vec3(7, 0, 0),
                        aabb(1, 8, 16).move(15, 0, 0),
                        cull(SOUTH | DOWN)
                );
                assemblePiece(
                        context,
                        transform,
                        vec3(5, 0, 0),
                        aabb(2, 10, 16).move(13, 0, 0),
                        cull(SOUTH | UP | DOWN)
                );
                assemblePiece(
                        context,
                        transform,
                        vec3(4, 0, 0),
                        aabb(1, 12, 16).move(12, 0, 0),
                        cull(SOUTH | UP | DOWN)
                );
            }
            case INNER_LEFT, INNER_RIGHT -> {
                boolean flipX = shape == StairsShape.INNER_RIGHT;
                GlobalTransform transform = t -> t.flipX(flipX).rotate(facing).flipY(top);
                assemblePiece(
                        context,
                        transform,
                        vec3(4, 12, 0),
                        aabb(12, 4, 16).move(4, 12, 0),
                        cull(NORTH | DOWN)
                );
                assemblePiece(
                        context,
                        transform,
                        vec3(4, 0, 12),
                        aabb(12, 12, 4).move(4, 0, 12),
                        cull(SOUTH | WEST | DOWN)
                );
                assemblePiece(
                        context,
                        transform,
                        vec3(0, 0, 0),
                        aabb(4, 16, 16).move(0, 0, 0),
                        cull(UP)
                );
                assemblePiece(
                        context,
                        transform,
                        vec3(4, 0, 0),
                        aabb(1, 12, 12).move(12, 0, 0),
                        cull(EAST | SOUTH | UP | DOWN)
                );
                assemblePiece(
                        context,
                        transform,
                        vec3(5, 0, 0),
                        aabb(2, 10, 10).move(13, 0, 0),
                        cull(EAST | SOUTH | UP | DOWN)
                );
                assemblePiece(
                        context,
                        transform,
                        vec3(7, 0, 0),
                        aabb(1, 8, 8).move(15, 0, 0),
                        cull(EAST | SOUTH | DOWN)
                );
                assemblePiece(
                        context,
                        transform,
                        vec3(8, 8, 0),
                        aabb(8, 2, 8).move(8, 0, 0),
                        cull(EAST | SOUTH | DOWN)
                );
                assemblePiece(
                        context,
                        transform,
                        vec3(8, 8, 8),
                        aabb(8, 2, 1).move(8, 0, 8),
                        cull(NORTH | EAST | SOUTH | WEST | DOWN)
                );
                assemblePiece(
                        context,
                        transform,
                        vec3(8, 8, 9),
                        aabb(8, 2, 1).move(8, 8, 1),
                        cull(NORTH | EAST | SOUTH | WEST | DOWN)
                );
                assemblePiece(
                        context,
                        transform,
                        vec3(5, 10, 0),
                        aabb(11, 2, 11).move(5, 2, 0),
                        cull(NORTH | EAST | SOUTH | DOWN)
                );
                assemblePiece(
                        context,
                        transform,
                        vec3(7, 8, 0),
                        aabb(1, 2, 10).move(7, 0, 0),
                        cull(NORTH | EAST | SOUTH | UP | DOWN)
                );
                assemblePiece(
                        context,
                        transform,
                        vec3(7, 0, 8),
                        aabb(1, 8, 2).move(7, 0, 0),
                        cull(EAST | SOUTH | WEST | UP | DOWN)
                );
                assemblePiece(
                        context,
                        transform,
                        vec3(8, 0, 8),
                        aabb(8, 8, 2).move(8, 0, 0),
                        cull(EAST | SOUTH | DOWN)
                );
                assemblePiece(
                        context,
                        transform,
                        vec3(5, 0, 10),
                        aabb(11, 10, 2).move(5, 0, 2),
                        cull(EAST | SOUTH | WEST | DOWN)
                );
                assemblePiece(
                        context,
                        transform,
                        vec3(5, 10, 11),
                        aabb(11, 2, 1).move(5, 10, 3),
                        cull(NORTH | EAST | SOUTH | WEST | DOWN)
                );
            }
            case OUTER_LEFT, OUTER_RIGHT -> {
                boolean flipX = shape == StairsShape.OUTER_RIGHT;
                GlobalTransform transform = t -> t.flipX(flipX).rotate(facing).flipY(top);
                assemblePiece(
                        context,
                        transform,
                        vec3(4, 12, 12),
                        aabb(12, 4, 4).move(4, 12, 12),
                        cull(NORTH | WEST | DOWN)
                );
                assemblePiece(
                        context,
                        transform,
                        vec3(8, 8, 12),
                        aabb(8, 1, 4).move(8, 0, 12),
                        cull(SOUTH | WEST | DOWN)
                );
                assemblePiece(
                        context,
                        transform,
                        vec3(6, 9, 12),
                        aabb(10, 2, 4).move(6, 1, 12),
                        cull(NORTH | SOUTH | WEST | DOWN)
                );
                assemblePiece(
                        context,
                        transform,
                        vec3(4, 11, 12),
                        aabb(12, 1, 4).move(4, 3, 12),
                        cull(NORTH | SOUTH | WEST | DOWN)
                );
                assemblePiece(
                        context,
                        transform,
                        vec3(8, 12, 8),
                        aabb(8, 4, 1).move(8, 12, 0),
                        cull(NORTH | EAST | DOWN)
                );
                assemblePiece(
                        context,
                        transform,
                        vec3(6, 12, 9),
                        aabb(10, 4, 2).move(6, 12, 1),
                        cull(NORTH | EAST | WEST | DOWN)
                );
                assemblePiece(
                        context,
                        transform,
                        vec3(4, 12, 11),
                        aabb(12, 4, 1).move(4, 12, 3),
                        cull(NORTH | EAST | WEST | DOWN)
                );
                assemblePiece(
                        context,
                        transform,
                        vec3(8, 8, 8),
                        aabb(8, 4, 4).move(8, 0, 0),
                        cull(EAST | SOUTH | DOWN)
                );
                assemblePiece(
                        context,
                        transform,
                        vec3(0, 0, 0),
                        aabb(4, 16, 16).move(0, 0, 0),
                        cull(UP)
                );
                assemblePiece(
                        context,
                        transform,
                        vec3(4, 0, 0),
                        aabb(4, 8, 8).move(12, 0, 0),
                        cull(EAST | SOUTH | DOWN)
                );
                assemblePiece(
                        context,
                        transform,
                        vec3(6, 0, 8),
                        aabb(2, 8, 8).move(14, 0, 8),
                        cull(SOUTH | WEST | DOWN)
                );
                assemblePiece(
                        context,
                        transform,
                        vec3(4, 0, 8),
                        aabb(2, 11, 8).move(12, 0, 8),
                        cull(SOUTH | WEST | UP | DOWN)
                );
                assemblePiece(
                        context,
                        transform,
                        vec3(6, 8, 8),
                        aabb(2, 1, 8).move(14, 8, 8),
                        cull(NORTH | SOUTH | WEST | UP | DOWN)
                );
                assemblePiece(
                        context,
                        transform,
                        vec3(6, 8, 0),
                        aabb(2, 8, 8).move(14, 8, 0),
                        cull(NORTH | EAST | DOWN)
                );
                assemblePiece(
                        context,
                        transform,
                        vec3(4, 8, 0),
                        aabb(2, 8, 11).move(12, 8, 0),
                        cull(NORTH | EAST | UP | DOWN)
                );
                assemblePiece(
                        context,
                        transform,
                        vec3(6, 8, 8),
                        aabb(2, 8, 1).move(14, 8, 8),
                        cull(NORTH | EAST | WEST | UP | DOWN)
                );
            }
        }
    }
}
