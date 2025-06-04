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

/**
 * In MC >= 1.20.2, the current magiclib design can only submit packet registrations to Minecraft
 * during the initialization of vanilla classes {@link net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket}
 * and {@link net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket}.
 *
 * <p>
 * This is the only chance, so we need to ensure that all packet registrations are collected
 * before submitting them to Minecraft.
 * </p>
 *
 * <p>
 * A solution is to mixin into the following methods, utilizing them as the opportunity for registering your packets.
 * These methods will be called by magiclib before related packets are about to be submitted to Minecraft.
 * </p>
 *
 * <p>
 * Example mixin:
 * <pre>
 * {@code
 * @Mixin(value = MagicPacketRegistrationCenter.class, remap = false)
 * public abstract class MagicPacketRegistrationCenterMixin {
 *     @Inject(method = "common", at = @At("HEAD"))
 *     private static void register(CallbackInfo ci) {
 *         MyMod.registerMyPacketsToMagicLib();
 *     }
 * }
 * }
 * </pre>
 * </p>
 */
public class MagicPacketRegistrationCenter {
    /**
     * MagicLib ensures that it will be called before submitting Serverbound or Clientbound packet registrations to Minecraft.
     */
    public static void common() {
    }

    /**
     * MagicLib ensures that it will be called before submitting Serverbound packet registrations to Minecraft.
     */
    public static void serverbound() {
    }

    /**
     * MagicLib ensures that it will be called before submitting Clientbound packet registrations to Minecraft.
     */
    public static void clientbound() {
    }
}
