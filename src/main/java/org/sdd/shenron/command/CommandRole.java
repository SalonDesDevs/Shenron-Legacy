package org.sdd.shenron.command;

import fr.litarvan.krobot.command.message.MessageCommandCaller;
import fr.litarvan.krobot.motor.User;
import fr.litarvan.krobot.util.KrobotFunctions;
import fr.litarvan.krobot.util.PermissionManager;
import java.util.List;
import joptsimple.internal.Strings;
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
        return list.size() > 0;
    }

    @Override
    public void handle(MessageCommandCaller caller, List<String> args) throws Exception
    {
        String message = Strings.join(args, " ");
        String name = message.trim();

        if (name.startsWith("@"))
        {
            name = name.substring(1);
        }

        User user = caller.getConversation().userByName(name);
        String role = "a regular user";
        PermissionManager manager = Shenron.get().getPermissionManager();

        if (manager.hasPermission(user, "owner"))
        {
            role = "the owner <3";
        }
        else if (manager.hasPermission(user, "admin"))
        {
            role = "an admin";
        }
        else if (manager.hasPermission(user, "admin_sdd"))
        {
            role = "an admin of 'SDD'";
        }
        else if (manager.hasPermission(user, "admin_support"))
        {
            role = "an admin of 'Support Launcher'";
        }

        caller.getConversation().sendMessage(KrobotFunctions.mention(user) + " is " + role);
    }
}
