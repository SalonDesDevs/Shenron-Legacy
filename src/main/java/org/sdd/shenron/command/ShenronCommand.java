package org.sdd.shenron.command;

import fr.litarvan.krobot.command.Command;
import fr.litarvan.krobot.command.ICommandCaller;
import fr.litarvan.krobot.command.message.MessageCommandCaller;
import fr.litarvan.krobot.motor.Message;
import fr.litarvan.krobot.motor.discord.DiscordMessage;
import java.util.List;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import org.sdd.shenron.Shenron;

public abstract class ShenronCommand extends Command
{
    @Override
    public final void handleCall(ICommandCaller iCommandCaller, List<String> list)
    {
        // TODO: KROBOT: Message
        Message message = ((MessageCommandCaller) iCommandCaller).getConversation().messages(1)[0];

        try
        {
            if (message instanceof DiscordMessage)
            {
                ((DiscordMessage) message).getMessage().deleteMessage().block();
            }

            handle(iCommandCaller, list);
        }
        catch (Exception e)
        {
            Shenron.handleCommandException(iCommandCaller, this, list, e);
        }
    }

    public abstract void handle(ICommandCaller caller, List<String> args) throws Exception;
}
