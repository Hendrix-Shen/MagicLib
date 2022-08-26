package top.hendrixshen.magiclib;

import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.hendrixshen.magiclib.util.VersionParser;

public class MagicLibReference {
    private static final String MOD_ID = "magiclib";

    //#if MC > 11802
    public static final String CURRENT_MOD_ID = MOD_ID + "-1_19_2";
    //#elseif MC > 11701
    //$$ public static final String CURRENT_MOD_ID = MOD_ID + "-1_18_2";
    //#elseif MC > 11605
    //$$ public static final String CURRENT_MOD_ID = MOD_ID + "-1_17_1";
    //#elseif MC > 11502
    //$$ public static final String CURRENT_MOD_ID = MOD_ID + "-1_16_5";
    //#elseif MC > 11404
    //$$ public static final String CURRENT_MOD_ID = MOD_ID + "-1_15_2";
    //#else
    //$$ public static final String CURRENT_MOD_ID = MOD_ID + "-1_14_4";
    //#endif

    private static final String MOD_NAME = FabricLoader.getInstance().getModContainer(CURRENT_MOD_ID)
            .orElseThrow(RuntimeException::new).getMetadata().getName();
    private static final String MOD_VERSION = FabricLoader.getInstance().getModContainer(CURRENT_MOD_ID)
            .orElseThrow(RuntimeException::new).getMetadata().getVersion().getFriendlyString();
    private static final String MOD_VERSION_TYPE = VersionParser.getVersionType(MOD_VERSION);

    public static Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static String getModId() {
        return MOD_ID;
    }

    public static String getModName() {
        return MOD_NAME;
    }

    public static String getModVersion() {
        return MOD_VERSION;
    }

    public static String getModVersionType() {
        return MOD_VERSION_TYPE;
    }
}
