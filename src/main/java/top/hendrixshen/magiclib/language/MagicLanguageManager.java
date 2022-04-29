package top.hendrixshen.magiclib.language;

import com.google.common.collect.Lists;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.MagicLibConfigs;
import top.hendrixshen.magiclib.MagicLibReference;
import top.hendrixshen.magiclib.util.MiscUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.IllegalFormatException;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class MagicLanguageManager implements ResourceManagerReloadListener {

    public static final String DEFAULT_CODE = "en_us";

    private String currentCode = DEFAULT_CODE;
    public ConcurrentHashMap<String, String> defaultLanguage = new ConcurrentHashMap<>();
    public ConcurrentHashMap<String, ConcurrentHashMap<String, String>> language = new ConcurrentHashMap<>();

    ArrayList<String> fallbackLanguageList = Lists.newArrayList(DEFAULT_CODE);
    @NotNull
    private ResourceManager resourceManager;

    private MagicLanguageManager() {
        this.resourceManager = new MagicLanguageResourceManager();
        this.reload();
    }

    public void setCurrentCode(String currentCode) {
        if (!Objects.equals(this.currentCode, currentCode)) {
            this.currentCode = currentCode;
            reload();
        }
    }

    public static final MagicLanguageManager INSTANCE = new MagicLanguageManager();

    @Environment(EnvType.CLIENT)
    public void initClient() {
        this.resourceManager = Minecraft.getInstance().getResourceManager();
        this.fallbackLanguageList = MagicLibConfigs.fallbackLanguageList;
        ((ReloadableResourceManager) Minecraft.getInstance().getResourceManager()).registerReloadListener(INSTANCE);
        this.reload();
    }


    private void initLanguage(String code, ConcurrentHashMap<String, String> language) {
        String languagePath = String.format("lang/%s.json", code);
        for (String namespace : resourceManager.getNamespaces()) {
            ResourceLocation resourceLocation = new ResourceLocation(namespace, languagePath);
            try {
                for (Resource resource : resourceManager.getResources(resourceLocation)) {
                    try {
                        InputStream inputStream = resource.getInputStream();
                        try {
                            MiscUtil.loadStringMapFromJson(inputStream, language::put);
                        } catch (Throwable e) {
                            if (inputStream != null) {
                                inputStream.close();
                            }
                            throw e;
                        }
                        // from minecraft code
                        if (inputStream != null) {
                            inputStream.close();
                        }
                    } catch (IOException e) {
                        MagicLibReference.LOGGER.warn("Failed to load translations from {} ({})", resource, e);
                    }
                }
            } catch (IOException e) {
                MagicLibReference.LOGGER.warn("Failed to load translations from {}:{} ({})", namespace, languagePath, e);
            }
        }
    }

    public void reload() {
        defaultLanguage.clear();
        language.clear();
        ArrayList<String> codes = new ArrayList<>(fallbackLanguageList);
        ConcurrentHashMap<String, String> currentLanguage = new ConcurrentHashMap<>();
        if (codes.size() >= 1 && !codes.get(0).equals(currentCode)) {
            codes.add(0, currentCode);
        }
        for (int i = codes.size() - 1; i >= 0; --i) {
            String code = codes.get(i);
            initLanguage(code, currentLanguage);
            language.put(code, currentLanguage);
            defaultLanguage.putAll(currentLanguage);
        }
    }


    @Override
    public void onResourceManagerReload(@NotNull ResourceManager resourceManager) {
        reload();
    }

    public String get(String key, Object... objects) {
        String translateValue = defaultLanguage.getOrDefault(key, key);
        try {
            return String.format(translateValue, objects);
        } catch (IllegalFormatException var4) {
            return "Format error: " + translateValue;
        }
    }

    public String getByCode(String code, String key, Object... objects) {
        ConcurrentHashMap<String, String> currentLanguage = language.getOrDefault(code, null);
        if (currentLanguage == null) {
            currentLanguage = new ConcurrentHashMap<>();
            initLanguage(code, currentLanguage);
            language.put(code, currentLanguage);
        }
        String translateValue = currentLanguage.getOrDefault(key, key);
        try {
            return String.format(translateValue, objects);
        } catch (IllegalFormatException var4) {
            return "Format error: " + translateValue;
        }
    }

    public boolean exists(String key) {
        return defaultLanguage.contains(key);
    }

    public boolean exists(String code, String key) {
        ConcurrentHashMap<String, String> currentLanguage = language.getOrDefault(code, null);
        if (currentLanguage == null) {
            return false;
        }
        return currentLanguage.contains(key);
    }
}