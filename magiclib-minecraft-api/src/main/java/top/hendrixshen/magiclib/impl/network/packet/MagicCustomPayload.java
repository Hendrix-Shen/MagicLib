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
import lombok.Getter;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

// CHECKSTYLE.OFF: ImportOrder
//#if MC >= 12002
//$$ import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
//#endif
// CHECKSTYLE.ON: ImportOrder

import top.hendrixshen.magiclib.api.network.packet.PacketCodec;
import top.hendrixshen.magiclib.api.network.packet.PacketType;

// CHECKSTYLE.OFF: ImportOrder
//#if MC < 12002
import top.hendrixshen.magiclib.api.compat.minecraft.network.protocol.common.custom.CustomPacketPayload;
//#endif
// CHECKSTYLE.ON: ImportOrder

/**
 * Reference to <a href="https://github.com/Fallen-Breath/fanetlib/blob/20335b54fda0bc38cf52552878ad84755d9a66f9/src/main/java/me/fallenbreath/fanetlib/impl/packet/FanetlibCustomPayload.java">fanetlib</a>.
 */
@AllArgsConstructor
public class MagicCustomPayload<P> implements CustomPacketPayload {
    private final PacketType<P> type;
    private final PacketCodec<P> codec;
    @Getter
    private final P packet;

    public MagicCustomPayload(PacketType<P> id, PacketCodec<P> codec, FriendlyByteBuf buf) {
        this(id, codec, codec.decode(buf));
    }

    //#if MC < 12005
    @Override
    //#endif
    public void write(FriendlyByteBuf buf) {
        this.codec.encode(this.packet, buf);
    }

    //#if MC < 12005
    @Override
    //#endif
    public ResourceLocation id() {
        return this.type.getIdentifier();
    }

    //#if MC >= 12005
    //$$ @Override
    //$$ public Type<? extends CustomPacketPayload> type() {
    //$$     return new Type<>(this.id());
    //$$ }
    //#endif
}
