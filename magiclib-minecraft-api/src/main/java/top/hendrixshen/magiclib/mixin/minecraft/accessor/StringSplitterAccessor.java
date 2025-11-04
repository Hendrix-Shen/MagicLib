package top.hendrixshen.magiclib.mixin.minecraft.accessor;

import net.minecraft.client.StringSplitter;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

// CHECKSTYLE.OFF: JavadocStyle
/**
 * <li>mc1.14 ~ mc1.15.2: subproject 1.15.2 [dummy]</li>
 * <li>mc1.16.5+        : subproject 1.16.5 (main project)        &lt;--------</li>
 */
// CHECKSTYLE.ON: JavadocStyle
@Mixin(StringSplitter.class)
public interface StringSplitterAccessor {
    @Accessor("widthProvider")
    StringSplitter.WidthProvider magiclib$widthProvider();
}
