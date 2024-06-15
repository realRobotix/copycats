package com.copycatsplus.copycats.content.copycat.base.model.assembly;


public record Mutation(MutationType type, int value) {
    public MutableVec3 mutate(MutableVec3 vec3) {
        return switch (type) {
            case ROTATE -> vec3.rotate(value);
            case MIRROR -> {
                if (value == 0) yield vec3.flipX(true);
                else if (value == 1) yield vec3.flipY(true);
                else if (value == 2) yield vec3.flipZ(true);
                else yield vec3;
            }
        };
    }

    public MutableVec3 undoMutate(MutableVec3 vec3) {
        return switch (type) {
            case ROTATE -> vec3.rotate(-value);
            case MIRROR -> {
                if (value == 0) yield vec3.flipX(true);
                else if (value == 1) yield vec3.flipY(true);
                else if (value == 2) yield vec3.flipZ(true);
                else yield vec3;
            }
        };
    }

    public enum MutationType {
        ROTATE,
        MIRROR
    }
}
