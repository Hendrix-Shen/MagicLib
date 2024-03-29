package top.hendrixshen.magiclib.impl.platform;

import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.Maps;
import lombok.Getter;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforgespi.language.IModInfo;
import top.hendrixshen.magiclib.MagicLibProperties;
import top.hendrixshen.magiclib.api.platform.DistType;
import top.hendrixshen.magiclib.api.platform.Platform;
import top.hendrixshen.magiclib.api.platform.PlatformType;
import top.hendrixshen.magiclib.api.platform.adapter.ModContainerAdapter;
import top.hendrixshen.magiclib.impl.platform.adapter.NeoForgeLoadingModList;
import top.hendrixshen.magiclib.impl.platform.adapter.NeoForgeModContainer;
import top.hendrixshen.magiclib.impl.platform.adapter.NeoForgeModList;
import top.hendrixshen.magiclib.util.collect.ValueContainer;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public class NeoForgePlatformImpl implements Platform {
    @Getter(lazy = true)
    private static final Platform instance = new NeoForgePlatformImpl();
    public static final ImmutableBiMap<DistType, Dist> distTypeMappings = ImmutableBiMap.of(
            DistType.CLIENT, Dist.CLIENT,
            DistType.SERVER, Dist.DEDICATED_SERVER
    );

    private final Map<String, ModContainerAdapter> modMap = Maps.newConcurrentMap();

    private NeoForgePlatformImpl() {
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
        return PlatformType.NEOFORGE;
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
        return ModList.get().isLoaded(modIdentifier);
    }

    @Override
    public boolean isModExist(String modIdentifier) {
        return NeoForgeLoadingModList.getInstance().getModFileById(modIdentifier).isPresent();
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return !FMLLoader.isProduction();
    }

    @Override
    public ValueContainer<ModContainerAdapter> getMod(String modIdentifier) {
        return ValueContainer.ofNullable(this.modMap.get(modIdentifier)).or(() -> {
            try {
                ModContainerAdapter mod = NeoForgeModContainer.of(modIdentifier);
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
        return ValueContainer.ofNullable(NeoForgeModList.getInstance().getMods())
                .orElse(NeoForgeLoadingModList.getInstance().getMods())
                .orElseThrow(() -> new IllegalStateException("Access ModList too early!"))
                .stream()
                .map(IModInfo::getModId)
                .collect(Collectors.toList());
    }

    @Override
    public String getNamedMappingName() {
        String name = MagicLibProperties.DEV_MAPPING_NAME.getStringValue();

        if (name != null) {
            return name;
        }

        if (!this.isDevelopmentEnvironment()) {
            return null;
        }

        return "mojang";
    }

    public Dist getCurrentEnvType() {
        return FMLLoader.getDist();
    }

    public DistType getDistType(Dist envType) {
        return NeoForgePlatformImpl.distTypeMappings.inverse().get(envType);
    }

    public Dist getDist(DistType sideType) {
        return NeoForgePlatformImpl.distTypeMappings.get(sideType);
    }
}
