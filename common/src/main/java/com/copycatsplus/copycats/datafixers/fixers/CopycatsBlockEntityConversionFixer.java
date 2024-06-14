package com.copycatsplus.copycats.datafixers.fixers;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.serialization.Dynamic;
import net.minecraft.util.datafix.fixes.References;

import java.util.Optional;

public class CopycatsBlockEntityConversionFixer extends DataFix {

    private String name;
    public CopycatsBlockEntityConversionFixer(Schema outputSchema, String name) {
        super(outputSchema, false);
        this.name = name;
    }

    @Override
    protected TypeRewriteRule makeRule() {
        return this.fixTypeEverywhereTyped(this.name + " for block_entity", this.getInputSchema().getType(References.BLOCK_ENTITY), typed -> typed.update(DSL.remainderFinder(), dynamic -> {
            Optional<String> optional = dynamic.get("id").asString().result();
            Optional<String> name = dynamic.get("Name").asString().result();
            if (optional.isPresent() && optional.get().equalsIgnoreCase("create:copycat")
                    && name.isPresent() && name.get().startsWith("copycats:")) {

                Dynamic<?> material = dynamic.get("Material").orElseEmptyMap();
                Dynamic<?> item = dynamic.get("Item").orElseEmptyMap();

                Dynamic<?> material_data = dynamic.get("material_data").orElseEmptyMap();
                Dynamic<?> placeholder = material_data.get("placeholder").orElseEmptyMap();
                placeholder.set("material", material);
                placeholder.set("item", item);

                dynamic = dynamic.set("material_data", material_data);
                dynamic = dynamic.set("id", dynamic.createString("copycats:multistate_copycat"));
                return dynamic;
            }
            return dynamic;
        }));
    }
}
