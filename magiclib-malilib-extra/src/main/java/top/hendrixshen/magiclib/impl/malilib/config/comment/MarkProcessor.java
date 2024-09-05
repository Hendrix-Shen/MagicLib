/*
 * This file is part of the TweakerMore project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2024  Fallen_Breath and contributors
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

import fi.dy.masa.malilib.gui.GuiBase;
import org.jetbrains.annotations.NotNull;

/**
 * Reference to <a href="https://github.com/Fallen-Breath/tweakermore/blob/1459fbdcaaa47fa1c505c0629a4722710d1f975d/src/main/java/me/fallenbreath/tweakermore/config/comment/MarkProcessor.java">TweakerMore</a>
 */
public class MarkProcessor {
    public static String processMarks(String text) {
        text = MarkProcessor.processCommand(text);
        return text;
    }

    private static String processCommand(@NotNull String text) {
        if (text.contains("`")) {
            String[] parts = text.split("`");

            if (parts.length % 2 == 1) {
                StringBuilder builder = new StringBuilder();

                for (int i = 0; i < parts.length; i++) {
                    if (i % 2 == 1) {
                        builder.append(GuiBase.TXT_GRAY);
                    }

                    builder.append(parts[i]);

                    if (i % 2 == 1) {
                        builder.append(GuiBase.TXT_RST);
                    }
                }

                text = builder.toString();
            }
        }

        return text;
    }
}
