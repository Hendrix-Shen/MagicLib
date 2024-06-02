//#if FORGE
//$$ package top.hendrixshen.magiclib.entrypoint.minecraft;
//$$
//$$ import net.minecraftforge.fml.common.Mod;
//$$ import top.hendrixshen.magiclib.api.entrypoint.ModInitializer;
//$$ import top.hendrixshen.magiclib.impl.i18n.minecraft.MinecraftLanguageManager;
//$$ import top.hendrixshen.magiclib.impl.minecraft.MagicLibMinecraft;
//$$ import top.hendrixshen.magiclib.impl.mixin.audit.minecraft.MinecraftMixinAudit;
//$$
//$$ @Mod("@MOD_IDENTIFIER@")
//$$ public class MagicLibForge implements ModInitializer {
//$$     public MagicLibForge() {
//$$         this.construct();
//$$     }
//$$
//$$     @Override
//$$     public void onInitializeClient() {
//$$         MinecraftLanguageManager.init();
//$$     }
//$$
//$$     @Override
//$$     public void onInitializeServer() {
//$$     }
//$$
//$$     @Override
//$$     public void onInitialize() {
//$$         MinecraftMixinAudit.init();
//$$         MagicLibMinecraft.init();
//$$     }
//$$ }
//#endif
