package top.hendrixshen.magiclib.impl.malilib.config;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fi.dy.masa.malilib.config.ConfigUtils;
import fi.dy.masa.malilib.config.IConfigHandler;
import fi.dy.masa.malilib.util.JsonUtils;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.hendrixshen.magiclib.api.malilib.annotation.Config;
import top.hendrixshen.magiclib.api.malilib.config.MagicConfigManager;
import top.hendrixshen.magiclib.api.malilib.config.option.MagicIConfigBase;
import top.hendrixshen.magiclib.impl.malilib.config.statistic.ConfigStatisticSaver;
import top.hendrixshen.magiclib.util.FileUtil;
import top.hendrixshen.magiclib.util.serializable.JsonSaveAble;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

//#if MC < 11800
import com.google.gson.JsonParser;
//#endif

//#if MC < 11700
import top.hendrixshen.magiclib.MagicLib;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
//#endif

public class MagicConfigHandler implements IConfigHandler {
    @Getter
    private final MagicConfigManager configManager;
    @Getter
    private final String identifier;
    private final File configFile;
    private JsonObject loadedJson = new JsonObject();
    private final Map<String, JsonSaveAble> internalDataSavers = Maps.newHashMap();
    private final Map<String, JsonSaveAble> externalDataSavers = Maps.newHashMap();
    @Getter
    private final GlobalConfig globalConfig = new GlobalConfig();

    @Setter
    @Nullable
    private Consumer<MagicConfigHandler> preDeserializeCallback;
    @Setter
    @Nullable
    private Consumer<MagicConfigHandler> postDeserializeCallback;
    @Setter
    @Nullable
    private Consumer<MagicConfigHandler> preSerializeCallback;
    @Setter
    @Nullable
    private Consumer<MagicConfigHandler> postSerializeCallback;

    public MagicConfigHandler(@NotNull MagicConfigManager configManager, int configVersion) {
        this.identifier = configManager.getIdentifier();
        this.configFile = FileUtil.getConfigFile(this.identifier);
        this.configManager = configManager;
        this.internalDataSavers.put("global", this.globalConfig);
        this.internalDataSavers.put("config_gui", this.configManager.getGuiSetting());
        this.internalDataSavers.put("configStatistic", new ConfigStatisticSaver(this.configManager));
    }

    public boolean registerExternalData(String namespace, JsonSaveAble data) {
        if (this.externalDataSavers.containsKey(namespace)) {
            return false;
        }

        this.externalDataSavers.put(namespace, data);
        return true;
    }

    public boolean unregisterExternalData(String namespace) {
        if (!this.externalDataSavers.containsKey(namespace)) {
            return false;
        }

        this.externalDataSavers.remove(namespace);
        return true;
    }

    public void loadConfig(JsonObject root) {
        for (String category : this.configManager.getCategories().stream()
                .filter(category -> !category.equals(Config.defaultCategory))
                .collect(Collectors.toList())) {
            List<MagicIConfigBase> configs = this.configManager.getContainers(category).stream()
                    .map(ConfigContainer::getConfig)
                    .collect(Collectors.toList());
            ConfigUtils.readConfigBase(root, category, configs);
        }
    }

    public void saveConfig(JsonObject root) {
        for (String category : this.configManager.getCategories().stream()
                .filter(category -> !category.equals(Config.defaultCategory))
                .collect(Collectors.toList())) {
            List<MagicIConfigBase> configs = this.configManager.getContainers(category).stream()
                    .map(ConfigContainer::getConfig)
                    .collect(Collectors.toList());
            ConfigUtils.writeConfigBase(root, category, configs);
        }
    }

    public final void loadFromJson(@NotNull JsonObject root) {
        //#if MC > 11701
        //$$ this.loadedJson = root.deepCopy();
        //#else
        this.loadedJson = new JsonParser().parse(root.toString()).getAsJsonObject();
        //#endif

        if (this.preDeserializeCallback != null) {
            this.preDeserializeCallback.accept(this);
        }

        this.loadConfig(root);
        this.loadInternal(root);
        this.loadExternal(root);

        if (this.postDeserializeCallback != null) {
            this.postDeserializeCallback.accept(this);
        }

        this.configManager.onConfigLoaded();
    }

    public final void saveToJson() {
        if (this.preSerializeCallback != null) {
            this.preSerializeCallback.accept(this);
        }

        this.saveConfig(this.loadedJson);
        this.saveInternal(this.loadedJson);
        this.saveExternal(this.loadedJson);

        if (this.postSerializeCallback != null) {
            this.postSerializeCallback.accept(this);
        }
    }

    private void loadExternal(JsonObject jsonObject) {
        this.loadInjected(jsonObject, "external", this.externalDataSavers);
    }

    private void saveExternal(JsonObject jsonObject) {
        this.saveInjected(jsonObject, "external", this.externalDataSavers);
    }

    private void loadInternal(JsonObject jsonObject) {
        this.loadInjected(jsonObject, "internal", this.internalDataSavers);
    }

    private void saveInternal(JsonObject jsonObject) {
        this.saveInjected(jsonObject, "internal", this.internalDataSavers);
    }

    private void loadInjected(JsonObject jsonObject, String namespace, Map<String, JsonSaveAble> mapping) {
        JsonObject injected = JsonUtils.getNestedObject(jsonObject, namespace, false);

        if (injected != null) {
            mapping.forEach((name, jsonSaveAble) -> {
                JsonObject object = JsonUtils.getNestedObject(injected, name, false);

                if (object != null) {
                    jsonSaveAble.loadFromJsonSafe(object);
                }
            });
        }
    }

    private void saveInjected(JsonObject jsonObject, String namespace, @NotNull Map<String, JsonSaveAble> mapping) {
        JsonObject injected = JsonUtils.getNestedObject(jsonObject, namespace, true);
        assert injected != null;
        mapping.forEach((name, jsonSaveAble) -> injected.add(name, jsonSaveAble.dumpToJson()));
    }

    @Override
    public final void load() {
        JsonObject root = null;

        if (this.configFile.exists() && this.configFile.isFile() && this.configFile.canRead()) {
            JsonElement element = JsonUtils.parseJsonFile(this.configFile);

            if (element != null && element.isJsonObject()) {
                root = element.getAsJsonObject();
            }
        }

        if (root != null) {
            this.loadFromJson(root);
        }
    }

    @Override
    public final void save() {
        this.saveToJson();

        //#if MC > 11605
        //$$ JsonUtils.writeJsonToFile(this.loadedJson, configFile);
        //#else
        try {
            File tempFile = new File(configFile.getParent(), configFile.getName() + ".tmp");
            JsonUtils.writeJsonToFile(this.loadedJson, tempFile);
            Files.move(tempFile.toPath(), configFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            MagicLib.getLogger().error("Failed to save the config file of {}", this.identifier, e);
        }
        //#endif
    }

    private static class GlobalConfig implements JsonSaveAble {
        private int configVersion = 0;

        @Override
        public void dumpToJson(@NotNull JsonObject jsonObject) {
            jsonObject.addProperty("config_version", this.configVersion);
        }

        @Override
        public void loadFromJson(@NotNull JsonObject jsonObject) {
            this.configVersion = jsonObject.get("config_version").getAsInt();
        }
    }
}
