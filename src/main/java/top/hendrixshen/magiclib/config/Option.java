package top.hendrixshen.magiclib.config;

import fi.dy.masa.malilib.config.options.ConfigBase;
import top.hendrixshen.magiclib.api.dependencyValidator.annotation.OptionDependencyPredicate;
import top.hendrixshen.magiclib.config.annotation.Config;
import top.hendrixshen.magiclib.util.ModDependencies;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import java.util.function.Consumer;

public class Option {
    private final Config annotation;
    private final ConfigBase<?> config;
    private final ModDependencies modDependencies;
    private final OptionDependencyPredicate predicate;

    private Consumer<Option> valueChangeCallback = option -> {
    };

    public Option(Config annotation, ConfigBase<?> config) {
        this.annotation = annotation;
        this.config = config;
        this.modDependencies = ModDependencies.of(annotation.dependencies(), this);
        try {
            this.predicate = annotation.predicate().getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException(e);
        }
    }

    public String getCategory() {
        return this.annotation.category();
    }

    public String getName() {
        return this.config.getName();
    }

    public ModDependencies getModDependencies() {
        return this.modDependencies;
    }

    public boolean isEnabled() {
        return this.modDependencies.satisfied && predicate.test(this);
    }

    public ConfigBase<?> getConfig() {
        return this.config;
    }

    public <T> Optional<T> getConfig(Class<T> clazz) {
        if (clazz.isInstance(this.config)) {
            return Optional.of(clazz.cast(this.config));
        } else {
            return Optional.empty();
        }
    }

    public void setValueChangeCallback(Consumer<Option> valueChangeCallback) {
        this.valueChangeCallback = valueChangeCallback;
    }

    public Consumer<Option> getValueChangeCallback() {
        return this.valueChangeCallback;
    }
}
