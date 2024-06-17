package com.copycatsplus.copycats.content.copycat.base.model.assembly.forge;

import com.copycatsplus.copycats.content.copycat.base.model.assembly.MutableVec3;
import com.copycatsplus.copycats.content.copycat.base.model.assembly.QuadScale;
import com.simibubi.create.foundation.model.BakedQuadHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.phys.Vec3;

public class QuadScaleImpl {

    public static <T> T transformVertices(QuadScale self, T data, TextureAtlasSprite sprite) {
        int[] vertexData = (int[]) data;
        MutableVec3 mutableVertex = new MutableVec3(0, 0, 0);
        for (int i = 0; i < 4; i++) {
            Vec3 vertex = BakedQuadHelper.getXYZ(vertexData, i);
            self.undoMutate(mutableVertex.set(vertex.x, vertex.y, vertex.z));

            mutableVertex.x = (mutableVertex.x - self.pivot.x) * self.scale.x + self.pivot.x;
            mutableVertex.y = (mutableVertex.y - self.pivot.y) * self.scale.y + self.pivot.y;
            mutableVertex.z = (mutableVertex.z - self.pivot.z) * self.scale.z + self.pivot.z;

            BakedQuadHelper.setXYZ(vertexData, i, self.mutate(mutableVertex).toVec3());
        }
        return (T) vertexData;
    }
}
