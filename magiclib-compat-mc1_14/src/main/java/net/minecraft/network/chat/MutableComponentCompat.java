package net.minecraft.network.chat;

import net.minecraft.ChatFormatting;
import top.hendrixshen.magiclib.compat.annotation.Remap;

@Remap("net/minecraft/class_5250")
public interface MutableComponentCompat extends Component {

    @Remap("method_27693")
    MutableComponentCompat remap$append(String string);

    @Remap("method_10852")
    MutableComponentCompat remap$append(Component component);

    @Remap("method_27696")
    MutableComponentCompat withStyle(Style style);

    @Remap("method_27692")
    MutableComponentCompat withStyle(ChatFormatting chatFormatting);

    @Remap("method_27695")
    MutableComponentCompat withStyle(ChatFormatting[] chatFormatting);
}
