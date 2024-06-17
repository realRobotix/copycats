package com.copycatsplus.copycats.content.copycat.base.model.assembly.fabric;

import com.copycatsplus.copycats.content.copycat.base.model.assembly.UVHelper;
import com.simibubi.create.foundation.model.BakedQuadHelper;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.phys.Vec3;

import static com.simibubi.create.foundation.block.render.SpriteShiftEntry.getUnInterpolatedU;
import static com.simibubi.create.foundation.block.render.SpriteShiftEntry.getUnInterpolatedV;

public class UVHelperImpl {
    public static <T> T mapWithUV(T data, TextureAtlasSprite sprite, UVHelper.QuadVertexMapper mapper) {
        MutableQuadView quad = (MutableQuadView) data;
        Vec3 xyz0 = BakedQuadHelper.getXYZ(quad, 0);
        Vec3 xyz1 = BakedQuadHelper.getXYZ(quad, 1);
        Vec3 xyz2 = BakedQuadHelper.getXYZ(quad, 2);
        Vec3 xyz3 = BakedQuadHelper.getXYZ(quad, 3);

        Vec3 uAxis = xyz3.add(xyz2)
                .scale(.5);
        Vec3 vAxis = xyz1.add(xyz2)
                .scale(.5);
        Vec3 center = xyz3.add(xyz2)
                .add(xyz0)
                .add(xyz1)
                .scale(.25);

        float u0 = quad.spriteU(0, 0);
        float u3 = quad.spriteU(3, 0);
        float v0 = quad.spriteV(0, 0);
        float v1 = quad.spriteV(1, 0);

        float uScale = (float) Math
                .round((getUnInterpolatedU(sprite, u3) - getUnInterpolatedU(sprite, u0)) / xyz3.distanceTo(xyz0));
        float vScale = (float) Math
                .round((getUnInterpolatedV(sprite, v1) - getUnInterpolatedV(sprite, v0)) / xyz1.distanceTo(xyz0));

        if (uScale == 0) {
            float v3 = quad.spriteV(3, 0);
            float u1 = quad.spriteU(1, 0);
            uAxis = xyz1.add(xyz2)
                    .scale(.5);
            vAxis = xyz3.add(xyz2)
                    .scale(.5);
            uScale = (float) Math
                    .round((getUnInterpolatedU(sprite, u1) - getUnInterpolatedU(sprite, u0)) / xyz1.distanceTo(xyz0));
            vScale = (float) Math
                    .round((getUnInterpolatedV(sprite, v3) - getUnInterpolatedV(sprite, v0)) / xyz3.distanceTo(xyz0));

        }

        uAxis = uAxis.subtract(center)
                .normalize();
        vAxis = vAxis.subtract(center)
                .normalize();

        for (int vertex = 0; vertex < 4; vertex++) {
            Vec3 xyz = BakedQuadHelper.getXYZ(quad, vertex);
            Vec3 newXyz = mapper.map(xyz, vertex);
            Vec3 diff = newXyz.subtract(xyz);

            if (diff.lengthSqr() > 0) {
                float u = quad.spriteU(vertex, 0);
                float v = quad.spriteV(vertex, 0);
                float uDiff = (float) uAxis.dot(diff) * uScale;
                float vDiff = (float) vAxis.dot(diff) * vScale;
                quad.sprite(vertex, 0,
                        sprite.getU(getUnInterpolatedU(sprite, u) + uDiff),
                        sprite.getV(getUnInterpolatedV(sprite, v) + vDiff));
            }

            BakedQuadHelper.setXYZ(quad, vertex, newXyz);
        }

        return data;
    }
}
