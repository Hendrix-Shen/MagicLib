/*
 * Copyright (c) Copyright 2020 - 2021 The Cat Town Craft and contributors.
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package top.hendrixshen.magiclib;

import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MagicLib implements ModInitializer {
    public static Logger getLogger() {
        return LogManager.getLogger(MagicLibReference.getModId());
    }

    @Override
    public void onInitialize() {
        getLogger().info(String.format("[%s]: Mod initialized - Version: %s", MagicLibReference.getModName(), MagicLibReference.getModVersion()));
    }
}
