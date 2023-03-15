package top.hendrixshen.magiclib.compat.minecraft.mixin.network.chat;

//#if MC > 11502 && MC <= 11605
//$$ import net.minecraft.network.chat.ClickEvent;
//$$ import net.minecraft.network.chat.HoverEvent;
//$$ import net.minecraft.network.chat.TextColor;
//$$ import net.minecraft.resources.ResourceLocation;
//$$ import org.jetbrains.annotations.Nullable;
//$$ import org.spongepowered.asm.mixin.gen.Invoker;
//#endif
import net.minecraft.network.chat.Style;
import org.spongepowered.asm.mixin.Mixin;

//Internal api
@Mixin(Style.class)
public interface AccessorStyle {
    //#if MC > 11502 && MC <= 11605
    //$$ @Invoker(value = "<init>")
    //$$ static Style invokeConstructor(@Nullable TextColor color, @Nullable Boolean bold, @Nullable Boolean italic,
    //$$                                @Nullable Boolean underlined, @Nullable Boolean strikethrough, @Nullable Boolean obfuscated,
    //$$                                @Nullable ClickEvent clickEvent, @Nullable HoverEvent hoverEvent, @Nullable String insertion,
    //$$                                @Nullable ResourceLocation font) {
    //$$     throw new UnsupportedOperationException();
    //$$ }
    //#endif
}
