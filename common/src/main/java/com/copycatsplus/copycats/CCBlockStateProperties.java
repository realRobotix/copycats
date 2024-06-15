package com.copycatsplus.copycats;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.NotNull;

public class CCBlockStateProperties {

    public static final EnumProperty<VerticalStairShape> VERTICAL_STAIR_SHAPE = EnumProperty.create("vertical_stair_shape", VerticalStairShape.class);

    public enum VerticalStairShape implements StringRepresentable {
        /**
         * The straight side is always on the left
         */
        STRAIGHT,
        OUTER_TOP_LEFT,
        OUTER_TOP_RIGHT,
        OUTER_BOTTOM_LEFT,
        OUTER_BOTTOM_RIGHT,
        /**
         * Inner stairs always have their missing piece on the right
         */
        INNER_TOP,
        /**
         * Inner stairs always have their missing piece on the right
         */
        INNER_BOTTOM;

        @Override
        public @NotNull String getSerializedName() {
            return name().toLowerCase();
        }

        public boolean isVerticallyFlipped() {
            return this == OUTER_BOTTOM_LEFT || this == OUTER_BOTTOM_RIGHT || this == INNER_BOTTOM;
        }
    }
}
