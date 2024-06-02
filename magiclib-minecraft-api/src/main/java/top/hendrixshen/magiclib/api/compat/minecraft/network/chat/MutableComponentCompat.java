package top.hendrixshen.magiclib.api.compat.minecraft.network.chat;

import net.minecraft.ChatFormatting;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.impl.compat.minecraft.network.chat.MutableComponentCompatImpl;

import java.util.function.UnaryOperator;

//#if MC > 11502
import net.minecraft.network.chat.MutableComponent;
//#else
//$$ import net.minecraft.network.chat.BaseComponent;
//#endif

public interface MutableComponentCompat extends ComponentCompat {
    static @NotNull MutableComponentCompat of(
            //#if MC > 11502
            @NotNull MutableComponent mutableOrBaseComponent
            //#else
            //$$ @NotNull BaseComponent mutableOrBaseComponent
            //#endif
    ) {
        return new MutableComponentCompatImpl(mutableOrBaseComponent);
    }

    @Override
    @NotNull
    //#if MC > 11502
    MutableComponent
    //#else
    //$$ BaseComponent
    //#endif
    get();

    void setStyle(@NotNull StyleCompat style);

    MutableComponentCompat append(@NotNull ComponentCompat component);

    default MutableComponentCompat append(String string) {
        this.append(ComponentCompat.literal(string));
        return this;
    }

    MutableComponentCompat withStyle(@NotNull StyleCompat style);

    default MutableComponentCompat withStyle(@NotNull UnaryOperator<StyleCompat> style) {
        this.setStyle(style.apply(this.getStyle()));
        return this;
    }

    default MutableComponentCompat withStyle(ChatFormatting... chatFormattings) {
        this.getStyle().applyFormats(chatFormattings);
        return this;
    }
}
