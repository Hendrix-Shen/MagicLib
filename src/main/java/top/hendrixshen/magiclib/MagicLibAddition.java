package top.hendrixshen.magiclib;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import top.hendrixshen.magiclib.api.rule.WrapperSettingManager;
import top.hendrixshen.magiclib.api.rule.CarpetExtensionCompatApi;

public class MagicLibAddition implements CarpetExtensionCompatApi {
    @Override
    public WrapperSettingManager getSettingsManagerCompat() {
        return WrapperSettingManager.get(MagicLibReference.getModId());
    }

    @Override
    public void registerCommandCompat(CommandDispatcher<CommandSourceStack> dispatcher) {
    }

    @Override
    public void onGameStarted() {
        WrapperSettingManager.get(MagicLibReference.getModId()).parseSettingsClass(MagicLibSettings.class);
    }
}
