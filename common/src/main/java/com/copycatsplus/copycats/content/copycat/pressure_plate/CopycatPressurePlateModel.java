package com.copycatsplus.copycats.content.copycat.pressure_plate;

import com.copycatsplus.copycats.content.copycat.base.model.SimpleCopycatPart;
import com.copycatsplus.copycats.content.copycat.base.model.assembly.GlobalTransform;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.WeightedPressurePlateBlock;
import net.minecraft.world.level.block.state.BlockState;

import static com.copycatsplus.copycats.content.copycat.base.model.assembly.Assembler.*;
import static com.copycatsplus.copycats.content.copycat.base.model.assembly.MutableCullFace.*;

public class CopycatPressurePlateModel implements SimpleCopycatPart {


    @Override
    public void emitCopycatQuads(BlockState state, CopycatRenderContext<?, ?> context, BlockState material) {
        boolean powered = state.getOptionalValue(PressurePlateBlock.POWERED)
                .or(() -> state.getOptionalValue(WeightedPressurePlateBlock.POWER).map(power -> power > 0))
                .orElse(false);
        if (powered) {
            assemblePiece(
                    context,
                    GlobalTransform.IDENTITY,
                    vec3(1, 0, 1),
                    aabb(7, 0.5f, 7).move(0, 0, 0),
                    cull(SOUTH | EAST)
            );
            assemblePiece(
                    context,
                    GlobalTransform.IDENTITY,
                    vec3(8, 0, 8),
                    aabb(7, 0.5f, 7).move(9, 0, 9),
                    cull(NORTH | WEST)
            );
            assemblePiece(
                    context,
                    GlobalTransform.IDENTITY,
                    vec3(8, 0, 1),
                    aabb(7, 0.5f, 7).move(9, 0, 0),
                    cull(WEST | SOUTH)
            );
            assemblePiece(
                    context,
                    GlobalTransform.IDENTITY,
                    vec3(1, 0, 8),
                    aabb(7, 0.5f, 7).move(0, 0, 9),
                    cull(NORTH | EAST)
            );
        } else {
            assemblePiece(
                    context,
                    GlobalTransform.IDENTITY,
                    vec3(1, 0, 1),
                    aabb(7, 1, 7).move(0, 0, 0),
                    cull(SOUTH | EAST)
            );
            assemblePiece(
                    context,
                    GlobalTransform.IDENTITY,
                    vec3(8, 0, 8),
                    aabb(7, 1, 7).move(9, 0, 9),
                    cull(NORTH | WEST)
            );
            assemblePiece(
                    context,
                    GlobalTransform.IDENTITY,
                    vec3(8, 0, 1),
                    aabb(7, 1, 7).move(9, 0, 0),
                    cull(WEST | SOUTH)
            );
            assemblePiece(
                    context,
                    GlobalTransform.IDENTITY,
                    vec3(1, 0, 8),
                    aabb(7, 1, 7).move(0, 0, 9),
                    cull(NORTH | EAST)
            );
        }
    }
}
