package top.hendrixshen.magiclib.api.i18n;

import java.util.Map;
import java.util.regex.Pattern;

public interface LanguageProvider {
    Pattern LANGUAGE_PATH_PATTERN = Pattern.compile("^assets/([\\w-]*)/lang/([a-zA-Z\\d-_]*)\\.json$");

    void init();

    void reload();

    void reload(String LanguageCode);

    void loadLanguage(String languageCode);

    Map<String, String> getLanguage(String languageCode);
}
