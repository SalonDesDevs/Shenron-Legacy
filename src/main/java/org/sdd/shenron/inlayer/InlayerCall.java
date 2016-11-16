package org.sdd.shenron.inlayer;

import fr.litarvan.krobot.command.message.MessageCommandCaller;
import java.util.List;

public class InlayerCall
{
    private InlayerCommand command;
    private List<String> args;
    private int pos;
    private MessageCommandCaller caller;

    public InlayerCall(InlayerCommand command, List<String> args, int pos, MessageCommandCaller caller)
    {
        this.command = command;
        this.args = args;
        this.pos = pos;
        this.caller = caller;
    }

    public InlayerCommand getCommand()
    {
        return command;
    }

    public List<String> getArgs()
    {
        return args;
    }

    public int getPos()
    {
        return pos;
    }

    public MessageCommandCaller getCaller()
    {
        return caller;
    }
}
