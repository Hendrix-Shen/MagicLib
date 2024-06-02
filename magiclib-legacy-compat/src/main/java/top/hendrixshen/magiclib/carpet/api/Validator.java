package top.hendrixshen.magiclib.carpet.api;

import carpet.settings.ParsedRule;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import top.hendrixshen.magiclib.carpet.impl.RuleHelper;
import top.hendrixshen.magiclib.carpet.impl.RuleOption;
import top.hendrixshen.magiclib.impl.carpet.CarpetEntrypoint;
import top.hendrixshen.magiclib.util.minecraft.ComponentUtil;
import top.hendrixshen.magiclib.util.minecraft.MessageUtil;

//#if MC > 11900
//$$ import carpet.api.settings.CarpetRule;
//#endif

//#if MC > 11900
//$$ @SuppressWarnings("removal")
//#endif
public abstract class Validator<T> extends
        //#if MC > 11900
        //$$ carpet.api.settings.Validator<T>
        //#else
        carpet.settings.Validator<T>
        //#endif
{
    @Override
    public T validate(
            CommandSourceStack source,
            //#if MC > 11900
            //$$ CarpetRule<T> carpetRule,
            //#else
            ParsedRule<T> carpetRule,
            //#endif
            T newValue,
            String userInput) {
        return this.validateCompat(
                source,
                //#if MC > 11900
                //$$ RuleHelper.getSettingManager((ParsedRule<T>) carpetRule).getRuleOption((ParsedRule<T>) carpetRule),
                //#else
                RuleHelper.getSettingManager(carpetRule).getRuleOption(carpetRule),
                //#endif
                newValue,
                userInput);
    }

    //#if MC > 11502
    @Override
    public void notifyFailure(
            CommandSourceStack source,
            //#if MC > 11900
            //$$ CarpetRule<T> carpetRule,
            //#else
            ParsedRule<T> carpetRule,
            //#endif
            String providedValue) {
    }
    //#endif

    public Component getDescription() {
        return null;
    }

    public T validateCompat(CommandSourceStack source, RuleOption ruleOption, T newValue, String userInput) {
        if (this.getValidValue(source, ruleOption, newValue, userInput) != null) {
            return this.getValidValue(source, ruleOption, newValue, userInput);
        }

        MessageUtil.sendMessageCompat(source, ComponentUtil.tr("magiclib.ui.could_not_set",
                        CarpetEntrypoint.getSettingManager().getTranslatedRuleName(source, ruleOption.getName()))
                .withStyle(style -> style.withColor(ChatFormatting.RED)));

        if (this.getDescription() != null) {
            MessageUtil.sendMessage(source, this.getDescription());
        }

        return null;
    }

    public abstract T getValidValue(CommandSourceStack source, RuleOption ruleOption, T newValue, String userInput);
}
