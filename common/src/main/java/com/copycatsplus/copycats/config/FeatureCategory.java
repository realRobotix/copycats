package com.copycatsplus.copycats.config;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum FeatureCategory implements StringRepresentable {
    SLOPES("All copycats with a sloped surface");

    private final String description;

    FeatureCategory(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    @Override
    public @NotNull String getSerializedName() {
        return this.name().toLowerCase();
    }

    public static FeatureCategory byName(String name) {
        for (FeatureCategory category : values()) {
            if (category.getSerializedName().equalsIgnoreCase(name)) {
                return category;
            }
        }
        return null;
    }
}
