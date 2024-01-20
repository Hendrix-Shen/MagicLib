package top.hendrixshen.magiclib;

import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.hendrixshen.magiclib.api.platform.PlatformManager;

@Getter
public final class MagicLib {
    @Getter(lazy = true)
    private static final MagicLib instance = new MagicLib();
    @Getter(lazy = true)
    private static final Logger logger = LogManager.getLogger("magiclib");

    private final PlatformManager platformManage = new PlatformManager(this);

    private MagicLib() {
    }
}
