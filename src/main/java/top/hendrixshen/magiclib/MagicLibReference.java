package top.hendrixshen.magiclib;

import net.fabricmc.loader.api.FabricLoader;

public class MagicLibReference {
    private static final String MOD_ID = "magiclib";
    private static final String MOD_NAME = "MagicLib";
    private static final String MOD_VERSION = FabricLoader.getInstance().getModContainer(getModId()).orElseThrow(RuntimeException::new).getMetadata().getVersion().getFriendlyString();

    public static String getModId() {
        return MOD_ID;
    }

    public static String getModName() {
        return MOD_NAME;
    }

    public static String getModVersion() {
        return MOD_VERSION;
    }
}
