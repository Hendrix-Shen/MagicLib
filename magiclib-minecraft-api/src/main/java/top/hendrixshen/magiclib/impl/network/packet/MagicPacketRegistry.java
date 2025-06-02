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

package top.hendrixshen.magiclib.impl.network.packet;

import com.google.common.collect.Maps;
import io.netty.buffer.Unpooled;
import lombok.AllArgsConstructor;
import lombok.Getter;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;

import top.hendrixshen.magiclib.MagicLib;
import top.hendrixshen.magiclib.api.network.packet.ClientboundPacketHandler;
import top.hendrixshen.magiclib.api.network.packet.PacketCodec;
import top.hendrixshen.magiclib.api.network.packet.PacketType;
import top.hendrixshen.magiclib.api.network.packet.ServerboundPacketHandler;
import top.hendrixshen.magiclib.api.platform.DistType;

import java.util.Map;

/**
 * Reference to <a href="https://github.com/Fallen-Breath/fanetlib/blob/20335b54fda0bc38cf52552878ad84755d9a66f9/src/main/java/me/fallenbreath/fanetlib/impl/packet/FanetlibPacketRegistry.java">fanetlib</a>.
 */
@AllArgsConstructor
public class MagicPacketRegistry<Handler, Packet> {
    public static final MagicPacketRegistry<ServerboundPacketHandler<?>, ServerboundCustomPayloadPacket> SERVERBOUND_GAME = new MagicPacketRegistry<>(Direction.SERVERBOUND);
    public static final MagicPacketRegistry<ClientboundPacketHandler<?>, ClientboundCustomPayloadPacket> CLIENTBOUND_GAME = new MagicPacketRegistry<>(Direction.CLIENTBOUND);

    private final Direction direction;
    @Getter
    private final Map<PacketType<?>, RegistryEntry<Handler, ?>> registry = Maps.newHashMap();

    public <P> void register(PacketType<P> type, PacketCodec<P> codec, Handler handler) {
        if (this.registry.containsKey(type)) {
            throw new IllegalArgumentException(String.format("Duplicate packet type: %s", type));
        }

        this.registry.put(type, new RegistryEntry<>(handler, codec));
    }

    public <P> void unregister(PacketType<P> type) {
        this.registry.remove(type);
    }

    @SuppressWarnings("unchecked")
    public <P> RegistryEntry<Handler, P> getEntry(PacketType<P> type) {
        return (RegistryEntry<Handler, P>) this.registry.get(type);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public <P> Packet createPacket(PacketType<P> type, P packet) {
        RegistryEntry<Handler, ?> entry = this.getEntry(type);

        if (entry == null) {
            throw new IllegalArgumentException(String.format("Unknown packet id: %s", type));
        }

        PacketCodec codec = entry.getCodec();
        MagicCustomPayload<P> mcp = new MagicCustomPayload<P>(type, codec, packet);
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        mcp.write(buf);

        if (this.direction == Direction.SERVERBOUND) {
            DistType distType = MagicLib.getInstance().getCurrentPlatform().getCurrentDistType();

            if (distType != DistType.CLIENT) {
                throw new RuntimeException("Cannot send Serverbound packet in non-client, current env: " + distType);
            }

            return (Packet) new ServerboundCustomPayloadPacket(
                    //#if MC >= 12002
                    //$$ mcp
                    //#else
                    type.getIdentifier(),
                    buf
                    //#endif
            );
        } else {
            return (Packet) new ClientboundCustomPayloadPacket(
                    //#if MC >= 12002
                    //$$ mcp
                    //#else
                    type.getIdentifier(),
                    buf
                    //#endif
            );
        }
    }

    private enum Direction {
        SERVERBOUND,
        CLIENTBOUND
    }
}
