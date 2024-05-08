package top.hendrixshen.magiclib;

import lombok.Getter;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.hendrixshen.magiclib.util.StringUtil;

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
    private static final String modVersionType = StringUtil.getVersionType(modVersion);
    @Getter
    private static final Logger logger = LogManager.getLogger(modIdentifier);
}
