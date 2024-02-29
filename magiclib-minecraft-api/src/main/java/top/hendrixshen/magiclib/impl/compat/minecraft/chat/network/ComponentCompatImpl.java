package top.hendrixshen.magiclib.impl.compat.minecraft.chat.network;

import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.api.compat.AbstractCompat;
import top.hendrixshen.magiclib.api.compat.minecraft.chat.network.ComponentCompat;

public class ComponentCompatImpl extends AbstractCompat<Component> implements ComponentCompat {
    public ComponentCompatImpl(@NotNull Component type) {
        super(type);
    }
}
