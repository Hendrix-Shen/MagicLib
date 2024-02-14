package top.hendrixshen.magiclib.api.i18n;

import java.util.Map;

public interface LanguageProvider {
    void init();

    void reload();

    void reload(String LanguageCode);

    void loadLanguage(String languageCode);

    Map<String, String> getLanguage(String languageCode);
}
