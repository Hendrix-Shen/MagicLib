package top.hendrixshen.magiclib.impl.dev;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.MagicLib;
import top.hendrixshen.magiclib.util.VersionUtil;

public class SharedConstants {
    @Getter
    private static final String modIdentifier = "@MOD_IDENTIFIER@";
    @Getter
    private static final String modName = "@MOD_NAME@";
    @Getter
    private static final String modVersion = MagicLib.getInstance().getPlatformManage().getCurrentPlatform()
            .getModVersion(SharedConstants.modIdentifier);
    @Getter
    private static final String modVersionType = VersionUtil.getVersionType(SharedConstants.modVersion);

    public static @NotNull String getTranslatedModVersionType() {
        return VersionUtil.translateVersionType(SharedConstants.modVersion);
    }
}
