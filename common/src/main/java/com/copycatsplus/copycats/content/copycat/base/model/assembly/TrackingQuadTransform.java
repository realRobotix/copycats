package com.copycatsplus.copycats.content.copycat.base.model.assembly;

import com.copycatsplus.copycats.content.copycat.base.model.assembly.Mutation.MutationType;

import java.util.ArrayList;
import java.util.List;

public abstract class TrackingQuadTransform implements QuadTransform {
    List<Mutation> mutations = new ArrayList<>(2);

    public MutableVec3 mutate(MutableVec3 vec3) {
        for (Mutation mutation : mutations) {
            mutation.mutate(vec3);
        }
        return vec3;
    }

    public MutableVec3 undoMutate(MutableVec3 vec3) {
        for (int i = mutations.size() - 1; i >= 0; i--) {
            Mutation mutation = mutations.get(i);
            mutation.undoMutate(vec3);
        }
        return vec3;
    }

    @Override
    public QuadTransform rotateX(int angle) {
        mutations.add(new Mutation(MutationType.ROTATE_X, angle));
        return this;
    }

    @Override
    public QuadTransform rotateY(int angle) {
        mutations.add(new Mutation(MutationType.ROTATE_Y, angle));
        return this;
    }

    @Override
    public QuadTransform rotateZ(int angle) {
        mutations.add(new Mutation(MutationType.ROTATE_Z, angle));
        return this;
    }

    @Override
    public QuadTransform flipX(boolean flip) {
        if (!flip) return this;
        mutations.add(new Mutation(MutationType.MIRROR, 0));
        return this;
    }

    @Override
    public QuadTransform flipY(boolean flip) {
        if (!flip) return this;
        mutations.add(new Mutation(MutationType.MIRROR, 1));
        return this;
    }

    @Override
    public QuadTransform flipZ(boolean flip) {
        if (!flip) return this;
        mutations.add(new Mutation(MutationType.MIRROR, 2));
        return this;
    }
}
