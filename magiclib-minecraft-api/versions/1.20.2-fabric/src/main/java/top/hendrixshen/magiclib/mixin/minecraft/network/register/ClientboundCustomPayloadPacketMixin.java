/*
 * This file is part of the fanetlib project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2024  Fallen_Breath and contributors
 *
 * fanetlib is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * fanetlib is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with fanetlib.  If not, see <https://www.gnu.org/licenses/>.
 */

package top.hendrixshen.magiclib.mixin.minecraft.network.register;

import com.google.common.collect.ImmutableMap;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import top.hendrixshen.magiclib.impl.network.packet.MagicCustomPayload;
import top.hendrixshen.magiclib.impl.network.packet.MagicPacketRegistrationCenterHelper;
import top.hendrixshen.magiclib.impl.network.packet.MagicPacketRegistry;

import java.util.Map;

/**
 * Reference to <a href="https://github.com/Fallen-Breath/fanetlib/blob/20335b54fda0bc38cf52552878ad84755d9a66f9/versions/1.20.2/src/main/java/me/fallenbreath/fanetlib/mixins/register/CustomPayloadS2CPacketMixin.java">fanetlib</a>.
 *
 * <li>mc1.14 ~ mc1.20.1  : subproject 1.16.5 (main project) [dummy]</li>
 * <li>mc1.20.2 ~ mc1.20.5: subproject 1.20.2        &lt;--------</li>
 * <li>mc1.20.6+          : subproject 1.20.6</li>
 */
@SuppressWarnings({"rawtypes", "unchecked"})
@Mixin(ClientboundCustomPayloadPacket.class)
public abstract class ClientboundCustomPayloadPacketMixin {
    @Mutable
    @Shadow
    @Final
    private static Map<ResourceLocation, FriendlyByteBuf.Reader<? extends CustomPacketPayload>> KNOWN_TYPES;

    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void registerServerboundMagicPayload(CallbackInfo ci) {
        MagicPacketRegistrationCenterHelper.collectClientbound();
        ImmutableMap.Builder<ResourceLocation, FriendlyByteBuf.Reader<? extends CustomPacketPayload>> builder = ImmutableMap
                .<ResourceLocation, FriendlyByteBuf.Reader<? extends CustomPacketPayload>>builder()
                .putAll(ClientboundCustomPayloadPacketMixin.KNOWN_TYPES);
        MagicPacketRegistry.CLIENTBOUND_GAME.getRegistry()
                .forEach((type, entry) ->
                        builder.put(type.getIdentifier(), buf -> new MagicCustomPayload(type, entry.getCodec(), buf)));
        ClientboundCustomPayloadPacketMixin.KNOWN_TYPES = builder.build();
    }
}
