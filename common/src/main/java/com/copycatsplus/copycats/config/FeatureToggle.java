package com.copycatsplus.copycats.config;

import com.copycatsplus.copycats.compat.CopycatsJEI;
import com.copycatsplus.copycats.compat.Mods;
import com.copycatsplus.copycats.mixin.feature_toggle.CreativeModeTabsAccessor;
import com.copycatsplus.copycats.multiloader.LogicalSidedProvider;
import com.copycatsplus.copycats.multiloader.Platform;
import com.tterrag.registrate.builders.Builder;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FeatureToggle {
    public static final Set<ResourceLocation> TOGGLEABLE_FEATURES = new HashSet<>();
    public static final Map<ResourceLocation, ResourceLocation> DEPENDENT_FEATURES = new HashMap<>();
    public static final Map<ResourceLocation, FeatureCategory> FEATURE_CATEGORIES = new HashMap<>();

    public static void register(ResourceLocation key) {
        TOGGLEABLE_FEATURES.add(key);
    }

    public static void register(ResourceLocation key, FeatureCategory category) {
        register(key);
        FEATURE_CATEGORIES.put(key, category);
    }

    public static void registerDependent(ResourceLocation key, ResourceLocation dependency) {
        DEPENDENT_FEATURES.put(key, dependency);
    }

    public static void registerDependent(ResourceLocation key, ResourceLocation dependency, FeatureCategory category) {
        registerDependent(key, dependency);
        FEATURE_CATEGORIES.put(key, category);
    }

    /**
     * Register this object to be a feature that is toggleable by the user
     */
    public static <R, T extends R, P, S extends Builder<R, T, P, S>> NonNullUnaryOperator<S> register() {
        return b -> {
            register(new ResourceLocation(b.getOwner().getModid(), b.getName()));
            return b;
        };
    }

    /**
     * Register this object to be a feature that is toggleable by the user
     */
    public static <R, T extends R, P, S extends Builder<R, T, P, S>> NonNullUnaryOperator<S> register(FeatureCategory category) {
        return b -> {
            register(new ResourceLocation(b.getOwner().getModid(), b.getName()), category);
            return b;
        };
    }

    /**
     * Register this object to be dependent on another feature.
     * This object cannot be toggled directly, and will only be enabled if the dependency is enabled.
     */
    public static <R, T extends R, P, S extends Builder<R, T, P, S>> NonNullUnaryOperator<S> registerDependent(ResourceLocation dependency) {
        return b -> {
            registerDependent(new ResourceLocation(b.getOwner().getModid(), b.getName()), dependency);
            return b;
        };
    }

    /**
     * Register this object to be dependent on another feature.
     * This object cannot be toggled directly, and will only be enabled if the dependency is enabled.
     */
    public static <R, T extends R, P, S extends Builder<R, T, P, S>> NonNullUnaryOperator<S> registerDependent(ResourceLocation dependency, FeatureCategory category) {
        return b -> {
            registerDependent(new ResourceLocation(b.getOwner().getModid(), b.getName()), dependency, category);
            return b;
        };
    }

    /**
     * Register this object to be dependent on another feature.
     * This object cannot be toggled directly, and will only be enabled if the dependency is enabled.
     */
    public static <R, T extends R, P, S extends Builder<R, T, P, S>> NonNullUnaryOperator<S> registerDependent(BlockEntry<?> dependency) {
        return b -> {
            registerDependent(new ResourceLocation(b.getOwner().getModid(), b.getName()), dependency.getId());
            return b;
        };
    }

    /**
     * Register this object to be dependent on another feature.
     * This object cannot be toggled directly, and will only be enabled if the dependency is enabled.
     */
    public static <R, T extends R, P, S extends Builder<R, T, P, S>> NonNullUnaryOperator<S> registerDependent(BlockEntry<?> dependency, FeatureCategory category) {
        return b -> {
            registerDependent(new ResourceLocation(b.getOwner().getModid(), b.getName()), dependency.getId(), category);
            return b;
        };
    }

    private static CFeatures getToggles() {
        return CCConfigs.common().toggle;
    }

    private static CFeatureCategories getCategories() {
        return CCConfigs.common().categories;
    }

    /**
     * Check whether a feature is enabled.
     * If the provided {@link ResourceLocation} is not registered with this feature toggle, it is assumed to be enabled.
     *
     * @param key The {@link ResourceLocation} of the feature.
     * @return Whether the feature is enabled.
     */
    public static boolean isEnabled(ResourceLocation key) {
        if (FEATURE_CATEGORIES.containsKey(key)) {
            if (!getCategories().isEnabled(FEATURE_CATEGORIES.get(key))) return false;
        }
        if (getToggles().hasToggle(key)) {
            return getToggles().isEnabled(key);
        } else {
            ResourceLocation dependency = DEPENDENT_FEATURES.get(key);
            if (dependency != null) return isEnabled(dependency);
        }
        return true;
    }

    /**
     * Refresh item visibility in all places when the list of enabled features has changed
     */
    static void refreshItemVisibility() {
        Platform.Environment.CLIENT.runIfCurrent(() -> () ->
                LogicalSidedProvider.WORKQUEUE.get(Platform.Environment.CLIENT).submit(() -> {
                    CreativeModeTab.ItemDisplayParameters cachedParameters = CreativeModeTabsAccessor.getCACHED_PARAMETERS();
                    if (cachedParameters != null) {
                        CreativeModeTabsAccessor.callBuildAllTabContents(cachedParameters);
                    }
                    Mods.JEI.executeIfInstalled(() -> CopycatsJEI::refreshItemList);
                })
        );
    }
}
