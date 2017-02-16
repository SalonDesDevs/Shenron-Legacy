package org.sdd.shenron.command;

import fr.litarvan.krobot.command.ICommandCaller;
import fr.litarvan.krobot.command.message.MessageCommandCaller;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.sdd.shenron.util.MessageEditor;

public class CommandOsef extends ShenronCommand
{
    public static final String URL = "https://www.youtube.com/watch?v=XoDY9vFAaG8";

    @NotNull
    @Override
    public String getCommand()
    {
        return "osef";
    }

    @NotNull
    @Override
    public String getDescription()
    {
        return "Do this when you don't give a fuck";
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
    public void handle(MessageCommandCaller caller, List<String> args) throws Exception
    {
        MessageEditor.edit(caller.getUser(), caller.getConversation(), caller.getMessage(), URL, false);
    }
}
