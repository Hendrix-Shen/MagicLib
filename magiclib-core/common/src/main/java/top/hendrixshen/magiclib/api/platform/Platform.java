package top.hendrixshen.magiclib.api.platform;

import top.hendrixshen.magiclib.api.platform.adapter.ModContainerAdapter;
import top.hendrixshen.magiclib.util.collect.ValueContainer;

import java.nio.file.Path;
import java.util.Collection;

public interface Platform {
    Path getGameFolder();

    Path getConfigFolder();

    Path getModsFolder();

    String getPlatformName();

    DistType getCurrentDistType();

    boolean matchesDist(DistType side);

    boolean isModLoaded(String modIdentifier);

    boolean isDevelopmentEnvironment();

    default String getModName(String modIdentifier) {
        return this.getMod(modIdentifier)
                .map(mod -> mod.getModMetaData().getName())
                .orElse("?");
    }

    default String getModVersion(String modIdentifier) {
        return this.getMod(modIdentifier)
                .map(mod -> mod.getModMetaData().getVersion())
                .orElse("?");
    }

    ValueContainer<ModContainerAdapter> getMod(String modIdentifier);

    Collection<ModContainerAdapter> getMods();

    Collection<String> getModIds();
}

