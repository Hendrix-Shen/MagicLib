//#if FABRIC
package top.hendrixshen.magiclib.entrypoint.minecraft;


import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;
import top.hendrixshen.magiclib.MagicLib;
import top.hendrixshen.magiclib.api.event.minecraft.LanguageSelectListener;
import top.hendrixshen.magiclib.impl.i18n.minecraft.MinecraftLanguageManager;

public class MagicLibFabric implements ModInitializer, ClientModInitializer, DedicatedServerModInitializer {
    @Override
    public void onInitializeClient() {
        MagicLib.getInstance().getEventManager().register(LanguageSelectListener.class,
                MinecraftLanguageManager.getInstance());
    }

    @Override
    public void onInitializeServer() {
    }

    @Override
    public void onInitialize() {
    }
}
//#endif
