package com.copycatsplus.copycats.fabric;

import com.copycatsplus.copycats.CCSpriteShifts;
import com.copycatsplus.copycats.content.copycat.base.multistate.MultiStateCopycatBlock;
import com.copycatsplus.copycats.content.copycat.casing.WrappedCasingBlock;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.decoration.encasing.EncasedCTBehaviour;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.data.SharedProperties;
import com.simibubi.create.foundation.data.TagGen;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;

import static com.simibubi.create.foundation.data.CreateRegistrate.casingConnectivity;
import static com.simibubi.create.foundation.data.CreateRegistrate.connectedTextures;
import static com.simibubi.create.foundation.data.TagGen.axeOrPickaxe;

public class CCBuilderTransformersImpl {

    public static <B extends MultiStateCopycatBlock, P> NonNullUnaryOperator<BlockBuilder<B, P>> multiCopycat() {
        return b -> b.initialProperties(SharedProperties::softMetal)
                .blockstate((c, p) -> p.simpleBlock(c.get(), p.models()
                        .getExistingFile(p.mcLoc("air"))))
                .initialProperties(SharedProperties::softMetal)
                .properties(p -> p.noOcclusion()
                        .mapColor(MapColor.NONE).lightLevel(state -> state.getLightEmission()))
                .addLayer(() -> RenderType::solid)
                .addLayer(() -> RenderType::cutout)
                .addLayer(() -> RenderType::cutoutMipped)
                .addLayer(() -> RenderType::translucent)
                .color(() -> MultiStateCopycatBlock::wrappedColor)
                .transform(TagGen.axeOrPickaxe());
    }

    public static <B extends MultiStateCopycatBlock, P> NonNullUnaryOperator<BlockBuilder<B, P>> testBlockMultiCopycat() {
        return b -> b.initialProperties(SharedProperties::softMetal)
                .blockstate((c, p) -> p.simpleBlock(c.get(), p.models()
                        .getExistingFile(p.mcLoc("air"))))
                .initialProperties(SharedProperties::softMetal)
                .properties(p -> p.noOcclusion()
                        .mapColor(MapColor.NONE).lightLevel(state -> state.getLightEmission()))
                .addLayer(() -> RenderType::solid)
                .addLayer(() -> RenderType::cutout)
                .addLayer(() -> RenderType::cutoutMipped)
                .addLayer(() -> RenderType::translucent)
                .color(() -> MultiStateCopycatBlock::wrappedColor);
    }

    public static <B extends WrappedCasingBlock, P> NonNullUnaryOperator<BlockBuilder<B, P>> wrappedCasing(CTSpriteShiftEntry spriteShift) {
        return b -> b.initialProperties(SharedProperties::stone)
                .properties(p -> p.mapColor(MapColor.PODZOL).sound(SoundType.WOOD).noOcclusion())
                .transform(axeOrPickaxe())
                .blockstate((c, p) -> p.simpleBlock(c.get()))
                .addLayer(() -> RenderType::cutoutMipped)
                .onRegister(connectedTextures(() -> new EncasedCTBehaviour(spriteShift)))
                .onRegister(casingConnectivity((block, cc) -> cc.makeCasing(block, spriteShift)))
                .tag(AllTags.AllBlockTags.CASING.tag);
    }
}
