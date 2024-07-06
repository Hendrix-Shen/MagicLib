package top.hendrixshen.magiclib;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.hendrixshen.magiclib.api.dependency.version.SemanticVersion;

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
    private final SemanticVersion semver;
    @Getter
    private final String originalVersion;

    MagicLibVersion(@NotNull String versionName) {
        this.originalVersion = versionName.trim();
        this.semver = SemanticVersion.parse(this.originalVersion);
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
        SemanticVersion semver = SemanticVersion.parse(name);

        for (MagicLibVersion version : MagicLibVersion.values()) {
            if (version.getSemver().getVersionComponent(0) == semver.getVersionComponent(0) &&
                    version.getSemver().getVersionComponent(1) == semver.getVersionComponent(1)) {
                return version;
            }
        }

        return null;
    }
}
