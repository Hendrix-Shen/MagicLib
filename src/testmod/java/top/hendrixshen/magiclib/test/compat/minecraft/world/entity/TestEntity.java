package top.hendrixshen.magiclib.test.compat.minecraft.world.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import top.hendrixshen.magiclib.compat.minecraft.network.chat.ComponentCompatApi;

public class TestEntity {
    public static void test() {
        Entity entity = new FallingBlockEntity(EntityType.FALLING_BLOCK, null);
        entity.getLevelCompat();
        entity.getBlockXCompat();
        entity.getBlockX();
        entity.getBlockYCompat();
        entity.getBlockY();
        entity.getBlockZCompat();
        entity.getBlockZ();
        entity.getYRotCompat();
        entity.getYRot();
        entity.setYRotCompat(0);
        entity.setYRot(0);
        entity.getXRotCompat();
        entity.getXRot();
        entity.setXRotCompat(0);
        entity.setXRot(0);
        entity.blockPositionCompat();
        entity.blockPosition();
        entity.isOnGroundCompat();
        entity.isOnGround();
        entity.setOnGroundCompat(true);
        entity.setOnGround(true);
        entity.sendSystemMessageCompat(ComponentCompatApi.literal("test"));
        entity.sendSystemMessage(ComponentCompatApi.literal("test"));
        entity.getXCompat();
        entity.getX();
        entity.getYCompat();
        entity.getY();
        entity.getZCompat();
        entity.getZ();
    }
}
