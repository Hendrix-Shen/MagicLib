package top.hendrixshen.magiclib.entrypoint.core;

import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.ApiStatus;
import top.hendrixshen.magiclib.MagicLib;
import top.hendrixshen.magiclib.impl.mixin.audit.MixinAuditor;
import top.hendrixshen.magiclib.impl.mixin.extension.MagicExtensions;
import top.hendrixshen.magiclib.impl.platform.ForgePlatformImpl;

@Mod("magiclib_core")
public class MagicLibForge {
    public MagicLibForge() {
        MixinAuditor.trigger("mod_init");
    }

    @ApiStatus.Internal
    public static void bootstrap() {
        MagicLib.getInstance().getPlatformManage().register(ForgePlatformImpl.getInstance());
        MagicExtensions.init();
    }
}
