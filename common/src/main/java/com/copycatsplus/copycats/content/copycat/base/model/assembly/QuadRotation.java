package com.copycatsplus.copycats.content.copycat.base.model.assembly;

import com.copycatsplus.copycats.content.copycat.base.model.assembly.Mutation.MutationType;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class QuadRotation implements QuadTransform {
    public final Vec3 pivot;
    public final Vec3 rotation;
    List<Mutation> mutations = new ArrayList<>(2);

    public QuadRotation(MutableVec3.AsPivot pivot, MutableVec3 rotation) {
        this.pivot = pivot.toVec3Unscaled();
        this.rotation = rotation.toVec3Unscaled();
    }

    @Override
    public <T> T transformVertices(T vertexData, TextureAtlasSprite sprite) {
        return transformVertices(this, vertexData, sprite);
    }

    @ExpectPlatform
    public static <T> T transformVertices(QuadRotation self, T vertexData, TextureAtlasSprite sprite) {
        return null;
    }

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
