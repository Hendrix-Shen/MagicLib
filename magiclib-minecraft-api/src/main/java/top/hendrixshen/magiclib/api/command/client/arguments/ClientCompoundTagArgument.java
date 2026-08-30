package top.hendrixshen.magiclib.api.command.client.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.CompoundTag;

// CHECKSTYLE.OFF: ImportOrder
//#if MC >= 1.21.5
//$$ import net.minecraft.nbt.TagParser;
//#endif
// CHECKSTYLE.ON: ImportOrder

import top.hendrixshen.magiclib.api.command.client.ClientCommandSource;

import java.util.Arrays;
import java.util.Collection;

/**
 * The client-side counterpart of {@link net.minecraft.commands.arguments.CompoundTagArgument}.
 *
 * <p>The parsing logic is delegated to the vanilla implementation, since it does not depend on the
 * command source. Only the getter method uses a {@link CommandContext} of {@link ClientCommandSource}
 * instead of {@code CommandSourceStack}.</p>
 */
public class ClientCompoundTagArgument implements ArgumentType<CompoundTag> {
    private static final Collection<String> EXAMPLES = Arrays.asList("{}", "{foo=bar}");

    private ClientCompoundTagArgument() {
    }

    /**
     * Creates a compound tag argument.
     *
     * @return the argument
     */
    public static ClientCompoundTagArgument compoundTag() {
        return new ClientCompoundTagArgument();
    }

    /**
     * Gets the compound tag from the context.
     *
     * @param context the command context
     * @param name    the argument name
     * @return the compound tag
     */
    public static CompoundTag getCompoundTag(CommandContext<ClientCommandSource> context, String name) {
        return context.getArgument(name, CompoundTag.class);
    }

    @Override
    public CompoundTag parse(StringReader stringReader) throws CommandSyntaxException {
        //#if MC >= 1.21.5
        //$$ return TagParser.parseCompoundAsArgument(stringReader);
        //#else
        return net.minecraft.commands.arguments.CompoundTagArgument.compoundTag().parse(stringReader);
        //#endif
    }

    @Override
    public Collection<String> getExamples() {
        return ClientCompoundTagArgument.EXAMPLES;
    }
}
