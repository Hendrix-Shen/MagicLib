package top.hendrixshen.magiclib.language;

import com.google.common.collect.Lists;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.IllegalFormatException;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


public class MagicLanguageManager implements ResourceManagerReloadListener {

    public static final String DEFAULT_CODE = "en_us";
    public static final MagicLanguageManager INSTANCE = new MagicLanguageManager();
    public ConcurrentHashMap<String, String> defaultLanguage = new ConcurrentHashMap<>();
    public ConcurrentHashMap<String, ConcurrentHashMap<String, String>> language = new ConcurrentHashMap<>();

    ArrayList<String> fallbackLanguageList = Lists.newArrayList(DEFAULT_CODE);
    private String currentCode = DEFAULT_CODE;
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

    @Environment(EnvType.CLIENT)
    public void initClient() {
        this.resourceManager = Minecraft.getInstance().getResourceManager();
        this.fallbackLanguageList = MagicLibConfigs.fallbackLanguageList;
        ((ReloadableResourceManager) Minecraft.getInstance().getResourceManager()).registerReloadListener(INSTANCE);
        this.reload();
    }


    private void initLanguage(String code, ConcurrentHashMap<String, String> language) {
        String languagePath = String.format("lang/%s.json", code);
        Set<String> nameSpaces;

        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
            // 1.15 的 server 缺少 ResourceManager.getNamespaces
            nameSpaces = ((MagicLanguageResourceManager) resourceManager).getNamespaces();
        } else {
            nameSpaces = resourceManager.getNamespaces();
        }
        for (String namespace : nameSpaces) {
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
                            MagicLibReference.LOGGER.warn("Failed to parse translations from {} ({})", resource, e);
                            continue;
                        }
                        // from minecraft code
                        if (inputStream != null) {
                            inputStream.close();
                        }
                    } catch (IOException e) {
                        MagicLibReference.LOGGER.warn("Failed to load translations from {} ({})", resource, e);
                    }
                }
            //#if MC <= 11802
            } catch (FileNotFoundException ignored) {
            // ignore..
            //#endif
            } catch (Exception e) {
                MagicLibReference.LOGGER.warn("Failed to load translations from {}:{} ({})", namespace, languagePath, e);
            }
        }
    }

    public void reload() {
        defaultLanguage.clear();
        language.clear();
        ArrayList<String> codes = new ArrayList<>(fallbackLanguageList);

        codes.remove(currentCode);
        codes.add(0, currentCode);

        if (!codes.contains(DEFAULT_CODE)) {
            codes.add(DEFAULT_CODE);
        }

        for (int i = codes.size() - 1; i >= 0; --i) {
            String code = codes.get(i);
            ConcurrentHashMap<String, String> currentLanguage = new ConcurrentHashMap<>();
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