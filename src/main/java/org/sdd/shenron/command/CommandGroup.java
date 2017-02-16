package org.sdd.shenron.command;

import fr.litarvan.krobot.command.message.MessageCommandCaller;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class CommandGroup extends ShenronCommand
{
    @NotNull
    @Override
    public String getCommand()
    {
        return "group";
    }

    @NotNull
    @Override
    public String getDescription()
    {
        return "Pour gerer les groupes du SDD (en rejoindre un, en créer un (admin), etc...)";
    }

    @NotNull
    @Override
    public String getSyntax()
    {
        return "list|join|create|leave <group> [(create) role-name]";
    }

    @Override
    public boolean checkSyntax(List<String> args)
    {
        String command = null;
        return args.size() == 0 || (args.size() == 1 && !(command = args.get(0)).equals("leave") && !command.equals("list")) || (args.size() == 3 && !command.equals("create"));
    }

    @Override
    public void handle(MessageCommandCaller caller, List<String> args) throws Exception
    {
        String command = args.get(0);
        caller.getConversation().sendMessage("Commande demandée : " + command);
    }

    private void join(MessageCommandCaller caller, String group)
    {

    }

    private void create(MessageCommandCaller caller, String name, String roleName)
    {

    }

    private void leave(MessageCommandCaller caller, String group)
    {

    }

    private void list(MessageCommandCaller caller)
    {

    }

    @Override
    public String getServer()
    {
        return "Salon des développeurs";
    }
}
