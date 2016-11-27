package org.sdd.shenron.command;

import fr.litarvan.krobot.command.Command;
import fr.litarvan.krobot.command.ICommandCaller;
import fr.litarvan.krobot.command.message.MessageCommandCaller;
import fr.litarvan.krobot.motor.discord.DiscordConversation;
import fr.litarvan.krobot.motor.discord.DiscordMessage;
import java.util.Arrays;
import java.util.List;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import org.jetbrains.annotations.NotNull;
import org.sdd.shenron.Shenron;

public class CommandWordReact extends Command
{
    @NotNull
    @Override
    public String getCommand()
    {
        return "wr";
    }

    @NotNull
    @Override
    public String getDescription()
    {
        return "Add a word as reactions letters emotes on the last message";
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
    public void handleCall(ICommandCaller iCommandCaller, List<String> args)
    {
        if (!(iCommandCaller instanceof MessageCommandCaller))
        {
            return;
        }

        MessageCommandCaller caller = (MessageCommandCaller) iCommandCaller;

        // TODO: KROBOT: Get messages of conversation

        if (!(caller.getConversation() instanceof DiscordConversation))
        {
            return;
        }

        DiscordConversation conversation = (DiscordConversation) caller.getConversation();

        try
        {
            Message message = conversation.getChannel().getHistory().retrievePast(1).block().get(0);
            System.out.println(Arrays.toString(message.getJDA().getEmotes().toArray()));
            message.addReaction(message.getJDA().getEmoteById("a")).block();
        }
        catch (RateLimitedException | IllegalArgumentException e)
        {
            Shenron.handleCommandException(caller, this, args, e);
        }
    }
}
