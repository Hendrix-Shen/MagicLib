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

import lombok.AllArgsConstructor;

import net.minecraft.network.FriendlyByteBuf;

import top.hendrixshen.magiclib.api.network.packet.PacketCodec;

/**
 * Reference to <a href="https://github.com/Fallen-Breath/fanetlib/blob/20335b54fda0bc38cf52552878ad84755d9a66f9/src/main/java/me/fallenbreath/fanetlib/impl/packet/PacketCodecImpl.java">fanetlib</a>.
 */
@AllArgsConstructor
public class PacketCodecImpl<P> implements PacketCodec<P> {
    private final Encoder<P> encoder;
    private final Decoder<P> decoder;

    @Override
    public void encode(P packet, FriendlyByteBuf buf) {
        this.encoder.encode(packet, buf);
    }

    @Override
    public P decode(FriendlyByteBuf buf) {
        return this.decoder.decode(buf);
    }
}
