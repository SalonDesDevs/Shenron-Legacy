package org.sdd.shenron.command;

import fr.litarvan.krobot.command.ICommandCaller;
import fr.litarvan.krobot.command.message.MessageCommandCaller;
import fr.litarvan.krobot.motor.Message;
import fr.litarvan.krobot.motor.discord.DiscordConversation;
import fr.litarvan.krobot.motor.discord.DiscordMessage;
import fr.litarvan.krobot.util.KrobotFunctions;
import fr.litarvan.krobot.util.TextEmoji;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import org.jetbrains.annotations.NotNull;
import org.sdd.shenron.Shenron;


import static fr.litarvan.krobot.util.KrobotFunctions.*;

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
        if (!(cCaller instanceof MessageCommandCaller) || !(((MessageCommandCaller) cCaller).getMessage() instanceof DiscordMessage))
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
                long time = 35L;
                int rateLimit = 0;
                boolean retry;

                do
                {
                    try
                    {
                        Thread.sleep(time);
                        ((DiscordMessage) lasts[0]).getMessage().addReaction(reaction.getUnicode()).block();

                        retry = false;
                    }
                    catch (InterruptedException | RateLimitedException e)
                    {
                        rateLimit++;
                        time += 35L * rateLimit;
                        retry = true;
                    }
                }
                while (retry);
            });
        }
    }
}
