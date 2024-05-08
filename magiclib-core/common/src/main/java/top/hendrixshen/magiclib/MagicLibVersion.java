package top.hendrixshen.magiclib;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.semver4j.Semver;

public enum MagicLibVersion {
    VERSION_0_1("0.1"),
    VERSION_0_2("0.2"),
    VERSION_0_3("0.3"),
    VERSION_0_4("0.4"),
    VERSION_0_5("0.5"),
    VERSION_0_6("0.6"),
    VERSION_0_7("0.7"),
    VERSION_0_8("0.8");

    @Getter(lazy = true)
    private static final MagicLibVersion currentMagicLibVersion = MagicLibVersion
            .toMagicLibVersion(SharedConstants.getMagiclibVersion());

    @Getter
    private final Semver semver;
    @Getter
    private final String originalVersion;

    MagicLibVersion(@NotNull String versionName) {
        this.originalVersion = versionName.trim();
        this.semver = Semver.coerce(this.originalVersion);
    }

    @Nullable
    public static MagicLibVersion toMagicLibVersion(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof MagicLibVersion) {
            return (MagicLibVersion) value;
        }

        String name = value.toString();
        Semver semver = Semver.coerce(name);

        if (semver == null) {
            return null;
        }

        for (MagicLibVersion version : MagicLibVersion.values()) {
            if (version.getSemver().getMajor() == semver.getMajor() &&
                    version.getSemver().getMinor() == semver.getMinor()) {
                return version;
            }
        }

        return null;
    }
}
