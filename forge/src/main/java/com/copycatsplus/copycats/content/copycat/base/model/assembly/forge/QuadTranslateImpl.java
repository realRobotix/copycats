package com.copycatsplus.copycats.content.copycat.base.model.assembly.forge;

import com.copycatsplus.copycats.content.copycat.base.model.assembly.MutableVec3;
import com.copycatsplus.copycats.content.copycat.base.model.assembly.QuadTranslate;
import com.simibubi.create.foundation.model.BakedQuadHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.phys.Vec3;

public class QuadTranslateImpl {

    public static <T> T transformVertices(QuadTranslate self, T data, TextureAtlasSprite sprite) {
        int[] vertexData = (int[]) data;
        MutableVec3 mutableVertex = new MutableVec3(0, 0, 0);
        for (int i = 0; i < 4; i++) {
            Vec3 vertex = BakedQuadHelper.getXYZ(vertexData, i);
            self.undoMutate(mutableVertex.set(vertex.x, vertex.y, vertex.z));
            mutableVertex.add(self.x, self.y, self.z);
            BakedQuadHelper.setXYZ(vertexData, i, self.mutate(mutableVertex).toVec3());
        }
        return (T) vertexData;
    }
}
