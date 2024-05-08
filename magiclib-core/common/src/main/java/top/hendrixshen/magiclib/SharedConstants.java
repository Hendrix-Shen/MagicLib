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
    @Getter
    private static final String magiclibIdentifier = "@ROOT_MOD_IDENTIFIER@";
    @Getter
    private static final String magiclibName = "@ROOT_MOD_NAME@";
    @Getter
    private static final String magiclibVersion = "@ROOT_MOD_VERSION@";
    @Getter
    private static final String magiclibVersionType = VersionUtil.getVersionType(SharedConstants.magiclibVersion);

    public static @NotNull String getTranslatedModVersionType() {
        return VersionUtil.translateVersionType(SharedConstants.modVersion);
    }

    @Deprecated
    @ApiStatus.ScheduledForRemoval
    public static String MAGICLIB_VERSION_0_1 = MagicLibVersion.VERSION_0_1.getOriginalVersion();
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    public static String MAGICLIB_VERSION_0_2 = MagicLibVersion.VERSION_0_2.getOriginalVersion();
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    public static String MAGICLIB_VERSION_0_3 = MagicLibVersion.VERSION_0_3.getOriginalVersion();
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    public static String MAGICLIB_VERSION_0_4 = MagicLibVersion.VERSION_0_4.getOriginalVersion();
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    public static String MAGICLIB_VERSION_0_5 = MagicLibVersion.VERSION_0_5.getOriginalVersion();
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    public static String MAGICLIB_VERSION_0_6 = MagicLibVersion.VERSION_0_6.getOriginalVersion();
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    public static String MAGICLIB_VERSION_0_7 = MagicLibVersion.VERSION_0_7.getOriginalVersion();
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    public static String MAGICLIB_VERSION_0_8 = MagicLibVersion.VERSION_0_8.getOriginalVersion(); // Current
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    public static String MAGICLIB_VERSION_0_9 = "0.9"; // Future

    @Deprecated
    @ApiStatus.ScheduledForRemoval
    public static String getCurrentMajorVersion() {
        return SharedConstants.MAGICLIB_VERSION_0_8;
    }

    @Deprecated
    @ApiStatus.ScheduledForRemoval
    public static String getNextMajorVersion() {
        return SharedConstants.MAGICLIB_VERSION_0_9;
    }
}
