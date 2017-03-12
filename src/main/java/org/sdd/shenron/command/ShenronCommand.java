package org.sdd.shenron.command;

import fr.litarvan.krobot.command.Command;
import fr.litarvan.krobot.command.ICommandCaller;
import fr.litarvan.krobot.command.message.MessageCommandCaller;
import fr.litarvan.krobot.motor.discord.DiscordConversation;
import fr.litarvan.krobot.motor.discord.DiscordMessage;
import fr.litarvan.krobot.util.KrobotFunctions;
import java.util.List;
import net.dv8tion.jda.core.entities.Guild;


import static fr.litarvan.krobot.util.KrobotFunctions.*;

public abstract class ShenronCommand extends Command
{
    @Override
    public final void handleCall(ICommandCaller iCommandCaller, List<String> list)
    {
        if (!(iCommandCaller instanceof MessageCommandCaller))
        {
            return;
        }

        MessageCommandCaller caller = (MessageCommandCaller) iCommandCaller;
        caller.getMessage().delete();

        if (!isRightServer(caller))
        {
            return;
        }

        try
        {
            handle(caller, list);
        }
        catch (Exception e)
        {
            handleCommandCrash(iCommandCaller, this, list, e);
        }
    }

    public abstract void handle(MessageCommandCaller caller, List<String> args) throws Exception;

    public String getServer()
    {
        return null;
    }

    public boolean isRightServer(MessageCommandCaller caller)
    {
        if (caller.getMessage() instanceof DiscordMessage)
        {
            DiscordConversation conversation = (DiscordConversation) caller.getConversation();
            Guild guild = ((DiscordMessage) caller.getMessage()).getMessage().getGuild();

            if (this.getServer() != null && !this.getServer().trim().equalsIgnoreCase(guild.getName().trim()))
            {
                conversation.getChannel().sendMessage(KrobotFunctions.mention(caller.getUser()) + " Sorry this command isn't supported on this server").queue();
                return false;
            }
        }

        return true;
    }
}
