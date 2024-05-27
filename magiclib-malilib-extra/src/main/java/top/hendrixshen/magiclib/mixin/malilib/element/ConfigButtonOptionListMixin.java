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

package top.hendrixshen.magiclib.mixin.malilib.element;

import fi.dy.masa.malilib.config.IConfigOptionList;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.button.ConfigButtonOptionList;
import fi.dy.masa.malilib.gui.interfaces.IGuiIcon;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.hendrixshen.magiclib.api.malilib.config.gui.ConfigButtonOptionListHovering;

/**
 * Reference to <a href="https://github.com/Fallen-Breath/tweakermore/blob/10e1a937aadcefb1f2d9d9bab8badc873d4a5b3d/src/main/java/me/fallenbreath/tweakermore/mixins/core/gui/element/ConfigButtonOptionListMixin.java">TweakerMore</a>
 */
@Mixin(value = ConfigButtonOptionList.class, remap = false)
public abstract class ConfigButtonOptionListMixin extends ButtonGeneric implements ConfigButtonOptionListHovering {
    @Shadow
    @Final
    private IConfigOptionList config;

    @Shadow
    public abstract void updateDisplayString();

    @Unique
    private boolean magiclib$enableValueHovering;

    public ConfigButtonOptionListMixin(int x, int y, IGuiIcon icon, String... hoverStrings) {
        super(x, y, icon, hoverStrings);
    }

    @Override
    public void magiclib$setEnableValueHovering() {
        this.magiclib$enableValueHovering = true;
        this.updateDisplayString();
    }

    @Inject(
            method = "updateDisplayString",
            at = @At(
                    value = "TAIL"
            )
    )
    private void makeSomeValueHovering(CallbackInfo ci) {
        if (this.magiclib$enableValueHovering) {
            this.setHoverStrings(this.magiclib$makeHoveringLines(this.config));
        }
    }
}
