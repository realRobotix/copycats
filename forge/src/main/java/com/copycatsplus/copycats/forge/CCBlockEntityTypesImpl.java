package com.copycatsplus.copycats.forge;

import com.copycatsplus.copycats.content.copycat.base.multi.MultiStateCopycatBlockEntity;
import com.copycatsplus.copycats.content.copycat.base.multi.forge.MultiStateCopycatBlockEntityForge;
import com.tterrag.registrate.builders.BlockEntityBuilder;

public class CCBlockEntityTypesImpl {

    public static BlockEntityBuilder.BlockEntityFactory<? extends MultiStateCopycatBlockEntity> getPlatformMultiState() {
        return MultiStateCopycatBlockEntityForge::new;
    }
}
