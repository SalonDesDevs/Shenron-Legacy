package org.sdd.shenron.command;

import fr.litarvan.krobot.command.ICommandCaller;
import fr.litarvan.krobot.command.message.MessageCommandCaller;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.sdd.shenron.Shenron;

public class CommandVersion extends ShenronCommand
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
    public void handle(MessageCommandCaller caller, List<String> args)
    {
        Shenron.get().sendMessage("Shenron v" + Shenron.get().getVersion(), caller.getConversation());
    }
}
