package com.copycatsplus.copycats.content.copycat.base.model.assembly.fabric;

import com.copycatsplus.copycats.content.copycat.base.model.assembly.MutableVec3;
import com.copycatsplus.copycats.content.copycat.base.model.assembly.QuadScale;
import com.simibubi.create.foundation.model.BakedQuadHelper;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.phys.Vec3;

public class QuadScaleImpl {

    @SuppressWarnings("unchecked")
    public static <T> T transformVertices(QuadScale self, T data, TextureAtlasSprite sprite) {
        MutableQuadView vertexData = (MutableQuadView) data;
        MutableVec3 mutableVertex = new MutableVec3(0, 0, 0);
        for (int i = 0; i < 4; i++) {
            Vec3 vertex = BakedQuadHelper.getXYZ(vertexData, i);
            self.undoMutate(mutableVertex.set(vertex.x * 16, vertex.y * 16, vertex.z * 16));

            mutableVertex.x = (mutableVertex.x - self.pivot.x) * self.scale.x + self.pivot.x;
            mutableVertex.y = (mutableVertex.y - self.pivot.y) * self.scale.y + self.pivot.y;
            mutableVertex.z = (mutableVertex.z - self.pivot.z) * self.scale.z + self.pivot.z;

            BakedQuadHelper.setXYZ(vertexData, i, self.mutate(mutableVertex).toVec3());
        }
        return (T) vertexData;
    }
}
