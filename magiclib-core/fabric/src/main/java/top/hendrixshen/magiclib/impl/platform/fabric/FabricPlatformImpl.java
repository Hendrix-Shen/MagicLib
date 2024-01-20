package top.hendrixshen.magiclib.impl.platform.fabric;

import com.google.common.collect.ImmutableBiMap;
import lombok.Getter;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.api.dependency.DistType;
import top.hendrixshen.magiclib.api.platform.IPlatform;

import java.util.Optional;

public class FabricPlatformImpl implements IPlatform {
    @Getter(lazy = true)
    private static final IPlatform instance = new FabricPlatformImpl();
    public final static ImmutableBiMap<DistType, EnvType> distTypeMappings = ImmutableBiMap.of(
            DistType.CLIENT, EnvType.CLIENT,
            DistType.SERVER, EnvType.SERVER
    );

    private FabricPlatformImpl() {
    }

    @Override
    public @NotNull String getPlatformName() {
        return "fabric";
    }

    @Override
    public DistType getCurrentDistType() {
        return this.getSideType(this.getCurrentEnvType());
    }

    @Override
    public boolean matchesSide(DistType side) {
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
    public String getModName(String modIdentifier) {
        Optional<ModContainer> modContainer = FabricLoader.getInstance().getModContainer(modIdentifier);
        return modContainer.isPresent() ? modContainer.get().getMetadata().getName() : "?";
    }

    @Override
    public String getModVersion(String modIdentifier) {
        Optional<ModContainer> modContainer = FabricLoader.getInstance().getModContainer(modIdentifier);
        return modContainer.isPresent() ? modContainer.get().getMetadata().getVersion().getFriendlyString() : "?";
    }

    public EnvType getCurrentEnvType() {
        return FabricLoader.getInstance().getEnvironmentType();
    }

    public DistType getSideType(EnvType envType) {
        return FabricPlatformImpl.distTypeMappings.inverse().get(envType);
    }

    public EnvType getEnvType(DistType sideType) {
        return FabricPlatformImpl.distTypeMappings.get(sideType);
    }
}
