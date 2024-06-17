package com.copycatsplus.copycats.content.copycat.base.model.assembly;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;

public class QuadSlope extends TrackingQuadTransform {

    public final Direction face;
    public final QuadSlopeFunction func;

    public QuadSlope(Direction face, QuadSlopeFunction func) {
        this.face = face;
        this.func = func;
    }

    @Override
    public <T> T transformVertices(T vertexData, TextureAtlasSprite sprite) {
        MutableVec3 mutableVertex = new MutableVec3(0, 0, 0);
        return (T) UVHelper.mapWithUV(vertexData, sprite, (vertex, i) -> {
            this.undoMutate(mutableVertex.set(vertex.x, vertex.y, vertex.z));

            double a;
            double b;

            switch (this.face.getAxis()) {
                case X:
                    a = mutableVertex.y;
                    b = mutableVertex.z;
                    break;
                case Y:
                    a = mutableVertex.x;
                    b = mutableVertex.z;
                    break;
                case Z:
                    a = mutableVertex.x;
                    b = mutableVertex.y;
                    break;
                default:
                    throw new RuntimeException("Unexpected value: " + this.face.getAxis());
            }

            double output = this.func.apply(a, b);

            switch (this.face.getAxis()) {
                case X:
                    if (this.face.getAxisDirection() == Direction.AxisDirection.POSITIVE) {
                        mutableVertex.x *= output;
                    } else {
                        mutableVertex.x = 1 - output * (1 - mutableVertex.x);
                    }
                    break;
                case Y:
                    if (this.face.getAxisDirection() == Direction.AxisDirection.POSITIVE) {
                        mutableVertex.y *= output;
                    } else {
                        mutableVertex.y = 1 - output * (1 - mutableVertex.y);
                    }
                    break;
                case Z:
                    if (this.face.getAxisDirection() == Direction.AxisDirection.POSITIVE) {
                        mutableVertex.z *= output;
                    } else {
                        mutableVertex.z = 1 - output * (1 - mutableVertex.z);
                    }
                    break;
                default:
                    throw new RuntimeException("Unexpected value: " + this.face.getAxis());
            }

            return this.mutate(mutableVertex).toVec3();
        });
    }

    public static double map(double fromStart, double fromEnd, double toStart, double toEnd, double value) {
        return toStart + (value - fromStart) / (fromEnd - fromStart) * (toEnd - toStart);
    }

    @FunctionalInterface
    public interface QuadSlopeFunction {
        double apply(double a, double b);
    }
}
