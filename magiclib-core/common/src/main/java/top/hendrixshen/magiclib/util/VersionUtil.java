package top.hendrixshen.magiclib.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.MagicLib;
import top.hendrixshen.magiclib.api.dependency.version.SemanticVersion;
import top.hendrixshen.magiclib.api.dependency.version.Version;
import top.hendrixshen.magiclib.api.dependency.version.VersionParsingException;
import top.hendrixshen.magiclib.api.dependency.version.VersionPredicate;
import top.hendrixshen.magiclib.api.i18n.I18n;
import top.hendrixshen.magiclib.impl.dependency.version.VersionPredicateParser;

import java.util.Collection;

public class VersionUtil {
    @Contract(pure = true)
    public static boolean isVersionSatisfyPredicates(String version, @NotNull Collection<String> versionPredicates) {
        return versionPredicates.isEmpty() || versionPredicates
                .stream()
                .allMatch((versionPredicate) -> VersionUtil.isVersionSatisfyPredicate(version, versionPredicate));
    }

    private static boolean isVersionSatisfyPredicate(String version, String versionPredicate) {
        SemanticVersion semver;

        try {
            semver = SemanticVersion.parse(version);
        } catch (VersionParsingException e) {
            MagicLib.getLogger().error("Failed to parse version {}: {}", version, e);
            return false;
        }

        try {
            return VersionPredicateParser.parse(versionPredicate).test(semver);
        } catch (VersionParsingException e) {
            MagicLib.getLogger().error("Failed to parse version predicate {}: {}", versionPredicate, e);
        }

        return false;
    }

    public static @NotNull String getVersionType(@NotNull String version) {
        return VersionUtil.translateVersionType(version, I18n.DEFAULT_CODE);
    }

    public static @NotNull String translateVersionType(@NotNull String version) {
        return VersionUtil.translateVersionType(version, I18n.getCurrentLanguageCode());
    }

    public static @NotNull String translateVersionType(@NotNull String version, String languageCode) {
        if (version.endsWith("beta")) {
            return I18n.trByCode(languageCode, "magiclib.misc.version_type.beta");
        } else if (version.endsWith("dev")) {
            return I18n.trByCode(languageCode, "magiclib.misc.version_type.development");
        } else if (version.endsWith("fork")) {
            return I18n.trByCode(languageCode, "magiclib.misc.version_type.fork");
        } else if (version.endsWith("pull_request")) {
            return I18n.trByCode(languageCode, "magiclib.misc.version_type.pull_request");
        } else if (version.endsWith("stable")) {
            return I18n.trByCode(languageCode, "magiclib.misc.version_type.stable");
        }

        return I18n.trByCode(languageCode, "magiclib.misc.version_type.unknown");
    }
}
