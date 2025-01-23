package top.hendrixshen.magiclib.entrypoint.core;

import org.jetbrains.annotations.ApiStatus;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;

import com.llamalad7.mixinextras.MixinExtrasBootstrap;

import top.hendrixshen.magiclib.MagicLib;
import top.hendrixshen.magiclib.impl.mixin.audit.MixinAuditor;
import top.hendrixshen.magiclib.impl.mixin.extension.MagicExtensions;
import top.hendrixshen.magiclib.impl.platform.FabricPlatformImpl;

public class MagicLibFabric implements ModInitializer, ClientModInitializer, DedicatedServerModInitializer {
    @Override
    public void onInitialize() {
        MixinAuditor.trigger("mod_init");
    }

    @Override
    public void onInitializeClient() {
    }

    @Override
    public void onInitializeServer() {
    }

    @ApiStatus.Internal
    public static void bootstrap() {
        MagicLib.getInstance().getPlatformManage().register(FabricPlatformImpl.getInstance());
        MixinExtrasBootstrap.init();
        MagicExtensions.init();
    }
}
