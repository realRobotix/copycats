package com.copycatsplus.copycats.content.copycat.base.model;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import static com.copycatsplus.copycats.content.copycat.base.model.assembly.Assembler.CopycatRenderContext;

public class PlatformModelUtils {

    @ExpectPlatform
    public static <Source, Destination> void quadShift(CopycatRenderContext<Source, Destination> context, Vec3i rowShiftNormal, Vec3i columnShiftNormal, AABB bb1, Vec3 offset) {

    }

    @ExpectPlatform
    public static <Source, Destination> void assembleVerticalStep(CopycatRenderContext<Source, Destination> context, boolean row, boolean column, AABB bb1, Vec3 offset) {
    }
}
