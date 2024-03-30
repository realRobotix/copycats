package com.copycatsplus.copycats;

import com.copycatsplus.copycats.content.copycat.vertical_stairs.CopycatVerticalStairBlock;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.NotNull;

public class CCBlockStateProperties {

    public static final EnumProperty<VerticalStairShape> VERTICAL_STAIR_SHAPE = EnumProperty.create("vertical_stair_shape", VerticalStairShape.class);

    public enum VerticalStairShape implements StringRepresentable {
        STRAIGHT,
        OUTER_LEFT,
        OUTER_RIGHT;

        @Override
        public @NotNull String getSerializedName() {
            return name().toLowerCase();
        }
    }
}
