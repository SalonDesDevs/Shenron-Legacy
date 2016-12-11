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

        String message = Strings.join(args, " ");
        String result = toEmoji(message);

        MessageEditor.edit(((MessageCommandCaller) caller).getUser(), ((MessageCommandCaller) caller).getConversation(), ((MessageCommandCaller) caller).getMessage(), result, false);
    }

    public static String toEmoji(String text)
    {
        String result = "";

        for (char c : text.toCharArray())
        {
            if (!CHAR_TO_EMOJI.containsKey(c))
            {
                result += c;
            }
            else
            {
                result += CHAR_TO_EMOJI.get(c).getUnicode();
            }

            result += " ";
        }

        return result;
    }

    private static final HashMap<Character, TextEmoji> CHAR_TO_EMOJI = new HashMap<>();

    static
    {
        // TODO: KROBOT: Fix that

        CHAR_TO_EMOJI.put('a', TextEmoji.RI_A);
        CHAR_TO_EMOJI.put('b', TextEmoji.RI_B);
        CHAR_TO_EMOJI.put('c', TextEmoji.RI_C);
        CHAR_TO_EMOJI.put('d', TextEmoji.RI_D);
        CHAR_TO_EMOJI.put('e', TextEmoji.RI_E);
        CHAR_TO_EMOJI.put('f', TextEmoji.RI_F);
        CHAR_TO_EMOJI.put('g', TextEmoji.RI_G);
        CHAR_TO_EMOJI.put('h', TextEmoji.RI_H);
        CHAR_TO_EMOJI.put('i', TextEmoji.RI_I);
        CHAR_TO_EMOJI.put('j', TextEmoji.RI_J);
        CHAR_TO_EMOJI.put('k', TextEmoji.RI_K);
        CHAR_TO_EMOJI.put('l', TextEmoji.RI_L);
        CHAR_TO_EMOJI.put('m', TextEmoji.RI_M);
        CHAR_TO_EMOJI.put('n', TextEmoji.RI_N);
        CHAR_TO_EMOJI.put('o', TextEmoji.RI_O);
        CHAR_TO_EMOJI.put('p', TextEmoji.RI_P);
        CHAR_TO_EMOJI.put('q', TextEmoji.RI_Q);
        CHAR_TO_EMOJI.put('r', TextEmoji.RI_R);
        CHAR_TO_EMOJI.put('s', TextEmoji.RI_S);
        CHAR_TO_EMOJI.put('t', TextEmoji.RI_T);
        CHAR_TO_EMOJI.put('u', TextEmoji.RI_U);
        CHAR_TO_EMOJI.put('v', TextEmoji.RI_V);
        CHAR_TO_EMOJI.put('w', TextEmoji.RI_W);
        CHAR_TO_EMOJI.put('x', TextEmoji.RI_X);
        CHAR_TO_EMOJI.put('y', TextEmoji.RI_Y);
        CHAR_TO_EMOJI.put('z', TextEmoji.RI_Z);
        CHAR_TO_EMOJI.put('1', TextEmoji.ONE);
        CHAR_TO_EMOJI.put('2', TextEmoji.TWO);
        CHAR_TO_EMOJI.put('3', TextEmoji.THREE);
        CHAR_TO_EMOJI.put('4', TextEmoji.FOUR);
        CHAR_TO_EMOJI.put('5', TextEmoji.FIVE);
        CHAR_TO_EMOJI.put('6', TextEmoji.SIX);
        CHAR_TO_EMOJI.put('7', TextEmoji.SEVEN);
        CHAR_TO_EMOJI.put('8', TextEmoji.EIGHT);
        CHAR_TO_EMOJI.put('9', TextEmoji.NINE);
    }
}
