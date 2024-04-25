package top.hendrixshen.magiclib.mixin.dev.accessor;

import net.minecraft.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

//#if MC > 11701
import org.slf4j.Logger;
//#else
//$$ import org.apache.logging.log4j.Logger;
//#endif

@Mixin(Util.class)
public interface UtilAccessor {
    @Accessor("LOGGER")
    static Logger magiclib$getLogger() {
        throw new AssertionError();
    }
}
