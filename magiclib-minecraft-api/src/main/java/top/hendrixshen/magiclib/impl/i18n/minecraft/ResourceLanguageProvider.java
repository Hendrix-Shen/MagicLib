package top.hendrixshen.magiclib.impl.i18n.minecraft;

import com.google.common.collect.Maps;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.FilePackResources;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.repository.Pack;

// CHECKSTYLE.OFF: ImportOrder
//#if MC > 11902
//$$ import net.minecraft.server.packs.PathPackResources;
//#else
import net.minecraft.server.packs.FolderPackResources;
//#endif
// CHECKSTYLE.ON: ImportOrder

import top.hendrixshen.magiclib.MagicLib;
import top.hendrixshen.magiclib.api.fake.i18n.PackAccessor;
import top.hendrixshen.magiclib.api.i18n.LanguageProvider;
import top.hendrixshen.magiclib.impl.i18n.provider.FileLanguageProvider.LanguageFileVisitor;
import top.hendrixshen.magiclib.impl.i18n.provider.JarLanguageProvider;
import top.hendrixshen.magiclib.util.JsonUtil;

// CHECKSTYLE.OFF: ImportOrder
//#if 11903 > MC && MC > 11404
import top.hendrixshen.magiclib.mixin.minecraft.accessor.PackResourcesAdapterV4Accessor;
//#endif

//#if MC < 11903
import top.hendrixshen.magiclib.mixin.minecraft.accessor.LegacyPackResourcesAdapterAccessor;
//#endif
// CHECKSTYLE.ON: ImportOrder

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResourceLanguageProvider implements LanguageProvider {
    @Getter(lazy = true)
    private static final ResourceLanguageProvider instance = new ResourceLanguageProvider();

    private final Map<String, Map<String, String>> languageMap = Maps.newConcurrentMap();

    @Override
    public void init() {
        this.languageMap.clear();
        Minecraft.getInstance().getResourcePackRepository().getSelectedPacks().stream()
                .filter(pack -> pack.getId().startsWith("file"))
                .map(Pack::open)
                .map(this::adaptPack)
                .filter(Objects::nonNull)
                .forEach(this::initLanguageMap);
    }

    @Override
    public void reload() {
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
        return this.languageMap.getOrDefault(languageCode, Collections.emptyMap());
    }

    private PackAccessor adaptPack(PackResources packResources) {
        return this.adaptPack(packResources, false);
    }

    private PackAccessor adaptPack(PackResources packResources, boolean recursive) {
        if (packResources instanceof PackAccessor) {
            return (PackAccessor) packResources;
        }

        //#if MC < 11903
        if (packResources instanceof LegacyPackResourcesAdapterAccessor) {
            try (PackResources v3PackRes = ((LegacyPackResourcesAdapterAccessor) packResources).magiclib$getSource()) {
                return this.adaptPack(v3PackRes, true);
            } catch (Exception e) {
                MagicLib.getLogger().error("Failed to unpack v3adapter {}.", packResources.getName(), e);
                return null;
            }
        }
        //#endif

        //#if 11903 > MC && MC > 11404
        if (packResources instanceof PackResourcesAdapterV4Accessor) {
            try (PackResources v4PackRes = ((PackResourcesAdapterV4Accessor) packResources).magiclib$getPack()) {
                return this.adaptPack(v4PackRes, true);
            } catch (Exception e) {
                MagicLib.getLogger().error("Failed to unpack v4adapter {}.", packResources.getName(), e);
                return null;
            }
        }
        //#endif

        if (!recursive) {
            MagicLib.getLogger().error("Failed to unpack {}.", packResources.getName());
        }

        return null;
    }

    private void initLanguageMap(PackAccessor pack) {
        if (pack instanceof FolderPackResources) {
            this.loadFromFolderPack(pack.magiclib$getFile().toPath());
        } else if (pack instanceof FilePackResources) {
            this.loadFromZipPack(pack.magiclib$getFile());
        } else {
            MagicLib.getLogger().error("Unknown resource the type of pack {}.", ((PackResources) pack).getName());
        }
    }

    @SuppressWarnings("UnstableApiUsage")
    private void loadFromZipPack(File file) {
        try (ZipFile zipFile = new ZipFile(file)) {
            for (ZipEntry entry : Collections.list(zipFile.entries())) {
                try (InputStream inputStream = zipFile.getInputStream(entry)) {
                    if (JarLanguageProvider.loadFromEntry(entry, inputStream, languageCode ->
                            this.languageMap.computeIfAbsent(languageCode, k -> Maps.newConcurrentMap()))) {
                        MagicLib.getLogger().debug("Loaded language file {} from {}.", entry.getName(), file.getName());
                    }
                } catch (IOException e) {
                    MagicLib.getLogger().error("Failed to load language file {} from {}.",
                            entry.getName(), zipFile.getName(), e);
                }
            }
        } catch (IOException e) {
            MagicLib.getLogger().error("Failed to load language file from {}.", file.getName(), e);
        }
    }

    @SuppressWarnings("UnstableApiUsage")
    private void loadFromFolderPack(Path path) {
        try {
            Map<String, List<Path>> files = Maps.newConcurrentMap();
            Files.walkFileTree(path, new LanguageFileVisitor(path, files, true));

            for (Entry<String, List<Path>> entry : files.entrySet()) {
                Map<String, String> map = this.languageMap.computeIfAbsent(entry.getKey(), k -> Maps.newConcurrentMap());

                for (Path p : entry.getValue()) {
                    try (InputStream inputStream = Files.newInputStream(p)) {
                        JsonUtil.loadStringMapFromJson(inputStream, map::put);
                        MagicLib.getLogger().debug("Loaded language file {}.", path);
                    }
                }
            }
        } catch (Exception e) {
            MagicLib.getLogger().error("Failed to load language file {}.", path, e);
        }
    }
}
