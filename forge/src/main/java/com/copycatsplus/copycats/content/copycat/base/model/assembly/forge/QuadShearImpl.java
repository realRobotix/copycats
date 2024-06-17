package com.copycatsplus.copycats.content.copycat.base.model.assembly.forge;

import com.copycatsplus.copycats.content.copycat.base.model.assembly.MutableVec3;
import com.copycatsplus.copycats.content.copycat.base.model.assembly.QuadShear;
import com.simibubi.create.foundation.model.BakedQuadHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

public class QuadShearImpl {

    public static <T> T transformVertices(QuadShear self, T data, TextureAtlasSprite sprite) {
        int[] vertexData = (int[]) data;
        MutableVec3 mutableVertex = new MutableVec3(0, 0, 0);
        for (int i = 0; i < 4; i++) {
            Vec3 vertex = BakedQuadHelper.getXYZ(vertexData, i);
            self.undoMutate(mutableVertex.set(vertex.x * 16, vertex.y * 16, vertex.z * 16));

            double shearAxis = mutableVertex.get(self.axis);
            double amount = self.amount * (self.direction.getAxisDirection() == Direction.AxisDirection.POSITIVE ? 1 : -1);
            mutableVertex.set(self.direction.getAxis(), mutableVertex.get(self.direction.getAxis()) + shearAxis / 16 * amount);

            BakedQuadHelper.setXYZ(vertexData, i, self.mutate(mutableVertex).toVec3());
        }
        return (T) vertexData;
    }
}
