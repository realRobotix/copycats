package com.copycatsplus.copycats.content.copycat.base.model.assembly.forge;

import com.copycatsplus.copycats.content.copycat.base.model.assembly.MutableVec3;
import com.copycatsplus.copycats.content.copycat.base.model.assembly.QuadRotation;
import com.simibubi.create.foundation.model.BakedQuadHelper;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.phys.Vec3;

public class QuadRotationImpl extends QuadRotation {
    public QuadRotationImpl(MutableVec3 pivot, MutableVec3 rotation) {
        super(pivot, rotation);
    }

    public <T> T transformVertices(T data, TextureAtlasSprite sprite) {
        int[] vertexData = (int[]) data;
        MutableVec3 mutableVertex = new MutableVec3(0, 0, 0);
        for (int i = 0; i < 4; i++) {
            Vec3 vertex = BakedQuadHelper.getXYZ(vertexData, i);
            undoMutate(mutableVertex.set(vertex.x * 16, vertex.y * 16, vertex.z * 16));
            Vec3 rotated = VecHelper.rotate(mutableVertex.toVec3Unscaled().subtract(pivot), rotation).add(pivot);
            BakedQuadHelper.setXYZ(vertexData, i, mutate(mutableVertex.set(rotated.x, rotated.y, rotated.z)).toVec3());
        }
        return (T) vertexData;
    }
}
