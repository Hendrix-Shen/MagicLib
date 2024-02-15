package top.hendrixshen.magiclib.impl.i18n.minecraft;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import top.hendrixshen.magiclib.impl.i18n.MagicLanguageManager;

@Environment(EnvType.CLIENT)
public class MinecraftLanguageManager {
    public static void reload() {
        MagicLanguageManager.getInstance().reload();
    }

    public static void updateLanguage() {
        MagicLanguageManager.getInstance().setCurrentCode(Minecraft.getInstance().options.languageCode);
    }
}
