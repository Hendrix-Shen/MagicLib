package top.hendrixshen.magiclib.util.serializable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.MagicLib;

/**
 * Reference to <a href="https://github.com/Fallen-Breath/TweakerMore">TweakerMore</a>
 */
public interface JsonSaveAble {
    default JsonObject dumpToJson() {
        JsonObject jsonObject = new JsonObject();
        this.dumpToJson(jsonObject);
        return jsonObject;
    }

    void dumpToJson(@NotNull JsonObject jsonObject);

    void loadFromJson(@NotNull JsonObject jsonObject);

    default void loadFromJsonSafe(JsonObject jsonObject) {
        try {
            this.loadFromJson(jsonObject);
        } catch (Exception e) {
            MagicLib.getLogger().warn("Failed to load data of {} from json object {}: {}",
                    this.getClass().getSimpleName(), jsonObject, e);
        }
    }

    @SuppressWarnings("unchecked")
    default <T extends Enum<T>> T getEnumSafe(@NotNull JsonObject jsonObject, String key, @NotNull T fallbackValue) {
        JsonElement jsonElement = jsonObject.get(key);

        if (jsonElement != null && jsonElement.isJsonPrimitive()) {
            String jsonName = jsonElement.getAsString();

            try {
                return Enum.valueOf((Class<T>) fallbackValue.getClass(), jsonName);
            } catch (Exception e) {
                MagicLib.getLogger().warn("Failed to load data of {} from json object {}: {}",
                        this.getClass().getSimpleName(), jsonObject, e);
            }
        }

        return fallbackValue;
    }
}
