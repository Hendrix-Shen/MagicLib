package top.hendrixshen.magiclib.impl.i18n.minecraft;

import com.google.common.collect.Maps;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.repository.Pack;

import top.hendrixshen.magiclib.MagicLib;
import top.hendrixshen.magiclib.api.fake.i18n.PackAccessor;
import top.hendrixshen.magiclib.api.i18n.LanguageProvider;
import top.hendrixshen.magiclib.impl.i18n.provider.FileLanguageProvider;
import top.hendrixshen.magiclib.util.JsonUtil;

// CHECKSTYLE.OFF: ImportOrder
//#if 11903 > MC && MC > 11404
import top.hendrixshen.magiclib.mixin.minecraft.accessor.PackResourcesAdapterV4Accessor;
//#endif

//#if MC < 11903
import top.hendrixshen.magiclib.mixin.minecraft.accessor.LegacyPackResourcesAdapterAccessor;
//#endif
// CHECKSTYLE.ON: ImportOrder

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResourceLanguageProvider implements LanguageProvider {
    @Getter(lazy = true)
    private static final ResourceLanguageProvider instance = new ResourceLanguageProvider();

    private final Map<String, List<Path>> files = Maps.newConcurrentMap();

    @Override
    public void init() {
        Minecraft.getInstance().getResourcePackRepository().getSelectedPacks().stream()
                .filter(pack -> pack.getId().startsWith("file"))
                .map(Pack::open)
                .map(this::adaptPack)
                .filter(Objects::nonNull)
                .map(pack -> pack.magiclib$getFile().toPath())
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

    private void updateFileList(Path path) {
        try {
            Files.walkFileTree(path, new FileLanguageProvider.LanguageFileVisitor(path, this.files, true));
        } catch (IOException ignore) {
            // ignore.
        }
    }
}
