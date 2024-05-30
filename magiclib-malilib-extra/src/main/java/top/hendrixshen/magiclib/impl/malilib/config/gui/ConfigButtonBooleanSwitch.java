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

package top.hendrixshen.magiclib.impl.malilib.config.gui;

import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import top.hendrixshen.magiclib.api.i18n.I18n;
import top.hendrixshen.magiclib.api.malilib.config.option.HotkeyWithSwitch;

/**
 * Reference to <a href="https://github.com/Fallen-Breath/tweakermore/blob/10e1a937aadcefb1f2d9d9bab8badc873d4a5b3d/src/main/java/me/fallenbreath/tweakermore/gui/ConfigButtonBooleanSwitch.java">TweakerMore</a>
 */
public class ConfigButtonBooleanSwitch extends ButtonGeneric {
    private final HotkeyWithSwitch config;

    public ConfigButtonBooleanSwitch(int x, int y, int width, int height, HotkeyWithSwitch config) {
        super(x, y, width, height, "");
        this.config = config;
        this.updateDisplayString();
    }

    @Override
    protected boolean onMouseClickedImpl(int mouseX, int mouseY, int mouseButton) {
        this.config.toggleBooleanValue();
        this.updateDisplayString();
        return super.onMouseClickedImpl(mouseX, mouseY, mouseButton);
    }

    @Override
    public void updateDisplayString() {
        if (this.config.getEnableState()) {
            this.displayString = GuiBase.TXT_DARK_GREEN +
                    I18n.tr("magiclib.config.gui.element.config_button_boolean_switch.enabled") + GuiBase.TXT_RST;
        } else {
            this.displayString = GuiBase.TXT_DARK_RED +
                    I18n.tr("magiclib.config.gui.element.config_button_boolean_switch.disabled") + GuiBase.TXT_RST;
        }
    }
}
