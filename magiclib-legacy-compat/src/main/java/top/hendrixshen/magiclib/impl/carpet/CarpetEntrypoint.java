package top.hendrixshen.magiclib.impl.carpet;

import carpet.settings.ParsedRule;
import org.jetbrains.annotations.Nullable;
import top.hendrixshen.magiclib.MagicLibReference;
import top.hendrixshen.magiclib.carpet.impl.WrappedSettingManager;
import top.hendrixshen.magiclib.util.FabricUtil;
import top.hendrixshen.magiclib.util.ReflectionUtil;

import java.util.Arrays;

public class CarpetEntrypoint {
    private static final boolean isCarpetLoaded = FabricUtil.isModLoaded("carpet");

    public static @Nullable WrappedSettingManager getSettingManager() {
        return isCarpetLoaded ? WrappedSettingManager.get(MagicLibReference.getModIdentifier()) : null;
    }

    public static void init() {
        if (!isCarpetLoaded) {
            return;
        }

        ReflectionUtil.invoke("top.hendrixshen.magiclib.carpet.impl.WrappedSettingManager", "register",
                null,
                new Class[] {
                        String.class,
                        WrappedSettingManager.class,
                        ReflectionUtil.getClass("top.hendrixshen.magiclib.carpet.api.CarpetExtensionCompatApi")
                                .orElseThrow(RuntimeException::new)
                },
                MagicLibReference.getModIdentifier(),
                new MagicLibSettingManager(MagicLibReference.getModVersion(),
                        MagicLibReference.getModIdentifier(),
                        MagicLibReference.getModNameCurrent()),
                ReflectionUtil.newInstance("top.hendrixshen.magiclib.impl.carpet.MagicLibAddition", null)
                        .orElseThrow(RuntimeException::new));
    }
}
