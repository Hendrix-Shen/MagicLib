//#if FABRIC
package top.hendrixshen.magiclib.entrypoint.minecraft;


import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;
import top.hendrixshen.magiclib.impl.i18n.minecraft.MinecraftLanguageManager;
import top.hendrixshen.magiclib.impl.mixin.audit.minecraft.MinecraftMixinAudit;

public class MagicLibFabric implements ModInitializer, ClientModInitializer, DedicatedServerModInitializer {
    @Override
    public void onInitializeClient() {
        MinecraftLanguageManager.init();
    }

    @Override
    public void onInitializeServer() {
    }

    @Override
    public void onInitialize() {
        MinecraftMixinAudit.init();
    }
}
//#endif
