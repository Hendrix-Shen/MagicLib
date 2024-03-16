package top.hendrixshen.magiclib.impl.i18n.provider;

import com.google.common.collect.Maps;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.MagicLib;
import top.hendrixshen.magiclib.api.i18n.LanguageProvider;
import top.hendrixshen.magiclib.util.JsonUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JarLanguageProvider implements LanguageProvider {
    @Getter(lazy = true)
    private final static JarLanguageProvider instance = new JarLanguageProvider();

    private final Map<String, Map<String, String>> languageMap = Maps.newConcurrentMap();
    private final Pattern languageResourcePattern = Pattern.compile("^assets/([\\w-]*)/lang/([a-zA-Z\\d-_]*)\\.json$");

    @Override
    public void init() {
        try {
            for (URL resource : Collections.list(this.getClass().getClassLoader().getResources("assets"))) {
                if (!resource.getProtocol().equals("jar")) {
                    continue;
                }

                JarURLConnection connection = (JarURLConnection) resource.openConnection();
                this.loadFromJar(connection.getJarFile());
            }
        } catch (IOException e) {
            MagicLib.getLogger().error("Failed to load language file.", e);
        }
    }

    @Override
    public void reload() {
        // NO-OP
    }

    @Override
    public void reload(String LanguageCode) {
        // NO-OP
    }

    @Override
    public void loadLanguage(String languageCode) {
        // NO-OP
    }

    @Override
    public Map<String, String> getLanguage(@NotNull String languageCode) {
        return this.languageMap.computeIfAbsent(languageCode.toLowerCase(), key -> Maps.newConcurrentMap());
    }

    private void loadFromJar(@NotNull JarFile jar) {
        for (JarEntry entry : Collections.list(jar.entries())) {
            Matcher matcher = languageResourcePattern.matcher(entry.getName());

            if (!matcher.find()) {
                continue;
            }

            Map<String, String> language = this.getLanguage(matcher.group(2));

            try (InputStream inputStream = jar.getInputStream(entry);) {
                JsonUtil.loadStringMapFromJson(inputStream, language::put);
                MagicLib.getLogger().debug("Loaded language file {} from {}.", entry.getName(), jar.getName());
            } catch (Exception e) {
                MagicLib.getLogger().error("Failed to load language file {} from {}.", entry.getName(), jar.getName(), e);
            }
        }
    }
}
