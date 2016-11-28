package org.sdd.shenron;

import fr.litarvan.krobot.message.IMessageListener;
import fr.litarvan.krobot.message.MessageReceivedEvent;
import fr.litarvan.krobot.motor.Conversation;
import fr.litarvan.krobot.motor.Message;
import fr.litarvan.krobot.motor.User;
import fr.litarvan.krobot.util.ResponseAnalyser;
import org.sdd.shenron.util.MessageEditor;
import org.sdd.shenron.util.MessageSearch;

public class AutoFail implements IMessageListener
{
    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        String message = event.getMessage().getText();

        if (!message.startsWith(Shenron.AUTO_DETECT_START + "*"))
        {
            return;
        }

        Message toCorrect = MessageSearch.findLastMessageOf(event.getUser(), event.getConversation());

        if (toCorrect == null)
        {
            return;
        }

        User user = event.getUser();
        Conversation conversation = event.getConversation();
        String correction = correct(toCorrect, message);

        try
        {
            MessageEditor.edit(user, conversation, event.getMessage(), correction);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return;
        }

        event.getMessage().delete();
    }

    public static String correct(Message message, String correction)
    {
        String text = message.getText();
        String[] split = text.split(" ");
        ResponseAnalyser analyser = new ResponseAnalyser(correction, split);
        String toReplace = split[analyser.get()];

        message.delete();

        return text.replace(toReplace, correction);
    }

    @Override
    public void onPrivateMessageReceived(MessageReceivedEvent messageReceivedEvent)
    {
    }
}