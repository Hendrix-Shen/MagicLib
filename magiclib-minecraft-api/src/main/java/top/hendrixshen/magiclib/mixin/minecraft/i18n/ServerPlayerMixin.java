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

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.hendrixshen.magiclib.api.compat.minecraft.network.chat.MutableComponentCompat;
import top.hendrixshen.magiclib.api.fake.i18n.ServerPlayerLanguage;
import top.hendrixshen.magiclib.impl.i18n.minecraft.translation.MagicTranslation;
import top.hendrixshen.magiclib.util.MiscUtil;

import java.util.Locale;

//#if MC > 12001
//$$ import net.minecraft.server.level.ClientInformation;
//#else
import net.minecraft.network.protocol.game.ServerboundClientInformationPacket;
//#endif

//#if MC > 11502
import net.minecraft.network.chat.MutableComponent;
//#else
//$$ import net.minecraft.network.chat.BaseComponent;
//#endif

//#if MC > 11502 && MC < 11800
import top.hendrixshen.magiclib.mixin.minecraft.accessor.ServerboundClientInformationPacketAccessor;
//#endif

/**
 * Reference to <a href="https://github.com/TISUnion/Carpet-TIS-Addition/blob/2733a1dfa4978374e7422376486b5c291ebb1bbc/src/main/java/carpettisaddition/mixins/translations/ServerPlayerEntityMixin.java">Carpet-TIS-Addition</a>
 */
@Mixin(ServerPlayer.class)
public class ServerPlayerMixin implements ServerPlayerLanguage {
    @Unique
    private String magiclib$language = "en_us";

    @Inject(
            method = "updateOptions",
            at = @At(
                    value = "HEAD"
            )
    )
    private void preUpdateOptions(
            //#if MC > 12001
            //$$ @NotNull ClientInformation clientInformation,
            //#else
            @NotNull ServerboundClientInformationPacket serverboundClientInformationPacket,
            //#endif
            CallbackInfo ci
    ) {
        //#if MC > 12001
        //$$ this.magiclib$language = clientInformation.language().toLowerCase(Locale.ROOT);
        //#elseif MC > 11701
        //$$ this.magiclib$language = serverboundClientInformationPacket.language().toLowerCase(Locale.ROOT);
        //#elseif MC > 11502
        this.magiclib$language = ((ServerboundClientInformationPacketAccessor) serverboundClientInformationPacket)
                .magiclib$getLanguage().toLowerCase(Locale.ROOT);
        //#else
        //$$ this.magiclib$language = serverboundClientInformationPacket.getLanguage().toLowerCase(Locale.ROOT);
        //#endif
    }

    @Override
    public String magicLib$getLanguage() {
        return this.magiclib$language;
    }

    /**
     * This handle all Magic translation on chat messages
     */
    @ModifyVariable(
            method = {
                    //#if MC > 11900
                    //$$ "sendSystemMessage(Lnet/minecraft/network/chat/Component;Z)V",
                    //#elseif MC > 11502
                    "sendMessage(Lnet/minecraft/network/chat/Component;Ljava/util/UUID;)V",
                    "sendMessage(Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/ChatType;Ljava/util/UUID;)V",
                    //#else
                    //$$ "displayClientMessage",
                    //$$ "sendMessage(Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/ChatType;)V",
                    //#endif
            },
            at = @At(
                    "HEAD"
            ),
            argsOnly = true
    )
    private Component applyMagicTranslationToChatMessage(Component message) {
        if (message instanceof
                //#if MC > 11502
                MutableComponent
            //#else
            //$$ BaseComponent
            //#endif
        ) {
            message = MagicTranslation.translate(MutableComponentCompat.of(MiscUtil.cast(message)),
                    (ServerPlayer) (Object) this).get();
        }

        return message;
    }
}
