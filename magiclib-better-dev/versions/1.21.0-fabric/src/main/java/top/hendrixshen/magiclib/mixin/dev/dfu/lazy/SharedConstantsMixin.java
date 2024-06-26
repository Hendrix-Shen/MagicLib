package top.hendrixshen.magiclib.mixin.dev.dfu.lazy;

import com.mojang.datafixers.DSL;
import net.minecraft.client.main.Main;
import net.minecraft.util.datafix.DataFixers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Mixin(Main.class)
public class SharedConstantsMixin {
    @Redirect(method = "main", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/datafix/DataFixers;optimize(Ljava/util/Set;)Ljava/util/concurrent/CompletableFuture;"))
    private static CompletableFuture<?> onOptimize(Set<DSL.TypeReference> set) {
        return DataFixers.optimize(Set.of());
    }
}
