package org.sdd.shenron.inlayer.command;

import fr.litarvan.krobot.command.message.MessageCommandCaller;
import fr.litarvan.krobot.util.Markdown;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.sdd.shenron.inlayer.InlayerCommand;
import org.sdd.shenron.inlayer.StringParser;

public class InlayerCommandMarkdown extends InlayerCommand
{
    private String command;
    private char shortcut;
    private String mdModifier;
    private String modifierName;

    public InlayerCommandMarkdown(String command, char shortcut, String mdModifier, String modifierName)
    {
        this.command = command;
        this.shortcut = shortcut;
        this.mdModifier = mdModifier;
        this.modifierName = modifierName;
    }

    @NotNull
    @Override
    public String getCommand()
    {
        return this.command;
    }

    @NotNull
    @Override
    public String getDescription()
    {
        return "Apply markdown '" + modifierName + "' the next token";
    }

    @NotNull
    @Override
    public String getSyntax()
    {
        return "[number of words]";
    }

    @Override
    public boolean checkSyntax(List<String> list)
    {
        return list.size() == 1;
    }

    @Override
    public char getShortcut()
    {
        return shortcut;
    }

    @Override
    public String handle(MessageCommandCaller caller, String message, StringParser parser, List<String> args)
    {
        String start = message.substring(0, parser.index());
        String toApply = args.get(0);

        try
        {
            int count = Integer.parseInt(toApply);

            toApply = "";

            for (int i = 0; i < count; i++)
            {
                toApply += parser.nextToken(StringParser.SPACE_STOP_LIST) + (parser.done() ? "" : parser.current());
            }
        }
        catch (NumberFormatException ignored)
        {
        }

        String end = message.substring(parser.index());
        String mdChar = Markdown.mdChar(mdModifier);

        return start + mdChar + toApply + mdChar + end;
    }
}
