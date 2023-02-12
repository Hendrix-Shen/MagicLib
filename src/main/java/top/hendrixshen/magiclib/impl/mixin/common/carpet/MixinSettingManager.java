package top.hendrixshen.magiclib.impl.mixin.common.carpet;

//#if MC > 11802
import carpet.api.settings.SettingsManager;
//#else
//$$ import carpet.settings.SettingsManager;
//#if MC <= 11502
//$$ import com.mojang.brigadier.CommandDispatcher;
//$$ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//#endif
//#endif
import net.minecraft.commands.CommandSourceStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.hendrixshen.magiclib.api.rule.WrapperSettingManager;

@Mixin(value = SettingsManager.class, remap = false)
public class MixinSettingManager {
    //#if MC <= 11502
    //$$ @SuppressWarnings("ConstantConditions")
    //$$ @Inject(
    //$$         method = "registerCommand",
    //$$         at = @At(
    //$$                 value = "HEAD"
    //$$         )
    //$$ )
    //$$ private void registerCommand(CommandDispatcher<CommandSourceStack> dispatcher, CallbackInfo ci) {
    //$$     if (((SettingsManager) (Object)this) instanceof WrapperSettingManager) {
    //$$         ((WrapperSettingManager) (Object)this).registerCommandCompat(dispatcher);
    //$$     }
    //$$ }
    //#endif

    @Inject(
            method = "listAllSettings",
            at = @At(
                    value = "INVOKE",
                    //#if MC > 11802
                    target = "Lcarpet/api/settings/SettingsManager;getCategories()Ljava/lang/Iterable;"
                    //#else
                    //$$ target = "Lcarpet/settings/SettingsManager;getCategories()Ljava/lang/Iterable;"
                    //#endif
            )
    )
    private void printAdditionVersion(CommandSourceStack source, CallbackInfoReturnable<Integer> cir) {
        WrapperSettingManager.printAllExtensionVersion(source);
    }
}
