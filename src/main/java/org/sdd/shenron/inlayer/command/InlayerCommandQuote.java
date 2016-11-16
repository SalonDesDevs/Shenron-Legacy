package org.sdd.shenron.inlayer.command;

import fr.litarvan.krobot.command.message.MessageCommandCaller;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.sdd.shenron.inlayer.InlayerCommand;
import org.sdd.shenron.inlayer.StringParser;

public class InlayerCommandQuote extends InlayerCommand
{
    @NotNull
    @Override
    public String getCommand()
    {
        return "quote";
    }

    @NotNull
    @Override
    public String getDescription()
    {
        return "Search for a message with the given query and quote it";
    }

    @NotNull
    @Override
    public String getSyntax()
    {
        return "<query>";
    }

    @Override
    public boolean checkSyntax(List<String> list)
    {
        return list.size() == 1;
    }

    @Override
    public char getShortcut()
    {
        return 'q';
    }

    @Override
    public String handle(MessageCommandCaller caller, String message, StringParser parser, List<String> args)
    {
        return message;
    }
}
