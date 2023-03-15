package top.hendrixshen.magiclib.util;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.MagicLib;
import top.hendrixshen.magiclib.MagicLibReference;
import top.hendrixshen.magiclib.language.api.I18n;

public class StringUtil {
    public static @NotNull String getVersionType(@NotNull String version) {
        if (version.endsWith("stable")) {
            return "Public Release";
        } else if (version.endsWith("beta")) {
            return "Public Beta";
        } else if (version.endsWith("dev")) {
            return "Development";
        }

        return "Unknown";
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

    public static @NotNull String trVersionType(@NotNull String version) {
        return StringUtil.tr(StringUtil.getVersionTypeCode(version));
    }

    @ApiStatus.OverrideOnly
    public static String tr(String node) {
        return I18n.get(String.format("%s.%s", MagicLibReference.getModIdentifier(), node));
    }

    @ApiStatus.OverrideOnly
    public static String tr(String node, Object... obj) {
        return I18n.get(String.format("%s.%s", MagicLibReference.getModIdentifier(), node), obj);
    }
}
