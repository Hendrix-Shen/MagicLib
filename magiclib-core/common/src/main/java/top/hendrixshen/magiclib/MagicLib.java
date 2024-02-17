package top.hendrixshen.magiclib;

import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.hendrixshen.magiclib.impl.event.EventManager;
import top.hendrixshen.magiclib.impl.i18n.MagicLanguageManager;
import top.hendrixshen.magiclib.impl.platform.PlatformManager;

@Getter
public final class MagicLib {
    @Getter(lazy = true)
    private static final MagicLib instance = new MagicLib();
    @Getter(lazy = true)
    private static final Logger logger = LogManager.getLogger("magiclib");

    private final EventManager eventManager = new EventManager(this);
    private final PlatformManager platformManage = new PlatformManager(this);

    private MagicLib() {
    }

    public MagicLanguageManager getLanguageManager() {
        return MagicLanguageManager.getInstance();
    }
}
