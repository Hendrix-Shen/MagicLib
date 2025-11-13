package top.hendrixshen.magiclib.impl.i18n.minecraft;

import lombok.Getter;

import net.minecraft.client.Minecraft;

import top.hendrixshen.magiclib.MagicLib;
import top.hendrixshen.magiclib.api.event.minecraft.LanguageManagerListener;
import top.hendrixshen.magiclib.api.event.minecraft.MinecraftListener;
import top.hendrixshen.magiclib.impl.i18n.MagicLanguageManager;

public class MinecraftLanguageManager implements LanguageManagerListener, MinecraftListener {
    @Getter
    private static final MinecraftLanguageManager instance = new MinecraftLanguageManager();

    private MinecraftLanguageManager() {
        MagicLib.getInstance().getEventManager().register(LanguageManagerListener.class, this);
        MagicLib.getInstance().getEventManager().register(MinecraftListener.class, this);
    }

    public static void init() {
        // NO-OP
    }

    @Override
    public void postLanguageReload() {
        if (!MagicLanguageManager.getInstance().setCurrentCode(Minecraft.getInstance().options.languageCode)) {
            MagicLanguageManager.getInstance().reload();
        }
    }

    @Override
    public void postLanguageSelect() {
        // NO-OP
    }

    @Override
    public void postInit() {
        MagicLanguageManager.getInstance().setCurrentCode(Minecraft.getInstance().options.languageCode);
        MagicLanguageManager.getInstance().registerLanguageProvider(ResourceLanguageProvider.getInstance());
    }
}
