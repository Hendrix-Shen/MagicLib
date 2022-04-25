package top.hendrixshen.magiclib.test.compat.minecraft.client.renderer.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.FallingBlockEntity;

@Environment(EnvType.CLIENT)
public class TestEntityRenderDispatcher {
    public static void test() {
        //#if MC > 11404
        EntityRenderDispatcher entityRenderDispatcher = new EntityRenderDispatcher(null, null, null, null, null);
        //#else
        //$$ EntityRenderDispatcher entityRenderDispatcher = new EntityRenderDispatcher(null, null, null);
        //#endif
        entityRenderDispatcher.camera = new Camera();
        Entity entity = new FallingBlockEntity(EntityType.FALLING_BLOCK, null);
        entityRenderDispatcher.distanceToSqrCompat(entity);
        entityRenderDispatcher.distanceToSqr(entity);
        entityRenderDispatcher.cameraOrientationCompat();
        entityRenderDispatcher.cameraOrientation();
    }
}
