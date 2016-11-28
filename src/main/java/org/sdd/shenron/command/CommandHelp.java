package org.sdd.shenron.command;

import fr.litarvan.krobot.command.Command;
import fr.litarvan.krobot.command.ICommandCaller;
import fr.litarvan.krobot.command.message.MessageCommandCaller;
import fr.litarvan.krobot.util.Markdown;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.sdd.shenron.Shenron;

public class CommandHelp extends ShenronCommand
{
    @NotNull
    @Override
    public String getCommand()
    {
        return "help";
    }

    @NotNull
    @Override
    public String getDescription()
    {
        return "Prints the list of command with their description and their syntax";
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
    public void handle(ICommandCaller caller, List<String> args)
    {
        if (!(caller instanceof MessageCommandCaller))
        {
            return;
        }

        String message = Markdown.mdUnderline("List of commands :") + "\n";

        for (Command c : Shenron.get().getCommandHandler().getCommandList())
        {
            message += ("    " + Shenron.get().getCommandHandler().getPrefix() + c.getCommand() + " " + c.getSyntax() + "\n" +
                        "        " + c.getDescription() + "\n");
        }

        Shenron.get().sendMessage(message, ((MessageCommandCaller) caller).getConversation());
    }
}
