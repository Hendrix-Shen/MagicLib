package top.hendrixshen.magiclib.entrypoint.core;

import net.neoforged.fml.common.Mod;
import org.jetbrains.annotations.ApiStatus;
import top.hendrixshen.magiclib.MagicLib;
import top.hendrixshen.magiclib.impl.mixin.extension.MagicExtensions;
import top.hendrixshen.magiclib.impl.platform.NeoForgePlatformImpl;

@Mod("magiclib_core")
public class MagicLibNeoForge {
    @ApiStatus.Internal
    public static void bootstrap() {
        MagicLib.getInstance().getPlatformManage().register(NeoForgePlatformImpl.getInstance());
        MagicExtensions.init();
    }
}
