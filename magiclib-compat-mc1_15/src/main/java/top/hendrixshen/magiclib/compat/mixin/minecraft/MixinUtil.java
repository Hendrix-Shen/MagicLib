package top.hendrixshen.magiclib.compat.mixin.minecraft;

import net.minecraft.Util;
import org.spongepowered.asm.mixin.Mixin;
import top.hendrixshen.magiclib.compat.annotation.Public;
import top.hendrixshen.magiclib.compat.annotation.Remap;

import java.util.UUID;

@Mixin(Util.class)
public class MixinUtil {
    @Public
    @Remap("field_25140")
    private static final UUID NIL_UUID = new UUID(0L, 0L);
}
