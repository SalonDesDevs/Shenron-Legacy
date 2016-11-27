package org.sdd.shenron.inlayer.command;

import fr.litarvan.krobot.command.message.MessageCommandCaller;
import fr.litarvan.krobot.motor.Message;
import fr.litarvan.krobot.motor.discord.DiscordMessage;
import java.util.List;
import net.dv8tion.jda.core.entities.impl.EmoteImpl;
import net.dv8tion.jda.core.requests.Route.Emotes;
import org.jetbrains.annotations.NotNull;
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
        discordSource.getMessage().getEmotes().add(new EmoteImpl("a", discordSource.getMessage().getJDA()));

        return message;
    }
}
