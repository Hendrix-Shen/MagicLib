package top.hendrixshen.magiclib.util;

public class IntegerUtil {
    public static int parseIntegerWithBound(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            if (str.startsWith("-")) {
                return Integer.MIN_VALUE;
            } else {
                return Integer.MAX_VALUE;
            }
        }
    }
}
