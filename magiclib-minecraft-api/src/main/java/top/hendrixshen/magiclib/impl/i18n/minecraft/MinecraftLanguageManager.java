package top.hendrixshen.magiclib.impl.i18n.minecraft;

import lombok.Getter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import top.hendrixshen.magiclib.api.event.minecraft.LanguageReloadListener;
import top.hendrixshen.magiclib.api.event.minecraft.LanguageSelectListener;
import top.hendrixshen.magiclib.impl.i18n.MagicLanguageManager;

@Environment(EnvType.CLIENT)
public class MinecraftLanguageManager implements LanguageReloadListener, LanguageSelectListener {
    @Getter(lazy = true)
    private static final MinecraftLanguageManager instance = new MinecraftLanguageManager();

    @Override
    public void postLanguageReload() {
        MagicLanguageManager.getInstance().reload();
    }

    @Override
    public void postLanguageSelect() {
        MagicLanguageManager.getInstance().setCurrentCode(Minecraft.getInstance().options.languageCode);
    }
}
