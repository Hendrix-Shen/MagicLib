package top.hendrixshen.magiclib;

import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.ApiStatus;
import top.hendrixshen.magiclib.util.VersionUtil;

@Deprecated
@ApiStatus.ScheduledForRemoval
public class MagicLibReference {
    @Getter
    private static final String modIdentifier = SharedConstants.getMagiclibIdentifier();
    @Getter
    private static final String modIdentifierCurrent = "magiclib-@MINECRAFT_VERSION_IDENTIFY@";
    @Getter
    private static final String modName = "MagicLib";
    @Getter
    private static final String modNameCurrent = "MagicLib Legacy Compat for @MINECRAFT_VERSION@";
    @Getter
    private static final String modVersion = "@MOD_VERSION@";
    @Getter
    private static final String modVersionType = VersionUtil.getVersionType(modVersion);
    @Getter
    private static final Logger logger = LogManager.getLogger(modIdentifier);
}
