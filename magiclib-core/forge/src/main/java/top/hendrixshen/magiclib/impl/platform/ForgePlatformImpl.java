package top.hendrixshen.magiclib.impl.platform;

import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.Maps;
import cpw.mods.modlauncher.api.INameMappingService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;
import net.minecraftforge.forgespi.language.IModInfo;
import org.jetbrains.annotations.Nullable;
import top.hendrixshen.magiclib.MagicLibProperties;
import top.hendrixshen.magiclib.api.platform.DistType;
import top.hendrixshen.magiclib.api.platform.Platform;
import top.hendrixshen.magiclib.api.platform.PlatformType;
import top.hendrixshen.magiclib.api.platform.adapter.ModContainerAdapter;
import top.hendrixshen.magiclib.impl.platform.adapter.ForgeLoadingModList;
import top.hendrixshen.magiclib.impl.platform.adapter.ForgeModContainer;
import top.hendrixshen.magiclib.impl.platform.adapter.ForgeModList;
import top.hendrixshen.magiclib.util.ReflectionUtil;
import top.hendrixshen.magiclib.util.collect.ValueContainer;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ForgePlatformImpl implements Platform {
    @Getter(lazy = true)
    private static final Platform instance = new ForgePlatformImpl();
    public static final ImmutableBiMap<DistType, Dist> distTypeMappings = ImmutableBiMap.of(
            DistType.CLIENT, Dist.CLIENT,
            DistType.SERVER, Dist.DEDICATED_SERVER
    );
    // TODO: Standalone cross-platform generic remapping api.
    private static final BiFunction<INameMappingService.Domain, String, String> remapNameMethod = (domain, srgName) -> {
        // Minecraft Forge >1.17-
        ValueContainer<Class<?>> helperClazz = ReflectionUtil.getClass("net.minecraftforge.fml.util.ObfuscationReflectionHelper")
                // Minecraft Forge <1.16.5-
                .or(() -> ReflectionUtil.getClass("net.minecraftforge.fml.common.ObfuscationReflectionHelper"));

        // If no class found, just skip the calling logic.
        if (helperClazz.isEmpty()) {
            return srgName;
        }

        return ReflectionUtil.invokeStatic(helperClazz, "remapName",
                        new Class[]{INameMappingService.class, String.class}, domain, srgName)
                .map(Object::toString)
                .orElse(srgName);
    };

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
        return PlatformType.FORGE;
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
        return ForgeLoadingModList.getInstance()
                .getModFileById(modIdentifier)
                .isPresent();
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return !FMLLoader.isProduction();
    }

    @Override
    public String getModName(String modIdentifier) {
        return ForgeModList.getInstance().getModFileById(modIdentifier)
                .or(() -> ForgeLoadingModList.getInstance().getModFileById(modIdentifier))
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
        return ForgeModList.getInstance().getModFileById(modIdentifier)
                .or(() -> ForgeLoadingModList.getInstance().getModFileById(modIdentifier))
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
        return ForgeModList.getInstance().getMods()
                .or(() -> ForgeLoadingModList.getInstance().getMods())
                .orElseThrow(() -> new IllegalStateException("Access ModList too early!"))
                .stream()
                .map(ModInfo::getModId)
                .collect(Collectors.toList());
    }

    @Override
    public @Nullable String getNamedMappingName() {
        String name = MagicLibProperties.DEV_MAPPING_NAME.getStringValue();

        if (name != null) {
            return name;
        }

        String className = ForgePlatformImpl.remapNameMethod.apply(INameMappingService.Domain.CLASS, "net.minecraft.src.C_3391_");

        switch (className) {
            case "net.minecraft.client.Minecraft":
                String methodName = ForgePlatformImpl.remapNameMethod.apply(INameMappingService.Domain.METHOD, "func_230150_b_");

                if ("updateTitle".equals(methodName)) {
                    return "mojang";
                } else if ("setDefaultMinecraftTitle".equals(methodName)) {
                    return "mcp";
                }

                return "unknown";
            case "net.minecraft.client.MinecraftClient":
                return "yarn";
            case "net.minecraft.src.C_3391_":
                return null;
            default:
                return "unknown";
        }
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
