package top.hendrixshen.magiclib.api.rule;

import carpet.CarpetServer;
import carpet.settings.ParsedRule;
import com.google.common.collect.ImmutableList;
import net.minecraft.commands.CommandSourceStack;
import top.hendrixshen.magiclib.language.I18n;
import top.hendrixshen.magiclib.util.MessageUtil;

import java.util.List;
import java.util.Locale;

//TODO Auto i18n
public class Validators {
    public static class Strict<T> extends Validator<T> {
        @Override
        public T validateCompat(CommandSourceStack source, ParsedRule<T> changingRule, T newValue, String userInput) {
            if (!changingRule.options.contains(userInput)) {
                MessageUtil.sendMessage(source, I18n.get("magiclib.validator.strict.failure", changingRule.options.toString()));
                return null;
            }

            return newValue;
        }
    }

    public static class Command extends Validator<String> {
        public static final List<String> FULL_OPTIONS = ImmutableList.of("true", "false", "ops", "0", "1", "2", "3", "4");
        public static final List<String> MINIMAL_OPTIONS = ImmutableList.of("true", "false", "ops");

        @Override
        public String validateCompat(CommandSourceStack source, ParsedRule<String> changingRule, String newValue, String userInput) {
            return !FULL_OPTIONS.contains(newValue.toLowerCase(Locale.ROOT)) ? null : newValue.toLowerCase(Locale.ROOT);
        }

        @Override
        public String description() {
            return I18n.get("magiclib.validator.command.description");
        }
    }

    public static class CommandUpdate<T> extends Validator<T> {
        @Override
        public T validateCompat(CommandSourceStack commandSourceStack, ParsedRule<T> carpetRule, T newValue, String userInput) {
            if (CarpetServer.settingsManager != null) {
                CarpetServer.settingsManager.notifyPlayersCommandsChanged();
            }

            return newValue;
        }
    }

    public static class Numeric<T> extends Validator<T> {
        private final double max;
        private final double min;
        private final boolean canMaxEquals;
        private final boolean canMinEquals;

        public Numeric(double max, double min, boolean canMaxEquals, boolean canMinEquals) {
            this.max = max;
            this.min = min;
            this.canMaxEquals = canMaxEquals;
            this.canMinEquals = canMinEquals;
        }

        @Override
        public T validateCompat(CommandSourceStack source, ParsedRule<T> changingRule, T newValue, String userInput) {
            if (!(newValue instanceof Number)) {
                throw new IllegalArgumentException("Illegal validator call.");
            }
            Number number = (Number) newValue;
            if ((this.canMaxEquals && number.doubleValue() > this.max) ||
                    (!this.canMaxEquals && number.doubleValue() >= this.max) ||
                    (this.canMinEquals && number.doubleValue() < this.min) ||
                    (!this.canMinEquals && number.doubleValue() <= this.min)) {
                return null;
            }
            return newValue;
        }
    }
}
