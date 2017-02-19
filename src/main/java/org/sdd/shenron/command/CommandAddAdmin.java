package org.sdd.shenron.command;

import fr.litarvan.krobot.command.message.MessageCommandCaller;
import fr.litarvan.krobot.motor.User;
import fr.litarvan.krobot.util.PermissionManager;
import java.util.List;
import joptsimple.internal.Strings;
import org.jetbrains.annotations.NotNull;
import org.sdd.shenron.Shenron;


import static fr.litarvan.krobot.util.KrobotFunctions.*;

public class CommandAddAdmin extends ShenronCommand
{
    @NotNull
    @Override
    public String getCommand()
    {
        return "add-admin";
    }

    @NotNull
    @Override
    public String getDescription()
    {
        return "Add an administrator";
    }

    @NotNull
    @Override
    public String getSyntax()
    {
        return "<user>";
    }

    @Override
    public boolean checkSyntax(List<String> list)
    {
        return list.size() < 3;
    }

    @Override
    public void handle(MessageCommandCaller caller, List<String> args) throws Exception
    {
        PermissionManager manager = Shenron.get().getPermissionManager();

        if (!manager.hasOrFail(caller.getUser(), "owner", caller.getConversation(), "Seul l'owner du bot peut executer cette commande"))
        {
            return;
        }

        String message = Strings.join(args, " ");
        String name = message.trim();

        if (name.startsWith("@"))
        {
            name = name.substring(1);
        }

        User user = caller.getConversation().userByName(name);

        if (user == null)
        {
            caller.getConversation().sendMessage(mention(caller.getUser()) + " Can't find user '" + name + "'");
            return;
        }

        String perm = "admin";
        String suffix = args.get(1).trim();

        if (args.size() == 2)
        {
            perm += "_" + suffix.toLowerCase();
        }

        manager.addPermission(user, perm);
        caller.getConversation().sendMessage(mention(user) + " is now admin (" + suffix.toUpperCase() + ")");
    }
}
