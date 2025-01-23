package top.hendrixshen.magiclib.mixin.minecraft.accessor;

import net.minecraft.client.resources.LegacyPackResourcesAdapter;
import net.minecraft.server.packs.PackResources;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LegacyPackResourcesAdapter.class)
public interface LegacyPackResourcesAdapterAccessor {
    @Accessor("source")
    PackResources magiclib$getSource();
}
