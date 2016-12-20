package org.sdd.shenron.command;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import fr.litarvan.krobot.command.ICommandCaller;
import fr.litarvan.krobot.command.message.MessageCommandCaller;
import fr.litarvan.krobot.motor.Message;
import fr.litarvan.krobot.motor.discord.DiscordConversation;
import fr.litarvan.krobot.motor.discord.DiscordMessage;
import fr.litarvan.krobot.util.TextEmoji;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CommandWordReact extends ShenronCommand
{
    private static ExecutorService pool = Executors.newSingleThreadExecutor();

    @NotNull
    @Override
    public String getCommand()
    {
        return "wr";
    }

    @NotNull
    @Override
    public String getDescription()
    {
        return "Add a word as reactions letters emotes on the last message";
    }

    @NotNull
    @Override
    public String getSyntax()
    {
        return "<word>";
    }

    @Override
    public boolean checkSyntax(List<String> list)
    {
        return list.size() == 1;
    }

    @Override
    public void handle(ICommandCaller cCaller, List<String> args) throws RateLimitedException
    {
        if (!(cCaller instanceof MessageCommandCaller) ||
                !(((MessageCommandCaller) cCaller).getMessage() instanceof DiscordMessage))
        {
            return;
        }

        MessageCommandCaller caller = (MessageCommandCaller) cCaller;

        if (!(caller.getConversation() instanceof DiscordConversation))
        {
            return;
        }

        Message[] lasts = caller.getConversation().messages(1);
        TextEmoji[] reactions = TextEmoji.Companion.toEmoji(args.get(0).trim().toLowerCase());

        for (TextEmoji reaction : reactions)
        {
            pool.submit(() -> {
                final int time = 35;
                boolean retry = true;

                do
                {
                    try
                    {
                        net.dv8tion.jda.core.entities.Message msg = ((DiscordMessage) lasts[0]).getMessage();

                        Thread.sleep(time);

                        HttpResponse<String> resp = Unirest.put("https://discordapp.com/api/channels/"
                                + msg.getChannel().getId() + "/messages/"
                                + msg.getId() + "/reactions/" + reaction.getUnicode() + "/@me")
                                .header("Authorization", msg.getJDA().getToken()).asString();

                        if(resp.getStatus() < 400)
                            retry = false;
                    }
                    catch (UnirestException | InterruptedException e)
                    {
                        retry = true;
                    }
                }
                while (retry);
            });
        }
    }
}
