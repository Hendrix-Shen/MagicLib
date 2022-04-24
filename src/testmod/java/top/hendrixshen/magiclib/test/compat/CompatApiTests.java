package top.hendrixshen.magiclib.test.compat;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.MixinEnvironment;
import top.hendrixshen.magiclib.MagicLibReference;
import top.hendrixshen.magiclib.test.compat.minecraft.TestUtil;
import top.hendrixshen.magiclib.test.compat.minecraft.blaze3d.vertex.TestVertexFormat;
import top.hendrixshen.magiclib.test.compat.minecraft.client.gui.TestScreen;
import top.hendrixshen.magiclib.test.compat.minecraft.network.chat.TestComponent;
import top.hendrixshen.magiclib.test.compat.minecraft.network.chat.TestStyle;
import top.hendrixshen.magiclib.test.compat.minecraft.world.TestSimpleContainer;
import top.hendrixshen.magiclib.test.compat.minecraft.world.entity.TestEntity;
import top.hendrixshen.magiclib.test.compat.minecraft.world.entity.player.TestPlayer;
import top.hendrixshen.magiclib.test.compat.minecraft.world.item.TestItemStack;
import top.hendrixshen.magiclib.test.compat.minecraft.world.level.TestLevel;
import top.hendrixshen.magiclib.test.compat.minecraft.world.level.block.entity.TestBlockEntity;

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
        TestComponent.test();
        TestEntity.test();
        TestItemStack.test();
        TestSimpleContainer.test();
        TestStyle.test(false);
        TestUtil.test();
    }

    @Override
    public void onInitializeClient() {
        TestScreen.test();
        TestStyle.test(true);
        TestVertexFormat.test();
    }

    @Override
    public void onInitializeServer() {
    }
}