package com.copycatsplus.copycats;

import com.copycatsplus.copycats.config.CCConfigs;
import com.copycatsplus.copycats.datafixers.api.DataFixesInternals;
import com.copycatsplus.copycats.datafixers.fixers.CopycatsBlockEntityConversionFixer;
import com.mojang.datafixers.DataFixerBuilder;
import com.mojang.datafixers.schemas.Schema;
import net.minecraft.Util;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.util.datafix.schemas.NamespacedSchema;

import java.util.Set;
import java.util.function.BiFunction;

import static com.copycatsplus.copycats.datafixers.api.DataFixesInternals.BASE_SCHEMA;

public class CCDataFixers {
    private static final BiFunction<Integer, Schema, Schema> SAME = Schema::new;
    private static final BiFunction<Integer, Schema, Schema> SAME_NAMESPACED = NamespacedSchema::new;
    public static CopycatsBlockEntityConversionFixer fixer;

    public static void register() {
        Copycats.LOGGER.info("Registering data fixers");

        if (CCConfigs.safeGetter(CCConfigs.common().disableDataFixers::get, false).get()) {
            Copycats.LOGGER.warn("Skipping Datafixer Registration due to it being disabled in the config.");
            return;
        }

        DataFixesInternals api = DataFixesInternals.get();

        DataFixerBuilder builder = new DataFixerBuilder(Copycats.DATA_FIXER_VERSION);
        addFixers(builder);
        api.registerFixer(Copycats.DATA_FIXER_VERSION, builder.buildOptimized(Set.of(References.BLOCK_ENTITY), Util.backgroundExecutor()));
    }

    private static void addFixers(DataFixerBuilder builder) {
        builder.addSchema(0, BASE_SCHEMA);

        // Register a schema, and then the fixes to get *to* that schema

        Schema schemaV1 = builder.addSchema(1, SAME_NAMESPACED);
        builder.addFixer(fixer = new CopycatsBlockEntityConversionFixer(schemaV1, "Replace block_entity id and transfer nbt data"));
    }
}
