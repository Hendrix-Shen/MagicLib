package top.hendrixshen.magiclib.tool.doc;

import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.language.I18n;

public class BaseDocumentGenerator {
    public static String DEFAULT_LANGUAGE = "en_us";
    private String currentLanguageCode = BaseDocumentGenerator.DEFAULT_LANGUAGE;
    private final String identifier;

    public BaseDocumentGenerator(@NotNull String identifier) {
        this.identifier = identifier;
    }

    public void setCurrentLanguageCode(String currentLanguageCode) {
        this.currentLanguageCode = currentLanguageCode;
    }

    public String getCurrentLanguageCode() {
        return currentLanguageCode;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String tr(String key, Object... objects) {
        String value;
        return (value = I18n.getByCode(this.getCurrentLanguageCode(), key, objects)).equals(key) ?
                I18n.getByCode(ConfigDocumentGenerator.DEFAULT_LANGUAGE, key, objects) : value;
    }

    public String tr(String key) {
        String value;
        return (value = I18n.getByCode(this.getCurrentLanguageCode(), key)).equals(key) ?
                I18n.getByCode(ConfigDocumentGenerator.DEFAULT_LANGUAGE, key) : value;
    }
}
