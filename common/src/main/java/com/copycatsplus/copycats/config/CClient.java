package com.copycatsplus.copycats.config;

import net.minecraftforge.fml.config.ModConfig;

//Should never be synced just did it to allow registration
public class CClient extends SyncConfigBase {

    @Override
    protected ModConfig.Type type() {
        return ModConfig.Type.CLIENT;
    }

    @Override
    public String getName() {
        return "client";
    }
    public final ConfigBool useDevCape = b(true, "useDevCape", Comments.useDevCape, Comments.useDevCape2);
    public final ConfigBool useEnhancedModels = b(true, "useEnhancedModels", Comments.useEnhancedModels);


    private static class Comments {
        static String useEnhancedModels = "Use more complex copycat models to improve appearance with certain materials.";
//Not Finished
        static String useDevCape = "Makes Devs have there capes :P";
        static String useDevCape2 = "Might need to Rejoin for it to work";
    }
}
