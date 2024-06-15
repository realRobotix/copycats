package com.copycatsplus.copycats.content.copycat.base.model.assembly;

@FunctionalInterface
public interface GlobalTransform {
    public static final GlobalTransform IDENTITY = t -> {
    };

    void apply(Transformable<?> t);

    public interface Transformable<Self extends Transformable<Self>> {
        Self rotate(int angle);

        Self flipX(boolean flip);

        Self flipY(boolean flip);

        Self flipZ(boolean flip);
    }
}
