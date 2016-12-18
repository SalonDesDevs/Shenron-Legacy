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
        return null;
    }

    @NotNull
    @Override
    public String getDescription()
    {
        return null;
    }

    @NotNull
    @Override
    public String getSyntax()
    {
        return null;
    }

    @Override
    public boolean checkSyntax(List<String> list)
    {
        return false;
    }

    @Override
    public char getShortcut()
    {
        return 0;
    }

    @Override
    public String handle(MessageCommandCaller caller, String message, Message source, StringParser pos, List<String> args) throws Exception
    {
        String start = message.substring(0, pos.index());
        String end = message.substring(pos.index());
        String result = Markdown.mdEscape(Strings.join(args, " "));

        return start + result + end;
    }
}
