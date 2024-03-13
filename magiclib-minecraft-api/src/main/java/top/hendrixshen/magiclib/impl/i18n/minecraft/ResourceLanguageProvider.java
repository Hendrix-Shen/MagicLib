package top.hendrixshen.magiclib.impl.i18n.minecraft;

import com.google.common.collect.Maps;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.repository.UnopenedPack;
import top.hendrixshen.magiclib.MagicLib;
import top.hendrixshen.magiclib.api.fake.i18n.PackAccessor;
import top.hendrixshen.magiclib.api.i18n.LanguageProvider;
import top.hendrixshen.magiclib.impl.i18n.provider.FileLanguageProvider;
import top.hendrixshen.magiclib.util.JsonUtil;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResourceLanguageProvider implements LanguageProvider {
    @Getter(lazy = true)
    private final static ResourceLanguageProvider instance = new ResourceLanguageProvider();

    private final Map<String, List<Path>> files = Maps.newConcurrentMap();

    @Override
    public void init() {
        Minecraft.getInstance().getResourcePackRepository().getSelected().stream()
                .filter(pack -> pack.getId().startsWith("file"))
                .map(UnopenedPack::open)
                .filter(pack -> pack instanceof PackAccessor)
                .map(pack -> ((PackAccessor) pack).magiclib$getFile().toPath())
                .forEach(this::updateFileList);
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
                MagicLib.getLogger().error("Failed to load language file {}.", file, e);
            }
        });

        return result;
    }

    private void updateFileList(Path path) {
        try {
            Files.walkFileTree(path, new FileLanguageProvider.LanguageFileVisitor(path, this.files, true));
        } catch (IOException ignore) {
        }
    }
}
