package com.copycatsplus.copycats.content.copycat.base.model.assembly.fabric;

import com.copycatsplus.copycats.content.copycat.base.model.assembly.*;
import com.simibubi.create.foundation.model.BakedModelHelper;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.SpriteFinder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import static com.copycatsplus.copycats.content.copycat.base.model.assembly.Assembler.CopycatRenderContext;

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
        assembleQuad(context, select.toAABB(), offset.toVec3().subtract(select.minX, select.minY, select.minZ));
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
        assembleQuad(context, select.toAABB(), offset.toVec3().subtract(select.minX, select.minY, select.minZ), transforms);
    }

    public static void assembleQuad(CopycatRenderContext<?, ?> ctx) {
        CopycatRenderContextFabric context = (CopycatRenderContextFabric) ctx;
        assembleQuad(context.source(), context.destination());
    }

    public static <Source extends MutableQuadView, Destination extends QuadEmitter> void assembleQuad(Source src, Destination dest) {
        dest.copyFrom(src);
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
        dest.copyFrom(src);
        BakedModelHelper.cropAndMove(dest, spriteFinder.find(src), crop, move);
        dest.emit();
    }

    public static <Source extends MutableQuadView, Destination extends QuadEmitter> void assembleQuad(Source src, Destination dest, AABB crop, Vec3 move, QuadTransform... transforms) {
        dest.copyFrom(src);
        TextureAtlasSprite sprite = spriteFinder.find(src);
        BakedModelHelper.cropAndMove(dest, sprite, crop, move);
        for (QuadTransform transform : transforms) {
            src = transform.transformVertices(src, sprite);
        }
    }

    public static class CopycatRenderContextFabric extends CopycatRenderContext<MutableQuadView, QuadEmitter> {
        public CopycatRenderContextFabric(MutableQuadView source, QuadEmitter destination) {
            super(source, destination);
        }
    }
}
