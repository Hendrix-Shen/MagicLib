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

package top.hendrixshen.magiclib.mixin.minecraft.i18n;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import top.hendrixshen.magiclib.api.i18n.I18n;
import top.hendrixshen.magiclib.impl.i18n.minecraft.translation.HookTranslationManager;

//#if MC > 11802
//$$ import net.minecraft.network.chat.contents.TranslatableContents;
//#else
import net.minecraft.network.chat.TranslatableComponent;
//#endif

/**
 * Reference to <a href="https://github.com/TISUnion/Carpet-TIS-Addition/blob/2733a1dfa4978374e7422376486b5c291ebb1bbc/src/main/java/carpettisaddition/mixins/translations/TranslatableTextMixin.java">Carpet-TIS-Addition</a>
 */
@Mixin(
        //#if MC > 11802
        //$$ TranslatableContents.class
        //#else
        TranslatableComponent.class
        //#endif
)
public class TranslatableText {
    @Shadow
    @Final
    private String key;

    @ModifyArg(
            method = "decompose",
            at = @At(
                    value = "INVOKE",
                    //#if MC > 11802
                    //$$ target = "Lnet/minecraft/network/chat/contents/TranslatableContents;decomposeTemplate(Ljava/lang/String;Ljava/util/function/Consumer;)V"
                    //#elseif MC > 11701
                    //$$ target = "Lnet/minecraft/network/chat/TranslatableComponent;decomposeTemplate(Ljava/lang/String;Ljava/util/function/Consumer;)V"
                    //#else
                    target = "Lnet/minecraft/network/chat/TranslatableComponent;decomposeTemplate(Ljava/lang/String;)V"
                    //#endif
            )
    )
    private String applyMagicTranslation(String vanillaTranslatedFormattingString) {
        if (HookTranslationManager.getInstance().isNamespaceRegistered(this.key) &&
                vanillaTranslatedFormattingString.equals(this.key)) {
            String translated = I18n.tr(vanillaTranslatedFormattingString);

            if (translated != null) {
                return translated;
            }
        }

        return vanillaTranslatedFormattingString;
    }
}
