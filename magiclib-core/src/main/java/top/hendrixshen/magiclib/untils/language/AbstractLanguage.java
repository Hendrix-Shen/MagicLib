package top.hendrixshen.magiclib.untils.language;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.io.IOUtils;
import top.hendrixshen.magiclib.MagicLib;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractLanguage {
    public static String DEFAULT_LANGUAGE = "en_us";
    private final Gson GSON = new GsonBuilder().enableComplexMapKeySerialization().create();

    public void load() {
        if (getFallbackLanguages() != null) {
            for (String lang : getFallbackLanguages()) {
                this.loadTranslation(lang);
                // Make sure en_us always default language.
                if (lang.equals(DEFAULT_LANGUAGE)) {
                    return;
                }
            }
        }
        this.loadTranslation(DEFAULT_LANGUAGE);
    }

    public HashMap<String, String> loadTranslationFromInputStream(LanguageType languageType, String lang) {
        HashMap<String, String> map = Maps.newHashMap();
        for (String modId : I18n.getInstance().mods.keySet()) {
            if (FabricLoader.getInstance().isModLoaded(modId)) {
                try {
                    InputStream inputStream = getInputStream(languageType, modId, lang);
                    if (inputStream != null) {
                        loadTranslationFromJson(map, inputStream);
                        inputStream.close();
                    }
                } catch (IOException e) {
                    // ignore
                }
            }

        }
        return map;
    }

    public void loadTranslationFromJson(HashMap<String, String> map, InputStream inputStream) {
        try {
            map.putAll(GSON.fromJson(IOUtils.toString(inputStream, StandardCharsets.UTF_8), new TypeToken<Map<String, String>>() {
            }.getType()));
        } catch (NullPointerException | IOException e) {
            //ignore
        }
    }

    public InputStream getInputStream(LanguageType languageType, String modId, String lang) throws IOException {
        if (languageType == LanguageType.BUILD_IN) {
            return MagicLib.class.getClassLoader().getResourceAsStream(String.format("assets/%s/lang/%s.json", modId, lang));
        }

        if (languageType == LanguageType.RESOURCE) {
            return Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation(String.format("%s", modId), String.format("lang/%s.json", lang))).getInputStream();
        }

        return null;
    }

    public abstract String getCurrentLanguage();

    public abstract List<String> getFallbackLanguages();

    public abstract void loadTranslation(String lang);
}
