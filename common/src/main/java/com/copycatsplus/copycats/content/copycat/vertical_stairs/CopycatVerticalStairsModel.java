package com.copycatsplus.copycats.content.copycat.vertical_stairs;

import com.copycatsplus.copycats.CCBlockStateProperties.VerticalStairShape;
import com.copycatsplus.copycats.content.copycat.base.model.SimpleCopycatPart;
import com.copycatsplus.copycats.content.copycat.base.model.assembly.GlobalTransform;
import net.minecraft.world.level.block.state.BlockState;

import static com.copycatsplus.copycats.content.copycat.base.model.assembly.Assembler.*;

public class CopycatVerticalStairsModel implements SimpleCopycatPart {

    @Override
    public void emitCopycatQuads(BlockState state, CopycatRenderContext<?, ?> context, BlockState material) {
        int facing = (int) state.getValue(CopycatVerticalStairBlock.FACING).toYRot();
        VerticalStairShape shape = state.getValue(CopycatVerticalStairBlock.SHAPE);

        switch (shape) {
            case STRAIGHT -> {
                GlobalTransform transform = t -> t.rotateY(facing);
            }
            case INNER_BOTTOM, INNER_TOP -> {
                boolean flipY = shape == VerticalStairShape.INNER_TOP;
                GlobalTransform transform = t -> t.rotateY(facing).flipY(flipY);
            }
            case INNER_RIGHT -> {
            }
            case OUTER_LEFT -> {
            }
            case OUTER_RIGHT -> {
            }
        }
    }
}
