package top.hendrixshen.magiclib.util;

public class VersionParser {
    public static String getVersionType(String version) {
        if (version.endsWith("stable")) {
            return "Public Release";
        } else if (version.endsWith("beta")) {
            return "Public Beta";
        } else if (version.endsWith("dev")) {
            return "Development";
        }
        return "Unknown";
    }
}
