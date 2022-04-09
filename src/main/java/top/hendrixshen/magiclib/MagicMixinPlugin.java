package top.hendrixshen.magiclib;

import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.transformer.ext.Extensions;
import top.hendrixshen.magiclib.compat.MagicExtension;
import top.hendrixshen.magiclib.util.FabricUtil;

import java.lang.reflect.Field;

public class MagicMixinPlugin extends EmptyMixinPlugin {


    private static boolean compatVersionChecked = false;


    @Override
    public void onLoad(String mixinPackage) {
        if (!compatVersionChecked) {
            compatVersionChecked = true;
            FabricUtil.compatVersionCheck();

            try {
                Object transformer = MixinEnvironment.getCurrentEnvironment().getActiveTransformer();
                if (transformer == null) {
                    throw new IllegalStateException("Not running with a transformer?");
                }
                Field extensionsField = transformer.getClass().getDeclaredField("extensions");
                extensionsField.setAccessible(true);
                Extensions extensions = (Extensions) extensionsField.get(transformer);
                extensions.add(new MagicExtension());
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
