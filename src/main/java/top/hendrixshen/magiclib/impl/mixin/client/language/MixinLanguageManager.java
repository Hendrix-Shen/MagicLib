package top.hendrixshen.magiclib.impl.mixin.client.language;

import net.minecraft.client.resources.language.LanguageInfo;
import net.minecraft.client.resources.language.LanguageManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.hendrixshen.magiclib.MagicLibConfigs;
import top.hendrixshen.magiclib.language.MagicLanguageManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Mixin(LanguageManager.class)
public class MixinLanguageManager {
    @Shadow
    private String currentCode;

    //#if MC > 11502
    @Shadow
    private Map<String, LanguageInfo> languages;

    @Shadow
    @Final
    private static LanguageInfo DEFAULT_LANGUAGE;
    //#endif

    @Inject(method = "<init>", at = @At(value = "RETURN"))
    private void postInit(String string, CallbackInfo ci) {
        MagicLanguageManager.INSTANCE.setCurrentCode(this.currentCode);
    }

    @Inject(method = "setSelected", at = @At(value = "RETURN"))
    private void postSetSelected(LanguageInfo languageInfo, CallbackInfo ci) {
        MagicLanguageManager.INSTANCE.setCurrentCode(this.currentCode);
    }

    //#if MC > 11502
    @ModifyVariable(method = "onResourceManagerReload", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/resources/language/ClientLanguage;loadFrom(Lnet/minecraft/server/packs/resources/ResourceManager;Ljava/util/List;)Lnet/minecraft/client/resources/language/ClientLanguage;",
            ordinal = 0))
    private List<LanguageInfo> addFallbackLanguage(List<LanguageInfo> languageInfoList) {

        ArrayList<String> codes = new ArrayList<>(MagicLibConfigs.fallbackLanguageList);

        codes.remove(currentCode);
        codes.add(0, currentCode);

        if (!codes.contains(MagicLanguageManager.DEFAULT_CODE)) {
            codes.add(MagicLanguageManager.DEFAULT_CODE);
        }

        Collections.reverse(codes);
        languageInfoList.clear();
        for (String code : codes) {
            LanguageInfo languageInfo = this.languages.getOrDefault(code, null);
            if (languageInfo != null) {
                languageInfoList.add(languageInfo);
            }
        }
        if (languageInfoList.isEmpty()) {
            languageInfoList.add(DEFAULT_LANGUAGE);
        }

        return languageInfoList;
    }
    //#else
    //$$ @ModifyVariable(method = "onResourceManagerReload", at = @At(value = "INVOKE",
    //$$         target = "Lnet/minecraft/client/resources/language/Locale;loadFrom(Lnet/minecraft/server/packs/resources/ResourceManager;Ljava/util/List;)V",
    //$$         ordinal = 0))
    //$$ private List<String> addFallbackLanguage(List<String> languageCodeList) {
    //$$     ArrayList<String> codes = new ArrayList<>(MagicLibConfigs.fallbackLanguageList);
    //$$     codes.remove(currentCode);
    //$$     codes.add(0, currentCode);
    //$$     if (!codes.contains(MagicLanguageManager.DEFAULT_CODE)) {
    //$$         codes.add(MagicLanguageManager.DEFAULT_CODE);
    //$$     }
    //$$     return codes;
    //$$ }
    //#endif
}
