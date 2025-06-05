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

package top.hendrixshen.magiclib.api.network.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;
import net.minecraft.server.level.ServerPlayer;

import top.hendrixshen.magiclib.MagicLib;
import top.hendrixshen.magiclib.impl.network.packet.MagicPacketRegistrationCenterHelper;
import top.hendrixshen.magiclib.impl.network.packet.MagicPacketRegistry;

/**
 * Reference to <a href="https://github.com/Fallen-Breath/fanetlib/blob/20335b54fda0bc38cf52552878ad84755d9a66f9/src/main/java/me/fallenbreath/fanetlib/api/packet/FanetlibPackets.java">fanetlib</a>.
 */
public class MagicPackets {
    public static <P> void registerServerbound(PacketType<P> id, PacketCodec<P> codec, ServerboundPacketHandler<P> handler) {
        if (MagicPacketRegistrationCenterHelper.isTooLateForServerboundRegister()) {
            MagicLib.getLogger().warn("MagicLib receives an Serverbound packet register request with id {} too late", id);
        }

        MagicPacketRegistry.SERVERBOUND_GAME.register(id, codec, handler);
    }

    public static <P> void registerClientbound(PacketType<P> id, PacketCodec<P> codec, ClientboundPacketHandler<P> handler) {
        if (MagicPacketRegistrationCenterHelper.isTooLateForClientboundRegister()) {
            MagicLib.getLogger().warn("MagicLib receives an Clientbound packet register request with id {} too late", id);
        }

        MagicPacketRegistry.CLIENTBOUND_GAME.register(id, codec, handler);
    }

    public static <P> void registerDual(PacketType<P> id, PacketCodec<P> codec, ServerboundPacketHandler<P> serverboundHandler, ClientboundPacketHandler<P> clientboundHandler) {
        MagicPackets.registerServerbound(id, codec, serverboundHandler);
        MagicPackets.registerClientbound(id, codec, clientboundHandler);
    }

    public static <P> ServerboundCustomPayloadPacket createServerbound(PacketType<P> id, P packet) {
        return MagicPacketRegistry.SERVERBOUND_GAME.createPacket(id, packet);
    }

    public static <P> ClientboundCustomPayloadPacket createClientbound(PacketType<P> id, P packet) {
        return MagicPacketRegistry.CLIENTBOUND_GAME.createPacket(id, packet);
    }

    public static <P> void sendServerbound(PacketType<P> type, P buf) {
        MagicPackets.sendServerbound(MagicPackets.createServerbound(type, buf));
    }

    public static void sendServerbound(ServerboundCustomPayloadPacket customPayloadPacket) {
        if (Minecraft.getInstance().getConnection() != null) {
            Minecraft.getInstance().getConnection().send(customPayloadPacket);
            return;
        }

        throw new IllegalStateException("Cannot send packets when not in game!");
    }

    public static void sendClientbound(ServerPlayer player, PacketType<FriendlyByteBuf> type, FriendlyByteBuf buf) {
        MagicPackets.sendClientbound(player, MagicPackets.createClientbound(type, buf));
    }

    public static void sendClientbound(ServerPlayer player, ClientboundCustomPayloadPacket packet) {
        player.connection.send(packet);
    }
}
