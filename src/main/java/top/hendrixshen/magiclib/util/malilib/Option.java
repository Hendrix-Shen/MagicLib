package top.hendrixshen.magiclib.util.malilib;

import fi.dy.masa.malilib.config.IConfigBase;
import top.hendrixshen.magiclib.api.malilib.annotation.Config;
import top.hendrixshen.magiclib.util.ModDependency;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Option {
    private final Config annotation;
    private final IConfigBase config;
    private final List<ModDependency> modDependencies;
    private final List<ModDependency> minecraftDependencies;

    public Option(Config annotation, IConfigBase config) {
        this.annotation = annotation;
        this.config = config;
        this.modDependencies = Arrays.stream(annotation.dependencies()).map(ModDependency::of).collect(Collectors.toList());
        this.minecraftDependencies = Arrays.stream(annotation.dependencies()).map(r -> ModDependency.of(r, dp -> dp.value().equals("minecraft"))).collect(Collectors.toList());
    }

    public String getCategory() {
        return this.annotation.category();
    }

    public List<ModDependency> getModDependencies() {
        return this.modDependencies;
    }

    public boolean isEnabled() {
        return this.modDependencies.isEmpty() || this.modDependencies.stream().anyMatch(ModDependency::isSatisfied);
    }

    public boolean isMatchedMinecraftVersion() {
        return this.minecraftDependencies.isEmpty() || this.minecraftDependencies.stream().anyMatch(ModDependency::isSatisfied);
    }

    public boolean isDebug() {
        return this.annotation.debug();
    }

    public boolean isDevOnly() {
        return this.annotation.devOnly();
    }

    public IConfigBase getConfig() {
        return this.config;
    }
}
