package com.copycatsplus.copycats.content.copycat.base.model.multistate;

import com.copycatsplus.copycats.content.copycat.base.model.QuadHelper.CopycatRenderContext;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.Map;

import static com.copycatsplus.copycats.content.copycat.base.model.multistate.MultiStateQuadHelper.*;

public interface SimpleMultiStateCopycatPart {

    static final AABB CUBE_AABB = new AABB(BlockPos.ZERO);

    @ExpectPlatform
    static BakedModel create(BakedModel original, SimpleMultiStateCopycatPart part) {
        throw new AssertionError();
    }


    default void emitCopycatQuads(BlockState state, CopycatRenderContext context, Map<String, BlockState> propertyMaterials) {
        throw new AssertionError("If this is appearing then a model isn't implemented correctly");
    }

    default MutableCullFace cull(int mask) {
        return new MutableCullFace(mask);
    }

    default MutableVec3 vec3(double x, double y, double z) {
        return new MutableVec3(x, y, z);
    }

    default MutableVec3 pivot(double x, double y, double z) {
        return new MutableVec3(x, y, z);
    }

    default MutableAABB aabb(double sizeX, double sizeY, double sizeZ) {
        return new MutableAABB(sizeX, sizeY, sizeZ);
    }

    default MutableRotation rot(MutableVec3 pivot, MutableVec3 rot) {
        return new MutableRotation(pivot, rot);
    }
}
