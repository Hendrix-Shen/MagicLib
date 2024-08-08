package top.hendrixshen.magiclib.api.compat.minecraft.network.chat;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.impl.compat.minecraft.network.chat.MutableComponentCompatImpl;

import java.util.function.UnaryOperator;

public interface MutableComponentCompat extends ComponentCompat {
    static @NotNull MutableComponentCompat of(@NotNull BaseComponent mutableOrBaseComponent) {
        return new MutableComponentCompatImpl(mutableOrBaseComponent);
    }

    @Override
    @NotNull
    BaseComponent get();

    void setStyle(@NotNull Style style);

    void setStyle(@NotNull StyleCompat styleCompat);

    MutableComponentCompat append(@NotNull Component component);

    MutableComponentCompat append(@NotNull ComponentCompat componentCompat);

    default MutableComponentCompat append(String string) {
        this.append(ComponentCompat.literal(string));
        return this;
    }

    MutableComponentCompat withStyle(@NotNull StyleCompat styleCompat);

    default MutableComponentCompat withStyle(@NotNull UnaryOperator<Style> style) {
        this.setStyle(style.apply(this.get().getStyle()));
        return this;
    }

    default MutableComponentCompat withStyleCompat(@NotNull UnaryOperator<StyleCompat> styleCompat) {
        this.setStyle(styleCompat.apply(this.getStyleCompat()));
        return this;
    }

    default MutableComponentCompat withStyle(ChatFormatting... chatFormattings) {
        this.setStyle(this.getStyleCompat().applyFormats(chatFormattings));
        return this;
    }
}
