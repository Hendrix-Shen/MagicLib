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

package top.hendrixshen.magiclib.mixin.minecraft.network.hook;

import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import net.minecraft.resources.ResourceLocation;

// CHECKSTYLE.OFF: ImportOrder
//#if MC >= 12002
//$$ import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl;
//#else
import net.minecraft.network.FriendlyByteBuf;
//#endif
// CHECKSTYLE.ON: ImportOrder

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import top.hendrixshen.magiclib.api.network.packet.ClientboundPacketHandler;
import top.hendrixshen.magiclib.api.network.packet.PacketType;
import top.hendrixshen.magiclib.impl.network.packet.MagicCustomPayload;
import top.hendrixshen.magiclib.impl.network.packet.MagicPacketRegistry;
import top.hendrixshen.magiclib.impl.network.packet.PacketHandlerContextImpl;
import top.hendrixshen.magiclib.impl.network.packet.RegistryEntry;

// CHECKSTYLE.OFF: ImportOrder
//#if MC < 12002
import top.hendrixshen.magiclib.mixin.minecraft.accessor.ClientboundCustomPayloadPacketAccessor;
import top.hendrixshen.magiclib.util.MiscUtil;
//#endif
// CHECKSTYLE.ON: ImportOrder

/**
 * Reference to <a href="https://github.com/Fallen-Breath/fanetlib/blob/20335b54fda0bc38cf52552878ad84755d9a66f9/src/main/java/me/fallenbreath/fanetlib/mixins/hook/ClientPlayNetworkHandlerMixin.java">fanetlib</a>.
 */
@Mixin(
        //#if MC >= 12002
        //$$ ClientCommonPacketListenerImpl.class
        //#else
        ClientPacketListener.class
        //#endif
)
public abstract class ClientPacketListenerMixin {
    @SuppressWarnings({
            // CHECKSTYLE.OFF: NoWhitespaceBefore
            // CHECKSTYLE.OFF: SeparatorWrap
            "rawtypes",
            "unchecked"
            //#if MC >= 12002
            //$$ , "ConstantConditions"
            //#endif
            // CHECKSTYLE.ON: SeparatorWrap
            // CHECKSTYLE.ON: NoWhitespaceBefore
    })
    @Inject(
            //#if MC >= 12002
            //$$ method = "handleCustomPayload(Lnet/minecraft/network/protocol/common/ClientboundCustomPayloadPacket;)V",
            //#else
            method = "handleCustomPayload",
            //#endif
            at = @At("HEAD"),
            cancellable = true
    )
    private void handleClientboundCustomPayload(ClientboundCustomPayloadPacket packet, CallbackInfo ci) {
        //#if MC >= 12005
        //$$ ResourceLocation identifier = packet.payload().type().id();
        //#elseif MC >= 12002
        //$$ ResourceLocation identifier = packet.payload().id();
        //#else
        ResourceLocation identifier = ((ClientboundCustomPayloadPacketAccessor) packet).magiclib$getIdentifier();
        //#endif
        PacketType packetId = PacketType.of(identifier);
        RegistryEntry<ClientboundPacketHandler<?>, ?> entry = MagicPacketRegistry.CLIENTBOUND_GAME.getEntry(packetId);

        if (entry == null) {
            return;
        }

        //#if MC >= 12002
        //$$ if (packet.payload() instanceof MagicCustomPayload mcp && (Object) this instanceof ClientPacketListener self) {
        //$$     ClientPacketListenerMixin.magiclib$handleCustomPayload(mcp, (ClientboundPacketHandler) entry.getHandler(), self);
        //$$     ci.cancel();
        //$$ }
        //#else
        FriendlyByteBuf buf = ((ClientboundCustomPayloadPacketAccessor) packet).magiclib$getData();

        try {
            MagicCustomPayload<?> payload = new MagicCustomPayload<>(packetId, entry.getCodec(), buf);
            ClientPacketListenerMixin.magiclib$handleCustomPayload(payload, (ClientboundPacketHandler) entry.getHandler(), MiscUtil.cast(this));
            ci.cancel();
        } finally {
            // Fix https://bugs.mojang.com/browse/MC/issues/MC-121884, for magic packets.
            buf.release();
        }
        //#endif
    }

    @Unique
    private static <P> void magiclib$handleCustomPayload(MagicCustomPayload<P> payload, ClientboundPacketHandler<P> handler, ClientPacketListener self) {
        handler.handle(payload.getPacket(), new PacketHandlerContextImpl.Clientbound(self));
    }
}
