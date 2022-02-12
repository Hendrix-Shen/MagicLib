package top.hendrixshen.magiclib.untils.language;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import top.hendrixshen.magiclib.helpers.OMMCConfig;
import top.hendrixshen.magiclib.untils.fabricloader.DependencyValidator;

import java.util.HashMap;
import java.util.List;

@Environment(EnvType.CLIENT)
public class ClientLanguage extends AbstractLanguage {
    private static final ClientLanguage INSTANCE = new ClientLanguage();

    public static ClientLanguage getInstance() {
        return INSTANCE;
    }

    @Override
    public void load() {
        this.loadTranslation(getCurrentLanguage());
        super.load();
    }

    @Override
    public String getCurrentLanguage() {
        return Minecraft.getInstance().options.languageCode;
    }

    @Override
    public List<String> getFallbackLanguages() {
        // Get fallback language from ommc
        if (DependencyValidator.isModLoaded("ommc", ">=0.3.7")) {
            return OMMCConfig.getFallbackListFromOMMC();
        }
        return null;
    }

    @Override
    public void loadTranslation(String lang) {
        HashMap<String, String> map = this.loadTranslationFromInputStream(LanguageType.RESOURCE, lang);

        for (String key : map.keySet()) {
            if (I18n.getOriginal(key).contentEquals(key)) {
                I18n.translations.put(key, map.get(key));
            }
        }
    }
}
