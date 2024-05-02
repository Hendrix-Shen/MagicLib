package top.hendrixshen.magiclib.malilib.api.config;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.ApiStatus;

@Deprecated()
@ApiStatus.ScheduledForRemoval(inVersion = "0.8")
@Environment(EnvType.CLIENT)
public interface TranslatableConfig extends IMagicConfigBase {
}
