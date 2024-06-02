package top.hendrixshen.magiclib.impl.carpet;

import top.hendrixshen.magiclib.carpet.api.annotation.Command;
import top.hendrixshen.magiclib.carpet.api.annotation.Numeric;
import top.hendrixshen.magiclib.carpet.api.annotation.Rule;
import top.hendrixshen.magiclib.dependency.api.annotation.Dependencies;
import top.hendrixshen.magiclib.dependency.api.annotation.Dependency;
import top.hendrixshen.magiclib.dependency.impl.RuleDependencyPredicates;

public class MagicLibSettings {
    @Rule(
            categories = "generic"
    )
    public static boolean debug = false;

    @Deprecated
    @Rule(
            categories = "generic",
            options = {"en_us", "zh_cn"}
    )
    public static String language = "en_us";

    @Command(full = true)
    @Rule(categories = "generic")
    public static String settingManagerLevel = "ops";

    @Rule(categories = "debug", predicate = RuleDependencyPredicates.DebugRulePredicate.class)
    public static boolean booleanTest = true;

    @Rule(
            categories = "debug",
            dependencies = @Dependencies(
                    and = {
                            @Dependency("carpet-extra")
                    }
            ),
            predicate = RuleDependencyPredicates.DebugRulePredicate.class
    )
    public static boolean carpetExtraTest = true;

    @Command(full = true)
    @Rule(categories = "debug", predicate = RuleDependencyPredicates.DebugRulePredicate.class)
    public static String commandFullTest = "ops";

    @Command()
    @Rule(categories = "debug", predicate = RuleDependencyPredicates.DebugRulePredicate.class)
    public static String commandLiteTest = "ops";

    @Numeric(minValue = 10, canMinEquals = true, maxValue = 200)
    @Rule(categories = "debug", predicate = RuleDependencyPredicates.DebugRulePredicate.class)
    public static int intTest = 10;

    @Rule(categories = "debug", predicate = RuleDependencyPredicates.DebugRulePredicate.class)
    public static EnumOption enumTest = EnumOption.OPTION1;

    public enum EnumOption {
        OPTION1,
        OPTION2
    }
}
