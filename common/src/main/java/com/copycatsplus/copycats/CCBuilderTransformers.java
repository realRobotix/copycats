package com.copycatsplus.copycats;

import com.copycatsplus.copycats.content.copycat.base.multistate.MultiStateCopycatBlock;
import com.copycatsplus.copycats.content.copycat.casing.WrappedCasingBlock;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.decoration.encasing.CasingBlock;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.block.connected.HorizontalCTBehaviour;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import dev.architectury.injectables.annotations.ExpectPlatform;

import java.util.function.Supplier;

import static com.simibubi.create.foundation.data.CreateRegistrate.casingConnectivity;
import static com.simibubi.create.foundation.data.CreateRegistrate.connectedTextures;
import static com.simibubi.create.foundation.data.TagGen.axeOrPickaxe;

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

    @ExpectPlatform
    public static <B extends CasingBlock, P> NonNullUnaryOperator<BlockBuilder<B, P>> wrappedLayeredCasing(
            CTSpriteShiftEntry ct, CTSpriteShiftEntry ct2) {
        throw new AssertionError("Shouldn't appear");
    }
}
