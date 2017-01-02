package org.sdd.shenron.command;

import fr.litarvan.krobot.command.Command;
import fr.litarvan.krobot.command.ICommandCaller;
import fr.litarvan.krobot.command.message.MessageCommandCaller;
import java.util.List;

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
