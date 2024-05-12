package top.hendrixshen.magiclib.impl.compat.minecraft.network.chat;

import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.api.compat.minecraft.network.chat.MutableComponentCompat;
import top.hendrixshen.magiclib.api.compat.minecraft.network.chat.StyleCompat;

//#if MC < 11900
import net.minecraft.network.chat.BaseComponent;
//#endif

//#if MC > 11502
import net.minecraft.network.chat.MutableComponent;
//#endif

public class MutableComponentCompatImpl extends ComponentCompatImpl implements MutableComponentCompat {
    public MutableComponentCompatImpl(
            //#if MC > 11502
            @NotNull MutableComponent type
            //#else
            //$$ @NotNull BaseComponent type
            //#endif
    ) {
        super(type);
    }

    @Override
    public void setStyle(@NotNull StyleCompat style) {
        //#if MC > 11502
        ((MutableComponent) this.get()).setStyle(style.get());
        //#else
        //$$ this.get().setStyle(style.get());
        //#endif
    }

    @Override
    public MutableComponentCompat append(@NotNull MutableComponentCompat component) {
        //#if MC > 11502
        ((MutableComponent) this.get()).append(component.get());
        //#else
        //$$ this.get().append(component.get());
        //#endif
        return this;
    }

    @Override
    public MutableComponentCompat withStyle(@NotNull StyleCompat style) {
        //#if MC > 11802
        //$$ ((MutableComponent) this.get()).withStyle(style.get());
        //#elseif MC > 11502
        ((BaseComponent) this.get()).withStyle(style.get());
        //#else
        //$$ this.get().setStyle(style.get());
        //#endif
        return this;
    }
}
