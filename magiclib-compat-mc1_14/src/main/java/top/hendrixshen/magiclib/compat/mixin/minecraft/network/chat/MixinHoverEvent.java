package top.hendrixshen.magiclib.compat.mixin.minecraft.network.chat;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.HoverEventCompat;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import top.hendrixshen.magiclib.compat.annotation.InitMethod;
import top.hendrixshen.magiclib.compat.annotation.ThisInitMethod;

@Mixin(HoverEvent.class)
public class MixinHoverEvent {
    private static HoverEvent.Action getLegacyAction(HoverEventCompat.Action action) {
        switch (action.action) {
            case HoverEventCompat.Action_SHOW_TEXT:
                return HoverEvent.Action.SHOW_TEXT;
            case HoverEventCompat.Action_SHOW_ITEM:
                return HoverEvent.Action.SHOW_ITEM;
            case HoverEventCompat.Action_SHOW_ENTITY:
                return HoverEvent.Action.SHOW_ENTITY;
        }
        throw new RuntimeException("wtf");
    }

    private static Component getLegacyComponent(HoverEventCompat.Action action, Object object) {
        switch (action.action) {
            case HoverEventCompat.Action_SHOW_TEXT:
                return (Component) object;
            case HoverEventCompat.Action_SHOW_ITEM: {
                ItemStack itemStack = ((HoverEventCompat.ItemStackInfo) object).itemStack;
                CompoundTag compoundTag = itemStack.save(new CompoundTag());
                return new TextComponent(compoundTag.toString());
            }
            case HoverEventCompat.Action_SHOW_ENTITY: {
                HoverEventCompat.EntityTooltipInfo entityTooltipInfo = (HoverEventCompat.EntityTooltipInfo) object;
                CompoundTag compoundTag = new CompoundTag();
                ResourceLocation resourceLocation = EntityType.getKey(entityTooltipInfo.type);
                compoundTag.putString("id", entityTooltipInfo.id.toString());
                if (resourceLocation != null) {
                    compoundTag.putString("type", resourceLocation.toString());
                }
                compoundTag.putString("name", Component.Serializer.toJson(entityTooltipInfo.name));
                return new TextComponent(compoundTag.toString());
            }
        }
        throw new RuntimeException("wtf");
    }

    @InitMethod
    public void magicInit(HoverEventCompat.Action action, Object object) {
        magicThisInit(getLegacyAction(action), getLegacyComponent(action, object));
    }

    @ThisInitMethod
    public void magicThisInit(HoverEvent.Action ignoredAction, Component ignoredComponent) {
    }

}
