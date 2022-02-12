package top.hendrixshen.magiclib.untils.language;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.HashMap;
import java.util.List;

@Environment(EnvType.SERVER)
public class ServerLanguage extends AbstractLanguage {
    private static final ServerLanguage INSTANCE = new ServerLanguage();

    public static ServerLanguage getInstance() {
        return INSTANCE;
    }

    @Override
    public String getCurrentLanguage() {
        return DEFAULT_LANGUAGE;
    }

    @Override
    public List<String> getFallbackLanguages() {
        return null;
    }

    @Override
    public void loadTranslation(String lang) {
        HashMap<String, String> map = this.loadTranslationFromInputStream(LanguageType.BUILD_IN, lang);

        for (String key : map.keySet()) {
            if (I18n.getOriginal(key).contentEquals(key)) {
                I18n.translations.put(key, map.get(key));
            }
        }
    }
}
