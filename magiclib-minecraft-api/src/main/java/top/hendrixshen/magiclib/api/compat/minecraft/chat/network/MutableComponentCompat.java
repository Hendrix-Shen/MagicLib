package top.hendrixshen.magiclib.api.compat.minecraft.chat.network;

import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.impl.compat.minecraft.chat.network.*;

import java.util.function.UnaryOperator;

//#if MC > 11502
//$$ import net.minecraft.network.chat.MutableComponent;
//#else
import net.minecraft.network.chat.BaseComponent;
//#endif

public interface MutableComponentCompat extends ComponentCompat {
    static @NotNull MutableComponentCompat of(@NotNull
                                              //#if MC > 11502
                                              //$$ MutableComponent
                                              //#else
                                              BaseComponent
                                                        //#endif
                                                        mutableComponent) {
        return new MutableComponentCompatImpl(mutableComponent);
    }

    void setStyle(@NotNull StyleCompat style);

    MutableComponentCompat append(@NotNull MutableComponentCompat component);

    default MutableComponentCompat append(String string) {
        this.append(ComponentCompat.literal(string));
        return this;
    }

    MutableComponentCompat withStyle(@NotNull StyleCompat style);

    default MutableComponentCompat withStyle(@NotNull UnaryOperator<StyleCompat> style) {
        this.setStyle(style.apply(this.getStyle()));
        return this;
    }
}
