package top.hendrixshen.magiclib.util.minecraft;

import net.minecraft.commands.CommandSource;
import top.hendrixshen.magiclib.api.i18n.minecraft.I18n;

public class StringUtil {
    public static String translateOrFallback(String key, String fallback) {
        String translated = I18n.tr(key);

        if (!key.equals(translated)) {
            return translated;
        }

        return fallback;
    }

    public static String translateOrFallback(String key, String fallback, Object... object) {
        String translated = I18n.tr(key, object);

        if (!key.equals(translated)) {
            return translated;
        }

        return fallback;
    }

    public static String translateOrFallback(CommandSource source, String key, String fallback) {
        String translated = I18n.tr(source, key);

        if (!key.equals(translated)) {
            return translated;
        }

        return fallback;
    }

    public static String translateOrFallback(CommandSource source, String key, String fallback, Object... object) {
        String translated = I18n.tr(source, key, object);

        if (!key.equals(translated)) {
            return translated;
        }

        return fallback;
    }
}
