package top.hendrixshen.magiclib.impl.platform;

import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.Maps;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import org.jetbrains.annotations.Nullable;
import top.hendrixshen.magiclib.MagicLibProperties;
import top.hendrixshen.magiclib.api.platform.DistType;
import top.hendrixshen.magiclib.api.platform.Platform;
import top.hendrixshen.magiclib.api.platform.PlatformType;
import top.hendrixshen.magiclib.api.platform.adapter.ModContainerAdapter;
import top.hendrixshen.magiclib.impl.platform.adapter.FabricModContainer;
import top.hendrixshen.magiclib.util.collect.ValueContainer;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FabricPlatformImpl implements Platform {
    @Getter(lazy = true)
    private static final Platform instance = new FabricPlatformImpl();
    public static final ImmutableBiMap<DistType, EnvType> distTypeMappings = ImmutableBiMap.of(
            DistType.CLIENT, EnvType.CLIENT,
            DistType.SERVER, EnvType.SERVER
    );

    private final Map<String, ModContainerAdapter> modMap = Maps.newConcurrentMap();

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
    public PlatformType getPlatformType() {
        return PlatformType.FABRIC;
    }

    @Override
    public DistType getCurrentDistType() {
        return this.getDistType(this.getCurrentEnvType());
    }

    @Override
    public boolean matchesDist(DistType distType) {
        return this.getCurrentDistType().matches(distType);
    }

    @Override
    public boolean isModLoaded(String modIdentifier) {
        return FabricLoader.getInstance().isModLoaded(modIdentifier);
    }

    @Override
    public boolean isModExist(String modIdentifier) {
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

    @Override
    public @Nullable String getNamedMappingName() {
        String name = MagicLibProperties.DEV_MAPPING_NAME.getStringValue();

        if (name != null) {
            return name;
        }

        String className = FabricLoader.getInstance().getMappingResolver()
                .mapClassName("intermediary", "net.minecraft.class_310");

        switch (className) {
            case "net.minecraft.client.Minecraft":
                return "mojang";
            case "net.minecraft.client.MinecraftClient":
                return "yarn";
            case "net.minecraft.class_310":
                return null;
            default:
                return "unknown";
        }
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
