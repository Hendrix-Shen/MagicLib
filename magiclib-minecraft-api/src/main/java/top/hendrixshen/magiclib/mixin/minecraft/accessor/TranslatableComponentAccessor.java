package top.hendrixshen.magiclib.mixin.minecraft.accessor;

import net.minecraft.network.chat.TranslatableComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

//#if MC > 11701
//$$ import java.util.function.Consumer;
//#else
import java.util.List;
//#endif

//#if MC > 11502
import net.minecraft.network.chat.FormattedText;
//#else
//$$ import net.minecraft.network.chat.Component;
//#endif

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
            String translation
            //#if MC > 11701
            //$$ , Consumer<FormattedText> partsConsumer
            //#endif
    );
}
