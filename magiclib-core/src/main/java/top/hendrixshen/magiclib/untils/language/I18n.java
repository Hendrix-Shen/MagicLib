package top.hendrixshen.magiclib.untils.language;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.IllegalFormatException;
import java.util.List;
import java.util.Map;

public class I18n implements ILanguageDispatcher {
    protected static final HashMap<String, String> translations = Maps.newHashMap();
    protected static final List<String> fallbackLanguage = Lists.newArrayList(); // Wait for OMMC
    private static final I18n INSTANCE = new I18n();
    protected final Map<String, List<String>> mods = Maps.newHashMap();

    /**
     * Get the formatted localised text
     *
     * @param key     Localisation key.
     * @param objects Objects list.
     * @return Formatted localised string.
     */
    public static String translate(String key, Object... objects) {
        try {
            return String.format(getOriginal(key), objects);
        } catch (IllegalFormatException e) {
            return String.format("Format error: %s", getOriginal(key));
        }
    }

    /**
     * Get localised raw text.
     *
     * @param key Localisation key.
     * @return Localised raw string.
     */
    public static String getOriginal(String key) {
        return getOriginal(key, key);
    }

    /**
     * Get localised raw text.
     *
     * @param key          Localisation key.
     * @param defaultValue Default value used when query result does not exist.
     * @return Localised raw string.
     */
    public static String getOriginal(String key, String defaultValue) {
        return translations.getOrDefault(key, defaultValue);
    }

    /**
     * Get fallback languages list.
     *
     * @return List of fallback languages.
     */
    public static List<String> getFallbackLanguages() {
        return fallbackLanguage;
    }

    public static I18n getInstance() {
        return INSTANCE;
    }

    /**
     * Register the localised language for your mod.
     *
     * @param modId    Your mod Identifier.
     * @param language Localised language code.
     */
    @Override
    public void register(String modId, String language) {
        if (this.mods.get(modId) == null) {
            this.mods.put(modId, Lists.newArrayList());
            this.mods.get(modId).add(AbstractLanguage.DEFAULT_LANGUAGE);
        }

        if (!this.mods.get(modId).contains(language)) {
            this.mods.get(modId).add(language);
        }
    }
}
