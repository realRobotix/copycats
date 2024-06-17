package com.copycatsplus.copycats.content.copycat.base.model.assembly.fabric;

import com.copycatsplus.copycats.content.copycat.base.model.assembly.MutableVec3;
import com.copycatsplus.copycats.content.copycat.base.model.assembly.QuadRotate;
import com.simibubi.create.foundation.model.BakedQuadHelper;
import com.simibubi.create.foundation.utility.VecHelper;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.phys.Vec3;

public class QuadRotateImpl {

    @SuppressWarnings("unchecked")
    public static <T> T transformVertices(QuadRotate self, T data, TextureAtlasSprite sprite) {
        MutableQuadView vertexData = (MutableQuadView) data;
        MutableVec3 mutableVertex = new MutableVec3(0, 0, 0);
        for (int i = 0; i < 4; i++) {
            Vec3 vertex = BakedQuadHelper.getXYZ(vertexData, i);
            self.undoMutate(mutableVertex.set(vertex.x, vertex.y, vertex.z));
            Vec3 rotated = VecHelper.rotate(mutableVertex.toVec3().subtract(self.pivot), self.rotation).add(self.pivot);
            BakedQuadHelper.setXYZ(vertexData, i, self.mutate(mutableVertex.set(rotated.x, rotated.y, rotated.z)).toVec3());
        }
        return (T) vertexData;
    }
}
