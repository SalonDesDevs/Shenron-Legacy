package org.sdd.shenron.command;

import fr.litarvan.krobot.command.Command;
import fr.litarvan.krobot.command.ICommandCaller;
import java.util.List;
import org.sdd.shenron.Shenron;

public abstract class ShenronCommand extends Command
{
    @Override
    public final void handleCall(ICommandCaller iCommandCaller, List<String> list)
    {
        try
        {
            handle(iCommandCaller, list);
        }
        catch (Exception e)
        {
            Shenron.handleCommandException(iCommandCaller, this, list, e);
        }
    }

    public abstract void handle(ICommandCaller caller, List<String> args) throws Exception;
}
