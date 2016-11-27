package org.sdd.shenron.inlayer;

import fr.litarvan.krobot.command.message.MessageCommandCaller;
import fr.litarvan.krobot.message.IMessageListener;
import fr.litarvan.krobot.message.MessageReceivedEvent;
import fr.litarvan.krobot.motor.Message;
import fr.litarvan.krobot.motor.discord.DiscordConversation;
import fr.litarvan.krobot.motor.discord.DiscordMessage;
import fr.litarvan.krobot.motor.discord.DiscordUser;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import org.apache.commons.lang3.tuple.Pair;
import org.sdd.shenron.util.DiscordSudo;
import org.sdd.shenron.Shenron;
import org.sdd.shenron.WebhookException;

public class InlayerCommandHandler implements IMessageListener
{
    private char start;
    private char prefix;

    private List<InlayerCommand> commands = new ArrayList<>();

    public InlayerCommandHandler(char start, char prefix)
    {
        this.start = start;
        this.prefix = prefix;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        String message = event.getMessage().getText();

        if (!message.startsWith(Character.toString(start)) || !message.contains(Character.toString(prefix)) || message.length() < 2)
        {
            return;
        }

        message = message.substring(1);

        MessageCommandCaller caller = new MessageCommandCaller(event.getUser(), event.getConversation());
        InlayerParser parser = new InlayerParser(caller, message, prefix, this);
        Pair<String, List<InlayerCall>> result = parser.get();

        if (result.getRight().isEmpty())
        {
            return;
        }

        String parsed = apply(result.getLeft(), event.getMessage(), result.getRight().toArray(new InlayerCall[result.getRight().size()]));

        if (event.getMessage() instanceof DiscordMessage)
        {
            DiscordMessage discordMessage = (DiscordMessage) event.getMessage();

            try
            {
                discordMessage.getMessage().deleteMessage().block();
                DiscordSudo.sendFor((DiscordUser) event.getUser(), (DiscordConversation) event.getConversation(), parsed);
            }
            catch (RateLimitedException | WebhookException e)
            {
                Shenron.handleCommandException(caller, null, null, e);
            }
        }
        else
        {
            event.getConversation().sendMessage(event.getUser().getUsername() + ": " + parsed);
        }
    }

    private String apply(String message, Message source, InlayerCall[] calls)
    {
        String result = message;
        int toAdd = 0;

        for (InlayerCall call : calls)
        {
            int oldSize = result.length();

            StringParser parser = new StringParser(result, InlayerParser.ESCAPE_CHAR);
            parser.setIndex(call.getPos() + toAdd);

            if (call.getCommand() != null)
            {
                try
                {
                    result = call.getCommand().handle(call.getCaller(), result, source, parser, call.getArgs());
                }
                catch (Exception e)
                {
                    Shenron.handleCommandException(call.getCaller(), call.getCommand(), call.getArgs(), e);
                }
            }
            else
            {
                result = result.substring(0, call.getPos()) + "??" + result.substring(call.getPos());
            }

            toAdd += result.length() - oldSize;
        }

        return result;
    }

    @Override
    public void onPrivateMessageReceived(MessageReceivedEvent messageReceivedEvent)
    {
    }

    public char getStart()
    {
        return start;
    }

    public char getPrefix()
    {
        return prefix;
    }

    public void register(InlayerCommand... command)
    {
        commands.addAll(Arrays.asList(command));
    }

    public InlayerCommand[] getCommands()
    {
        return commands.toArray(new InlayerCommand[commands.size()]);
    }
}
