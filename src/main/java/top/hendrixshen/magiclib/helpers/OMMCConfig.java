package top.hendrixshen.magiclib.helpers;

//import com.plusls.ommc.config.Configs;

import java.util.ArrayList;
import java.util.List;

public class OMMCConfig {
    public static List<String> getFallbackListFromOMMC() {
        //return Configs.Lists.FALLBACK_LANGUAGE_LIST.getStrings();
        return new ArrayList<>();
    }
}
