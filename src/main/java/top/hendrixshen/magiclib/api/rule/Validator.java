package top.hendrixshen.magiclib.api.rule;

//#if MC >= 11901
import carpet.api.settings.CarpetRule;
//#endif
import carpet.settings.ParsedRule;
import net.minecraft.commands.CommandSourceStack;

//#if MC >= 11901
public abstract class Validator<T> extends carpet.api.settings.Validator<T> {
//#else
//$$ public abstract class Validator<T> extends carpet.settings.Validator<T> {
//#endif
    @Override
    //#if MC >= 11901
    public T validate(CommandSourceStack commandSourceStack, CarpetRule<T> carpetRule, T newValue, String userInput) {
        return this.validateCompat(commandSourceStack, (ParsedRule<T>) carpetRule, newValue, userInput);
    //#else
    //$$ public T validate(CommandSourceStack commandSourceStack, ParsedRule<T> carpetRule, T newValue, String userInput) {
    //$$     return this.validateCompat(commandSourceStack, carpetRule, newValue, userInput);
    //#endif
    }

    public abstract T validateCompat(CommandSourceStack commandSourceStack, ParsedRule<T> carpetRule, T newValue, String userInput);
}
