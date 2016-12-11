package org.sdd.shenron.command;

import fr.litarvan.krobot.command.ICommandCaller;
import fr.litarvan.krobot.command.message.MessageCommandCaller;
import fr.litarvan.krobot.motor.Conversation;
import fr.litarvan.krobot.motor.discord.DiscordConversation;
import fr.litarvan.krobot.util.TextEmoji;
import java.util.Arrays;
import java.util.List;
import joptsimple.internal.Strings;
import org.jetbrains.annotations.NotNull;
import org.sdd.shenron.util.MessageEditor;

public class CommandTextToEmoji extends ShenronCommand
{
    @NotNull
    @Override
    public String getCommand()
    {
        return "emoji";
    }

    @NotNull
    @Override
    public String getDescription()
    {
        return "Convert the given text to emoji";
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

        String message = Strings.join(args, " ");
        TextEmoji[] toEmoji = TextEmoji.Companion.toEmoji(message);
        String result = "";

        for (TextEmoji emoji : toEmoji)
        {
            result += emoji.getUnicode()/* + " "*/;
        }

        MessageEditor.edit(((MessageCommandCaller) caller).getUser(), ((MessageCommandCaller) caller).getConversation(), ((MessageCommandCaller) caller).getMessage(), result);
    }
}
