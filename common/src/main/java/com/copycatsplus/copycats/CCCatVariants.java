package com.copycatsplus.copycats;

import com.simibubi.create.foundation.utility.Pair;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.CatVariant;

import java.util.LinkedList;
import java.util.List;

public class CCCatVariants {
    protected static final List<Pair<Holder.Reference<CatVariant>, ResourceLocation>> ENTRIES = new LinkedList<>();

    public static final Holder.Reference<CatVariant> COPY_CAT = register("copy_cat", Copycats.asResource("textures/entity/cat/copy_cat.png"));

    private static Holder.Reference<CatVariant> register(String key, ResourceLocation texture) {
        Pair<Holder.Reference<CatVariant>, ResourceLocation> pair = Pair.of(Holder.Reference.createStandAlone(
                BuiltInRegistries.CAT_VARIANT.holderOwner(),
                ResourceKey.create(Registries.CAT_VARIANT, Copycats.asResource(key))
        ), texture);
        ENTRIES.add(pair);
        return pair.getFirst();
    }

    @ExpectPlatform
    public static void register() {

    }
}