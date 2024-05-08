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

//#if MC > 11802
//$$ import carpet.api.settings.SettingsManager;
//$$ import net.minecraft.commands.CommandBuildContext;
//#else
import carpet.settings.SettingsManager;
//#endif

public interface CarpetExtensionCompatApi extends CarpetExtension {
    @Override
    //#if MC >= 11901
    //$$ default void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext commandBuildContext) {
    //$$     this.registerCommandCompat(dispatcher);
    //#else
    default void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
        this.registerCommandCompat(dispatcher);
    //#endif
    }

    @Override
    //#if MC >= 11901
    //$$ default SettingsManager extensionSettingsManager() {
    //#else
    default SettingsManager customSettingsManager() {
    //#endif
        return this.getSettingsManagerCompat();
    }

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
