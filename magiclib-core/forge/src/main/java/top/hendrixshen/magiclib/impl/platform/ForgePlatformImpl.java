package top.hendrixshen.magiclib.impl.platform;

import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.Maps;
import lombok.Getter;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;
import net.minecraftforge.forgespi.language.IModInfo;
import top.hendrixshen.magiclib.api.platform.DistType;
import top.hendrixshen.magiclib.api.platform.Platform;
import top.hendrixshen.magiclib.api.platform.PlatformType;
import top.hendrixshen.magiclib.api.platform.adapter.ModContainerAdapter;
import top.hendrixshen.magiclib.impl.platform.adapter.ForgeModContainer;
import top.hendrixshen.magiclib.util.collect.ValueContainer;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public class ForgePlatformImpl implements Platform {
    @Getter(lazy = true)
    private static final Platform instance = new ForgePlatformImpl();
    public static final ImmutableBiMap<DistType, Dist> distTypeMappings = ImmutableBiMap.of(
            DistType.CLIENT, Dist.CLIENT,
            DistType.SERVER, Dist.DEDICATED_SERVER
    );

    private final Map<String, ModContainerAdapter> modMap = Maps.newConcurrentMap();

    private ForgePlatformImpl() {
    }

    @Override
    public Path getGameFolder() {
        return FMLPaths.GAMEDIR.get();
    }

    @Override
    public Path getConfigFolder() {
        return FMLPaths.CONFIGDIR.get();
    }

    @Override
    public Path getModsFolder() {
        return FMLPaths.MODSDIR.get();
    }

    @Override
    public PlatformType getPlatformType() {
        return PlatformType.FORGE;
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
        return ModList.get().isLoaded(modIdentifier);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return !FMLLoader.isProduction();
    }

    @Override
    public ValueContainer<ModContainerAdapter> getMod(String modIdentifier) {
        return ValueContainer.ofNullable(this.modMap.get(modIdentifier)).or(() -> {
            try {
                ModContainerAdapter mod = ForgeModContainer.of(modIdentifier);
                this.modMap.put(modIdentifier, mod);
                return ValueContainer.of(mod);
            } catch (Exception e) {
                return ValueContainer.empty();
            }
        });
    }

    @Override
    public Collection<ModContainerAdapter> getMods() {
        for (IModInfo info : ModList.get().getMods()) {
            this.getMod(info.getModId());
        }

        return this.modMap.values();
    }

    @Override
    public Collection<String> getModIds() {
        return ModList.get().getMods().stream()
                .map(ModInfo::getModId)
                .collect(Collectors.toList());
    }

    public Dist getCurrentEnvType() {
        return FMLLoader.getDist();
    }

    public DistType getDistType(Dist envType) {
        return ForgePlatformImpl.distTypeMappings.inverse().get(envType);
    }

    public Dist getDist(DistType sideType) {
        return ForgePlatformImpl.distTypeMappings.get(sideType);
    }
}
