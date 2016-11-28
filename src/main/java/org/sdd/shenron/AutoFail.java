package org.sdd.shenron;

import fr.litarvan.krobot.message.IMessageListener;
import fr.litarvan.krobot.message.MessageReceivedEvent;
import fr.litarvan.krobot.motor.Conversation;
import fr.litarvan.krobot.motor.Message;
import fr.litarvan.krobot.motor.User;
import fr.litarvan.krobot.motor.discord.DiscordMessage;
import fr.litarvan.krobot.motor.discord.DiscordUser;
import fr.litarvan.krobot.util.ResponseAnalyser;
import org.sdd.shenron.util.MessageEditor;
import org.sdd.shenron.util.MessageSearch;

public class AutoFail implements IMessageListener
{
    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        String message = event.getMessage().getText();

        if (!message.startsWith(/*Shenron.AUTO_DETECT_START + */"*"))
        {
            return;
        }
        else if (event.getUser() instanceof DiscordUser)
        {
            DiscordUser user = (DiscordUser) event.getUser();

            if (user.getUser().isBot() || user.getUser().isFake())
            {
                return;
            }
        }

        event.getMessage().delete();

        Message toCorrect = MessageSearch.findLastMessageOf(event.getUser(), event.getConversation());

        if (toCorrect == null)
        {
            return;
        }

        User user = event.getUser();
        Conversation conversation = event.getConversation();
        String correction = correct(toCorrect, message.substring(1));

        try
        {
            MessageEditor.edit(user, conversation, toCorrect, correction);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static String correct(Message message, String correction)
    {
        String text = message.getText();
        String[] split = text.split(" ");
        ResponseAnalyser analyser = new ResponseAnalyser(correction, split);
        String toReplace = split[analyser.get()];

        return text.replace(toReplace, correction);
    }

    @Override
    public void onPrivateMessageReceived(MessageReceivedEvent messageReceivedEvent)
    {
    }
}
