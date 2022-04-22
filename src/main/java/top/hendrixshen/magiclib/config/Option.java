package top.hendrixshen.magiclib.config;

import fi.dy.masa.malilib.config.options.ConfigBase;
import top.hendrixshen.magiclib.config.annotation.Config;
import top.hendrixshen.magiclib.dependency.Dependencies;
import top.hendrixshen.magiclib.dependency.annotation.OptionDependencyPredicate;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import java.util.function.Consumer;

public class Option {
    private final Config annotation;
    private final ConfigBase<?> config;
    private final Dependencies<Option> modDependencies;
    private final OptionDependencyPredicate predicate;

    private Consumer<Option> valueChangeCallback = option -> {
    };

    public Option(Config annotation, ConfigBase<?> config) {
        this.annotation = annotation;
        this.config = config;
        this.modDependencies = Dependencies.of(annotation.dependencies(), Option.class);
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

    public Dependencies<Option> getModDependencies() {
        return this.modDependencies;
    }

    public boolean isEnabled() {
        return this.modDependencies.satisfied(this) && predicate.test(this);
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

    public Consumer<Option> getValueChangeCallback() {
        return this.valueChangeCallback;
    }

    public void setValueChangeCallback(Consumer<Option> valueChangeCallback) {
        this.valueChangeCallback = valueChangeCallback;
    }
}
