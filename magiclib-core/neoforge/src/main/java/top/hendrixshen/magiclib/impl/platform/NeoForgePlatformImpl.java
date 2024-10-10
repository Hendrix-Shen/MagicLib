package top.hendrixshen.magiclib.impl.platform;

import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.Maps;
import cpw.mods.modlauncher.api.INameMappingService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.fml.util.ObfuscationReflectionHelper;
import net.neoforged.neoforgespi.language.IModInfo;
import org.jetbrains.annotations.Nullable;
import top.hendrixshen.magiclib.MagicLib;
import top.hendrixshen.magiclib.MagicLibProperties;
import top.hendrixshen.magiclib.api.platform.DistType;
import top.hendrixshen.magiclib.api.platform.Platform;
import top.hendrixshen.magiclib.api.platform.PlatformType;
import top.hendrixshen.magiclib.api.platform.adapter.ModContainerAdapter;
import top.hendrixshen.magiclib.impl.platform.adapter.NeoForgeLoadingModList;
import top.hendrixshen.magiclib.impl.platform.adapter.NeoForgeModContainer;
import top.hendrixshen.magiclib.impl.platform.adapter.NeoForgeModList;
import top.hendrixshen.magiclib.util.VersionUtil;
import top.hendrixshen.magiclib.util.collect.ValueContainer;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class NeoForgePlatformImpl implements Platform {
    @Getter(lazy = true)
    private static final Platform instance = new NeoForgePlatformImpl();
    public static final ImmutableBiMap<DistType, Dist> distTypeMappings = ImmutableBiMap.of(
            DistType.CLIENT, Dist.CLIENT,
            DistType.SERVER, Dist.DEDICATED_SERVER
    );

    private final Map<String, ModContainerAdapter> modMap = Maps.newConcurrentMap();

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
    public String getModName(String modIdentifier) {
        return NeoForgeModList.getInstance().getModFileById(modIdentifier)
                .or(() -> NeoForgeLoadingModList.getInstance().getModFileById(modIdentifier))
                .orElseThrow(() -> new IllegalStateException("Access ModList too early!"))
                .getMods()
                .stream()
                .filter(modInfo -> modInfo.getModId().equals(modIdentifier))
                .findFirst()
                .map(IModInfo::getDisplayName)
                .orElse("?");
    }

    @Override
    public String getModVersion(String modIdentifier) {
        return NeoForgeModList.getInstance().getModFileById(modIdentifier)
                .or(() -> NeoForgeLoadingModList.getInstance().getModFileById(modIdentifier))
                .orElseThrow(() -> new IllegalStateException("Access ModList too early!"))
                .getMods()
                .stream()
                .filter(modInfo -> modInfo.getModId().equals(modIdentifier))
                .findFirst()
                .map(iModInfo -> iModInfo.getVersion().toString())
                .orElse("?");
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
        return NeoForgeModList.getInstance().getMods()
                .or(() -> NeoForgeLoadingModList.getInstance().getMods())
                .orElseThrow(() -> new IllegalStateException("Access ModList too early!"))
                .stream()
                .map(IModInfo::getModId)
                .collect(Collectors.toList());
    }

    @Override
    public @Nullable String getNamedMappingName() {
        String name = MagicLibProperties.DEV_MAPPING_NAME.getStringValue();

        if (name != null) {
            return name;
        }

        String mcVer = MagicLib.getInstance().getCurrentPlatform().getModVersion("minecraft");

        if (VersionUtil.isVersionSatisfyPredicate(mcVer, ">1.20.5-")) {
            return this.isDevelopmentEnvironment() ? "mojang" : null;
        }

        String intermediaryMethodName;

        if (VersionUtil.isVersionSatisfyPredicate(mcVer, ">1.17-")) {
            intermediaryMethodName = "m_91341_";
        } else {
            intermediaryMethodName = "func_230150_b_";
        }

        String methodName = ObfuscationReflectionHelper.remapName(INameMappingService.Domain.METHOD, intermediaryMethodName);

        switch (methodName) {
            case "updateTitle":
                return "mojang";
            case "updateWindowTitle":
                return "yarn";
            case "setDefaultMinecraftTitle":
                return "mcp";
            case "m_91341_": // Minecraft Forge >1.17- intermediary
            case "func_230150_b_": // Minecraft Forge <1.16.5- intermediary
                return null;
            default:
                return "unknown";
        }
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
