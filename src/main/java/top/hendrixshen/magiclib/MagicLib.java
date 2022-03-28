package top.hendrixshen.magiclib;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;
import top.hendrixshen.magiclib.compat.test.TestCompat;
import top.hendrixshen.magiclib.config.ConfigHandler;
import top.hendrixshen.magiclib.config.ConfigManager;
import top.hendrixshen.magiclib.dependency.annotation.Dependencies;
import top.hendrixshen.magiclib.dependency.annotation.Dependency;

public class MagicLib implements ModInitializer, ClientModInitializer, DedicatedServerModInitializer {
    private static final int CONFIG_VERSION = 1;

    @Override
    public void onInitialize() {
        TestCompat.test();
        MagicLibReference.LOGGER.info("[{}]: Mod initialized - Version: {}", MagicLibReference.getModName(),
                MagicLibReference.getModVersion());
    }

    @Dependencies(and = @Dependency(value = "malilib"))
    @Override
    public void onInitializeClient() {
        ConfigManager cm = ConfigManager.get(MagicLibReference.getModId());
        cm.parseConfigClass(MagicLibConfigs.class);
        ConfigHandler.register(new ConfigHandler(MagicLibReference.getModId(), cm, CONFIG_VERSION, null, null));
        MagicLibConfigs.init(cm);
    }

    @Override
    public void onInitializeServer() {
    }
}
