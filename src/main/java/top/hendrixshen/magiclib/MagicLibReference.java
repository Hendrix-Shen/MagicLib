/*
 * Copyright (c) Copyright 2020 - 2021 The Cat Town Craft and contributors.
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package top.hendrixshen.magiclib;

import net.fabricmc.loader.api.FabricLoader;

public class MagicLibReference {
    private static final String MOD_ID = "magiclib";
    private static final String MOD_NAME = "MagicLib";
    private static final String MOD_VERSION = FabricLoader.getInstance().getModContainer(getModId()).orElseThrow(RuntimeException::new).getMetadata().getVersion().getFriendlyString();

    public static String getModId() {
        return MOD_ID;
    }

    public static String getModName() {
        return MOD_NAME;
    }

    public static String getModVersion() {
        return MOD_VERSION;
    }
}
