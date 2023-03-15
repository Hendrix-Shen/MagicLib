package top.hendrixshen.magiclib.impl.carpet;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import top.hendrixshen.magiclib.MagicLibReference;
import top.hendrixshen.magiclib.carpet.api.CarpetExtensionCompatApi;
import top.hendrixshen.magiclib.carpet.impl.WrappedSettingManager;

public class MagicLibAddition implements CarpetExtensionCompatApi {
    @Override
    public WrappedSettingManager getSettingsManagerCompat() {
        return WrappedSettingManager.get(MagicLibReference.getModIdentifier());
    }

    @Override
    public void registerCommandCompat(CommandDispatcher<CommandSourceStack> dispatcher) {
    }

    @Override
    public void onGameStarted() {
        this.getSettingsManagerCompat().parseSettingsClass(MagicLibSettings.class);
    }
}
