//#if NEO_FORGE
//$$ package top.hendrixshen.magiclib.entrypoint.minecraft;
//$$
//$$ import net.neoforged.fml.common.Mod;
//$$ import top.hendrixshen.magiclib.MagicLib;
//$$ import top.hendrixshen.magiclib.api.platform.DistType;
//$$ import top.hendrixshen.magiclib.impl.i18n.minecraft.MinecraftLanguageManager;
//$$ import top.hendrixshen.magiclib.impl.mixin.audit.minecraft.MinecraftMixinAudit;
//$$
//$$ @Mod("magiclib_minecraft_api")
//$$ public class MagicLibNeoForge {
//$$     public MagicLibNeoForge() {
//$$         this.onInitialize();
//$$
//$$         if (MagicLib.getInstance().getPlatformManage().getCurrentPlatform().matchesDist(DistType.CLIENT)) {
//$$             this.onInitializeClient();
//$$         } else if (MagicLib.getInstance().getPlatformManage().getCurrentPlatform().matchesDist(DistType.SERVER)) {
//$$             this.onInitializeServer();
//$$         }
//$$     }
//$$
//$$     public void onInitializeClient() {
//$$         MinecraftLanguageManager.init();
//$$     }
//$$
//$$     public void onInitializeServer() {
//$$     }
//$$
//$$     public void onInitialize() {
//$$         MinecraftMixinAudit.init();
//$$     }
//$$ }
//#endif
