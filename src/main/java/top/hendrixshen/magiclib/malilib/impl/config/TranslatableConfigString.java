package top.hendrixshen.magiclib.malilib.impl.config;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.ApiStatus;

@Deprecated()
@ApiStatus.ScheduledForRemoval(inVersion = "0.8")
@Environment(EnvType.CLIENT)
public class TranslatableConfigString extends MagicConfigString {
    public TranslatableConfigString(String prefix, String name, String defaultValue) {
        super(prefix, name, defaultValue);
    }
}
