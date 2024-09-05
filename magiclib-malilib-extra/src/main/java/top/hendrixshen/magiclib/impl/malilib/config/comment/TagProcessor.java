/*
 * This file is part of the TweakerMore project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2023  Fallen_Breath and contributors
 * Copyright (C) 2024  Hendrix-Shen
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
 *
 * This file has been modified by the magiclib project (repackage, minor changes).
 */

package top.hendrixshen.magiclib.impl.malilib.config.comment;

import com.google.common.collect.ImmutableMap;
import fi.dy.masa.malilib.gui.GuiBase;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.api.i18n.I18n;
import top.hendrixshen.magiclib.impl.malilib.config.ConfigContainer;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Reference to <a href="https://github.com/Fallen-Breath/tweakermore/blob/1459fbdcaaa47fa1c505c0629a4722710d1f975d/src/main/java/me/fallenbreath/tweakermore/config/comment/TagProcessor.java">TweakerMore</a>
 */
public class TagProcessor {
    private static final Map<String, Transformer> TRANSFORMERS = ImmutableMap.of(
            "tr", TagProcessor::transformTranslation,
            "config", TagProcessor::transformConfig
    );

    @FunctionalInterface
    private interface Transformer {
        String transform(ConfigContainer configContainer, String value);
    }

    /**
     * Tag Syntax:
     * <li>"@tr#my.translation.key@": translation with the given key.</li>
     * <li>"@config#myConfig@": translated and colored name of the given magic config.</li>
     */
    public static @NotNull String processReferences(ConfigContainer configContainer, String comment) {
        String patternString = "@([a-zA-Z0-9]+)#([a-zA-Z0-9.]+)@";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(comment);

        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            String type = matcher.group(1);
            String value = matcher.group(2);
            Transformer transformer = TagProcessor.TRANSFORMERS.getOrDefault(type, (s, c) -> matcher.group());
            matcher.appendReplacement(sb, transformer.transform(configContainer, value));
        }

        matcher.appendTail(sb);
        return sb.toString();
    }

    private static String transformTranslation(ConfigContainer configContainer, String translationKey) {
        return I18n.tr(translationKey);
    }

    private static String transformConfig(ConfigContainer configContainer, String configName) {
        // Only look up configs from the same ConfigManager.
        if (configContainer.getConfigManager() == null) {
            return configName;
        }

        return configContainer.getConfigManager().getContainerByName(configName).map(config -> {
            String displayName = config.getConfig().getConfigGuiDisplayName();
            return GuiBase.TXT_YELLOW + displayName + GuiBase.TXT_RST;
        }).orElse(configName);
    }
}
