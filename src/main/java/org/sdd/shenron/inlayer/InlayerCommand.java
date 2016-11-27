package org.sdd.shenron.inlayer;

import fr.litarvan.krobot.command.Command;
import fr.litarvan.krobot.command.ICommandCaller;
import fr.litarvan.krobot.command.message.MessageCommandCaller;
import fr.litarvan.krobot.motor.Message;
import java.util.List;
import org.sdd.shenron.command.ShenronCommand;

public abstract class InlayerCommand extends ShenronCommand
{
    public abstract char getShortcut();

    @Override
    public final void handle(ICommandCaller commandCaller, List<String> args)
    {
    }

    public abstract String handle(MessageCommandCaller caller, String message, Message source, StringParser pos, List<String> args) throws Exception;
}
