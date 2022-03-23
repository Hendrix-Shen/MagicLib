package top.hendrixshen.magiclib.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import fi.dy.masa.malilib.config.ConfigUtils;
import fi.dy.masa.malilib.config.IConfigHandler;
import fi.dy.masa.malilib.config.options.ConfigBase;
import fi.dy.masa.malilib.util.FileUtils;
import fi.dy.masa.malilib.util.JsonUtils;
import top.hendrixshen.magiclib.MagicLib;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ConfigHandler implements IConfigHandler {

    public final Path configPath;
    public final ConfigManager configManager;
    public final int configVersion;
    public final String modId;

    @Nullable
    public final Consumer<ConfigHandler> preDeserializeCallback;
    @Nullable
    public final Consumer<ConfigHandler> postSerializeCallback;

    public JsonObject jsonObject;


    public ConfigHandler(String modId, ConfigManager configManager, int configVersion,
                         @Nullable Consumer<ConfigHandler> preDeserializeCallback,
                         @Nullable Consumer<ConfigHandler> postSerializeCallback) {
        this(modId, Paths.get(String.format("%s.json", modId)), configManager, configVersion,
                preDeserializeCallback, postSerializeCallback);
    }

    public ConfigHandler(String modId, Path configPath, ConfigManager configManager, int configVersion,
                         @Nullable Consumer<ConfigHandler> preDeserializeCallback,
                         @Nullable Consumer<ConfigHandler> postSerializeCallback) {
        this.configPath = FileUtils.getConfigDirectory().toPath().resolve(configPath);
        this.configManager = configManager;
        this.preDeserializeCallback = preDeserializeCallback;
        this.postSerializeCallback = postSerializeCallback;
        this.configVersion = configVersion;
        this.jsonObject = new JsonObject();
        this.modId = modId;
    }

    public static void register(ConfigHandler configHandler) {
        fi.dy.masa.malilib.config.ConfigManager.getInstance().registerConfigHandler(configHandler.modId, configHandler);
    }

    // from 1.18 malilib
    public static boolean writeJsonToFile(JsonObject root, File file) {
        File fileTmp = new File(file.getParentFile(), file.getName() + ".tmp");
        if (fileTmp.exists()) {
            fileTmp = new File(file.getParentFile(), UUID.randomUUID() + ".tmp");
        }

        try {
            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(fileTmp), StandardCharsets.UTF_8);

            boolean var4;
            try {
                writer.write(JsonUtils.GSON.toJson(root));
                writer.close();
                if (file.exists() && file.isFile() && !file.delete()) {
                    MagicLib.getLogger().warn("Failed to delete file '{}'", file.getAbsolutePath());
                }

                var4 = fileTmp.renameTo(file);
            } catch (Throwable var7) {
                try {
                    writer.close();
                } catch (Throwable var6) {
                    var7.addSuppressed(var6);
                }

                throw var7;
            }

            writer.close();
            return var4;
        } catch (Exception var8) {
            MagicLib.getLogger().warn("Failed to write JSON data to file '{}'", fileTmp.getAbsolutePath(), var8);
            return false;
        }
    }

    public void loadFromFile() {

        JsonElement jsonElement = JsonUtils.parseJsonFile(this.configPath.toFile());

        if (jsonElement != null && jsonElement.isJsonObject()) {

            this.jsonObject = jsonElement.getAsJsonObject();

            if (this.preDeserializeCallback != null) {
                this.preDeserializeCallback.accept(this);
            }

            for (String category : this.configManager.getCategories()) {

                List<ConfigBase<?>> configs = this.configManager.getOptionsByCategory(category).stream()
                        .map(Option::getConfig).collect(Collectors.toList());

                ConfigUtils.readConfigBase(this.jsonObject, category, configs);
            }
        }
    }

    public void saveToFile() {

        for (String category : this.configManager.getCategories()) {

            List<ConfigBase<?>> configs = this.configManager.getOptionsByCategory(category).stream()
                    .map(Option::getConfig).collect(Collectors.toList());

            ConfigUtils.writeConfigBase(this.jsonObject, category, configs);
        }

        if (this.preDeserializeCallback != null) {
            this.preDeserializeCallback.accept(this);
        }
        this.jsonObject.add("configVersion", new JsonPrimitive(this.configVersion));
        writeJsonToFile(this.jsonObject, this.configPath.toFile());
    }

    @Override
    public void load() {
        loadFromFile();
    }

    @Override
    public void save() {
        saveToFile();
    }
}
