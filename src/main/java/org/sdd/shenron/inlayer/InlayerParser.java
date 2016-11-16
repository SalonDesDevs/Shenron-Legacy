package org.sdd.shenron.inlayer;

import fr.litarvan.krobot.command.message.MessageCommandCaller;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class InlayerParser
{
    public static final char SINGLE_ARGUMENT = '!';
    public static final char MULTIPLE_ARGUMENT = '{';

    public static final char MULTIPLE_ARGUMENT_END = '}';
    public static final char MULTIPLE_ARGUMENT_SEPARATOR = ',';

    public static final char[] COMMAND_STOP_LIST = new char[] {SINGLE_ARGUMENT, MULTIPLE_ARGUMENT};
    public static final char[] ARGS_STOP_LIST = new char[] {MULTIPLE_ARGUMENT_SEPARATOR, MULTIPLE_ARGUMENT_END};

    public static final char ESCAPE_CHAR = '\\';

    private MessageCommandCaller caller;
    private char prefix;
    private InlayerCommandHandler handler;

    private List<InlayerCall> calls;
    private StringParser parser;
    private String result;
    private int toRemove = 0;

    public InlayerParser(MessageCommandCaller caller, String message, char prefix, InlayerCommandHandler handler)
    {
        this.caller = caller;
        this.prefix = prefix;
        this.handler = handler;

        this.parser = new StringParser(message, ESCAPE_CHAR);
        this.result = "";
    }

    public Pair<String, List<InlayerCall>> get()
    {
        calls = new ArrayList<>();
        while (parse());

        return new ImmutablePair<>(result, calls);
    }

    private boolean parse()
    {
        result += parser.waitFor(prefix);

        if (parser.done())
        {
            return false;
        }

        int oldIndex = parser.index();

        int index = parser.index() - toRemove;
        String commandName = parser.nextToken(COMMAND_STOP_LIST);
        char callType = parser.current();
        List<String> args = getArgs(callType);
        InlayerCommand command = findCommand(commandName);

        toRemove += parser.index() - oldIndex;

        calls.add(new InlayerCall(command, args, index, caller));

        if (!parser.done())
        {
            result += parser.current();
        }

        return !parser.done();
    }

    private List<String> getArgs(char callType)
    {
        List<String> args = new ArrayList<>();

        if (callType == SINGLE_ARGUMENT)
        {
            args.add(parser.nextToken(StringParser.SPACE_STOP_LIST));
        }
        else if (callType == MULTIPLE_ARGUMENT)
        {
            while (parser.current() != MULTIPLE_ARGUMENT_END)
            {
                args.add(parser.nextToken(ARGS_STOP_LIST));
            }
        }

        return args;
    }

    private InlayerCommand findCommand(String name)
    {
        for (InlayerCommand command : handler.getCommands())
        {
            if (command.getCommand().equalsIgnoreCase(name) || (name.length() != 0 && command.getShortcut() == name.charAt(0)))
            {
                return command;
            }
        }

        return null;
    }
}
