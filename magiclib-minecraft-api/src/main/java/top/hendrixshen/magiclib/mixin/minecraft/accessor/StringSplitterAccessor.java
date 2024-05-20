package top.hendrixshen.magiclib.mixin.minecraft.accessor;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.StringSplitter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Environment(EnvType.CLIENT)
@Mixin(StringSplitter.class)
public interface StringSplitterAccessor {
    @Accessor("widthProvider")
    StringSplitter.WidthProvider magiclib$widthProvider();
}
