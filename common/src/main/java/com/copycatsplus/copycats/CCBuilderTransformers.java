package com.copycatsplus.copycats;

import com.copycatsplus.copycats.content.copycat.base.multistate.MultiStateCopycatBlock;
import com.copycatsplus.copycats.content.copycat.casing.WrappedCasingBlock;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import dev.architectury.injectables.annotations.ExpectPlatform;

public class CCBuilderTransformers {

    @ExpectPlatform
    public static <B extends MultiStateCopycatBlock, P> NonNullUnaryOperator<BlockBuilder<B, P>> multiCopycat() {
        throw new AssertionError("Shouldn't appear");
    }

    @ExpectPlatform
    public static <B extends MultiStateCopycatBlock, P> NonNullUnaryOperator<BlockBuilder<B, P>> testBlockMultiCopycat() {
        throw new AssertionError("Shouldn't appear");
    }

    @ExpectPlatform
    public static <B extends WrappedCasingBlock, P> NonNullUnaryOperator<BlockBuilder<B, P>> wrappedCasing(CTSpriteShiftEntry spriteShift) {
        throw new AssertionError("Shouldn't appear");
    }
}
