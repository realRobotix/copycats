package com.copycatsplus.copycats.content.copycat.base.model.assembly;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Assembler {

    /**
     * Assemble the quads of a piece of copycat material.
     *
     * @param context         Source and destination quads.
     * @param globalTransform The global transform to apply to the piece.
     * @param offset          In voxel space, the final position of the assembled piece.
     * @param select          In voxel space, the selection on the source model to copy from.
     * @param cull            Faces to skip rendering in the destination model. Changed automatically according to `rotation` and `flipY`.
     */
    @ExpectPlatform
    public static void assemblePiece(
            CopycatRenderContext<?, ?> context,
            @NotNull GlobalTransform globalTransform,
            MutableVec3 offset,
            MutableAABB select,
            MutableCullFace cull
    ) {

    }

    /**
     * Assemble the quads of a piece of copycat material.
     *
     * @param context         Source and destination quads.
     * @param globalTransform The global transform to apply to the piece.
     * @param offset          In voxel space, the final position of the assembled piece.
     * @param select          In voxel space, the selection on the source model to copy from.
     * @param cull            Faces to skip rendering in the destination model. Changed automatically according to `rotation` and `flipY`.
     * @param transforms      Additional transforms to apply to the quads.
     */
    @ExpectPlatform
    public static void assemblePiece(
            CopycatRenderContext<?, ?> context,
            @NotNull GlobalTransform globalTransform,
            MutableVec3 offset,
            MutableAABB select,
            MutableCullFace cull,
            QuadTransform... transforms
    ) {

    }

    /**
     * Copy ALL quads from source to destination without modification.
     */
    @ExpectPlatform
    public static void assembleQuad(CopycatRenderContext<?, ?> context) {

    }

    /**
     * Copy a quad from source to destination without modification.
     */
    @ExpectPlatform
    public static <Source, Destination> void assembleQuad(Source source, Destination destination) {

    }

    /**
     * Copy ALL quads from source to destination while applying the specified crop and move.
     */
    @ExpectPlatform
    public static void assembleQuad(CopycatRenderContext<?, ?> context, AABB crop, Vec3 move) {

    }

    /**
     * Copy ALL quads from source to destination while applying the specified transforms.
     */
    @ExpectPlatform
    public static void assembleQuad(CopycatRenderContext<?, ?> context, AABB crop, Vec3 move, QuadTransform... transforms) {

    }

    /**
     * Copy a quad from source to destination while applying the specified crop and move.
     */
    @ExpectPlatform
    public static <Source, Destination> void assembleQuad(Source src, Destination dest, AABB crop, Vec3 move) {

    }

    /**
     * Copy a quad from source to destination while applying the specified transforms.
     */
    @ExpectPlatform
    public static <Source, Destination> void assembleQuad(Source src, Destination dest, AABB crop, Vec3 move, QuadTransform... transforms) {

    }

    public static MutableCullFace cull(int mask) {
        return new MutableCullFace(mask);
    }

    public static MutableVec3 vec3(double x, double y, double z) {
        return new MutableVec3(x, y, z);
    }

    public static MutableVec3.AsPivot pivot(double x, double y, double z) {
        return new MutableVec3.AsPivot(x, y, z);
    }

    public static MutableAABB aabb(double sizeX, double sizeY, double sizeZ) {
        return new MutableAABB(sizeX, sizeY, sizeZ);
    }

    public static QuadRotate rotate(MutableVec3.AsPivot pivot, MutableVec3 rot) {
        return new QuadRotate(pivot, rot);
    }

    public static QuadScale scale(MutableVec3.AsPivot pivot, MutableVec3 scale) {
        return new QuadScale(pivot, scale);
    }

    public static QuadTranslate translate(double x, double y, double z) {
        return new QuadTranslate(x, y, z);
    }

    public static QuadSlope slope(Direction face, QuadSlope.QuadSlopeFunction func) {
        return new QuadSlope(face, func);
    }

    public static QuadShear shear(Direction.Axis axis, Direction direction, double amount) {
        return new QuadShear(axis, direction, amount);
    }

    public static class CopycatRenderContext<Source, Destination> {
        private final Source source;
        private final Destination destination;

        public CopycatRenderContext(Source source, Destination destination) {
            this.source = source;
            this.destination = destination;
        }

        public Source source() {
            return source;
        }

        public Destination destination() {
            return destination;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (!(obj instanceof CopycatRenderContext<?, ?> that)) return false;
            return Objects.equals(this.source, that.source) &&
                    Objects.equals(this.destination, that.destination);
        }

        @Override
        public int hashCode() {
            return Objects.hash(source, destination);
        }

        @Override
        public String toString() {
            return "CopycatRenderContext[" +
                    "source=" + source + ", " +
                    "destination=" + destination + ']';
        }
    }
}
