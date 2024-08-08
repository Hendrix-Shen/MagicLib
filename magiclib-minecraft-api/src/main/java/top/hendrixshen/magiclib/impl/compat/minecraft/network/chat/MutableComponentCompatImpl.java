package top.hendrixshen.magiclib.impl.compat.minecraft.network.chat;

import net.minecraft.network.chat.BaseComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.api.compat.minecraft.network.chat.ComponentCompat;
import top.hendrixshen.magiclib.api.compat.minecraft.network.chat.MutableComponentCompat;
import top.hendrixshen.magiclib.api.compat.minecraft.network.chat.StyleCompat;

public class MutableComponentCompatImpl extends ComponentCompatImpl implements MutableComponentCompat {
    public MutableComponentCompatImpl(@NotNull BaseComponent type) {
        super(type);
    }

    @NotNull
    @Override
    public
    BaseComponent get() {
        return (BaseComponent) super.get();
    }

    @Override
    public void setStyle(@NotNull Style style) {
        this.get().setStyle(style);
    }

    @Override
    public void setStyle(@NotNull StyleCompat style) {
        this.setStyle(style.get());
    }

    @Override
    public MutableComponentCompat append(@NotNull Component component) {
        this.get().append(component);
        return this;
    }

    @Override
    public MutableComponentCompat append(@NotNull ComponentCompat componentCompat) {
        return this.append(componentCompat.get());
    }

    @Override
    public MutableComponentCompat withStyle(@NotNull StyleCompat style) {
        //#if MC > 11502
        this.get().withStyle(style.get());
        //#else
        //$$ this.get().setStyle(style.get());
        //#endif
        return this;
    }
}
