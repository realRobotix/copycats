package com.copycatsplus.copycats;

import com.copycatsplus.copycats.content.copycat.base.multi.MultiStateCopycatBlock;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import dev.architectury.injectables.annotations.ExpectPlatform;

public class CCBuilderTransformers {

    @ExpectPlatform
    public static <B extends MultiStateCopycatBlock, P> NonNullUnaryOperator<BlockBuilder<B, P>> multiCopycat() {
        throw new AssertionError("Shouldn't appear");
    }
}
