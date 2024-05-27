/*
 * This file is part of the TweakerMore project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2023  Fallen_Breath and contributors
 *
 * TweakerMore is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TweakerMore is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with TweakerMore.  If not, see <https://www.gnu.org/licenses/>.
 */

package top.hendrixshen.magiclib.util.serializable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.MagicLib;

/**
 * Reference to <a href="https://github.com/Fallen-Breath/tweakermore/blob/10e1a937aadcefb1f2d9d9bab8badc873d4a5b3d/src/main/java/me/fallenbreath/tweakermore/util/JsonSaveAble.java">TweakerMore</a>
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
