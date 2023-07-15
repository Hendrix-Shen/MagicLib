package top.hendrixshen.magiclib.malilib.impl;

import fi.dy.masa.malilib.config.options.ConfigBase;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.dependency.api.ConfigDependencyPredicate;
import top.hendrixshen.magiclib.dependency.impl.Dependencies;
import top.hendrixshen.magiclib.malilib.api.annotation.Config;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public class ConfigOption {
    private final Config annotation;
    private final ConfigBase<?> config;
    private final Dependencies<ConfigOption> modDependencies;
    private final ConfigDependencyPredicate predicate;

    private Consumer<ConfigOption> valueChangeCallback = option -> {
    };

    public ConfigOption(@NotNull Config annotation, ConfigBase<?> config) {
        this.annotation = annotation;
        this.config = config;
        this.modDependencies = Dependencies.of(annotation.dependencies(), ConfigOption.class);

        try {
            this.predicate = annotation.predicate().getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public String getCategory() {
        return this.annotation.category();
    }

    public String getName() {
        return this.config.getName();
    }

    public Dependencies<ConfigOption> getModDependencies() {
        return this.modDependencies;
    }

    public boolean isEnabled() {
        return this.modDependencies.satisfied(this) && this.predicate.isSatisfied(this);
    }

    public ConfigBase<?> getConfig() {
        return this.config;
    }

    public <T> Optional<T> getConfig(@NotNull Class<T> clazz) {
        if (clazz.isInstance(this.config)) {
            return Optional.of(clazz.cast(this.config));
        } else {
            return Optional.empty();
        }
    }

    public Consumer<ConfigOption> getValueChangeCallback() {
        return this.valueChangeCallback;
    }

    public void setValueChangeCallback(Consumer<ConfigOption> valueChangeCallback) {
        this.valueChangeCallback = valueChangeCallback;
    }
}
