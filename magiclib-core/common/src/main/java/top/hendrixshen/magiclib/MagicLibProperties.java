package top.hendrixshen.magiclib;

import lombok.Getter;
import org.jetbrains.annotations.ApiStatus;
import top.hendrixshen.magiclib.impl.mixin.audit.MixinAuditor;
import top.hendrixshen.magiclib.util.SystemUtil;
import top.hendrixshen.magiclib.util.SystemUtil.Option;

import java.util.Comparator;

@Getter
public final class MagicLibProperties {
    public static final Option ROOT = Option.newOption("magiclib");
    public static final Option DEBUG = Option.newOption(MagicLibProperties.ROOT, Option.InheritType.INDEPENDENT,
            "debug");
    public static final Option MIXIN_AUDITOR = Option.newOption(MagicLibProperties.DEBUG,
            Option.InheritType.INDEPENDENT, "mixinAuditor");
    public static final Option MIXIN_AUDITOR_ENABLE = Option.newOption(MagicLibProperties.MIXIN_AUDITOR,
            Option.InheritType.ALLOW_OVERRIDE, "enable");
    public static final Option MIXIN_AUDITOR_EXIT_MODE = Option.newOption(MagicLibProperties.MIXIN_AUDITOR,
            Option.InheritType.INDEPENDENT, "exitMode", String.valueOf(MixinAuditor.ExitMode.DEFAULT));
    public static final Option MIXIN_AUDITOR_FAIL_CODE = Option.newOption(MagicLibProperties.MIXIN_AUDITOR,
            Option.InheritType.INDEPENDENT, "failCode", String.valueOf(MixinAuditor.DEFAULT_FAIL_CODE));
    public static final Option MIXIN_AUDITOR_TRIGGER = Option.newOption(MagicLibProperties.MIXIN_AUDITOR,
            Option.InheritType.INDEPENDENT, "trigger", "mod_init");
    public static final Option DEV_QOL = Option.newOption(MagicLibProperties.ROOT,
            Option.InheritType.INDEPENDENT, "devQOL");
    public static final Option DEV_QOL_AUTH = Option.newOption(MagicLibProperties.DEV_QOL,
            Option.InheritType.INHERIT, "auth");
    public static final Option DEV_QOL_AUTH_EMPTY_KEY = Option.newOption(MagicLibProperties.DEV_QOL_AUTH,
            Option.InheritType.INHERIT, "emptyKey");
    public static final Option DEV_QOL_AUTH_SILENT_VERIFY_ERROR = Option.newOption(MagicLibProperties.DEV_QOL_AUTH,
            Option.InheritType.INHERIT, "silentVerify");
    public static final Option DEV_QOL_CHUNK = Option.newOption(MagicLibProperties.DEV_QOL,
            Option.InheritType.INHERIT, "chunk");
    public static final Option DEV_QOL_DFU = Option.newOption(MagicLibProperties.DEV_QOL,
            Option.InheritType.INHERIT, "dfu");
    public static final Option DEV_QOL_DFU_LAZY = Option.newOption(MagicLibProperties.DEV_QOL_DFU,
            Option.InheritType.INHERIT, "lazy");
    public static final Option DEV_QOL_DFU_BREAK = Option.newOption(MagicLibProperties.DEV_QOL_DFU,
            Option.InheritType.INDEPENDENT, "destroy");

    @ApiStatus.Internal
    public static void printDetail() {
        if (!MagicLibProperties.DEBUG.getBooleanValue()) {
            return;
        }

        MagicLib.getLogger().warn("MagicLib properties stats: ");
        SystemUtil.getProperties().stream()
                .sorted(Comparator.comparing(Option::getProperty))
                .forEach(option -> {
                    StringBuilder indent = new StringBuilder();

                    for (int i = 0; i < option.getDepth(); i++) {
                        indent.append("- ");
                    }

                    MagicLib.getLogger().warn("{}{}: {}", indent.toString(),
                            option.getProperty(), option);
                });
    }
}
