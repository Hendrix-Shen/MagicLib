package top.hendrixshen.magiclib.language.api;

import org.jetbrains.annotations.ApiStatus;

/**
* See {@link top.hendrixshen.magiclib.api.i18n.minecraft.I18n}
*/
@Deprecated
@ApiStatus.ScheduledForRemoval
public class I18n {
    /**
     * Get the formatted localised text.
     *
     * @param key     Localisation key.
     * @param objects Objects list.
     * @return Formatted localised string.
     */
    public static String get(String key, Object... objects) {
        return top.hendrixshen.magiclib.api.i18n.minecraft.I18n.tr(key, objects);
    }

    /**
     * Get localised raw text.
     *
     * @param key Localisation key.
     * @return Localised raw string.
     */
    public static String get(String key) {
        return top.hendrixshen.magiclib.api.i18n.minecraft.I18n.tr(key);
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
        return top.hendrixshen.magiclib.api.i18n.minecraft.I18n.trByCode(code, key, objects);
    }

    /**
     * Get localised raw text by code.
     *
     * @param code Language code.
     * @param key  Localisation key.
     * @return Localised raw string.
     */
    public static String getByCode(String code, String key) {
        return top.hendrixshen.magiclib.api.i18n.minecraft.I18n.trByCode(code, key);
    }

    public static boolean exists(String key) {
        return top.hendrixshen.magiclib.api.i18n.minecraft.I18n.exists(key);
    }

    public static boolean exists(String code, String key) {
        return top.hendrixshen.magiclib.api.i18n.minecraft.I18n.exists(code, key);
    }
}
