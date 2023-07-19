package top.hendrixshen.magiclib;

public class SharedConstants {
    public static String MAGICLIB_VERSION_0_1 = "0.1";
    public static String MAGICLIB_VERSION_0_2 = "0.2";
    public static String MAGICLIB_VERSION_0_3 = "0.3";
    public static String MAGICLIB_VERSION_0_4 = "0.4";
    public static String MAGICLIB_VERSION_0_5 = "0.5";
    public static String MAGICLIB_VERSION_0_6 = "0.6";
    public static String MAGICLIB_VERSION_0_7 = "0.7"; // Current
    public static String MAGICLIB_VERSION_0_8 = "0.8"; // Future
    
    public static String getCurrentMajorVersion() {
        return SharedConstants.MAGICLIB_VERSION_0_7;
    }

    public static String getNextMajorVersion() {
        return SharedConstants.MAGICLIB_VERSION_0_8;
    }
}
