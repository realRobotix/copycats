package com.copycatsplus.copycats.content.copycat.base.model.assembly;

import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

public class MutableVec3 implements GlobalTransform.Transformable<MutableVec3> {
    public double x;
    public double y;
    public double z;

    public MutableVec3(double x, double y, double z) {
        set(x, y, z);
    }

    public MutableVec3 rotateY(int angle) {
        // rotate around the Y axis clockwise
        angle = angle % 360;
        if (angle < 0) angle += 360;
        return switch (angle) {
            case 90 -> set(16 - z, y, x);
            case 180 -> set(16 - x, y, 16 - z);
            case 270 -> set(z, y, 16 - x);
            default -> this;
        };
    }

    public MutableVec3 rotateX(int angle) {
        // rotate around the X axis clockwise
        angle = angle % 360;
        if (angle < 0) angle += 360;
        return switch (angle) {
            case 90 -> set(x, z, 16 - y);
            case 180 -> set(x, 16 - y, 16 - z);
            case 270 -> set(x, 16 - z, y);
            default -> this;
        };
    }

    public MutableVec3 rotateZ(int angle) {
        // rotate around the Z axis clockwise
        angle = angle % 360;
        if (angle < 0) angle += 360;
        return switch (angle) {
            case 90 -> set(y, 16 - x, z);
            case 180 -> set(16 - x, 16 - y, z);
            case 270 -> set(16 - y, x, z);
            default -> this;
        };
    }

    public MutableVec3 flipX(boolean flip) {
        if (!flip) return this;
        return set(16 - x, y, z);
    }

    public MutableVec3 flipY(boolean flip) {
        if (!flip) return this;
        return set(x, 16 - y, z);
    }

    public MutableVec3 flipZ(boolean flip) {
        if (!flip) return this;
        return set(x, y, 16 - z);
    }

    public Vec3 toVec3() {
        return new Vec3(x / 16f, y / 16f, z / 16f);
    }

    public Vec3 toVec3Unscaled() {
        return new Vec3(x, y, z);
    }

    public MutableVec3 set(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public double get(Direction.Axis axis) {
        return switch (axis) {
            case X -> x;
            case Y -> y;
            case Z -> z;
        };
    }

    public MutableVec3 set(Direction.Axis axis, double value) {
        switch (axis) {
            case X -> x = value;
            case Y -> y = value;
            case Z -> z = value;
        }
        return this;
    }

    /**
     * Marker subclass for when a Vec3 is used as a pivot point
     */
    public static class AsPivot extends MutableVec3 {
        public AsPivot(double x, double y, double z) {
            super(x, y, z);
        }
    }
}
