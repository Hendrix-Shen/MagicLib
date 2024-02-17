//#if FORGE
//$$ package top.hendrixshen.magiclib.entrypoint.minecraft;
//$$
//$$ import net.minecraftforge.fml.common.Mod;
//$$ import top.hendrixshen.magiclib.MagicLib;
//$$ import top.hendrixshen.magiclib.api.event.minecraft.LanguageSelectListener;
//$$ import top.hendrixshen.magiclib.api.platform.DistType;
//$$ import top.hendrixshen.magiclib.impl.i18n.minecraft.MinecraftLanguageManager;
//$$
//$$ @Mod("magiclib_minecraft_api")
//$$ public class MagicLibForge {
//$$     public MagicLibForge() {
//$$         if (MagicLib.getInstance().getPlatformManage().getCurrentPlatform().matchesDist(DistType.CLIENT)) {
//$$             MagicLib.getInstance().getEventManager().register(LanguageSelectListener.class,
//$$                     MinecraftLanguageManager.getInstance());
//$$         }
//$$     }
//$$ }
//#endif
