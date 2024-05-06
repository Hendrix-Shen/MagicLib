package top.hendrixshen.magiclib;

import lombok.Getter;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.util.VersionUtil;

public class SharedConstants {
    @Getter
    private static final String modIdentifier = "@MOD_IDENTIFIER@";
    @Getter
    private static final String modName = "@MOD_NAME@";
    @Getter
    private static final String modVersion = "@MOD_VERSION@";
    @Getter
    private static final String modVersionType = VersionUtil.getVersionType(SharedConstants.modVersion);

    public static @NotNull String getTranslatedModVersionType() {
        return VersionUtil.translateVersionType(SharedConstants.modVersion);
    }

    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "0.9")
    public static String MAGICLIB_VERSION_0_1 = "0.1";
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "0.9")
    public static String MAGICLIB_VERSION_0_2 = "0.2";
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "0.9")
    public static String MAGICLIB_VERSION_0_3 = "0.3";
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "0.9")
    public static String MAGICLIB_VERSION_0_4 = "0.4";
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "0.9")
    public static String MAGICLIB_VERSION_0_5 = "0.5";
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "0.9")
    public static String MAGICLIB_VERSION_0_6 = "0.6";
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "0.9")
    public static String MAGICLIB_VERSION_0_7 = "0.7";
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "0.9")
    public static String MAGICLIB_VERSION_0_8 = "0.8"; // Current
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "0.9")
    public static String MAGICLIB_VERSION_0_9 = "0.9"; // Future

    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "0.9")
    public static String getCurrentMajorVersion() {
        return SharedConstants.MAGICLIB_VERSION_0_8;
    }

    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "0.9")
    public static String getNextMajorVersion() {
        return SharedConstants.MAGICLIB_VERSION_0_9;
    }
}
