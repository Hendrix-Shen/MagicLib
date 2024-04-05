package top.hendrixshen.magiclib.impl.malilib.config.statistic;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.api.malilib.config.MagicConfigManager;
import top.hendrixshen.magiclib.impl.malilib.config.ConfigContainer;
import top.hendrixshen.magiclib.util.serializable.JsonSaveAble;

/**
 * Reference to <a href="https://github.com/Fallen-Breath/TweakerMore">TweakerMore</a>
 */
public class ConfigStatisticSaver implements JsonSaveAble {
    private final MagicConfigManager configManager;

    public ConfigStatisticSaver(MagicConfigManager configManager) {
        this.configManager = configManager;
    }

    @Override
    public void dumpToJson(@NotNull JsonObject jsonObject) {
        for (ConfigContainer configContainer : configManager.getAllContainers()) {
            jsonObject.add(configContainer.getConfig().getName(), configContainer.getStatistic().toJson());
        }
    }

    @Override
    public void loadFromJson(@NotNull JsonObject jsonObject) {
        for (ConfigContainer configContainer : configManager.getAllContainers()) {
            String key = configContainer.getConfig().getName();

            if (jsonObject.has(key)) {
                configContainer.getStatistic().loadFromJson(jsonObject.get(key));
            }
        }
    }
}
