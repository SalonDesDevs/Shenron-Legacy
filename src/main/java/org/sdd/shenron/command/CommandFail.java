package org.sdd.shenron.command;

import fr.litarvan.krobot.command.ICommandCaller;
import fr.litarvan.krobot.command.message.MessageCommandCaller;
import fr.litarvan.krobot.motor.Message;
import fr.litarvan.krobot.motor.discord.DiscordConversation;
import fr.litarvan.krobot.motor.discord.DiscordMessage;
import fr.litarvan.krobot.motor.discord.DiscordUser;
import fr.litarvan.krobot.motor.skype.SkypeMessage;
import fr.litarvan.krobot.util.KrobotFunctions;
import fr.litarvan.krobot.util.ResponseAnalyser;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.sdd.shenron.util.DiscordSudo;

public class CommandFail extends ShenronCommand
{
    @NotNull
    @Override
    public String getCommand()
    {
        return "fail";
    }

    @NotNull
    @Override
    public String getDescription()
    {
        return "No arguments: Delete your last message, One argument: Replace the most similar word of the given, with the given one (on your last message). Two arguments: Replace the given word with the second one (on your last message).";
    }

    @NotNull
    @Override
    public String getSyntax()
    {
        return "[replacement/to replace] [replacement]";
    }

    @Override
    public boolean checkSyntax(List<String> list)
    {
        return list.size() < 3;
    }

    @Override
    public void handle(ICommandCaller cCaller, List<String> args) throws Exception
    {
        MessageCommandCaller caller = ((MessageCommandCaller) cCaller);
        Message[] messages = caller.getConversation().messages(25);
        Message message = null;

        for (Message m : messages)
        {
            if (m.getAuthor().getId().equals(caller.getUser().getId()))
            {
                message = m;
                break;
            }
        }

        if (message == null)
        {
            caller.getConversation().sendMessage(KrobotFunctions.mention(caller.getUser()) + " Can't find any message you sent in the last one");
            return;
        }

        String toReplace = null;
        String replaceBy = null;

        switch (args.size())
        {
            case 0:
                if (message instanceof DiscordMessage)
                {
                    // TODO: KROBOT: Delete message
                    ((DiscordMessage) message).getMessage().deleteMessage().block();
                }

                return;
            case 1:
                if (!(caller.getUser() instanceof DiscordUser))
                {
                    caller.getConversation().sendMessage("(" + caller.getUser().getUsername() + "): *" + args.get(0));
                    return;
                }

                String[] split = message.getText().split(" ");
                ResponseAnalyser analyzer = new ResponseAnalyser(args.get(0), split);

                toReplace = split[analyzer.get()];
                replaceBy = args.get(0);

                break;
            case 2:
                toReplace = args.get(0);
                replaceBy = args.get(1);

                break;
        }

        if (toReplace != null && replaceBy != null)
        {
            DiscordSudo.sendFor((DiscordUser) caller.getUser(), (DiscordConversation) caller.getConversation(), message.getText().replace(toReplace, replaceBy));
        }
    }
}
