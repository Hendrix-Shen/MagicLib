package top.hendrixshen.magiclib.util;

import com.google.common.collect.Sets;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.MagicLib;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Set;

public class FileUtil {
    public static @NotNull File getConfigFile(String identifier) {
        return MagicLib.getInstance().getCurrentPlatform().getConfigFolder()
                .resolve(identifier + ".json").toFile();
    }

    public static @NotNull Set<URI> getResources(String name) throws IOException {
        ClassLoader urlLoader = Thread.currentThread().getContextClassLoader();
        Set<URI> set = Sets.newHashSet();
        Enumeration<URL> urlEnumeration = urlLoader.getResources(name);

        while (urlEnumeration.hasMoreElements()) {
            try {
                set.add(urlEnumeration.nextElement().toURI());
            } catch (URISyntaxException ignore) {
            }
        }

        return set;
    }
}
