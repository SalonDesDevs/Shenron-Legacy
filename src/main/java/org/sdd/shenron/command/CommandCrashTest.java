package org.sdd.shenron.command;

import fr.litarvan.krobot.command.Command;
import fr.litarvan.krobot.command.ICommandCaller;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.sdd.shenron.Shenron;

public class CommandCrashTest extends Command
{
    @NotNull
    @Override
    public String getCommand()
    {
        return "crash";
    }

    @NotNull
    @Override
    public String getDescription()
    {
        return "Test the crash reporting system";
    }

    @NotNull
    @Override
    public String getSyntax()
    {
        return "[anything...]";
    }

    @Override
    public boolean checkSyntax(List<String> list)
    {
        return true;
    }

    @Override
    public void handleCall(ICommandCaller iCommandCaller, List<String> list)
    {
        Shenron.handleCommandException(iCommandCaller, this, list, new Exception());
    }
}
