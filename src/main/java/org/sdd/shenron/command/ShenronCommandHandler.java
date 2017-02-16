package org.sdd.shenron.command;

import fr.litarvan.krobot.command.Command;
import fr.litarvan.krobot.command.CommandHandler;
import fr.litarvan.krobot.command.ICommandCaller;
import fr.litarvan.krobot.command.message.MessageCommandCaller;
import fr.litarvan.krobot.exception.CommandException;
import fr.litarvan.krobot.message.IMessageListener;
import fr.litarvan.krobot.message.MessageReceivedEvent;
import fr.litarvan.krobot.util.KrobotFunctions;
import java.util.List;

public class ShenronCommandHandler extends CommandHandler implements IMessageListener
{
    // TODO: KROBOT: Make MessageCommandHAndler and more: open
    private String prefix;

    public ShenronCommandHandler(String prefix)
    {
        super();

        this.prefix = prefix;
    }

    @Override
    public void handleBadSyntax(ICommandCaller cCaller, Command command, List<String> list)
    {
        MessageCommandCaller caller = (MessageCommandCaller) cCaller;

        if (command instanceof ShenronCommand && !((ShenronCommand) command).isRightServer(caller))
        {
            return;
        }

        String message = KrobotFunctions.mention(caller.getUser()) + " Syntax : /" + command.getCommand() + " " + command.getSyntax();

        caller.getConversation().sendMessage(message);
    }

    @Override
    public void handleUnknownCommand(ICommandCaller iCommandCaller, String s, List<String> list)
    {
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        if (event.getMessage().getText().startsWith(prefix))
        {
            MessageCommandCaller caller = new MessageCommandCaller(event.getUser(), event.getMessage(), event.getConversation());

            try
            {
                this.handle(event.getMessage().getText().substring(prefix.length()), caller);
            }
            catch (CommandException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onPrivateMessageReceived(MessageReceivedEvent event)
    {
        onMessageReceived(event);
    }

    public String getPrefix()
    {
        return prefix;
    }
}
