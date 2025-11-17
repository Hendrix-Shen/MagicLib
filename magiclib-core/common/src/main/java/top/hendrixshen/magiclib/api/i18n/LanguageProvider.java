package top.hendrixshen.magiclib.api.i18n;

import top.hendrixshen.magiclib.MagicLib;
import top.hendrixshen.magiclib.api.platform.PlatformType;
import top.hendrixshen.magiclib.util.VersionUtil;

import java.util.Map;
import java.util.regex.Pattern;

public interface LanguageProvider {
    Pattern LANGUAGE_PATH_PATTERN = Pattern.compile("^assets/([\\w-]*)/lang/([a-zA-Z\\d-_]*)\\.json$");

    void init();

    void reload();

    void reload(String LanguageCode);

    void loadLanguage(String languageCode);

    Map<String, String> getLanguage(String languageCode);

    default ClassLoader getClassLoader() {
        if (VersionUtil.isVersionSatisfyPredicate(MagicLib.getInstance().getCurrentPlatform()
                .getModVersion("minecraft"), ">=1.21.9-")
                && MagicLib.getInstance().getCurrentPlatform().getPlatformType().matches(PlatformType.NEOFORGE)) {
            return ClassLoader.getSystemClassLoader();
        }

        return this.getClass().getClassLoader();
    }
}
