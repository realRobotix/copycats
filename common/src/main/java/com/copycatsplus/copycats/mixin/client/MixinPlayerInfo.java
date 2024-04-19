package com.copycatsplus.copycats.mixin.client;

import com.copycatsplus.copycats.Copycats;
import com.copycatsplus.copycats.content.copycat.extra.DevCapeUtils;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.Objects;

@Mixin(PlayerInfo.class)
public class MixinPlayerInfo {
    @Shadow @Final private GameProfile profile;
    @Shadow @Final private Map<MinecraftProfileTexture.Type, ResourceLocation> textureLocations;
    @Shadow private @Nullable String skinModel;

    @Unique private boolean railways$texturesLoaded;
    @Unique private static final ResourceLocation DEV_CAPE = Copycats.asResource("textures/misc/dev_cape.png");

    // Replaces skin inside the dev env with the conductor skin

    @Inject(method = "getCapeLocation", at = @At("HEAD"))
    private void registerCapeTextures(CallbackInfoReturnable<ResourceLocation> cir) {
        if (!railways$texturesLoaded && DevCapeUtils.INSTANCE.useDevCape(profile.getId())) {
            railways$texturesLoaded = true;
            this.textureLocations.put(MinecraftProfileTexture.Type.CAPE, DEV_CAPE);
        }
    }

    @Inject(method = "getCapeLocation", at = @At("RETURN"), cancellable = true)
    private void skipCapeIfNeeded(CallbackInfoReturnable<ResourceLocation> cir) {
        if (Objects.equals(DEV_CAPE, cir.getReturnValue()) && !DevCapeUtils.INSTANCE.useDevCape(profile.getId())) {
            cir.setReturnValue(null);
        }
    }
}