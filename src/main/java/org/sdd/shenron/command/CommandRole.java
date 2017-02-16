package org.sdd.shenron.command;

import fr.litarvan.krobot.command.message.MessageCommandCaller;
import fr.litarvan.krobot.motor.User;
import fr.litarvan.krobot.util.KrobotFunctions;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.sdd.shenron.Shenron;

public class CommandRole extends ShenronCommand
{
    @NotNull
    @Override
    public String getCommand()
    {
        return "role";
    }

    @NotNull
    @Override
    public String getDescription()
    {
        return "Displays the role of a user";
    }

    @NotNull
    @Override
    public String getSyntax()
    {
        return "<@user>";
    }

    @Override
    public boolean checkSyntax(List<String> list)
    {
        return list.size() == 1;
    }

    @Override
    public void handle(MessageCommandCaller caller, List<String> args) throws Exception
    {
        String name = args.get(0).trim();

        if (name.startsWith("@"))
        {
            name = name.substring(1);
        }

        User user = caller.getConversation().userByName(name);
        caller.getConversation().sendMessage(KrobotFunctions.mention(user) + " is " + (Shenron.get().isAdmin(user) ? "an admin" : "a regular user"));
    }
}
