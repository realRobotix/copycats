package com.copycatsplus.copycats.content.copycat.extra;

import com.copycatsplus.copycats.Copycats;
import com.simibubi.create.content.trains.entity.Train;
import com.simibubi.create.foundation.networking.SimplePacketBase;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.Locale;

public class Utils {
    @ExpectPlatform
    public static Path configDir() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static Path modsDir() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean isDevEnv() {
        throw new AssertionError();
    }

    public static boolean isEnvVarTrue(String name) {
        try {
            String result = System.getenv(name);
            return result != null && result.toLowerCase(Locale.ROOT).equals("true");
        } catch (SecurityException e) {
            Copycats.LOGGER.warn("Caught a security exception while trying to access environment variable `{}`.", name);
            return false;
        }
    }

    public static @Nullable String getEnvVar(String name) {
        try {
            String result = System.getenv(name);
            return result != null && !result.isEmpty() ? result : null;
        } catch (SecurityException e) {
            Copycats.LOGGER.warn("Caught a security exception while trying to access environment variable `{}`.", name);
            return null;
        }
    }

    @ExpectPlatform
    public static void sendCreatePacketToServer(SimplePacketBase packet) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void sendHonkPacket(Train train, boolean isHonk) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void postChunkEventClient(LevelChunk chunk, boolean load) {
        throw new AssertionError();
    }
}