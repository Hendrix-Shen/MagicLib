package top.hendrixshen.magiclib.compat;

import org.spongepowered.asm.service.MixinService;
import top.hendrixshen.magiclib.MagicMixinPlugin;
import top.hendrixshen.magiclib.util.MixinUtil;

import java.io.IOException;

public class MagicCompatMixinPlugin extends MagicMixinPlugin {
    @Override
    public void onLoad(String mixinPackage) {
        try {
            MixinUtil.remapAndLoadClass(MixinService.getService().getBytecodeProvider()
                    .getClassNode("net.minecraft.client.renderer.MultiBufferSourceCompat"));
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
