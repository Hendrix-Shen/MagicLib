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

package top.hendrixshen.magiclib.api.malilib.config.option;

import fi.dy.masa.malilib.config.IConfigOptionListEntry;
import top.hendrixshen.magiclib.util.minecraft.StringUtil;

import java.util.Arrays;

/**
 * Reference to <a href="https://github.com/Fallen-Breath/tweakermore/blob/10e1a937aadcefb1f2d9d9bab8badc873d4a5b3d/src/main/java/me/fallenbreath/tweakermore/config/options/listentries/EnumOptionEntry.java">TweakerMore</a>
 */
public interface EnumOptionEntry extends IConfigOptionListEntry {
    String name();

    int ordinal();

    EnumOptionEntry[] getAllValues();

    EnumOptionEntry getDefault();

    String getTranslationPrefix();

    @Override
    default String getStringValue() {
        return this.name().toLowerCase();
    }

    @Override
    default String getDisplayName() {
        return StringUtil.translateOrFallback(String.format("%s.value.%s", this.getTranslationPrefix(), this.name()),
                this.name());
    }

    @Override
    default IConfigOptionListEntry cycle(boolean forward) {
        int index = this.ordinal();
        EnumOptionEntry[] values = this.getAllValues();
        index += forward ? 1 : -1;
        index = (index + values.length) % values.length;
        return values[index];
    }

    @Override
    default IConfigOptionListEntry fromString(String value) {
        return Arrays.stream(this.getAllValues())
                .filter(o -> o.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseGet(this::getDefault);
    }
}
