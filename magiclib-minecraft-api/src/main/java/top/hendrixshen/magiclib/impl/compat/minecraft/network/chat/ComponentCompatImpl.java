package top.hendrixshen.magiclib.impl.compat.minecraft.network.chat;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.api.compat.AbstractCompat;
import top.hendrixshen.magiclib.api.compat.minecraft.network.chat.ComponentCompat;
import top.hendrixshen.magiclib.api.compat.minecraft.network.chat.StyleCompat;

public class ComponentCompatImpl extends AbstractCompat<Component> implements ComponentCompat {
    public ComponentCompatImpl(@NotNull Component type) {
        super(type);
    }

    @Override
    public Style getStyle() {
        return this.get().getStyle();
    }

    @Override
    public StyleCompat getStyleCompat() {
        return StyleCompat.of(this.get().getStyle());
    }
}
