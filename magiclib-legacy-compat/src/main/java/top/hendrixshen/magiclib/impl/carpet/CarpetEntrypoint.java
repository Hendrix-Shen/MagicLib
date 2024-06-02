package top.hendrixshen.magiclib.impl.carpet;

import org.jetbrains.annotations.Nullable;
import top.hendrixshen.magiclib.SharedConstants;
import top.hendrixshen.magiclib.carpet.impl.WrappedSettingManager;
import top.hendrixshen.magiclib.util.FabricUtil;
import top.hendrixshen.magiclib.util.ReflectionUtil;

public class CarpetEntrypoint {
    private static final boolean isCarpetLoaded = FabricUtil.isModLoaded("carpet");

    public static @Nullable WrappedSettingManager getSettingManager() {
        return isCarpetLoaded ? WrappedSettingManager.get(SharedConstants.getMagiclibIdentifier()) : null;
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
                SharedConstants.getMagiclibIdentifier(),
                new MagicLibSettingManager(SharedConstants.getMagiclibIdentifier(),
                        SharedConstants.getMagiclibIdentifier(),
                        SharedConstants.getMagiclibName()),
                ReflectionUtil.newInstance("top.hendrixshen.magiclib.impl.carpet.MagicLibAddition", null)
                        .orElseThrow(RuntimeException::new));
    }
}
