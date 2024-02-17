package top.hendrixshen.magiclib.impl.platform;

import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.Maps;
import lombok.Getter;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.api.platform.DistType;
import top.hendrixshen.magiclib.api.platform.Platform;
import top.hendrixshen.magiclib.api.platform.adapter.ModContainerAdapter;
import top.hendrixshen.magiclib.impl.platform.adapter.FabricModContainer;
import top.hendrixshen.magiclib.util.collect.ValueContainer;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public class FabricPlatformImpl implements Platform {
    @Getter(lazy = true)
    private static final Platform instance = new FabricPlatformImpl();
    public static final ImmutableBiMap<DistType, EnvType> distTypeMappings = ImmutableBiMap.of(
            DistType.CLIENT, EnvType.CLIENT,
            DistType.SERVER, EnvType.SERVER
    );

    private final Map<String, ModContainerAdapter> modMap = Maps.newConcurrentMap();

    private FabricPlatformImpl() {
    }

    @Override
    public Path getGameFolder() {
        return FabricLoader.getInstance().getGameDir().toAbsolutePath().normalize();
    }

    @Override
    public Path getConfigFolder() {
        return FabricLoader.getInstance()
                .getConfigDir()
                .toAbsolutePath()
                .normalize();
    }

    @Override
    public Path getModsFolder() {
        return this.getGameFolder().resolve("mods");
    }

    @Override
    public @NotNull String getPlatformName() {
        return "fabric";
    }

    @Override
    public DistType getCurrentDistType() {
        return this.getDistType(this.getCurrentEnvType());
    }

    @Override
    public boolean matchesDist(DistType side) {
        return this.getCurrentDistType().matches(side);
    }

    @Override
    public boolean isModLoaded(String modIdentifier) {
        return FabricLoader.getInstance().isModLoaded(modIdentifier);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public ValueContainer<ModContainerAdapter> getMod(String modIdentifier) {
        return ValueContainer.ofNullable(this.modMap.get(modIdentifier)).or(() -> {
            try {
                ModContainerAdapter mod = FabricModContainer.of(modIdentifier);
                this.modMap.put(modIdentifier, mod);
                return ValueContainer.of(mod);
            } catch (Exception e) {
                return ValueContainer.empty();
            }
        });
    }

    @Override
    public Collection<ModContainerAdapter> getMods() {
        for (ModContainer container : FabricLoader.getInstance().getAllMods()) {
            this.getMod(container.getMetadata().getId());
        }

        return this.modMap.values();
    }

    @Override
    public Collection<String> getModIds() {
        return FabricLoader.getInstance().getAllMods().stream()
                .map(mod -> mod.getMetadata().getId())
                .collect(Collectors.toList());
    }

    public EnvType getCurrentEnvType() {
        return FabricLoader.getInstance().getEnvironmentType();
    }

    public DistType getDistType(EnvType envType) {
        return FabricPlatformImpl.distTypeMappings.inverse().get(envType);
    }

    public EnvType getEnvType(DistType sideType) {
        return FabricPlatformImpl.distTypeMappings.get(sideType);
    }
}
