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
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.hendrixshen.magiclib.api.malilib.config.option.MagicIConfigBase;
import top.hendrixshen.magiclib.impl.malilib.config.GlobalConfigManager;
import top.hendrixshen.magiclib.util.MiscUtil;

//#if MC > 11701
//$$ import org.spongepowered.asm.mixin.Shadow;
//$$ import org.spongepowered.asm.mixin.injection.ModifyArg;
//#endif

/**
 * Reference to <a href="https://github.com/Fallen-Breath/tweakermore/blob/10e1a937aadcefb1f2d9d9bab8badc873d4a5b3d/src/main/java/me/fallenbreath/tweakermore/mixins/core/config/ConfigBaseMixin.java">TweakerMore</a>
 */
@Mixin(value = ConfigBase.class, remap = false)
public class ConfigBaseMixin {
    //#if MC > 11701
    //$$ @Shadow
    //$$ private String comment;
    //$$
    //$$ @ModifyArg(
    //$$         method = "getComment",
    //$$         at = @At(
    //$$                 value = "INVOKE",
    //$$                 target = "Lfi/dy/masa/malilib/util/StringUtils;getTranslatedOrFallback(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;"
    //$$         ),
    //$$         index = 0
    //$$ )
    //$$ private String magicConfigCommentIsTheTranslationKey(String key) {
    //$$     if (MiscUtil.cast(this) instanceof MagicIConfigBase) {
    //$$         key = this.comment;
    //$$     }
    //$$
    //$$     return key;
    //$$ }
    //#endif

    @Inject(
            method = "getComment",
            at = @At(
                    value = "TAIL"
            ),
            cancellable = true
    )
    private void appendComment(CallbackInfoReturnable<String> cir) {
        if (!(MiscUtil.cast(this) instanceof MagicIConfigBase)) {
            return;
        }

        GlobalConfigManager.getInstance().getContainerByConfig((MagicIConfigBase) this)
                .ifPresent(configContainer -> cir.setReturnValue(configContainer.modifyComment(cir.getReturnValue())));
    }
}
