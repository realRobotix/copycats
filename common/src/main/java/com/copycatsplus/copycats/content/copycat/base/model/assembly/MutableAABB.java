package com.copycatsplus.copycats.content.copycat.base.model.assembly;

import net.minecraft.world.phys.AABB;

public class MutableAABB implements GlobalTransform.Transformable<MutableAABB> {
    public double minX;
    public double minY;
    public double minZ;
    public double maxX;
    public double maxY;
    public double maxZ;

    public MutableAABB(double sizeX, double sizeY, double sizeZ) {
        set(0, 0, 0, sizeX, sizeY, sizeZ);
    }

    public MutableAABB move(double dX, double dY, double dZ) {
        minX += dX;
        maxX += dX;
        minY += dY;
        maxY += dY;
        minZ += dZ;
        maxZ += dZ;
        return this;
    }

    public MutableAABB rotate(int angle) {
        angle = angle % 360;
        if (angle < 0) angle += 360;
        return switch (angle) {
            case 90 -> set(16 - minZ, minY, minX, 16 - maxZ, maxY, maxX);
            case 180 -> set(16 - minX, minY, 16 - minZ, 16 - maxX, maxY, 16 - maxZ);
            case 270 -> set(minZ, minY, 16 - minX, maxZ, maxY, 16 - maxX);
            default -> this;
        };
    }

    public MutableAABB flipX(boolean flip) {
        if (!flip) return this;
        return set(16 - minX, minY, minZ, 16 - maxX, maxY, maxZ);
    }

    public MutableAABB flipY(boolean flip) {
        if (!flip) return this;
        return set(minX, 16 - minY, minZ, maxX, 16 - maxY, maxZ);
    }

    public MutableAABB flipZ(boolean flip) {
        if (!flip) return this;
        return set(minX, minY, 16 - minZ, maxX, maxY, 16 - maxZ);
    }

    public AABB toAABB() {
        return new AABB(minX / 16f, minY / 16f, minZ / 16f, maxX / 16f, maxY / 16f, maxZ / 16f);
    }

    public MutableAABB set(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
        return this;
    }
}
