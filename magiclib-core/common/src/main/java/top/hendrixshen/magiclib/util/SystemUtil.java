package top.hendrixshen.magiclib.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;
import java.util.List;
import java.util.Locale;


public class SystemUtil {
    private static final List<Option> PROPERTIES = Lists.newArrayList();

    public static @NotNull @Unmodifiable Collection<Option> getProperties() {
        return ImmutableList.copyOf(SystemUtil.PROPERTIES);
    }

    public static final class Option {
        /**
         * Parent option to this option, if non-null then this option is enabled if
         */
        @Getter
        private final Option parent;
        /**
         * Inheritance behaviour for this option
         */
        private final InheritType inheritType;
        /**
         * Java property name
         */
        @Getter
        private final String property;
        /**
         * Default value for string properties
         */
        private final String defaultValue;
        /**
         * Whether this property is boolean or not
         */
        private final boolean isFlag;
        /**
         * Number of parents
         */
        @Getter
        private final int depth;

        public static @NotNull Option newOption(String property) {
            return Option.newOption(null, property, true);
        }

        public static @NotNull Option newOption(InheritType inheritType, String property) {
            return Option.newOption(null, inheritType, property, true);
        }

        public static @NotNull Option newOption(String property, boolean flag) {
            return Option.newOption(null, property, flag);
        }

        public static @NotNull Option newOption(String property, String defaultStringValue) {
            return new Option(null, InheritType.INDEPENDENT, property, false, defaultStringValue);
        }

        public static @NotNull Option newOption(Option parent, String property) {
            return Option.newOption(parent, InheritType.INHERIT, property, true);
        }

        public static @NotNull Option newOption(Option parent, InheritType inheritType, String property) {
            return Option.newOption(parent, inheritType, property, true);
        }

        public static @NotNull Option newOption(Option parent, String property, boolean isFlag) {
            return new Option(parent, InheritType.INHERIT, property, isFlag, null);
        }

        public static @NotNull Option newOption(Option parent, InheritType inheritType,
                                                String property, boolean isFlag) {
            return new Option(parent, inheritType, property, isFlag, null);
        }

        public static @NotNull Option newOption(Option parent, String property, String defaultStringValue) {
            return new Option(parent, InheritType.INHERIT, property, false, defaultStringValue);
        }

        public static @NotNull Option newOption(Option parent, InheritType inheritType,
                                                String property, String defaultStringValue) {
            return new Option(parent, inheritType, property, false, defaultStringValue);
        }

        private Option(Option parent, InheritType inheritance, String property,
                       boolean isFlag, String defaultStringValue) {
            this.parent = parent;
            this.inheritType = inheritance;
            this.property = (parent != null ? parent.property + "." : "") + property;
            this.defaultValue = defaultStringValue;
            this.isFlag = isFlag;
            int depth = 0;

            for (; parent != null; depth++) {
                parent = parent.parent;
            }

            this.depth = depth;
            SystemUtil.PROPERTIES.add(this);
        }

        @Override
        public String toString() {
            return this.isFlag ? String.valueOf(this.getBooleanValue()) : this.getStringValue();
        }

        public boolean getLocalBooleanValue(boolean defaultValue) {
            return Boolean.parseBoolean(System.getProperty(this.property, Boolean.toString(defaultValue)));
        }

        public boolean getInheritedBooleanValue() {
            return this.parent != null && this.parent.getBooleanValue();
        }

        public boolean getBooleanValue() {
            if (this.inheritType == InheritType.ALWAYS_FALSE) {
                return false;
            }

            boolean local = this.getLocalBooleanValue(false);

            if (this.inheritType == InheritType.INDEPENDENT) {
                return local;
            }

            boolean inherited = local || this.getInheritedBooleanValue();
            return this.inheritType == InheritType.INHERIT ? inherited : this.getLocalBooleanValue(inherited);
        }

        public String getStringValue() {
            return (this.inheritType == InheritType.INDEPENDENT || this.parent == null || this.parent.getBooleanValue())
                    ? System.getProperty(this.property, this.defaultValue) : this.defaultValue;
        }

        public <E extends Enum<E>> E getEnumValue(@NotNull E defaultValue) {
            String value = System.getProperty(this.property, defaultValue.name());
            try {
                @SuppressWarnings("unchecked")
                E e = (E) Enum.valueOf(defaultValue.getClass(), value.toUpperCase(Locale.ROOT));
                return e;
            } catch (IllegalArgumentException ex) {
                return defaultValue;
            }
        }

        public enum InheritType {
            /**
             * If the parent is set, this option will be set too.
             */
            INHERIT,
            /**
             * If the parent is set, this option will be set too. However
             * setting the option explicitly to <tt>false</tt> will override the
             * parent value.
             */
            ALLOW_OVERRIDE,
            /**
             * This option ignores the value of the parent option, parent is
             * only used for grouping.
             */
            INDEPENDENT,
            /**
             * This option is always <tt>false</tt>.
             */
            ALWAYS_FALSE
        }
    }
}