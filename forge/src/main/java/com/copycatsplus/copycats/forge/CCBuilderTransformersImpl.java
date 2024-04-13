package com.copycatsplus.copycats.forge;

import com.copycatsplus.copycats.content.copycat.base.multi.MultiStateCopycatBlock;
import com.simibubi.create.content.decoration.copycat.CopycatBlock;
import com.simibubi.create.foundation.data.SharedProperties;
import com.simibubi.create.foundation.data.TagGen;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.material.MapColor;

public class CCBuilderTransformersImpl {

    public static <B extends MultiStateCopycatBlock, P> NonNullUnaryOperator<BlockBuilder<B, P>> multiCopycat() {
        return b -> b.initialProperties(SharedProperties::softMetal)
                .blockstate((c, p) -> p.simpleBlock(c.get(), p.models()
                        .getExistingFile(p.mcLoc("air"))))
                .initialProperties(SharedProperties::softMetal)
                .properties(p -> p.noOcclusion()
                        .mapColor(MapColor.NONE))
                .addLayer(() -> RenderType::solid)
                .addLayer(() -> RenderType::cutout)
                .addLayer(() -> RenderType::cutoutMipped)
                .addLayer(() -> RenderType::translucent)
                .color(() -> CopycatBlock::wrappedColor)
                .transform(TagGen.axeOrPickaxe());
    }
}
