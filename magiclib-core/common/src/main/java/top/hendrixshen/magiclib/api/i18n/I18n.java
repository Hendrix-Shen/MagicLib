package top.hendrixshen.magiclib.api.i18n;

import top.hendrixshen.magiclib.impl.i18n.MagicLanguageManager;

public class I18n {
    /**
     * Get localised raw text.
     *
     * @param key Localisation key.
     * @return Localised raw string.
     */
    public static String tr(String key) {
        return MagicLanguageManager.getInstance().get(key);
    }

    /**
     * Get the formatted localised text.
     *
     * @param key     Localisation key.
     * @param objects Objects list.
     * @return Formatted localised string.
     */
    public static String tr(String key, Object... objects) {
        return MagicLanguageManager.getInstance().get(key, objects);
    }

    /**
     * Get localised raw text by code.
     *
     * @param code Language code.
     * @param key  Localisation key.
     * @return Localised raw string.
     */
    public static String trByCode(String code, String key) {
        return MagicLanguageManager.getInstance().getByCode(code, key);
    }

    /**
     * Get the formatted localised text by code.
     *
     * @param code    Language code.
     * @param key     Localisation key.
     * @param objects Objects list.
     * @return Formatted localised string.
     */
    public static String trByCode(String code, String key, Object... objects) {
        return MagicLanguageManager.getInstance().getByCode(code, key, objects);
    }

    public static boolean exists(String key) {
        return MagicLanguageManager.getInstance().exists(key);
    }

    public static boolean exists(String code, String key) {
        return MagicLanguageManager.getInstance().exists(code, key);
    }

}
