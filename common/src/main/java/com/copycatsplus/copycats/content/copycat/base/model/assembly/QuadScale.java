package com.copycatsplus.copycats.content.copycat.base.model.assembly;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.phys.Vec3;

public class QuadScale extends TrackingQuadTransform {
    public final Vec3 pivot;
    public final Vec3 scale;

    public QuadScale(MutableVec3.AsPivot pivot, MutableVec3 scale) {
        this.pivot = pivot.toVec3Unscaled();
        this.scale = scale.toVec3Unscaled();
    }

    @Override
    public <T> T transformVertices(T vertexData, TextureAtlasSprite sprite) {
        return transformVertices(this, vertexData, sprite);
    }

    @ExpectPlatform
    public static <T> T transformVertices(QuadScale self, T vertexData, TextureAtlasSprite sprite) {
        return null;
    }
}
