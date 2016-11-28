package org.sdd.shenron.util;

import fr.litarvan.krobot.motor.Conversation;
import fr.litarvan.krobot.motor.Message;
import fr.litarvan.krobot.motor.User;
import fr.litarvan.krobot.util.KrobotFunctions;


import static fr.litarvan.krobot.util.KrobotFunctions.*;

public final class MessageSearch
{
    public static Message findLastMessageOf(User user, Conversation conversation)
    {
        Message[] messages = conversation.messages(25);
        Message message = null;

        for (Message m : messages)
        {
            if (m.getAuthor().getId().equals(user.getId()))
            {
                message = m;
                break;
            }
        }

        return message;
    }
}
