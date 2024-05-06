package top.hendrixshen.magiclib.entrypoint.core;

import net.neoforged.fml.common.Mod;
import org.jetbrains.annotations.ApiStatus;
import top.hendrixshen.magiclib.MagicLib;
import top.hendrixshen.magiclib.api.entrypoint.ModInitializer;
import top.hendrixshen.magiclib.impl.dependency.EntryPointDependency;
import top.hendrixshen.magiclib.impl.mixin.audit.MixinAuditor;
import top.hendrixshen.magiclib.impl.mixin.extension.MagicExtensions;
import top.hendrixshen.magiclib.impl.platform.NeoForgePlatformImpl;

@Mod("@MOD_IDENTIFIER@")
public class MagicLibNeoForge implements ModInitializer {
    public MagicLibNeoForge() {
        String headless = System.getProperty("java.awt.headless");

        if (Boolean.parseBoolean(headless)) {
            System.setProperty("java.awt.headless", "");
        }

        EntryPointDependency.getInstance().check();
        this.construct();
        MixinAuditor.trigger("mod_init");
    }

    @ApiStatus.Internal
    public static void bootstrap() {
        MagicLib.getInstance().getPlatformManage().register(NeoForgePlatformImpl.getInstance());
        MagicExtensions.init();
    }

    @Override
    public void onInitializeClient() {
    }

    @Override
    public void onInitializeServer() {
    }

    @Override
    public void onInitialize() {
    }
}
