package top.hendrixshen.magiclib.api.compat.minecraft.resources;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.util.collect.Provider;

public interface ResourceLocationCompat extends Provider<ResourceLocation> {
    static @NotNull ResourceLocation fromNamespaceAndPath(String namespace, String path) {
        //#if MC > 12006
        //$$ return ResourceLocation.fromNamespaceAndPath(namespace, path);
        //#else
        return new ResourceLocation(namespace, path);
        //#endif
    }

    static @NotNull ResourceLocation withDefaultNamespace(String path) {
        //#if MC > 12006
        //$$ return ResourceLocation.withDefaultNamespace(path);
        //#else
        return new ResourceLocation(path);
        //#endif
    }
}
