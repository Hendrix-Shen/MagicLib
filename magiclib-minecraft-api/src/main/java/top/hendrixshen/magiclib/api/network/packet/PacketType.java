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

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import net.minecraft.resources.ResourceLocation;

import top.hendrixshen.magiclib.api.compat.minecraft.resources.ResourceLocationCompat;

/**
 * Reference to <a href="https://github.com/Fallen-Breath/fanetlib/blob/20335b54fda0bc38cf52552878ad84755d9a66f9/src/main/java/me/fallenbreath/fanetlib/api/packet/PacketId.java">fanetlib</a>.
 */
@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class PacketType<P> {
    private final ResourceLocation identifier;

    public static <P> PacketType<P> of(ResourceLocation identifier) {
        return new PacketType<>(identifier);
    }

    public static <P> PacketType<P> of(String namespace, String identifier) {
        return new PacketType<>(ResourceLocationCompat.fromNamespaceAndPath(namespace, identifier));
    }
}
