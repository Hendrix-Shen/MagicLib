package net.minecraft.network.chat;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import top.hendrixshen.magiclib.compat.annotation.Remap;

import java.util.UUID;

public class HoverEventCompat {

    public static final String Action_SHOW_TEXT = "SHOW_TEXT";

    public static final String Action_SHOW_ITEM = "SHOW_ITEM";
    public static final String Action_SHOW_ENTITY = "SHOW_ENTITY";


    @Remap("net/minecraft/class_2568$class_5247")
    public static class Action {

        @Remap("field_24342")
        public static final Action SHOW_TEXT = new Action(Action_SHOW_TEXT);
        @Remap("field_24343")
        public static final Action SHOW_ITEM = new Action(Action_SHOW_ITEM);
        @Remap("field_24344")
        public static final Action SHOW_ENTITY = new Action(Action_SHOW_ENTITY);
        public final String action;

        public Action(String action) {
            this.action = action;
        }

    }

    @Remap("net/minecraft/class_2568$class_5249")
    public static class ItemStackInfo {
        public final ItemStack itemStack;

        public ItemStackInfo(ItemStack itemStack) {
            this.itemStack = itemStack;
        }
    }

    @Remap("net/minecraft/class_2568$class_5248")
    public static class EntityTooltipInfo {

        public final EntityType<?> type;
        public final UUID id;
        public final Component name;

        public EntityTooltipInfo(EntityType<?> entityType, UUID uUID, @Nullable Component component) {
            this.type = entityType;
            this.id = uUID;
            this.name = component;
        }
    }
}
