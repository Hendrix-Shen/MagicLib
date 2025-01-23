package top.hendrixshen.magiclib.mixin.minecraft.accessor;

import net.minecraft.client.resources.PackResourcesAdapterV4;
import net.minecraft.server.packs.PackResources;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PackResourcesAdapterV4.class)
public interface PackResourcesAdapterV4Accessor {
    @Accessor("pack")
    PackResources magiclib$getPack();
}
