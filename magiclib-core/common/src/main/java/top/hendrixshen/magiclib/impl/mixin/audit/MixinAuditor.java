package top.hendrixshen.magiclib.impl.mixin.audit;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.MixinEnvironment;
import top.hendrixshen.magiclib.MagicLib;

import java.util.concurrent.atomic.AtomicBoolean;

public class MixinAuditor {
    public static String ENABLE = "magiclib.mixinAuditor.enable";
    public static String EXIT_MODE = "magiclib.mixinAuditor.exitMode";
    public static String FAIL_CODE = "magiclib.mixinAuditor.failCode";
    public static String TRIGGER = "magiclib.mixinAuditor.trigger";
    public static int DEFAULT_FAIL_CODE = -0x4D454F57; // MEOW
    public static AtomicBoolean triggered = new AtomicBoolean(false);

    public static boolean isEnable() {
        return "true".equalsIgnoreCase(System.getProperty(MixinAuditor.ENABLE));
    }

    public static void trigger(@NotNull String trigger) {
        if (trigger.equalsIgnoreCase(System.getProperty(MixinAuditor.TRIGGER)) &&
                MixinAuditor.triggered.compareAndSet(false, true)) {
            MixinAuditor.runMixinAudit();
        }
    }

    public static int getFailCode() {
        String code = System.getProperty(MixinAuditor.FAIL_CODE, "");

        try {
            return Integer.parseInt(code);
        } catch (NumberFormatException e) {
            return MixinAuditor.DEFAULT_FAIL_CODE;
        }
    }

    public static void runMixinAudit() {
        if (!MixinAuditor.isEnable()) {
            return;
        }

        MagicLib.getLogger().info("Triggered mixin audit.");
        boolean status = MixinAuditor.runMixinAuditImpl();
        boolean exit;

        switch (ExitMode.get()) {
            case ALWAYS:
                exit = true;
                break;
            case FAILED:
                exit = !status;
                break;
            case NEVER:
                exit = false;
                break;
            default:
                throw new IllegalStateException();

        }

        if (exit) {
            // Bypass shutdown hook.
            Runtime.getRuntime().halt(status ? 0 : MixinAuditor.getFailCode());
        }
    }

    public static boolean runMixinAuditImpl() {
        try {
            MixinEnvironment.getCurrentEnvironment().audit();
        } catch (Throwable t) {
            MagicLib.getLogger().error("Exception occurs while auditing mixin.", t);
            return false;
        }

        MagicLib.getLogger().info("Mixin audited successfully!");
        return true;
    }

    public enum ExitMode {
        ALWAYS,
        FAILED,
        NEVER;

        public static ExitMode DEFAULT = ExitMode.ALWAYS;

        public static ExitMode get() {
            String mode = System.getProperty(MixinAuditor.EXIT_MODE, "");

            try {
                return ExitMode.valueOf(mode.toUpperCase());
            } catch (IllegalArgumentException e) {
                return ExitMode.DEFAULT;
            }
        }
    }
}
