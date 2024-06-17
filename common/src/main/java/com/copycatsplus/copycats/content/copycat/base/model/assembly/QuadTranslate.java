package com.copycatsplus.copycats.content.copycat.base.model.assembly;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public class QuadTranslate extends TrackingQuadTransform {
    public final double x;
    public final double y;
    public final double z;

    public QuadTranslate(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public <T> T transformVertices(T vertexData, TextureAtlasSprite sprite) {
        return transformVertices(this, vertexData, sprite);
    }

    @ExpectPlatform
    public static <T> T transformVertices(QuadTranslate self, T vertexData, TextureAtlasSprite sprite) {
        return null;
    }
}
