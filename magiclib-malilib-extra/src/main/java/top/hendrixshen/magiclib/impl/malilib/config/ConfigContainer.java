package top.hendrixshen.magiclib.impl.malilib.config;

import com.google.common.collect.ImmutableList;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.api.malilib.annotation.Config;
import top.hendrixshen.magiclib.api.malilib.config.option.MagicIConfigBase;
import top.hendrixshen.magiclib.impl.dependency.DependenciesContainer;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ConfigContainer {
    private final Config annotation;
    @Getter
    private final MagicIConfigBase config;
    private final List<DependenciesContainer<ConfigContainer>> dependencies;

    public ConfigContainer(@NotNull Config annotation, MagicIConfigBase config) {
        this.annotation = annotation;
        this.config = config;
        this.dependencies = Arrays.stream(this.annotation.compositeDependencies().value())
                .map(deps -> DependenciesContainer.of(deps, this))
                .collect(Collectors.toList());
    }

    public String getCategory() {
        return this.annotation.category();
    }

    public String getName() {
        return this.config.getName();
    }

    public ImmutableList<DependenciesContainer<ConfigContainer>> getDependencies() {
        return ImmutableList.copyOf(this.dependencies);
    }

    public boolean isSatisfied() {
        return this.dependencies.stream().allMatch(DependenciesContainer::isSatisfied);
    }
}
