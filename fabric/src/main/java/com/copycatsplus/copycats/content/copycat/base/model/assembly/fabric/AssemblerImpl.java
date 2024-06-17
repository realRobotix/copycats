package com.copycatsplus.copycats.content.copycat.base.model.assembly.fabric;

import com.copycatsplus.copycats.content.copycat.base.model.assembly.*;
import com.simibubi.create.foundation.model.BakedModelHelper;
import com.simibubi.create.foundation.model.BakedQuadHelper;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.SpriteFinder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

import static com.copycatsplus.copycats.content.copycat.base.model.assembly.Assembler.*;

public class AssemblerImpl {

    static SpriteFinder spriteFinder = SpriteFinder.get(Minecraft.getInstance().getModelManager().getAtlas(InventoryMenu.BLOCK_ATLAS));

    public static void assemblePiece(CopycatRenderContext<?, ?> ctx, GlobalTransform globalTransform, MutableVec3 offset, MutableAABB select, MutableCullFace cull) {
        CopycatRenderContextFabric context = (CopycatRenderContextFabric) ctx;
        globalTransform.apply(select);
        globalTransform.apply(offset);
        globalTransform.apply(cull);
        if (cull.isCulled(context.source().lightFace())) {
            return;
        }
        assembleQuad(context, select.toAABB(), offset.toVec3().subtract(select.minX / 16f, select.minY / 16f, select.minZ / 16f));
    }

    public static void assemblePiece(CopycatRenderContext<?, ?> ctx, GlobalTransform globalTransform, MutableVec3 offset, MutableAABB select, MutableCullFace cull, QuadTransform... transforms) {
        CopycatRenderContextFabric context = (CopycatRenderContextFabric) ctx;
        globalTransform.apply(select);
        globalTransform.apply(offset);
        globalTransform.apply(cull);
        for (QuadTransform transform : transforms) {
            globalTransform.apply(transform);
        }
        if (cull.isCulled(context.source().lightFace())) {
            return;
        }
        assembleQuad(context, select.toAABB(), offset.toVec3().subtract(select.minX / 16f, select.minY / 16f, select.minZ / 16f), transforms);
    }

    public static void assembleQuad(CopycatRenderContext<?, ?> ctx) {
        CopycatRenderContextFabric context = (CopycatRenderContextFabric) ctx;
        assembleQuad(context.source(), context.destination());
    }

    public static <Source extends MutableQuadView, Destination extends QuadEmitter> void assembleQuad(Source src, Destination dest) {
        src.copyTo(dest);
        dest.emit();
    }

    public static void assembleQuad(CopycatRenderContext<?, ?> ctx, AABB crop, Vec3 move) {
        CopycatRenderContextFabric context = (CopycatRenderContextFabric) ctx;
        assembleQuad(context.source(), context.destination(), crop, move);
    }

    public static void assembleQuad(CopycatRenderContext<?, ?> ctx, AABB crop, Vec3 move, QuadTransform... transforms) {
        CopycatRenderContextFabric context = (CopycatRenderContextFabric) ctx;
        assembleQuad(context.source(), context.destination(), crop, move, transforms);
    }


    public static <Source extends MutableQuadView, Destination extends QuadEmitter> void assembleQuad(Source src, Destination dest, AABB crop, Vec3 move) {
        src.copyTo(dest);
        BakedModelHelper.cropAndMove(dest, spriteFinder.find(src, 0), crop, move);
        dest.emit();
    }

    public static <Source extends MutableQuadView, Destination extends QuadEmitter> void assembleQuad(Source src, Destination dest, AABB crop, Vec3 move, QuadTransform... transforms) {
        src.copyTo(dest);
        TextureAtlasSprite sprite = spriteFinder.find(src, 0);
        BakedModelHelper.cropAndMove(dest, sprite, crop, move);
        List<Vec3> vertices = new ArrayList<>(4);
        vertices.add(BakedQuadHelper.getXYZ(dest, 0));
        vertices.add(BakedQuadHelper.getXYZ(dest, 1));
        vertices.add(BakedQuadHelper.getXYZ(dest, 2));
        vertices.add(BakedQuadHelper.getXYZ(dest, 3));
        for (QuadTransform transform : transforms) {
            vertices = transform.transformVertices(vertices, sprite);
        }
        BakedQuadHelper.setXYZ(dest, 0, vertices.get(0));
        BakedQuadHelper.setXYZ(dest, 1, vertices.get(1));
        BakedQuadHelper.setXYZ(dest, 2, vertices.get(2));
        BakedQuadHelper.setXYZ(dest, 3, vertices.get(3));
    }

    public static class CopycatRenderContextFabric extends CopycatRenderContext<MutableQuadView, QuadEmitter> {
        public CopycatRenderContextFabric(MutableQuadView source, QuadEmitter destination) {
            super(source, destination);
        }
    }
}
