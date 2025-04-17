package top.hendrixshen.magiclib.impl.malilib.config;

import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;
import org.jetbrains.annotations.NotNull;

import top.hendrixshen.magiclib.api.malilib.config.MagicConfigManager;

/**
 * Use {@link MagicConfigHandlerImpl} instead.
 */
@Deprecated
@ScheduledForRemoval
public class MagicConfigHandler extends MagicConfigHandlerImpl {
    public MagicConfigHandler(@NotNull MagicConfigManager configManager, int handlerVersion) {
        super(configManager, handlerVersion);
    }
}
