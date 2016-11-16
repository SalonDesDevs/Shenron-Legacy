package org.sdd.shenron;

import fr.litarvan.krobot.motor.discord.DiscordConversation;
import fr.litarvan.krobot.motor.discord.DiscordUser;
import fr.minuskube.bot.discord.util.Webhook;
import java.util.HashMap;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.impl.JDAImpl;
import org.json.JSONObject;

public final class DiscordSudo
{
    public static void sendFor(DiscordUser user, DiscordConversation conversation, String message) throws WebhookException
    {
        TextChannel channel = (TextChannel) conversation.getChannel();
        Guild guild = channel.getGuild();
        JDAImpl jda = (JDAImpl) guild.getJDA();
        Webhook hook = Webhook.getBotHook(jda, channel);

        if (hook == null)
        {
            throw new WebhookException("creating", channel.getName());
        }

        if (!hook.execute(jda, new JSONObject(new HashMap<String, Object>()
        {
            {
                put("username", user.getUsername());
                put("avatar_url", user.getAvatar());
                put("content", message);
            }
        })))
        {
            throw new WebhookException("sending", channel.getName());
        }
    }
}
