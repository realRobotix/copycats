package com.copycatsplus.copycats.content.copycat.test_block;

import com.copycatsplus.copycats.content.copycat.base.multi.MultiStateCopycatBlock;

public class CopycatTestBlock extends MultiStateCopycatBlock {

    public CopycatTestBlock(Properties properties) {
        super(properties);
    }

    @Override
    public int maxMaterials() {
        return 1;
    }
}
