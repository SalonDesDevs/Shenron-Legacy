package org.sdd.shenron.command;

import fr.litarvan.krobot.command.Command;
import fr.litarvan.krobot.command.ICommandCaller;
import fr.litarvan.krobot.command.message.MessageCommandCaller;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.sdd.shenron.Shenron;

public class CommandVersion extends Command
{
    @NotNull
    @Override
    public String getCommand()
    {
        return "version";
    }

    @NotNull
    @Override
    public String getDescription()
    {
        return "Prints the Shenron version";
    }

    @NotNull
    @Override
    public String getSyntax()
    {
        return "";
    }

    @Override
    public boolean checkSyntax(List<String> list)
    {
        return list.size() == 0;
    }

    @Override
    public void handleCall(ICommandCaller caller, List<String> list)
    {
        if (caller instanceof MessageCommandCaller)
        {
            ((MessageCommandCaller) caller).getConversation().sendMessage("Shenron v" + Shenron.get().getVersion());
        }
    }
}
