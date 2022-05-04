package top.hendrixshen.magiclib.test.compat;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.MixinEnvironment;
import top.hendrixshen.magiclib.MagicLibReference;
import top.hendrixshen.magiclib.language.I18n;
import top.hendrixshen.magiclib.test.compat.minecraft.TestUtil;
import top.hendrixshen.magiclib.test.compat.minecraft.blaze3d.vertex.TestBufferBuilder;
import top.hendrixshen.magiclib.test.compat.minecraft.blaze3d.vertex.TestVertexFormat;
import top.hendrixshen.magiclib.test.compat.minecraft.client.TestCamera;
import top.hendrixshen.magiclib.test.compat.minecraft.client.gui.TestScreen;
import top.hendrixshen.magiclib.test.compat.minecraft.client.renderer.entity.TestEntityRenderDispatcher;
import top.hendrixshen.magiclib.test.compat.minecraft.math.TestMatrix4f;
import top.hendrixshen.magiclib.test.compat.minecraft.math.TestQuaternion;
import top.hendrixshen.magiclib.test.compat.minecraft.math.TestVector3f;
import top.hendrixshen.magiclib.test.compat.minecraft.math.TestVector4f;
import top.hendrixshen.magiclib.test.compat.minecraft.network.chat.TestComponent;
import top.hendrixshen.magiclib.test.compat.minecraft.network.chat.TestStyle;
import top.hendrixshen.magiclib.test.compat.minecraft.world.TestSimpleContainer;
import top.hendrixshen.magiclib.test.compat.minecraft.world.entity.TestEntity;
import top.hendrixshen.magiclib.test.compat.minecraft.world.entity.player.TestPlayer;
import top.hendrixshen.magiclib.test.compat.minecraft.world.inventory.TestSlot;
import top.hendrixshen.magiclib.test.compat.minecraft.world.item.TestItemStack;
import top.hendrixshen.magiclib.test.compat.minecraft.world.level.TestLevel;
import top.hendrixshen.magiclib.test.compat.minecraft.world.level.block.entity.TestBlockEntity;
import top.hendrixshen.magiclib.test.compat.minecraft.world.level.block.state.TestBlockState;

public class CompatApiTests implements ModInitializer, ClientModInitializer, DedicatedServerModInitializer {
    private int ticks = 0;

    @Override
    public void onInitialize() {
        MagicLibReference.LOGGER.info("Started CompatApiTests");
        if (System.getProperty("magiclib.autoTest") != null) {
            ServerTickEvents.END_SERVER_TICK.register(server -> {
                if (ticks == 0) {
                    for (Level level : server.getAllLevels()) {
                        TestLevel.test(level);
                        TestPlayer.test(level);
                    }
                }
                ticks++;

                if (ticks == 50) {
                    MixinEnvironment.getCurrentEnvironment().audit();
                    server.halt(false);
                }
            });
        }
        TestBlockEntity.test();
        TestBlockState.test();
        TestComponent.test();
        TestEntity.test();
        TestItemStack.test();
        TestSimpleContainer.test();
        TestSlot.test();
        TestStyle.test(false);
        TestUtil.test();
        MagicLibReference.LOGGER.info("test i18n: {}", I18n.get("magiclib.gui.button.tab.test"));
        MagicLibReference.LOGGER.info("test i18n cn: {}", I18n.getByCode("zh_cn", "magiclib.gui.button.tab.test"));
    }

    @Override
    public void onInitializeClient() {
        TestBufferBuilder.test();
        TestCamera.test();
        //#if MC > 11404
        // TODO make it works in 1.14.4
        TestEntityRenderDispatcher.test();
        //#endif
        TestMatrix4f.test();
        TestQuaternion.test();
        TestScreen.test();
        TestStyle.test(true);
        TestVector3f.test();
        TestVector4f.test();
        TestVertexFormat.test();
    }

    @Override
    public void onInitializeServer() {
    }
}