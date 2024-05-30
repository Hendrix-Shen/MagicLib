/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2023  Fallen_Breath and contributors
 *
 * Carpet TIS Addition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Carpet TIS Addition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Carpet TIS Addition.  If not, see <https://www.gnu.org/licenses/>.
 */

package top.hendrixshen.magiclib.impl.i18n.minecraft.translation;

import com.google.common.base.Strings;
import lombok.Getter;
import top.hendrixshen.magiclib.api.compat.minecraft.network.chat.MutableComponentCompat;
import top.hendrixshen.magiclib.util.minecraft.ComponentUtil;

/**
 * Reference to <a href="https://github.com/TISUnion/Carpet-TIS-Addition/blob/2733a1dfa4978374e7422376486b5c291ebb1bbc/src/main/java/carpettisaddition/translations/Translator.java">Carpet-TIS-Addition</a>
 */
@Getter
public class Translator {
    private final String translationPath;

    public Translator(String translationPath) {
        if (
                Strings.isNullOrEmpty(translationPath) ||
                        translationPath.startsWith(".") ||
                        translationPath.endsWith(".")) {
            throw new RuntimeException("Invalid translation path: " + translationPath);
        }

        this.translationPath = translationPath;
    }

    public Translator getDerivedTranslator(String derivedName) {
        return new Translator(this.translationPath + "." + derivedName);
    }

    public MutableComponentCompat tr(String key, Object... args) {
        return ComponentUtil.tr(key, args);
    }
}
