package org.sdd.shenron.inlayer.command;

import fr.litarvan.krobot.command.message.MessageCommandCaller;
import fr.litarvan.krobot.motor.Message;
import fr.litarvan.krobot.motor.discord.DiscordMessage;
import java.util.Arrays;
import java.util.List;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.impl.EmoteImpl;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import org.jetbrains.annotations.NotNull;
import org.sdd.shenron.Shenron;
import org.sdd.shenron.inlayer.InlayerCommand;
import org.sdd.shenron.inlayer.StringParser;

public class InlayerCommandWordReact extends InlayerCommand
{
    @NotNull
    @Override
    public String getCommand()
    {
        return "wreact";
    }

    @NotNull
    @Override
    public String getDescription()
    {
        return "Put some word with reactions on this message";
    }

    @NotNull
    @Override
    public String getSyntax()
    {
        return "<word>";
    }

    @Override
    public boolean checkSyntax(List<String> list)
    {
        return list.size() == 1;
    }

    @Override
    public char getShortcut()
    {
        return 'r';
    }

    @Override
    public String handle(MessageCommandCaller caller, String message, Message source, StringParser pos, List<String> args)
    {
        if (!(source instanceof DiscordMessage))
        {
            return message;
        }

        DiscordMessage discordSource = (DiscordMessage) source;
        try
        {
            discordSource.getMessage().addReaction(discordSource.getMessage().getJDA().getEmoteById(":a:")).block();
            System.out.println(Arrays.toString(discordSource.getMessage().getJDA().getEmotes().toArray()));
        }
        catch (RateLimitedException e)
        {
            Shenron.handleCommandException(caller, this, args, e);
        }

        return message;
    }
}
