package org.sdd.shenron.inlayer;

import fr.litarvan.krobot.command.Command;
import fr.litarvan.krobot.command.ICommandCaller;
import fr.litarvan.krobot.command.message.MessageCommandCaller;
import fr.litarvan.krobot.motor.Message;
import java.util.List;

public abstract class InlayerCommand extends Command
{
    public abstract char getShortcut();

    @Override
    public final void handleCall(ICommandCaller iCommandCaller, List<String> list)
    {
    }

    public abstract String handle(MessageCommandCaller caller, String message, Message source, StringParser pos, List<String> args);
}
