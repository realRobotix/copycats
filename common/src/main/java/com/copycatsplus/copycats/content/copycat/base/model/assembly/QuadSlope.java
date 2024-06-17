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
            this.undoMutate(mutableVertex.set(vertex.x * 16, vertex.y * 16, vertex.z * 16));

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
                        mutableVertex.x *= output / 16;
                    } else {
                        mutableVertex.x = 16 - output * (16 - mutableVertex.x) / 16;
                    }
                    break;
                case Y:
                    if (this.face.getAxisDirection() == Direction.AxisDirection.POSITIVE) {
                        mutableVertex.y *= output / 16;
                    } else {
                        mutableVertex.y = 16 - output * (16 - mutableVertex.y) / 16;
                    }
                    break;
                case Z:
                    if (this.face.getAxisDirection() == Direction.AxisDirection.POSITIVE) {
                        mutableVertex.z *= output / 16;
                    } else {
                        mutableVertex.z = 16 - output * (16 - mutableVertex.z) / 16;
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
