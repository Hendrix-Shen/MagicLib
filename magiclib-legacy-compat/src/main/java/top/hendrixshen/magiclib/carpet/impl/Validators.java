package top.hendrixshen.magiclib.carpet.impl;

import carpet.CarpetServer;
import com.google.common.collect.ImmutableList;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.carpet.api.Validator;
import top.hendrixshen.magiclib.compat.minecraft.api.network.chat.ComponentCompatApi;
import top.hendrixshen.magiclib.impl.carpet.CarpetEntrypoint;
import top.hendrixshen.magiclib.util.MessageUtil;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

//#if MC > 11900
//$$ @SuppressWarnings("removal")
//#endif
public class Validators {
    public static class Strict<T> extends Validator<T> {
        @Override
        public T getValidValue(CommandSourceStack source, @NotNull RuleOption ruleOption, T newValue, String userInput) {
            if (ruleOption.getOptions().contains(userInput)) {
                return newValue;
            }

            MessageUtil.sendMessage(source, ComponentCompatApi.literal(CarpetEntrypoint.getSettingManager()
                    .trValidator("strict.validValue", ruleOption.getOptions().toString())).withStyle(style -> style.withColor(ChatFormatting.RED)));
            return null;
        }
    }

    public static class StrictIgnoreCase<T> extends Validator<T> {
        @Override
        public T getValidValue(CommandSourceStack source, @NotNull RuleOption ruleOption, T newValue, @NotNull String userInput) {
            if (ruleOption.getOptions().stream().map(s -> s.toLowerCase(Locale.ROOT)).collect(Collectors.toList()).contains(userInput.toLowerCase(Locale.ROOT))) {
                return newValue;
            }

            MessageUtil.sendMessage(source, ComponentCompatApi.literal(CarpetEntrypoint.getSettingManager()
                    .trValidator("strictIgnoreCase.validValue", ruleOption.getOptions().toString())).withStyle(style -> style.withColor(ChatFormatting.RED)));
            return null;
        }
    }

    public static class Command extends Validator<String> {
        public static final List<String> FULL_OPTIONS = ImmutableList.of("true", "false", "ops", "0", "1", "2", "3", "4");
        public static final List<String> MINIMAL_OPTIONS = ImmutableList.of("true", "false", "ops");

        @Override
        public String getValidValue(CommandSourceStack source, RuleOption ruleOption, @NotNull String newValue, String userInput) {
            if (FULL_OPTIONS.contains(newValue.toLowerCase(Locale.ROOT))) {
                if (CarpetServer.settingsManager != null) {
                    CarpetServer.settingsManager.notifyPlayersCommandsChanged();
                }

                return newValue.toLowerCase(Locale.ROOT);
            }

            MessageUtil.sendMessage(source, ComponentCompatApi.literal(CarpetEntrypoint.getSettingManager()
                    .trValidator("command.validValue", ruleOption.getOptions().toString())).withStyle(style -> style.withColor(ChatFormatting.RED)));
            return null;
        }
    }

    public static class Numeric<T> extends Validator<T> {
        private final double max;
        private final double min;
        private final boolean canMaxEquals;
        private final boolean canMinEquals;

        public Numeric(double max, double min, boolean canMaxEquals, boolean canMinEquals) {
            this.max = Math.max(min, max);
            this.min = Math.min(min, max);
            this.canMaxEquals = canMaxEquals;
            this.canMinEquals = canMinEquals;
        }

        @Override
        public T getValidValue(CommandSourceStack source, RuleOption ruleOption, T newValue, String userInput) {
            if (!(newValue instanceof Number)) {
                throw new IllegalArgumentException("Illegal validator call.");
            }

            Number number = (Number) newValue;

            if ((this.canMaxEquals && number.doubleValue() > this.max) ||
                    (!this.canMaxEquals && number.doubleValue() >= this.max) ||
                    (this.canMinEquals && number.doubleValue() < this.min) ||
                    (!this.canMinEquals && number.doubleValue() <= this.min)) {
                MessageUtil.sendMessage(source, ComponentCompatApi.literal(CarpetEntrypoint.getSettingManager()
                        .trValidator("numeric.validValue", String.format("%s%s, %s%s",
                                this.canMinEquals ? "[" : "(", this.min, this.max, this.canMaxEquals ? "]" : ")")))
                        .withStyle(style -> style.withColor(ChatFormatting.RED)));
                return null;
            }

            return newValue;
        }
    }
}
