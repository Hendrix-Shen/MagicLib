/*
 * Copyright (c) Copyright 2020 - 2022 The Cat Town Craft and contributors.
 * This source code is subject to the terms of the GNU Lesser General Public
 * License, version 3. If a copy of the LGPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/lgpl-3.0.txt
 */
package top.hendrixshen.magiclib.util;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
//#if MC > 11502
import net.minecraft.world.level.Level;
//#else
//$$ import net.minecraft.world.level.dimension.DimensionType;
//#endif
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.MagicLibReference;
import top.hendrixshen.magiclib.compat.minecraft.network.chat.ComponentCompatApi;

import java.util.List;

public class MessageUtil {
    public static void sendMessage(CommandSourceStack source, String message) {
        MessageUtil.sendMessage(source, ComponentCompatApi.literal(message));
    }

    public static void sendMessage(CommandSourceStack source, Component component) {
        if (source != null) {
            //#if MC >= 11600
            source.sendSuccess(component, source.getServer() != null && source.getServer().getLevel(Level.OVERWORLD) != null);
            //#else
            //$$ source.sendSuccess(component, source.getServer() != null && source.getServer().getLevel(DimensionType.OVERWORLD) != null);
            //#endif
        }
    }

    public static void sendMessage(CommandSourceStack source, @NotNull List<Component> component) {
        //#if MC > 11502
        net.minecraft.network.chat.MutableComponent mutableComponent = ComponentCompatApi.literal("");
        //#else
        //$$ Component mutableComponent = ComponentCompatApi.literal("");
        //#endif
        for (Component message : component) {
            mutableComponent.append(message);
        }
        MessageUtil.sendMessage(source, mutableComponent);
    }

    public static void sendServerMessage(MinecraftServer server, String message) {
        MessageUtil.sendServerMessage(server, ComponentCompatApi.literal(message));
    }

    public static void sendServerMessage(MinecraftServer server, Component component) {
        if (server != null) {
            MagicLibReference.LOGGER.info(component.getString());
            for (Player player : server.getPlayerList().getPlayers()) {
                player.sendSystemMessageCompat(component);
            }
        } else {
            MagicLibReference.LOGGER.error("Message not delivered: " + component.getString());
        }
    }

    public static void sendServerMessage(MinecraftServer server, @NotNull List<Component> component) {
        //#if MC > 11502
        net.minecraft.network.chat.MutableComponent mutableComponent = ComponentCompatApi.literal("");
        //#else
        //$$ Component mutableComponent = ComponentCompatApi.literal("");
        //#endif
        for (Component message : component) {
            mutableComponent.append(message);
        }
        MessageUtil.sendServerMessage(server, mutableComponent);
    }
}
