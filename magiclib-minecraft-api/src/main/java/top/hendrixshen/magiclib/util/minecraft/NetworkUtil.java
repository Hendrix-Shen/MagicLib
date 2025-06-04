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

package top.hendrixshen.magiclib.util.minecraft;

import io.netty.buffer.Unpooled;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

import top.hendrixshen.magiclib.MagicLib;
import top.hendrixshen.magiclib.api.compat.minecraft.nbt.TagCompat;

import java.util.Objects;

/**
 * Reference to <a href="https://github.com/Fallen-Breath/fanetlib/blob/20335b54fda0bc38cf52552878ad84755d9a66f9/src/main/java/me/fallenbreath/fanetlib/impl/nbt/NetworkNbtUtilsImpl.java">fanetlib</a>.
 */
public class NetworkUtil {
    /**
     * The next element inside the buffer should be a serialized nbt.
     */
    public static NbtStyle guessNbtStyle(@NotNull FriendlyByteBuf buf) {
        // Notes: reader index untouched.
        int n = buf.readableBytes();
        int prevReaderIndex = buf.readerIndex();
        try {
            if (n < 2) {
                return NbtStyle.UNKNOWN;
            }

            byte typeId = buf.readByte();

            if (typeId != TagCompat.TAG_COMPOUND) {
                return NbtStyle.UNKNOWN;
            }

            if (n == 2) {
                // >=1.20.2, empty nbt
                if (buf.readByte() == 0) {
                    return NbtStyle.MODERN;
                }

                return NbtStyle.UNKNOWN;
            } else {
                // n > 2
                byte[] bytes = new byte[2];
                buf.readBytes(bytes);

                // Double 0x00 for the empty root tag name.
                if (bytes[0] == 0 && bytes[1] == 0) {
                    return NbtStyle.LEGACY;
                } else if (0 <= bytes[0] && bytes[0] < 13) {
                    // A valid nbt type id.
                    return NbtStyle.MODERN;
                }
            }
        } finally {
            buf.readerIndex(prevReaderIndex);
        }

        return NbtStyle.UNKNOWN;
    }

    /**
     * Read an NBT from a {@link FriendlyByteBuf} with best-effort nbt format detection.
     *
     * <p>
     * Compatible with both mc >= 1.20.2 and mc < 1.20.2 formats
     * </p>
     */
    public static CompoundTag readNbtAuto(FriendlyByteBuf buf) {
        NbtStyle nbtStyle = NetworkUtil.guessNbtStyle(buf);
        return Objects.requireNonNull(NetworkUtil.readNbt(nbtStyle, buf));
    }

    /**
     * Write nbt with current serialization style appended.
     */
    public static void writeNbtWithFormat(FriendlyByteBuf buf, CompoundTag nbt) {
        buf.writeVarInt(NbtStyle.CURRENT.ordinal());
        buf.writeNbt(nbt);
    }

    /**
     * Read nbt with current serialization style appended.
     */
    public static CompoundTag readNbtWithFormat(FriendlyByteBuf buf) {
        int styleId = buf.readVarInt();
        NbtStyle nbtStyle = NbtStyle.values()[styleId];
        return Objects.requireNonNull(NetworkUtil.readNbt(nbtStyle, buf));
    }

    @Nullable
    private static CompoundTag readNbt(NbtStyle bufNbtStyle, FriendlyByteBuf buf) {
        if (bufNbtStyle == NbtStyle.UNKNOWN) {
            MagicLib.getLogger().debug("NetworkUtil.readNbt() called with unknown NbtStyle");
        }

        if (NbtStyle.CURRENT == NbtStyle.LEGACY && bufNbtStyle == NbtStyle.MODERN) {
            // I'm < mc1.20.2 (OLD), trying to read a nbt in NEW style
            //#if MC < 12002
            int prevReaderIndex = buf.readerIndex();
            FriendlyByteBuf tweakedBuf = new FriendlyByteBuf(Unpooled.buffer());
            tweakedBuf.writeByte(buf.readByte());  // 0x0A, tag type
            tweakedBuf.writeByte(0).writeByte(0);  // 2* 0x00
            tweakedBuf.writeBytes(buf);
            buf.readerIndex(prevReaderIndex);
            CompoundTag nbt = tweakedBuf.readNbt();
            int n = tweakedBuf.readerIndex();
            buf.readBytes(Math.max(0, n - 2));
            return nbt;
            //#endif
        } else if (NbtStyle.CURRENT == NbtStyle.MODERN && bufNbtStyle == NbtStyle.LEGACY) {
            // I'm >= mc1.20.2 (NEW), trying to read a nbt in OLD style
            int prevReaderIndex = buf.readerIndex();
            FriendlyByteBuf tweakedBuf = new FriendlyByteBuf(Unpooled.buffer());
            tweakedBuf.writeByte(buf.readByte());  // 0x0A, tag type
            buf.readBytes(2);  // consume the 2* 0x00
            tweakedBuf.writeBytes(buf);
            buf.readerIndex(prevReaderIndex);
            CompoundTag nbt = tweakedBuf.readNbt();
            int n = tweakedBuf.readerIndex();
            buf.readBytes(Math.max(0, n > 1 ? n + 2 : n));
            return nbt;
        }

        // direct read
        return buf.readNbt();
    }

    /**
     * @deprecated Use {@link #readNbtAuto(FriendlyByteBuf)} instead.
     */
    @Deprecated
    public static CompoundTag readNbt(FriendlyByteBuf buf) {
        NbtStyle nbtStyle = NetworkUtil.guessNbtStyle(buf);
        return NetworkUtil.readNbt(nbtStyle, buf);
    }

    /**
     * See <a href="https://minecraft.wiki/w/Minecraft_Wiki:Projects/wiki.vg_merge/NBT#Network_NBT_(Java_Edition)">Network NBT</a>.
     * for the nbt changes between mc < 1.20.2 and mc >= 1.20.2
     */
    public enum NbtStyle {
        UNKNOWN,
        LEGACY,  // <  1.20.2
        MODERN;  // >= 1.20.2

        //#if MC >= 12002
        //$$ public static final NbtStyle CURRENT = NbtStyle.MODERN;
        //#else
        public static final NbtStyle CURRENT = NbtStyle.LEGACY;
        //#endif
    }
}
