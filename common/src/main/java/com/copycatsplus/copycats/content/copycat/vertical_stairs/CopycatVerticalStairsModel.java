package com.copycatsplus.copycats.content.copycat.vertical_stairs;

import com.copycatsplus.copycats.CCBlockStateProperties;
import com.copycatsplus.copycats.content.copycat.base.model.SimpleCopycatPart;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;

import static com.copycatsplus.copycats.content.copycat.base.model.QuadHelper.*;
import static com.copycatsplus.copycats.content.copycat.base.model.QuadHelper.MutableCullFace.*;

public class CopycatVerticalStairsModel implements SimpleCopycatPart {

    @Override
    public void emitCopycatQuads(BlockState state, CopycatRenderContext context, BlockState material) {
        int facing = (int) state.getValue(CopycatVerticalStairBlock.FACING).toYRot();
        boolean flip = state.getValue(CopycatVerticalStairBlock.HALF).equals(Half.BOTTOM);
        CCBlockStateProperties.VerticalStairShape shape = state.getValue(CopycatVerticalStairBlock.VERTICAL_STAIR_SHAPE);
        switch (shape) {
            case STRAIGHT -> {
                if (flip) {
                    assemblePiece(context, facing, false,
                            vec3(0, 0, 0),
                            aabb(4, 16, 8),
                            cull(EAST | SOUTH));
                    assemblePiece(context, facing, false,
                            vec3(4, 0, 0),
                            aabb(4, 16, 8).move(12, 0, 0),
                            cull(WEST | SOUTH));
                    assemblePiece(context, facing, false,
                            vec3(8, 0, 8),
                            aabb(8, 16, 4).move(8, 0, 0),
                            cull(SOUTH | WEST));
                    assemblePiece(context, facing, false,
                            vec3(8, 0, 12),
                            aabb(8, 16, 4).move(8, 0 ,12),
                            cull(NORTH | WEST));
                    assemblePiece(context, facing, false,
                            vec3(0, 0, 8),
                            aabb(8, 16, 8).move(0, 0, 8),
                            cull(NORTH | EAST));
                } else {
                    assemblePiece(context, facing, false,
                            vec3(8, 0, 0),
                            aabb(8, 16, 4).move(8, 0, 0),
                            cull(SOUTH | WEST));
                    assemblePiece(context, facing, false,
                            vec3(8, 0, 4),
                            aabb(8, 16, 4).move(8, 0, 12),
                            cull(WEST | NORTH));
                    assemblePiece(context, facing, false,
                            vec3(0, 0, 0),
                            aabb(8, 16, 8).move(0, 0, 0),
                            cull(EAST | SOUTH));
                    assemblePiece(context, facing, false,
                            vec3(0, 0, 8),
                            aabb(4, 16, 8).move(0, 0, 8),
                            cull(NORTH | EAST));
                    assemblePiece(context, facing, false,
                            vec3(4, 0, 8),
                            aabb(4, 16, 8).move(12, 0, 8),
                            cull(NORTH | WEST));
                }
            }
            case OUTER_LEFT -> {
                assemblePiece(context, facing, flip,
                        vec3(0, 8, 8),
                        aabb(4, 8, 8).move(0, 8, 8),
                        cull(NORTH | EAST | DOWN));
                assemblePiece(context, facing, flip,
                        vec3(4, 8, 8),
                        aabb(4, 8, 8).move(12, 8, 8),
                        cull(NORTH | WEST | DOWN));
                assemblePiece(context, facing, flip,
                        vec3(0, 8, 0),
                        aabb(8, 8, 8).move(0, 8, 0),
                        cull( EAST | SOUTH | DOWN));
                assemblePiece(context, facing, flip,
                        vec3(0, 0, 0),
                        aabb(4, 8, 16).move(0, 0, 0),
                        cull(EAST | UP));
                assemblePiece(context, facing, flip,
                        vec3(4, 0, 0),
                        aabb(4, 8, 16).move(12, 0, 0),
                        cull(WEST | UP));
                assemblePiece(context, facing, flip,
                        vec3(8, 12, 0),
                        aabb(8, 4, 4).move(8, 12, 0),
                        cull(SOUTH | WEST | DOWN));
                assemblePiece(context, facing, flip,
                        vec3(8, 12, 4),
                        aabb(8, 4, 4).move(8, 12, 12),
                        cull(NORTH | WEST | DOWN));
                assemblePiece(context, facing, flip,
                        vec3(8, 8, 4),
                        aabb(8, 4, 4).move(8, 0, 12),
                        cull(NORTH | WEST | UP));
                assemblePiece(context, facing, flip,
                        vec3(8, 8, 0),
                        aabb(8, 4, 4).move(8, 0, 0),
                        cull(SOUTH | WEST | UP));
            }
            case OUTER_RIGHT -> {
                assemblePiece(context, facing, flip,
                        vec3(0,0,0),
                        aabb(4, 8, 16),
                        cull(EAST | UP));
                assemblePiece(context, facing, flip,
                        vec3(4,0,0),
                        aabb(4, 8, 16).move(12, 0, 0),
                        cull(WEST | UP));
                assemblePiece(context, facing, flip,
                        vec3(0,8,0),
                        aabb(4, 8, 8).move(0, 8, 0),
                        cull(EAST | SOUTH | DOWN));
                assemblePiece(context, facing, flip,
                        vec3(4,8,0),
                        aabb(4, 8, 8).move(12, 8, 0),
                        cull(SOUTH | WEST | DOWN));
                assemblePiece(context, facing, flip,
                        vec3(0,8,8),
                        aabb(8, 8, 8).move(0, 8, 8),
                        cull(NORTH | EAST | DOWN));
                assemblePiece(context, facing, flip,
                        vec3(8,8,8),
                        aabb(8, 4, 4).move(8, 0,0),
                        cull(SOUTH | WEST | UP));
                assemblePiece(context, facing, flip,
                        vec3(8,8,12),
                        aabb(8, 4, 4).move(8, 0, 12),
                        cull(NORTH | WEST | UP));
                assemblePiece(context, facing, flip,
                        vec3(8,12,8),
                        aabb(8, 4, 4).move(8, 12, 0),
                        cull(SOUTH | WEST | DOWN));
                assemblePiece(context, facing, flip,
                        vec3(8,12,12),
                        aabb(8, 4, 4).move(8, 12, 12),
                        cull(NORTH | WEST | DOWN));
            }
        }
    }
}
