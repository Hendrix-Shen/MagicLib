package top.hendrixshen.magiclib;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import top.hendrixshen.magiclib.dependency.api.annotation.Dependencies;
import top.hendrixshen.magiclib.dependency.api.annotation.Dependency;
import top.hendrixshen.magiclib.impl.carpet.CarpetEntrypoint;
import top.hendrixshen.magiclib.impl.config.ConfigEntrypoint;
import top.hendrixshen.magiclib.tool.mixin.MixinAuditExecutor;

public class MagicLib implements ModInitializer, ClientModInitializer {
    //#if MC > 11903 && MC < 12000
    //$$ @Dependencies(and = @Dependency(value = "malilib", versionPredicate = ">=0.15.4", optional = true))
    //#endif
    @Override
    public void onInitializeClient() {
        ConfigEntrypoint.init();
        MagicLibReference.getLogger().info("[{}|Client]: Mod initialized - Version: {}", MagicLibReference.getModName(), MagicLibReference.getModVersion());
    }

    @Override
    public void onInitialize() {
        MixinAuditExecutor.execute();
        CarpetEntrypoint.init();
        MagicLibReference.getLogger().info("[{}|Common]: Mod initialized - Version: {}", MagicLibReference.getModName(), MagicLibReference.getModVersion());
    }
}
