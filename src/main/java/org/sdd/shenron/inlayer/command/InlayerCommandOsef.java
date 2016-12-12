package org.sdd.shenron.inlayer.command;

import fr.litarvan.krobot.command.message.MessageCommandCaller;
import fr.litarvan.krobot.motor.Message;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.sdd.shenron.command.CommandOsef;
import org.sdd.shenron.inlayer.InlayerCommand;
import org.sdd.shenron.inlayer.StringParser;

public class InlayerCommandOsef extends InlayerCommand
{
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
        return "Insert a 'On s'en bat les couilles' video link at the command position";
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
    public char getShortcut()
    {
        return 'o';
    }

    @Override
    public String handle(MessageCommandCaller caller, String message, Message source, StringParser pos, List<String> args) throws Exception
    {
        String start = message.substring(0, pos.index());
        String end = message.substring(pos.index());

        return start + " " + CommandOsef.URL + " " + end;
    }
}
