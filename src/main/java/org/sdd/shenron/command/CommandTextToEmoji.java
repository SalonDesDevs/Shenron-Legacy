package org.sdd.shenron.command;

import fr.litarvan.krobot.command.ICommandCaller;
import fr.litarvan.krobot.command.message.MessageCommandCaller;
import fr.litarvan.krobot.motor.Conversation;
import fr.litarvan.krobot.motor.discord.DiscordConversation;
import fr.litarvan.krobot.util.TextEmoji;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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

        if (CHAR_TO_EMOJI == null)
        {
            ((MessageCommandCaller) caller).getConversation().sendMessage("Sorry, can't use the TextToEmoji command since there was an error tyring to get the Char -> Emoji table");
            return;
        }

        String message = Strings.join(args, " ");
        String result = toEmoji(message);

        MessageEditor.edit(((MessageCommandCaller) caller).getUser(), ((MessageCommandCaller) caller).getConversation(), ((MessageCommandCaller) caller).getMessage(), result, false);
    }

    public static String toEmoji(String text)
    {
        ArrayList<TextEmoji> emojis = new ArrayList<>();
        String result = "";

        for (char c : text.toCharArray())
        {
            if (!CHAR_TO_EMOJI.containsKey(c))
            {
                result += c;
            }
            else
            {
                result += CHAR_TO_EMOJI.get(c)[0].getUnicode();
            }

            result += " ";
        }

        return result;
    }

    private static HashMap<Character, TextEmoji[]> CHAR_TO_EMOJI;

    static
    {
        try
        {
            CHAR_TO_EMOJI = (HashMap<Character, TextEmoji[]>) TextEmoji.Companion.getClass().getField("CHAR_TO_EMOJI").get(null);
        }
        catch (Exception ignored)
        {
        }
    }
}
