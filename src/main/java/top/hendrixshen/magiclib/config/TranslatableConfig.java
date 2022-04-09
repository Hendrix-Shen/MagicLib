package top.hendrixshen.magiclib.config;

import fi.dy.masa.malilib.config.options.ConfigBase;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public interface TranslatableConfig {
    @Nullable
    Consumer<ConfigBase<?>> getValueChangedFromJsonCallback();

    void setValueChangedFromJsonCallback(@Nullable Consumer<ConfigBase<?>> valueChangedFromJsonCallback);

}
