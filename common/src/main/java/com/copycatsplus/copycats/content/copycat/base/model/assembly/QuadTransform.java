package com.copycatsplus.copycats.content.copycat.base.model.assembly;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;

import java.util.List;

public interface QuadTransform extends GlobalTransform.Transformable<QuadTransform> {
    <T> T transformVertices(T vertexData, TextureAtlasSprite sprite);
}
