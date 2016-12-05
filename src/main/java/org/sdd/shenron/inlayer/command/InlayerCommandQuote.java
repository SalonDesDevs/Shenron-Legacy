package org.sdd.shenron.inlayer.command;

import com.skype.SkypeException;
import fr.litarvan.krobot.command.message.MessageCommandCaller;
import fr.litarvan.krobot.motor.Conversation;
import fr.litarvan.krobot.motor.Message;
import fr.litarvan.krobot.motor.discord.DiscordMessage;
import fr.litarvan.krobot.motor.discord.DiscordUser;
import fr.litarvan.krobot.motor.skype.SkypeMessage;
import fr.litarvan.krobot.util.Markdown;
import fr.litarvan.krobot.util.ResponseAnalyser;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import joptsimple.internal.Strings;
import org.jetbrains.annotations.NotNull;
import org.sdd.shenron.Shenron;
import org.sdd.shenron.inlayer.InlayerCommand;
import org.sdd.shenron.inlayer.StringParser;

public class InlayerCommandQuote extends InlayerCommand
{
    public static final int MAX_MESSAGE_RETRIEVE = 250;

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
    public String handle(MessageCommandCaller caller, String message, Message source, StringParser parser, List<String> args)
    {
        Message result = search(Strings.join(args, " "), caller.getConversation());
        String toAdd = "{!! Message introuvable !!}";

        if (result != null)
        {
            toAdd = "\n\n" + quote(result) + "\n";
        }

        String start = message.substring(0, parser.index());
        String end = message.substring(parser.index());

        // TODO: FIX THAT OMG SO UGLY LIKE MINUSKUBE
        if (end.startsWith("}"))
        {
            end = end.substring(1);
        }

        return start + toAdd + end;
    }

    // TODO: Krobot Quoter

    public static String quote(Message message)
    {
        // TODO: Template file
        // TODO: KROBOT: Message send date

        String date = null;

        if (message instanceof DiscordMessage)
        {
            OffsetDateTime time = ((DiscordMessage) message).getMessage().getCreationTime();
            date = time.getDayOfMonth() + "/" + time.getMonth().ordinal() + "/" + time.getYear() + " à " + time.getHour() + ":" + time.getMinute() + ":" + time.getSecond();
        }
        else if (message instanceof SkypeMessage)
        {
            try
            {
                date = ((SkypeMessage) message).getMessage().getTime().toString();
            }
            catch (SkypeException ignored)
            {
            }
        }

        return Markdown.mdBold("> " + message.getAuthor().getUsername() + (date == null ? "" : " le " + date) + " a dit :") +
                        "\n" + Markdown.mdCode(message.getText(), "");
    }

    public static Message search(String query, Conversation conversation)
    {
        int amount = 0;
        int toRetrive = 10;

        while (amount < MAX_MESSAGE_RETRIEVE)
        {
            amount += toRetrive;

            Message[] messages = conversation.messages(toRetrive);

            for (int i = 0; i < messages.length; i++)
            {
                Message msg = messages[i];
                boolean fake = false;

                if (msg.getAuthor() instanceof DiscordUser)
                {
                    DiscordUser user = (DiscordUser) msg.getAuthor();
                    fake = user.getUser().isFake() || user.getUser().isBot();
                }

                if (msg.getText().toLowerCase().contains(query.toLowerCase()) && !fake && !msg.getText().startsWith(Character.toString(Shenron.AUTO_DETECT_START)))
                {
                    return msg;
                }
            }
        }

        return null;
    }
}
