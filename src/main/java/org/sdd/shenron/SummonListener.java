package org.sdd.shenron;

import fr.litarvan.krobot.message.IMessageListener;
import fr.litarvan.krobot.message.MessageReceivedEvent;
import fr.litarvan.krobot.motor.discord.DiscordConversation;
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
        DiscordConversation conversation = (DiscordConversation) event.getConversation();
        String message = event.getMessage().getText().trim().toLowerCase();

        if(StringUtils.getLevenshteinDistance(invoker, message) < 5)
        {
            conversation.getChannel().sendMessage("https://giphy.com/gifs/dragon-ball-z-GCBuPi2YPNcxG").queue();
            conversation.getChannel().sendMessage(mdBold("UN HUMAIN TEL QUE VOUS N'EST PAS APTE" + " A FAIRE APPARAITRE SHENRON !")).queue();
        }
        else if(StringUtils.getLevenshteinDistance("albatard", message) < 3)
        {
            conversation.getChannel().sendMessage("https://www.youtube.com/watch?v=AMIrFNHAGyE").queue();
        }

        event.setCancelled(true);
    }

    @Override
    public void onPrivateMessageReceived(MessageReceivedEvent event)
    {
        onMessageReceived(event);
    }
}
