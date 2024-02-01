package top.hendrixshen.magiclib.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.semver4j.Semver;
import top.hendrixshen.magiclib.MagicLib;

import java.util.Collection;

public class VersionUtil {
    @Contract(pure = true)
    public static boolean isVersionSatisfyPredicates(String version, @NotNull Collection<String> versionPredicates) {
        return versionPredicates.isEmpty() || versionPredicates
                .stream()
                .allMatch((versionPredicate) -> VersionUtil.isVersionSatisfyPredicate(version, versionPredicate));
    }

    private static boolean isVersionSatisfyPredicate(String version, String versionPredicate) {
        try {
            Semver semver = new Semver(version);
            return semver.satisfies(versionPredicate);
        } catch (Exception e) {
            MagicLib.getLogger().error("Failed to parse version {}: {}", version, e);
        }

        return false;
    }
}
