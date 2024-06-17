package com.copycatsplus.copycats.content.copycat.base.model.assembly.forge;

import com.copycatsplus.copycats.content.copycat.base.model.assembly.*;
import com.simibubi.create.foundation.model.BakedModelHelper;
import com.simibubi.create.foundation.model.BakedQuadHelper;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

import static com.copycatsplus.copycats.content.copycat.base.model.assembly.Assembler.*;

public class AssemblerImpl {

    public static void assemblePiece(CopycatRenderContext<?, ?> ctx, GlobalTransform globalTransform, MutableVec3 offset, MutableAABB select, MutableCullFace cull) {
        CopycatRenderContextForge context = (CopycatRenderContextForge) ctx;
        globalTransform.apply(select);
        globalTransform.apply(offset);
        globalTransform.apply(cull);
        for (BakedQuad quad : context.source()) {
            if (cull.isCulled(quad.getDirection())) {
                continue;
            }
            assembleQuad(quad, context.destination(), select.toAABB(), offset.toVec3().subtract(select.minX / 16f, select.minY / 16f, select.minZ / 16f));
        }
    }

    public static void assemblePiece(CopycatRenderContext<?, ?> ctx, GlobalTransform globalTransform, MutableVec3 offset, MutableAABB select, MutableCullFace cull, QuadTransform... transforms) {
        CopycatRenderContextForge context = (CopycatRenderContextForge) ctx;
        globalTransform.apply(select);
        globalTransform.apply(offset);
        globalTransform.apply(cull);
        for (QuadTransform transform : transforms) {
            globalTransform.apply(transform);
        }
        for (BakedQuad quad : context.source()) {
            if (cull.isCulled(quad.getDirection())) {
                continue;
            }
            assembleQuad(quad, context.destination(), select.toAABB(), offset.toVec3().subtract(select.minX / 16f, select.minY / 16f, select.minZ / 16f), transforms);
        }
    }

    public static void assembleQuad(CopycatRenderContext<?, ?> ctx) {
        CopycatRenderContextForge context = (CopycatRenderContextForge) ctx;
        for (BakedQuad quad : context.source()) {
            assembleQuad(quad, context.destination());
        }
    }

    public static <Source extends BakedQuad, Destination extends List<BakedQuad>> void assembleQuad(Source src, Destination dest) {
        dest.add(BakedQuadHelper.clone(src));
    }

    public static void assembleQuad(CopycatRenderContext<?, ?> ctx, AABB crop, Vec3 move) {
        CopycatRenderContextForge context = (CopycatRenderContextForge) ctx;
        for (BakedQuad quad : context.source()) {
            assembleQuad(quad, context.destination(), crop, move);
        }
    }

    public static void assembleQuad(CopycatRenderContext<?, ?> ctx, AABB crop, Vec3 move, QuadTransform... transforms) {
        CopycatRenderContextForge context = (CopycatRenderContextForge) ctx;
        for (BakedQuad quad : context.source()) {
            assembleQuad(quad, context.destination(), crop, move, transforms);
        }
    }


    public static <Source extends BakedQuad, Destination extends List<BakedQuad>> void assembleQuad(Source src, Destination dest, AABB crop, Vec3 move) {
        dest.add(BakedQuadHelper.cloneWithCustomGeometry(src,
                BakedModelHelper.cropAndMove(src.getVertices(), src.getSprite(), crop, move)));
    }

    public static <Source extends BakedQuad, Destination extends List<BakedQuad>> void assembleQuad(Source src, Destination dest, AABB crop, Vec3 move, QuadTransform... transforms) {
        int[] vertices = BakedModelHelper.cropAndMove(src.getVertices(), src.getSprite(), crop, move);
        for (QuadTransform transform : transforms) {
            vertices = transform.transformVertices(vertices, src.getSprite());
        }
        dest.add(BakedQuadHelper.cloneWithCustomGeometry(src, vertices));
    }

    public static class CopycatRenderContextForge extends CopycatRenderContext<List<BakedQuad>, List<BakedQuad>> {
        public CopycatRenderContextForge(List<BakedQuad> source, List<BakedQuad> destination) {
            super(source, destination);
        }
    }
}
