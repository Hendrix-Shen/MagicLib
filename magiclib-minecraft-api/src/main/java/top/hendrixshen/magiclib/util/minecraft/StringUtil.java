package top.hendrixshen.magiclib.util.minecraft;

import top.hendrixshen.magiclib.api.i18n.minecraft.I18n;

public class StringUtil {
    public static String translateOrFallback(String key, String fallback) {
        String translated = I18n.tr(key);

        if (!key.equals(translated)) {
            return translated;
        }

        return fallback;
    }
}
