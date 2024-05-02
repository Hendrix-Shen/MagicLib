package top.hendrixshen.magiclib.entrypoint.legacy;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;
import top.hendrixshen.magiclib.MagicLibReference;
import top.hendrixshen.magiclib.impl.carpet.CarpetEntrypoint;
import top.hendrixshen.magiclib.tool.mixin.MixinAuditExecutor;

public class MagicLibFabric implements ModInitializer, ClientModInitializer, DedicatedServerModInitializer {
    @Override
    public void onInitializeClient() {
        MagicLibReference.getLogger().info("[{}|Client]: Mod initialized - Version: {}", MagicLibReference.getModName(), MagicLibReference.getModVersion());
    }

    @Override
    public void onInitializeServer() {
    }

    @Override
    public void onInitialize() {
        MixinAuditExecutor.execute();
        CarpetEntrypoint.init();
        MagicLibReference.getLogger().info("[{}|Common]: Mod initialized - Version: {}", MagicLibReference.getModName(), MagicLibReference.getModVersion());
    }
}
