package com.copycatsplus.copycats.content.copycat.base.model.assembly;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;

public class QuadShear extends TrackingQuadTransform {
    public final Axis axis;
    public final Direction direction;
    public final double amount;

    public QuadShear(Axis axis, Direction direction, double amount) {
        this.axis = axis;
        this.direction = direction;
        this.amount = amount;
    }

    @Override
    public <T> T transformVertices(T vertexData, TextureAtlasSprite sprite) {
        return transformVertices(this, vertexData, sprite);
    }

    @ExpectPlatform
    public static <T> T transformVertices(QuadShear self, T vertexData, TextureAtlasSprite sprite) {
        return null;
    }
}
