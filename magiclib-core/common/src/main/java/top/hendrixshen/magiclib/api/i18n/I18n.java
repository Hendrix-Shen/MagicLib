package top.hendrixshen.magiclib.api.i18n;

import top.hendrixshen.magiclib.impl.i18n.MagicLanguageManager;

/**
 * Note:
 *
 * <li>
 * <b>Un-suffixed Translation Method</b>:
 * Prefer current language, if none exists then attempt fallback languages one by one.
 * </li>
 * <li>
 * <b>Translation method with ByCode suffix</b>:
 * Prefer specific language, if none exist then attempt fallback languages one by one.
 * </li>
 * <li>
 * <b>Translation method with InCode suffix</b>:
 * Only specific languages are used, no fallback languages are attempted.
 * </li>
 */
public class I18n {
    public static final String DEFAULT_CODE = "en_us";

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

    /**
     * Get localised raw text in specific language.
     *
     * <p>
     * Unlike {@link I18n#trByCode(String, String)}, this method returns the key
     * if it does not exist in specific language.
     *
     * @param code Language code.
     * @param key  Localisation key.
     * @return Returns Localised raw string if it exists in specific language, otherwise key.
     */
    public static String trInCode(String code, String key) {
        return MagicLanguageManager.getInstance().getInCode(code, key);
    }

    /**
     * Get the formatted localised text in specific language.
     *
     * <p>
     * Unlike {@link I18n#trByCode(String, String, Object...)}, this method returns the key
     * if it does not exist in specific language.
     *
     * @param code    Language code.
     * @param key     Localisation key.
     * @param objects Objects list.
     * @return Returns Formatted localised string if it exists in specific language, otherwise key.
     */
    public static String trInCode(String code, String key, Object... objects) {
        return MagicLanguageManager.getInstance().getInCode(code, key, objects);
    }

    /**
     * Get whether the translation exists.
     *
     * @param key Localisation key.
     * @return True if translation exists otherwise False.
     */
    public static boolean exists(String key) {
        return MagicLanguageManager.getInstance().exists(key);
    }

    /**
     * Get whether the translation exists.
     *
     * @param code Language code.
     * @param key  Localisation key.
     * @return True if translation exists otherwise False.
     */
    public static boolean exists(String code, String key) {
        return MagicLanguageManager.getInstance().exists(code, key);
    }

    /**
     * Get whether the translation exists in specific language.
     * <p>
     * Unlike {@link I18n#exists(String, String)}, this method only check specific language.
     *
     * @param code Language code.
     * @param key  Localisation key.
     * @return True if translation exists otherwise False.
     */
    public static boolean existsIn(String code, String key) {
        return MagicLanguageManager.getInstance().existsIn(code, key);
    }

    /**
     * This method returns fallback value if the translation does not exist, similar to getOrDefault.
     *
     * <p>
     * See {@link I18n#tr(String)}
     */
    public static String translateOrFallback(String key, String fallback) {
        String translated;
        return (translated = I18n.tr(key)).equals(key) ? fallback : translated;
    }

    /**
     * This method returns fallback value if the translation does not exist, similar to getOrDefault.
     *
     * <p>
     * See {@link I18n#tr(String, Object...)}
     */
    public static String translateOrFallback(String key, String fallback, Object... objects) {
        String translated;
        return (translated = I18n.tr(key, objects)).equals(key) ? fallback : translated;
    }

    /**
     * This method returns fallback value if the translation does not exist, similar to getOrDefault.
     *
     * <p>
     * See {@link I18n#trByCode(String, String)}
     */
    public static String translateByCodeOrFallback(String code, String key, String fallback) {
        String translated;
        return (translated = I18n.trByCode(code, key)).equals(key) ? fallback : translated;
    }

    /**
     * This method returns fallback value if the translation does not exist, similar to getOrDefault.
     *
     * <p>
     * See {@link I18n#trByCode(String, String, Object...)}
     */
    public static String translateByCodeOrFallback(String code, String key, String fallback, Object... object) {
        String translated;
        return (translated = I18n.trByCode(code, key, object)).equals(key) ? fallback : translated;
    }

    /**
     * This method returns fallback value if the translation does not exist, similar to getOrDefault.
     *
     * <p>
     * See {@link I18n#trInCode(String, String)}
     */
    public static String translateInCodeOrFallback(String code, String key, String fallback) {
        String translated;
        return (translated = I18n.trInCode(code, key)).equals(key) ? fallback : translated;
    }

    /**
     * This method returns fallback value if the translation does not exist, similar to getOrDefault.
     *
     * <p>
     * See {@link I18n#trInCode(String, String, Object...)}
     */
    public static String translateInCodeOrFallback(String code, String key, String fallback, Object... object) {
        String translated;
        return (translated = I18n.trInCode(code, key, object)).equals(key) ? fallback : translated;
    }

    public static String getCurrentLanguageCode() {
        return MagicLanguageManager.getInstance().getCurrentCode();
    }
}
