package top.hendrixshen.magiclib.api.compat.minecraft.chat.network;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.impl.compat.minecraft.chat.network.*;

//#if MC > 11502
//$$ import net.minecraft.network.chat.MutableComponent;
//#else
import net.minecraft.network.chat.BaseComponent;
//#endif

public interface MutableComponentCompat extends ComponentCompat {
    @Contract("_ -> new")
    static @NotNull MutableComponentCompat of(
            //#if MC > 11502
            //$$ MutableComponent
            //#else
            BaseComponent
                    //#endif
                    mutableComponent) {
        return new MutableComponentCompatImpl(mutableComponent);
    }

    MutableComponentCompat withStyle(StyleCompat style);
}
