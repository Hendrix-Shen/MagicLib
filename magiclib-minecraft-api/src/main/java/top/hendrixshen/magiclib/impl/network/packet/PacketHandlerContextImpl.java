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

import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

import top.hendrixshen.magiclib.api.network.packet.ClientboundPacketHandler;
import top.hendrixshen.magiclib.api.network.packet.ServerboundPacketHandler;
import top.hendrixshen.magiclib.mixin.minecraft.accessor.ClientPacketListenerAccessor;
import top.hendrixshen.magiclib.mixin.minecraft.accessor.ServerGamePacketListenerImplAccessor;

/**
 * Reference to <a href="https://github.com/Fallen-Breath/fanetlib/blob/20335b54fda0bc38cf52552878ad84755d9a66f9/src/main/java/me/fallenbreath/fanetlib/impl/packet/PacketHandlerContextImpl.java">fanetlib</a>.
 */
public class PacketHandlerContextImpl {
    @Getter
    public static class Clientbound implements ClientboundPacketHandler.Context {
        private final Minecraft client;
        private final ClientPacketListener packetListener;
        @Nullable
        private final LocalPlayer player;

        public Clientbound(ClientPacketListener packetListener) {
            this.packetListener = packetListener;
            this.client = ((ClientPacketListenerAccessor) packetListener).magiclib$getMinecraft();
            this.player = this.client.player;
        }

        @Override
        public void runSynced(Runnable runnable) {
            this.client.execute(runnable);
        }
    }

    @Getter
    public static class Serverbound implements ServerboundPacketHandler.Context {
        private final MinecraftServer server;
        private final ServerGamePacketListenerImpl packetListener;
        private final ServerPlayer player;

        public Serverbound(ServerGamePacketListenerImpl packetListener) {
            this.packetListener = packetListener;
            this.server = ((ServerGamePacketListenerImplAccessor) packetListener).magiclib$getServer();
            this.player = this.packetListener.player;
        }

        @Override
        public void runSynced(Runnable runnable) {
            this.server.execute(runnable);
        }
    }
}
