package org.sdd.shenron.command;

import fr.litarvan.krobot.command.ICommandCaller;
import fr.litarvan.krobot.command.message.MessageCommandCaller;
import fr.litarvan.krobot.util.Markdown;
import java.util.List;
import joptsimple.internal.Strings;
import org.jetbrains.annotations.NotNull;
import org.sdd.shenron.util.MessageEditor;

public class CommandEscape extends ShenronCommand
{
    @NotNull
    @Override
    public String getCommand()
    {
        return "e";
    }

    @NotNull
    @Override
    public String getDescription()
    {
        return "Escape markdown from your message";
    }

    @NotNull
    @Override
    public String getSyntax()
    {
        return "[message...]";
    }

    @Override
    public boolean checkSyntax(List<String> list)
    {
        return list.size() > 0;
    }

    @Override
    public void handle(ICommandCaller caller, List<String> args) throws Exception
    {
        if (!(caller instanceof MessageCommandCaller))
        {
            return;
        }

        MessageEditor.edit(((MessageCommandCaller) caller).getUser(),
                           ((MessageCommandCaller) caller).getConversation(),
                           ((MessageCommandCaller) caller).getMessage(),

                           Markdown.mdEscape(Strings.join(args, " ")), false);
    }
}
