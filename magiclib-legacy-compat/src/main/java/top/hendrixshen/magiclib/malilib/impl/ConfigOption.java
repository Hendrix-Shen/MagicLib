package top.hendrixshen.magiclib.malilib.impl;

import fi.dy.masa.malilib.config.options.ConfigBase;
import lombok.Getter;
import lombok.Setter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.dependency.api.ConfigDependencyPredicate;
import top.hendrixshen.magiclib.dependency.impl.Dependencies;
import top.hendrixshen.magiclib.malilib.api.annotation.Config;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import java.util.function.Consumer;

@Deprecated
@ApiStatus.ScheduledForRemoval
@Environment(EnvType.CLIENT)
public class ConfigOption {
    private final Config annotation;
    @Getter
    private final ConfigBase<?> config;
    @Getter
    private final Dependencies<ConfigOption> modDependencies;
    private final ConfigDependencyPredicate predicate;

    @Getter
    @Setter
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

    public boolean isEnabled() {
        return this.modDependencies.satisfied(this) && this.predicate.isSatisfied(this);
    }

    public <T> Optional<T> getConfig(@NotNull Class<T> clazz) {
        if (clazz.isInstance(this.config)) {
            return Optional.of(clazz.cast(this.config));
        } else {
            return Optional.empty();
        }
    }

}
