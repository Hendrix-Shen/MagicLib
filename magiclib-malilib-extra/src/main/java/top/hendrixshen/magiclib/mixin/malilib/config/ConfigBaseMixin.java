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

package top.hendrixshen.magiclib.mixin.malilib.config;

import fi.dy.masa.malilib.config.options.ConfigBase;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import top.hendrixshen.magiclib.api.i18n.I18n;
import top.hendrixshen.magiclib.api.malilib.config.option.MagicIConfigBase;
import top.hendrixshen.magiclib.impl.malilib.config.GlobalConfigManager;

/**
 * Reference to <a href="https://github.com/Fallen-Breath/tweakermore/blob/476a25a5458a7058bdd402683b1fd833b189ae60/src/main/java/me/fallenbreath/tweakermore/mixins/core/config/ConfigBaseMixin.java">TweakerMore</a>.
 */
@Mixin(value = ConfigBase.class, remap = false)
public abstract class ConfigBaseMixin {
    @Shadow
    private String comment;

    @Final
    @Shadow
    private String prettyName;

    @Unique
    private boolean magiclib$isMagicConfig() {
        return this instanceof MagicIConfigBase;
    }

    @Inject(method = "getPrettyName", at = @At("HEAD"), cancellable = true, remap = false)
    private void tweakerMoreUseMyPrettyName(CallbackInfoReturnable<String> cir) {
        if (this.magiclib$isMagicConfig()) {
            GlobalConfigManager.getInstance().getContainerByConfig((MagicIConfigBase) this)
                    .ifPresent(configContainer -> cir.setReturnValue(I18n.tr(this.prettyName)));
        }
    }

    @Inject(method = "getComment", at = @At("HEAD"), cancellable = true, remap = false)
    private void magiclibUseMagicComment(CallbackInfoReturnable<String> cir) {
        if (this.magiclib$isMagicConfig()) {
            GlobalConfigManager.getInstance().getContainerByConfig((MagicIConfigBase) this)
                    .ifPresent(configContainer -> {
                        String translatedComment = I18n.tr(this.comment);
                        translatedComment = configContainer.modifyComment(translatedComment);
                        cir.setReturnValue(translatedComment);
                    });
        }
    }
}
