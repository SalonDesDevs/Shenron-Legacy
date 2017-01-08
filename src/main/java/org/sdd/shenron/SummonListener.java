package org.sdd.shenron;

import fr.litarvan.krobot.message.IMessageListener;
import fr.litarvan.krobot.message.MessageReceivedEvent;
import org.apache.commons.lang3.StringUtils;


import static fr.litarvan.krobot.util.Markdown.*;

public class SummonListener implements IMessageListener
{
    private String invoker;

    public SummonListener(String invoker)
    {
        this.invoker = invoker.toLowerCase();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        String message = event.getMessage().getText().trim().toLowerCase();

        if(StringUtils.getLevenshteinDistance(invoker, message) < 5) {
            Shenron.get().sendMessage("https://giphy.com/gifs/dragon-ball-z-GCBuPi2YPNcxG",
                    event.getConversation());
            Shenron.get().sendMessage(mdBold("UN HUMAIN TEL QUE VOUS N'EST PAS APTE" +
                    " A FAIRE APPARAITRE SHENRON !"), event.getConversation());
        }
        else if(StringUtils.getLevenshteinDistance("albatard", message) < 3) {
            Shenron.get().sendMessage("https://www.youtube.com/watch?v=AMIrFNHAGyE", event.getConversation());
        }

        event.setCancelled(true);
    }

    @Override
    public void onPrivateMessageReceived(MessageReceivedEvent event)
    {
        onMessageReceived(event);
    }
}
