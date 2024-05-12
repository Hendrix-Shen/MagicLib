package top.hendrixshen.magiclib.util;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.MagicLibReference;
import top.hendrixshen.magiclib.api.i18n.I18n;

@Deprecated
@ApiStatus.ScheduledForRemoval
public class StringUtil {
    /**
     * See {@link VersionUtil#getVersionType(String)}
     */
    public static @NotNull String getVersionType(@NotNull String version) {
        return VersionUtil.getVersionType(version);
    }

    public static @NotNull String getVersionTypeCode(@NotNull String version) {
        if (version.endsWith("beta")) {
            return "misc.versionType.beta";
        } else if (version.endsWith("dev")) {
            return "misc.versionType.development";
        } else if (version.endsWith("stable")) {
            return "misc.versionType.stable";
        }

        return "misc.versionType.unknown";
    }

    /**
     * See {@link VersionUtil#translateVersionType(String)}
     */
    public static @NotNull String trVersionType(@NotNull String version) {
        return VersionUtil.translateVersionType(version);
    }

    public static String tr(String node) {
        return I18n.tr(String.format("%s.%s", MagicLibReference.getModIdentifier(), node));
    }

    public static String tr(String node, Object... obj) {
        return I18n.tr(String.format("%s.%s", MagicLibReference.getModIdentifier(), node), obj);
    }
}
