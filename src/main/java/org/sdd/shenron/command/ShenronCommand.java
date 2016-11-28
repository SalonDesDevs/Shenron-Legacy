package org.sdd.shenron.command;

import fr.litarvan.krobot.command.Command;
import fr.litarvan.krobot.command.ICommandCaller;
import fr.litarvan.krobot.command.message.MessageCommandCaller;
import fr.litarvan.krobot.motor.Message;
import fr.litarvan.krobot.motor.discord.DiscordMessage;
import fr.litarvan.krobot.motor.discord.DiscordUser;
import fr.litarvan.krobot.util.KrobotFunctions;
import java.util.List;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import org.sdd.shenron.Shenron;


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

        try
        {
            handle(iCommandCaller, list);
        }
        catch (Exception e)
        {
            handleCommandCrash(iCommandCaller, this, list, e);
        }
    }

    public abstract void handle(ICommandCaller caller, List<String> args) throws Exception;
}
