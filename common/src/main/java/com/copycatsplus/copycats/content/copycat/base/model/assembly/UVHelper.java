package com.copycatsplus.copycats.content.copycat.base.model.assembly;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.phys.Vec3;

public class UVHelper {
    @ExpectPlatform
    public static <T> T mapWithUV(T vertexData, TextureAtlasSprite sprite, QuadVertexMapper mapper) {
        return null;
    }

    @FunctionalInterface
    public interface QuadVertexMapper {
        Vec3 map(Vec3 vertex, int vertexIndex);
    }
}
