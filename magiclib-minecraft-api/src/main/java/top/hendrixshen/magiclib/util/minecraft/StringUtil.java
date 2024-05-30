package top.hendrixshen.magiclib.util.minecraft;

import java.text.NumberFormat;

public class StringUtil {
    public static String fractionDigit(double value, int digit) {
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMinimumFractionDigits(digit);
        nf.setMaximumFractionDigits(digit);
        return nf.format(value);
    }
}
