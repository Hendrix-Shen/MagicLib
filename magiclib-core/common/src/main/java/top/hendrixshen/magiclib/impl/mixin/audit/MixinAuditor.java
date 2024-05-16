package top.hendrixshen.magiclib.impl.mixin.audit;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.MixinEnvironment;
import top.hendrixshen.magiclib.MagicLib;
import top.hendrixshen.magiclib.MagicLibProperties;

import java.util.concurrent.atomic.AtomicBoolean;

public class MixinAuditor {
    public static final int DEFAULT_FAIL_CODE = -0x4D454F57; // MEOW
    public static final AtomicBoolean triggered = new AtomicBoolean(false);

    public static boolean isEnable() {
        return MagicLibProperties.MIXIN_AUDITOR_ENABLE.getBooleanValue();
    }

    public static void trigger(@NotNull String trigger) {
        if (trigger.equalsIgnoreCase(MagicLibProperties.MIXIN_AUDITOR_TRIGGER.getStringValue()) &&
                MixinAuditor.triggered.compareAndSet(false, true)) {
            MixinAuditor.runMixinAudit();
        }
    }

    public static int getFailCode() {
        try {
            return Integer.parseInt(MagicLibProperties.MIXIN_AUDITOR_FAIL_CODE.getStringValue());
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

        public static final ExitMode DEFAULT = ExitMode.ALWAYS;

        public static ExitMode get() {
            return MagicLibProperties.MIXIN_AUDITOR_EXIT_MODE.getEnumValue(ExitMode.DEFAULT);
        }
    }
}
