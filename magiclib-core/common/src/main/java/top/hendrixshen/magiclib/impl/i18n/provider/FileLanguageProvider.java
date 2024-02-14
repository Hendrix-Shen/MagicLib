package top.hendrixshen.magiclib.impl.i18n.provider;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.MagicLib;
import top.hendrixshen.magiclib.api.i18n.LanguageProvider;
import top.hendrixshen.magiclib.util.JsonUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileLanguageProvider implements LanguageProvider {
    @Getter(lazy = true)
    private final static FileLanguageProvider instance = new FileLanguageProvider();

    private final Map<String, List<Path>> files = Maps.newConcurrentMap();
    private final Pattern languageResourcePattern = Pattern.compile("^assets/([\\w-]*)/lang/([a-zA-Z\\d-_]*)\\.json$");

    @Override
    public void init() {
        try {
            for (URL resource : Collections.list(this.getClass().getClassLoader().getResources("assets"))) {
                if (!resource.getProtocol().equals("file")) {
                    continue;
                }

                Path path = Paths.get(resource.toURI());
                Files.walkFileTree(path, new LanguageFileVisitor(path));
            }
        } catch (IOException | URISyntaxException ignore) {
        }
    }

    @Override
    public void reload() {
        this.files.clear();
        this.init();
    }

    @Override
    public void reload(String LanguageCode) {
        this.reload();
    }

    @Override
    public void loadLanguage(String languageCode) {
        // NO-OP
    }

    @Override
    public Map<String, String> getLanguage(String languageCode) {
        Map<String, String> result = Maps.newConcurrentMap();

        this.files.getOrDefault(languageCode, Collections.emptyList()).forEach(file -> {
            try (InputStream inputStream = Files.newInputStream(file)) {
                JsonUtil.loadStringMapFromJson(inputStream, result::put);
                MagicLib.getLogger().debug("Loaded language file {}.", file);
            } catch (Exception e) {
                MagicLib.getLogger().error("Failed to load language file {}.", file);
            }
        });

        return result;
    }

    @AllArgsConstructor
    private static class LanguageFileVisitor implements FileVisitor<Path> {
        private final Path basePath;

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(@NotNull Path file, BasicFileAttributes attrs) {
            String name = "assets/" + this.basePath.relativize(file).toString().replace("\\", "/");
            Matcher matcher = FileLanguageProvider.getInstance().languageResourcePattern.matcher(name);

            if (!matcher.find()) {
                return FileVisitResult.CONTINUE;
            }

            FileLanguageProvider.getInstance().files.computeIfAbsent(matcher.group(2), key ->
                    Lists.newArrayList()).add(file);
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) {
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
            return FileVisitResult.CONTINUE;
        }
    }
}
