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

import top.hendrixshen.magiclib.api.network.packet.MagicPacketRegistrationCenter;
import top.hendrixshen.magiclib.util.runner.SingleRunner;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Reference to <a href="https://github.com/Fallen-Breath/fanetlib/blob/20335b54fda0bc38cf52552878ad84755d9a66f9/src/main/java/me/fallenbreath/fanetlib/impl/packet/FanetlibPacketRegistrationCenterHelper.java">fanetlib</a>.
 */
@SuppressWarnings({"Convert2MethodRef", "WriteOnlyObject"})
public class MagicPacketRegistrationCenterHelper {
    private static final SingleRunner commonInvoker = new SingleRunner(() -> MagicPacketRegistrationCenter.common());
    private static final SingleRunner serverboundInvoker = new SingleRunner(() -> MagicPacketRegistrationCenter.serverbound());
    private static final SingleRunner clientboundInvoker = new SingleRunner(() -> MagicPacketRegistrationCenter.clientbound());
    private static final AtomicBoolean hasInvokedServerbound = new AtomicBoolean(false);
    private static final AtomicBoolean hasInvokedClientbound = new AtomicBoolean(false);

    public static void collectAll() {
        MagicPacketRegistrationCenterHelper.collectServerbound();
        MagicPacketRegistrationCenterHelper.collectClientbound();
    }

    public static void collectServerbound() {
        MagicPacketRegistrationCenterHelper.commonInvoker.run();
        MagicPacketRegistrationCenterHelper.serverboundInvoker.run();
        MagicPacketRegistrationCenterHelper.hasInvokedServerbound.set(true);
    }

    public static void collectClientbound() {
        MagicPacketRegistrationCenterHelper.commonInvoker.run();
        MagicPacketRegistrationCenterHelper.clientboundInvoker.run();
        MagicPacketRegistrationCenterHelper.hasInvokedClientbound.set(true);
    }

    public static boolean isTooLateForServerboundRegister() {
        //#if MC >= 12002
        //$$ return MagicPacketRegistrationCenterHelper.hasInvokedServerbound.get();
        //#else
        return false;
        //#endif
    }

    public static boolean isTooLateForClientboundRegister() {
        //#if MC >= 12002
        //$$ return MagicPacketRegistrationCenterHelper.hasInvokedClientbound.get();
        //#else
        return false;
        //#endif
    }
}
