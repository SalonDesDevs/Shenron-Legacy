package org.sdd.shenron;

import fr.litarvan.krobot.bot.*;
import fr.litarvan.krobot.command.Command;
import fr.litarvan.krobot.command.ICommandCaller;
import fr.litarvan.krobot.command.message.MessageCommandCaller;
import fr.litarvan.krobot.command.message.MessageCommandHandler;
import fr.litarvan.krobot.console.ConsoleCommandCaller;
import fr.litarvan.krobot.message.MessageReceivedEvent;
import fr.litarvan.krobot.motor.IConversation;
import fr.litarvan.krobot.motor.IMotorExtension;
import fr.litarvan.krobot.motor.User;
import fr.litarvan.krobot.motor.discord.DiscordStartEvent;
import fr.litarvan.krobot.util.Markdown;
import fr.litarvan.krobot.util.MessageQueue;
import fr.litarvan.krobot.util.PermissionManager;
import fr.minuskube.bot.discord.util.Webhook;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jetbrains.annotations.NotNull;
import org.sdd.shenron.command.*;
import org.sdd.shenron.inlayer.InlayerCommandHandler;
import org.sdd.shenron.inlayer.command.InlayerCommandMarkdown;
import org.sdd.shenron.inlayer.command.InlayerCommandQuote;


import static fr.litarvan.krobot.util.KrobotFunctions.*;
import static fr.litarvan.krobot.util.Markdown.*;

public class Shenron extends Bot
{
    public static final String VERSION = "1.0.0";
    public static final String PREFIX = "/";
    public static final char INLAYER_START = ';';
    public static final char INLAYER_PREFIX = '#';
    public static final long MESSAGE_INTERVAL = 2000L;

    private File folder = new File(krobot().getFolder(), "shenron");
    private File permissionsFile = new File(folder, "permissions.json");

    private MessageCommandHandler commandHandler = new MessageCommandHandler(PREFIX);
    private PermissionManager permissionManager = new PermissionManager();
    private InlayerCommandHandler inlayerCommandHandler = new InlayerCommandHandler(INLAYER_START, INLAYER_PREFIX);
    private MessageQueue queue = new MessageQueue(MESSAGE_INTERVAL);

    @Override
    public void onStart(StartEvent startEvent)
    {
        info("Starting Shenron v" + VERSION);

        if (permissionsFile.exists())
        {
            permissionManager.load(permissionsFile);
        }

        commandHandler.register(new CommandChuck(), new CommandCrashTest(), new CommandHelp(), new CommandVersion(), new CommandWordReact());
        inlayerCommandHandler.register(new InlayerCommandQuote(),
                                       new InlayerCommandMarkdown("bold", 'b', Markdown.BOLD, "bold"),
                                       new InlayerCommandMarkdown("italic", 'i', Markdown.EMPHASIS, "italic"),
                                       new InlayerCommandMarkdown("underline", 'u', Markdown.UNDERLINE, "underline"),
                                       new InlayerCommandMarkdown("strike", 's', Markdown.STRIKE, "strike"));

        addMessageListener(commandHandler);
        addMessageListener(inlayerCommandHandler);

        info("Shenron started");
    }

    @Override
    public void onStop(StopEvent event)
    {
        super.onStop(event);

        info("Stopping Shenron");

        permissionManager.save(permissionsFile);

        info("Shenron stopped");
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        super.onMessageReceived(event);

        if (event.getMessage().getText().trim().toLowerCase().replace('î', 'i').contains("shenron apparait"))
        {
            sendMessage("https://giphy.com/gifs/dragon-ball-z-GCBuPi2YPNcxG", event.getConversation());
            sendMessage(mdBold("UN HUMAIN TEL QUE VOUS N\'EST PAS APTE A FAIRE APPARAITRE SHENRON !"), event.getConversation());

            return;
        }
    }

    public void info(String message)
    {
        logger().info(prefix() + " " + message);
    }

    public void error(String message)
    {
        logger().error(prefix() + " " + message);
    }

    public static void handleCommandException(@NotNull ICommandCaller caller, Command command, List<String> args, Exception ex)
    {
        // TODO: Send message to the admin

        if (caller instanceof MessageCommandCaller)
        {
            MessageCommandCaller commandCaller = (MessageCommandCaller) caller;
            User user = commandCaller.getUser();

            commandCaller.getConversation().sendMessage("Sorry ! An exception was thrown" + (command == null ? "" : " while executing command " + PREFIX + command.getCommand()) + "\nI sent you a crash report, " + mdUnderline("send it to the developers asap !"));

            String report = "####################################\n" +
                            "\n" +
                            "Shenron command crash report\n" +
                            "\n" +
                            "Version : " + bot().getVersion() + "\n" +
                            (command == null ? "" : "Command : /" + command.getCommand() + " (" + command.getClass().getName() + ")\n") +
                            (args == null ? "" : "Args    : " + String.join("; ", args) + "\n") +
                            "Caller  : " + user.getUsername() + "\n" +
                            "Time    : " + new Date() + "\n" +
                            "\n" +
                            ExceptionUtils.getStackTrace(ex) + "\n" +
                            "\n" +
                            "####################################";

            // TODO: KROBOT: JDA 96 + Emoji

            if (user.getPrivateConversation() != null)
            {
                for (String message : splitLongMessage(report, 2000 - mdChar(Markdown.CODE).length() * 2))
                {
                    Shenron.get().sendMessage(mdCode(message, ""), user.getPrivateConversation());

                    try
                    {
                        Thread.sleep(1000L);
                    }
                    catch (InterruptedException ignored)
                    {
                    }
                }
            }
        }
        else if (caller instanceof ConsoleCommandCaller)
        {
            logger().error(prefix() + " Exception thrown while executing command /" + command.getCommand() + " " + String.join(" ", args), ex);
        }
    }

    @NotNull
    @Override
    public String getName()
    {
        return "Shenron";
    }

    @NotNull
    @Override
    public String getIdentifier()
    {
        return "shenron";
    }

    @NotNull
    @Override
    public String getVersion()
    {
        return VERSION;
    }

    public Future<Void> sendMessage(String message, IConversation conversation)
    {
        return queue.push(message, conversation);
    }

    @NotNull
    @Override
    public IMotorExtension[] getExtensions()
    {
        return new IMotorExtension[0];
    }

    public MessageCommandHandler getCommandHandler()
    {
        return commandHandler;
    }

    @NotNull
    public static Shenron get()
    {
        return (Shenron) bot();
    }
}
