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

package top.hendrixshen.magiclib.impl.malilib.config.statistic;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.api.malilib.config.MagicConfigManager;
import top.hendrixshen.magiclib.impl.malilib.config.ConfigContainer;
import top.hendrixshen.magiclib.util.serializable.JsonSaveAble;

/**
 * Reference to <a href="https://github.com/Fallen-Breath/tweakermore/blob/10e1a937aadcefb1f2d9d9bab8badc873d4a5b3d/src/main/java/me/fallenbreath/tweakermore/config/statistic/OptionStatisticSaver.java">TweakerMore</a>
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
