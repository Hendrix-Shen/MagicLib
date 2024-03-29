package top.hendrixshen.magiclib.util;

import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.MagicLib;

import java.io.File;

public class FileUtil {
    public static @NotNull File getConfigFile(String identifier) {
        return MagicLib.getInstance().getPlatformManage().getCurrentPlatform()
                .getConfigFolder().resolve(identifier + ".json").toFile();
    }
}
