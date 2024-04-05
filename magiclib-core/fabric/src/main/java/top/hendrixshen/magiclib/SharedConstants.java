package top.hendrixshen.magiclib;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.util.VersionUtil;

public class SharedConstants {
    @Getter
    private static final String modIdentifier = "magiclib_core";
    @Getter
    private static final String modName = MagicLib.getInstance().getPlatformManage().getCurrentPlatform()
            .getModName(SharedConstants.modIdentifier);
    @Getter
    private static final String modVersion = MagicLib.getInstance().getPlatformManage().getCurrentPlatform()
            .getModVersion(SharedConstants.modIdentifier);
    @Getter
    private static final String modVersionType = VersionUtil.getVersionType(SharedConstants.modVersion);

    public static @NotNull String getTranslatedModVersionType() {
        return VersionUtil.translateVersionType(SharedConstants.modVersionType);
    }
}
