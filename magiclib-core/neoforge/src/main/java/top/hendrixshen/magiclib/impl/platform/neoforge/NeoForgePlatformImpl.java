package top.hendrixshen.magiclib.impl.platform.neoforge;

import com.google.common.collect.ImmutableBiMap;
import lombok.Getter;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.loading.moddiscovery.ModInfo;
import top.hendrixshen.magiclib.api.dependency.DistType;
import top.hendrixshen.magiclib.api.platform.Platform;

public class NeoForgePlatformImpl implements Platform {
    @Getter(lazy = true)
    private static final Platform instance = new NeoForgePlatformImpl();

    public final static ImmutableBiMap<DistType, Dist> sideTypeMappings = ImmutableBiMap.of(
            DistType.CLIENT, Dist.CLIENT,
            DistType.SERVER, Dist.DEDICATED_SERVER
    );

    @Override
    public String getPlatformName() {
        return "neoforge";
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
        return FMLLoader.getLoadingModList().getMods()
                .stream()
                .anyMatch(modInfo -> modInfo.getModId().equals(modIdentifier));
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return !FMLLoader.isProduction();
    }

    @Override
    public String getModName(String modIdentifier) {
        for (ModInfo modInfo : FMLLoader.getLoadingModList().getMods()) {
            if (modInfo.getModId().equals(modIdentifier)) {
                return modInfo.getDisplayName();
            }
        }

        return "(Unknown Mod Version)";
    }

    @Override
    public String getModVersion(String modIdentifier) {
        for (ModInfo modInfo : FMLLoader.getLoadingModList().getMods()) {
            if (modInfo.getModId().equals(modIdentifier)) {
                return modInfo.getDisplayName();
            }
        }

        return "(Unknown Mod Version)";
    }

    public Dist getCurrentEnvType() {
        return FMLLoader.getDist();
    }

    public DistType getSideType(Dist envType) {
        return NeoForgePlatformImpl.sideTypeMappings.inverse().get(envType);
    }

    public Dist getEnvType(DistType sideType) {
        return NeoForgePlatformImpl.sideTypeMappings.get(sideType);
    }
}
