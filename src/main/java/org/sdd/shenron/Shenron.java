package org.sdd.shenron;

import fr.litarvan.krobot.bot.*;
import fr.litarvan.krobot.command.message.MessageCommandHandler;
import fr.litarvan.krobot.message.MessageReceivedEvent;
import fr.litarvan.krobot.motor.IConversation;
import fr.litarvan.krobot.motor.IMotorExtension;
import fr.litarvan.krobot.util.Markdown;
import fr.litarvan.krobot.util.MessageQueue;
import fr.litarvan.krobot.util.PermissionManager;
import java.io.File;
import java.util.concurrent.Future;
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
    public static final char AUTO_DETECT_START = ';';
    public static final char INLAYER_PREFIX = '#';
    public static final long MESSAGE_INTERVAL = 2000L;

    private File folder = new File(krobot().getFolder(), "shenron");
    private File permissionsFile = new File(folder, "permissions.json");

    private MessageCommandHandler commandHandler = new MessageCommandHandler(PREFIX);
    private PermissionManager permissionManager = new PermissionManager();
    private InlayerCommandHandler inlayerCommandHandler = new InlayerCommandHandler(AUTO_DETECT_START, INLAYER_PREFIX);
    private AutoFail autoFail = new AutoFail();
    private MessageQueue queue = new MessageQueue(MESSAGE_INTERVAL);

    @Override
    public void onStart(StartEvent startEvent)
    {
        info("Starting Shenron v" + VERSION);

        if (permissionsFile.exists())
        {
            permissionManager.load(permissionsFile);
        }

        commandHandler.register(new CommandHelp(),
                                new CommandVersion(),
                                new CommandWordReact(),
                                new CommandFail());

        inlayerCommandHandler.register(new InlayerCommandQuote(),
                                       new InlayerCommandMarkdown("bold", 'b', Markdown.BOLD, "bold"),
                                       new InlayerCommandMarkdown("italic", 'i', Markdown.EMPHASIS, "italic"),
                                       new InlayerCommandMarkdown("underline", 'u', Markdown.UNDERLINE, "underline"),
                                       new InlayerCommandMarkdown("strike", 's', Markdown.STRIKE, "strike"));

        addMessageListener(commandHandler);
        addMessageListener(inlayerCommandHandler);
        addMessageListener(autoFail);

        info("Shenron started");

        // TODO: Shenron v1.0.0 -> Delete Chuck and CrashTest, lang, permission check, enable/disable inlayer/auto-fail
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
            sendMessage(mdBold("UN HUMAIN TEL QUE VOUS N'EST PAS APTE A FAIRE APPARAITRE SHENRON !"), event.getConversation());

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
