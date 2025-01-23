package top.hendrixshen.magiclib.util.serializable;

import com.google.gson.JsonElement;
import org.jetbrains.annotations.NotNull;

import top.hendrixshen.magiclib.MagicLib;

public interface JsonSerializable<O, J extends JsonElement> {
    J serialize(O object);

    O deserialize(@NotNull J jsonElement);

    default O deserializeSafe(@NotNull J jsonElement, @NotNull O defaultValue) {
        try {
            return this.deserialize(jsonElement);
        } catch (Exception e) {
            MagicLib.getLogger().warn("Failed to load data of {} from json object {}: {}",
                    this.getClass().getSimpleName(), jsonElement, e);
            return defaultValue;
        }
    }
}
