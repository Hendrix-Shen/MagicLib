package top.hendrixshen.magiclib;

import top.hendrixshen.magiclib.api.rule.annotation.Command;
import top.hendrixshen.magiclib.api.rule.annotation.Numeric;
import top.hendrixshen.magiclib.api.rule.annotation.Rule;

public class MagicLibSettings {
    @Rule(
            categories = "generic",
            options = {"en_us", "zh_cn"}
    )
    public static String language = "en_us";

    @Command(full = true)
    @Rule(categories = "generic")
    public static String settingManagerLevel = "ops";

    @Rule(categories = "example")
    public static boolean booleanTest = true;

    @Command(full = true)
    @Rule(categories = "example")
    public static String commandFullTest = "ops";

    @Command()
    @Rule(categories = "example")
    public static String commandLiteTest = "ops";

    @Numeric(minValue = 10, canMinEquals = true, maxValue = 200)
    @Rule(categories = "example")
    public static int intTest = 10;

    @Rule(categories = "example")
    public static EnumOption enumTest = EnumOption.OPTION1;

    public enum EnumOption {
        OPTION1,
        OPTION2
    }
}
