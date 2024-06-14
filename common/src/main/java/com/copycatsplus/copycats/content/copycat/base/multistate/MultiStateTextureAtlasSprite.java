package com.copycatsplus.copycats.content.copycat.base.multistate;

import com.copycatsplus.copycats.mixin_interfaces.TextureAtlasSpriteAccessor;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public class MultiStateTextureAtlasSprite extends TextureAtlasSprite {
    private final String property;

    public MultiStateTextureAtlasSprite(String property, TextureAtlasSprite wrapped) {
        super(wrapped.atlas(), ((TextureAtlasSpriteAccessor) wrapped).copycats$info(),
                ((TextureAtlasSpriteAccessor) wrapped).copycats$mipmap(), (int) (wrapped.getX() / wrapped.getU0()), (int) (wrapped.getY() / wrapped.getV0()), wrapped.getX(), wrapped.getY(), ((TextureAtlasSpriteAccessor) wrapped).copycats$nativeImage());
        this.property = property;
    }

    public String getProperty() {
        return property;
    }
}
