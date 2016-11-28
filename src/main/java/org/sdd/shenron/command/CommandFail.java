package org.sdd.shenron.command;

import fr.litarvan.krobot.command.ICommandCaller;
import fr.litarvan.krobot.command.message.MessageCommandCaller;
import fr.litarvan.krobot.motor.Conversation;
import fr.litarvan.krobot.motor.Message;
import fr.litarvan.krobot.motor.User;
import fr.litarvan.krobot.motor.discord.DiscordConversation;
import fr.litarvan.krobot.motor.discord.DiscordUser;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.sdd.shenron.AutoFail;
import org.sdd.shenron.util.MessageEditor;
import org.sdd.shenron.util.MessageSearch;


import static fr.litarvan.krobot.util.KrobotFunctions.*;

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
        if (!(cCaller instanceof MessageCommandCaller))
        {
            return;
        }

        MessageCommandCaller caller = ((MessageCommandCaller) cCaller);
        User user = caller.getUser();
        Conversation conversation = caller.getConversation();
        Message message = MessageSearch.findLastMessageOf(user, conversation);

        if (message == null)
        {
            conversation.sendMessage(mention(user) + " Can't find any message sent by you in the last one");
            return;
        }

        if (args.size() == 0)
        {
            message.delete();
            return;
        }

        String result = null;

        if (args.size() == 1)
        {
            result = AutoFail.correct(message, args.get(0));
        }
        else if (args.size() == 2)
        {
            result = message.getText().replace(args.get(0), args.get(1));
        }

        MessageEditor.edit(user, conversation, message, result);
    }
}
