package top.hendrixshen.magiclib.language.api;

import top.hendrixshen.magiclib.language.impl.MagicLanguageManager;

public class I18n {
    /**
     * Get the formatted localised text.
     *
     * @param key     Localisation key.
     * @param objects Objects list.
     * @return Formatted localised string.
     */
    public static String get(String key, Object... objects) {
        return MagicLanguageManager.INSTANCE.get(key, objects);
    }

    /**
     * Get localised raw text.
     *
     * @param key Localisation key.
     * @return Localised raw string.
     */
    public static String get(String key) {
        return MagicLanguageManager.INSTANCE.get(key);
    }

    /**
     * Get the formatted localised text by code.
     *
     * @param code    Language code.
     * @param key     Localisation key.
     * @param objects Objects list.
     * @return Formatted localised string.
     */
    public static String getByCode(String code, String key, Object... objects) {
        return MagicLanguageManager.INSTANCE.getByCode(code, key, objects);
    }

    /**
     * Get localised raw text by code.
     *
     * @param code Language code.
     * @param key  Localisation key.
     * @return Localised raw string.
     */
    public static String getByCode(String code, String key) {
        return MagicLanguageManager.INSTANCE.getByCode(code, key);
    }

    public static boolean exists(String key) {
        return MagicLanguageManager.INSTANCE.exists(key);
    }

    public static boolean exists(String code, String key) {
        return MagicLanguageManager.INSTANCE.exists(code, key);
    }
}
