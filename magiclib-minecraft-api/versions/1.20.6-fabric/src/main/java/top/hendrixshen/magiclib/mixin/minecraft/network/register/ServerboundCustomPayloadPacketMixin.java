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

package top.hendrixshen.magiclib.mixin.minecraft.network.register;

import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import top.hendrixshen.magiclib.impl.network.packet.MagicCustomPayload;
import top.hendrixshen.magiclib.impl.network.packet.MagicPacketRegistrationCenterHelper;
import top.hendrixshen.magiclib.impl.network.packet.MagicPacketRegistry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Reference to <a href="https://github.com/Fallen-Breath/fanetlib/blob/20335b54fda0bc38cf52552878ad84755d9a66f9/versions/1.20.6/src/main/java/me/fallenbreath/fanetlib/mixins/register/CustomPayloadC2SPacketMixin.java">fanetlib</a>.
 *
 * <li>mc1.14 ~ mc1.20.1  : subproject 1.16.5 (main project) [dummy]</li>
 * <li>mc1.20.2 ~ mc1.20.5: subproject 1.20.2</li>
 * <li>mc1.20.6+          : subproject 1.20.6        &lt;--------</li>
 */
@SuppressWarnings({"rawtypes", "unchecked"})
@Mixin(ServerboundCustomPayloadPacket.class)
public abstract class ServerboundCustomPayloadPacketMixin {
    @ModifyArg(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    //#if FORGE_LIKE
                    //$$ target = "Lnet/minecraft/network/protocol/common/custom/CustomPacketPayload;codec(Lnet/minecraft/network/protocol/common/custom/CustomPacketPayload$FallbackProvider;Ljava/util/List;Lnet/minecraft/network/ConnectionProtocol;Lnet/minecraft/network/protocol/PacketFlow;)Lnet/minecraft/network/codec/StreamCodec;"
                    //#else
                    target = "Lnet/minecraft/network/protocol/common/custom/CustomPacketPayload;codec(Lnet/minecraft/network/protocol/common/custom/CustomPacketPayload$FallbackProvider;Ljava/util/List;)Lnet/minecraft/network/codec/StreamCodec;"
                    //#endif
            )
    )
    private static List<CustomPacketPayload.TypeAndCodec<?, ?>> registerServerboundMagicPayload(List<CustomPacketPayload.TypeAndCodec<?, ?>> types) {
        MagicPacketRegistrationCenterHelper.collectServerbound();
        ArrayList<CustomPacketPayload.TypeAndCodec<?, ?>> newTypes = new ArrayList<>(types);
        MagicPacketRegistry.SERVERBOUND_GAME.getRegistry().forEach((type, entry) ->
                newTypes.add(new CustomPacketPayload.TypeAndCodec<>(
                        new CustomPacketPayload.Type<MagicCustomPayload<?>>(type.getIdentifier()),
                        CustomPacketPayload.codec(MagicCustomPayload::write, buf -> new MagicCustomPayload(type, entry.getCodec(), buf)))
                )
        );
        return Collections.unmodifiableList(newTypes);
    }
}
