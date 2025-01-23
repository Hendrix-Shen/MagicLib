package top.hendrixshen.magiclib.mixin.minecraft.accessor;

import net.minecraft.network.chat.TranslatableComponent;

// CHECKSTYLE.OFF: ImportOrder
//#if MC > 11502
import net.minecraft.network.chat.FormattedText;
//#else
//$$ import net.minecraft.network.chat.Component;
//#endif
// CHECKSTYLE.ON: ImportOrder

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

// CHECKSTYLE.OFF: ImportOrder
//#if MC > 11701
//$$ import java.util.function.Consumer;
//#else
import java.util.List;
//#endif
// CHECKSTYLE.ON: ImportOrder

@Mixin(TranslatableComponent.class)
public interface TranslatableComponentAccessor {
    @Mutable
    @Accessor("args")
    void magiclib$setArgs(Object[] args);

    //#if MC < 11800
    @Accessor("decomposedParts")
    //#if MC > 11502
    List<FormattedText> magiclib$getDecomposedParts();
    //#else
    //$$ List<Component> magiclib$getDecomposedParts();
    //#endif
    //#endif

    @Invoker("decomposeTemplate")
    void magiclib$invokeDecomposeTemplate(
            // CHECKSTYLE.OFF: NoWhitespaceBefore
            // CHECKSTYLE.OFF: SeparatorWrap
            String translation
            //#if MC > 11701
            //$$ , Consumer<FormattedText> partsConsumer
            //#endif
            // CHECKSTYLE.ON: SeparatorWrap
            // CHECKSTYLE.ON: NoWhitespaceBefore
    );
}
