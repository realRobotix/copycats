package com.copycatsplus.copycats.fabric;

import com.copycatsplus.copycats.content.copycat.base.multistate.MultiStateCopycatBlockEntity;
import com.copycatsplus.copycats.content.copycat.base.multistate.fabric.MultiStateCopycatBlockEntityFabric;
import com.tterrag.registrate.builders.BlockEntityBuilder;

public class CCBlockEntityTypesImpl {

    public static BlockEntityBuilder.BlockEntityFactory<? extends MultiStateCopycatBlockEntity> getPlatformMultiState() {
        return MultiStateCopycatBlockEntityFabric::new;
    }
}
