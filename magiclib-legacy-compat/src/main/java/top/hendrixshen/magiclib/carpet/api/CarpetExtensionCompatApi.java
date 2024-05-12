/*
 * Copyright (c) Copyright 2020 - 2022 The Cat Town Craft and contributors.
 * This source code is subject to the terms of the GNU Lesser General Public
 * License, version 3. If a copy of the LGPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/lgpl-3.0.txt
 */
package top.hendrixshen.magiclib.carpet.api;

import carpet.CarpetExtension;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import top.hendrixshen.magiclib.carpet.impl.WrappedSettingManager;

import java.util.Collections;
import java.util.Map;

//#if MC > 11900
//$$ import carpet.api.settings.SettingsManager;
//$$ import net.minecraft.commands.CommandBuildContext;
//#else
import carpet.settings.SettingsManager;
//#endif

public interface CarpetExtensionCompatApi extends CarpetExtension {
    @Override
    default void registerCommands(
            CommandDispatcher<CommandSourceStack> dispatcher
            //#if MC > 11900
            //$$ , CommandBuildContext commandBuildContext
            //#endif
    ) {
        this.registerCommandCompat(dispatcher);
    }

    @Override
    //#if MC > 11900
    //$$ default SettingsManager extensionSettingsManager() {
    //$$     return this.getSettingsManagerCompat();
    //$$ }
    //#else
    default SettingsManager customSettingsManager() {
        return this.getSettingsManagerCompat();
    }
    //#endif

    //#if MC > 11404
    @Override
    default Map<String, String> canHasTranslations(String lang) {
        return this.canHasTranslationsCompat(lang);
    }
    //#endif

    WrappedSettingManager getSettingsManagerCompat();

    void registerCommandCompat(CommandDispatcher<CommandSourceStack> dispatcher);

    default Map<String, String> canHasTranslationsCompat(String lang) {
        return Collections.emptyMap();
    }
}
