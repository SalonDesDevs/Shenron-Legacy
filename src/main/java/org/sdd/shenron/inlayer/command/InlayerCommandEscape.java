package org.sdd.shenron.inlayer.command;

import fr.litarvan.krobot.command.message.MessageCommandCaller;
import fr.litarvan.krobot.motor.Message;
import fr.litarvan.krobot.util.Markdown;
import java.util.List;
import joptsimple.internal.Strings;
import org.jetbrains.annotations.NotNull;
import org.sdd.shenron.inlayer.InlayerCommand;
import org.sdd.shenron.inlayer.StringParser;

public class InlayerCommandEscape extends InlayerCommand
{
    @NotNull
    @Override
    public String getCommand()
    {
        return "escape";
    }

    @NotNull
    @Override
    public String getDescription()
    {
        return "Escape the given text from Markdown";
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
    public char getShortcut()
    {
        return 'e';
    }

    @Override
    public String handle(MessageCommandCaller caller, String message, Message source, StringParser pos, List<String> args) throws Exception
    {
        String start = message.substring(0, pos.index());
        String end = message.substring(pos.index());
        String result = Markdown.mdEscape(Strings.join(args, " "));

        // TODO: FIX THAT OMG SO UGLY LIKE MINUSKUBE
        if (end.startsWith("}"))
        {
            end = end.substring(1);
        }

        return start + result + end;
    }
}
