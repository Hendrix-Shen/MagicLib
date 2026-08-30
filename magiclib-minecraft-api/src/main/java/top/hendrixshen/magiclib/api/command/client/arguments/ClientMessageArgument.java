package top.hendrixshen.magiclib.api.command.client.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.network.chat.Component;

import top.hendrixshen.magiclib.api.command.client.ClientCommandSource;
import top.hendrixshen.magiclib.api.compat.minecraft.network.chat.ComponentCompat;

import java.util.Arrays;
import java.util.Collection;

/**
 * The client-side counterpart of {@link net.minecraft.commands.arguments.MessageArgument}.
 *
 * <p>Unlike the vanilla implementation, which resolves entity selectors inside the message against
 * a {@code CommandSourceStack}, the client-side version keeps the message as plain text. This is
 * because entity selectors cannot be resolved without server-side context.</p>
 */
public class ClientMessageArgument implements ArgumentType<ClientMessageArgument.Message> {
    private static final Collection<String> EXAMPLES = Arrays.asList("Hello world!", "foo", "@e", "Hello @p :)");

    private ClientMessageArgument() {
    }

    /**
     * Creates a message argument.
     *
     * @return the argument
     */
    public static ClientMessageArgument message() {
        return new ClientMessageArgument();
    }

    /**
     * Gets the message from the context.
     *
     * @param context the command context
     * @param name    the argument name
     * @return the message
     */
    public static Component getMessage(CommandContext<ClientCommandSource> context, String name) {
        return context.<ClientMessageArgument.Message>getArgument(name, ClientMessageArgument.Message.class).toComponent();
    }

    @Override
    public ClientMessageArgument.Message parse(StringReader stringReader) throws CommandSyntaxException {
        return ClientMessageArgument.Message.parseText(stringReader);
    }

    @Override
    public Collection<String> getExamples() {
        return ClientMessageArgument.EXAMPLES;
    }

    /**
     * A plain text message.
     */
    public static class Message {
        private final String text;

        private Message(String text) {
            this.text = text;
        }

        /**
         * Converts the message to a component.
         *
         * @return the component
         */
        public Component toComponent() {
            return ComponentCompat.literal(this.text);
        }

        /**
         * Parses a plain text message, consuming the rest of the reader.
         *
         * @param stringReader the reader
         * @return the message
         */
        public static ClientMessageArgument.Message parseText(StringReader stringReader) {
            String string = stringReader.getString().substring(stringReader.getCursor(), stringReader.getTotalLength());
            stringReader.setCursor(stringReader.getTotalLength());
            return new ClientMessageArgument.Message(string);
        }
    }
}
