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

    @Shadow
    private Map<String, LanguageInfo> languages;

    @Shadow
    @Final
    private static LanguageInfo DEFAULT_LANGUAGE;

    @Inject(method = "<init>", at = @At(value = "RETURN"))
    private void postInit(String string, CallbackInfo ci) {
        MagicLanguageManager.INSTANCE.setCurrentCode(this.currentCode);
    }

    @Inject(method = "setSelected", at = @At(value = "RETURN"))
    private void postSetSelected(LanguageInfo languageInfo, CallbackInfo ci) {
        MagicLanguageManager.INSTANCE.setCurrentCode(this.currentCode);
    }

    @ModifyVariable(method = "onResourceManagerReload", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/resources/language/ClientLanguage;loadFrom(Lnet/minecraft/server/packs/resources/ResourceManager;Ljava/util/List;)Lnet/minecraft/client/resources/language/ClientLanguage;",
            ordinal = 0))
    private List<LanguageInfo> addFallbackLanguage(List<LanguageInfo> languageInfoList) {

        ArrayList<String> codes = new ArrayList<>(MagicLibConfigs.fallbackLanguageList);
        if (!codes.contains(currentCode)) {
            codes.add(0, currentCode);
        }

        if (!codes.contains(MagicLanguageManager.DEFAULT_CODE)) {
            codes.add(0, MagicLanguageManager.DEFAULT_CODE);
        }

        LanguageInfo defaultLanguageInfo = this.languages.getOrDefault("en_us", DEFAULT_LANGUAGE);
        Collections.reverse(codes);
        for (String code : codes) {
            if (languageInfoList.stream().noneMatch(languageInfo -> languageInfo.getCode().equals(code))) {
                languageInfoList.add(0, this.languages.getOrDefault(code, defaultLanguageInfo));
            }
        }

        return languageInfoList;
    }
}
