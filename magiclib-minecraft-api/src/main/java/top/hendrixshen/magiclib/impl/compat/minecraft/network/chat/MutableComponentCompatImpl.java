package top.hendrixshen.magiclib.impl.compat.minecraft.network.chat;

import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.api.compat.minecraft.network.chat.ComponentCompat;
import top.hendrixshen.magiclib.api.compat.minecraft.network.chat.MutableComponentCompat;
import top.hendrixshen.magiclib.api.compat.minecraft.network.chat.StyleCompat;


//#if MC > 11502
import net.minecraft.network.chat.MutableComponent;
//#else
//$$ import net.minecraft.network.chat.BaseComponent;
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
    public
    //#if MC > 11502
    @NotNull MutableComponent
    //#else
    //$$ BaseComponent
    //#endif
    get() {
        //#if MC > 11502
        return (MutableComponent) super.get();
        //#else
        //$$ return (BaseComponent) super.get();
        //#endif
    }

    @Override
    public void setStyle(@NotNull StyleCompat style) {
        this.get().setStyle(style.get());
    }

    @Override
    public MutableComponentCompat append(@NotNull ComponentCompat component) {
        this.get().append(component.get());
        return this;
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
