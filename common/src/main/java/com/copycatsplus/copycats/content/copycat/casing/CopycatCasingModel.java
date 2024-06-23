package com.copycatsplus.copycats.content.copycat.casing;

import com.copycatsplus.copycats.content.copycat.base.model.assembly.GlobalTransform;
import com.copycatsplus.copycats.content.copycat.base.model.multistate.SimpleMultiStateCopycatPart;
import net.minecraft.world.level.block.state.BlockState;

import static com.copycatsplus.copycats.content.copycat.base.model.assembly.Assembler.*;

public class CopycatCasingModel implements SimpleMultiStateCopycatPart {
    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext<?, ?> context, BlockState material) {
        if (key.equals(CopycatCasingBlock.Slot.OUTER.getSerializedName())) {
            assembleQuad(context);
        } else {
            assemblePiece(context,
                    GlobalTransform.IDENTITY,
                    vec3(0, 0, 0),
                    aabb(16, 16, 16),
                    cull(0),
                    scale(
                            pivot(8, 8, 8),
                            scale(15.96 / 16, 15.96 / 16, 15.96 / 16)
                    )
            );
        }
    }
}
