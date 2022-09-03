package top.hendrixshen.magiclib.impl.mixin.common.carpet;

//#if MC > 11802
import carpet.api.settings.SettingsManager;
//#else
//$$ import carpet.settings.SettingsManager;
//#if MC <= 11502
//$$ import carpet.settings.SettingsManager;
//$$ import com.mojang.brigadier.CommandDispatcher;
//$$ import net.minecraft.commands.CommandSourceStack;
//$$ import org.spongepowered.asm.mixin.injection.At;
//$$ import org.spongepowered.asm.mixin.injection.Inject;
//$$ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//$$ import top.hendrixshen.magiclib.api.rule.WrapperSettingManager;
//#endif
//#endif
import org.spongepowered.asm.mixin.Mixin;


@Mixin(SettingsManager.class)
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
}
