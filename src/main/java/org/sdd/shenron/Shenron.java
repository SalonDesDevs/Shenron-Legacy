package org.sdd.shenron;

import fr.litarvan.krobot.bot.*;
import fr.litarvan.krobot.command.Command;
import fr.litarvan.krobot.command.ICommandCaller;
import fr.litarvan.krobot.command.message.MessageCommandCaller;
import fr.litarvan.krobot.command.message.MessageCommandHandler;
import fr.litarvan.krobot.console.ConsoleCommandCaller;
import fr.litarvan.krobot.message.MessageReceivedEvent;
import fr.litarvan.krobot.motor.IMotorExtension;
import fr.litarvan.krobot.motor.discord.DiscordUser;
import fr.litarvan.krobot.util.PermissionManager;
import java.io.File;
import java.util.Date;
import java.util.List;
import net.dv8tion.jda.entities.User;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jetbrains.annotations.NotNull;
import org.sdd.shenron.command.*;


import static fr.litarvan.krobot.util.KrobotFunctions.*;
import static fr.litarvan.krobot.util.Markdown.*;

public class Shenron extends Bot
{
    public static final String VERSION = "1.0.0";
    public static final String PREFIX = "/";

    private File folder;
    private File permissionsFile;

    private MessageCommandHandler commandHandler = new MessageCommandHandler(PREFIX);
    private PermissionManager permissionManager = new PermissionManager();

    @Override
    public void onStart(StartEvent startEvent)
    {
        info("Starting Shenron v" + VERSION);

        folder = new File(krobot().getFolder(), "shenron");
        permissionsFile = new File(folder, "permissions.json");

        folder.mkdirs();

        if (permissionsFile.exists())
        {
            permissionManager.load(permissionsFile);
        }

        commandHandler.register(new CommandChuck(), new CommandCrashTest(), new CommandHelp(), new CommandVersion());

        addMessageListener(commandHandler);

        info("Shenron started");
    }

    @Override
    public void onStop(StopEvent event)
    {
        super.onStop(event);

        // TODO: KROBOT: Set bot to null only when fully stopped
        logger().info("[Shenron] Stopping Shenron");

        permissionManager.save(permissionsFile);

        logger().info("[Shenron] Shenron stopped");
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        super.onMessageReceived(event);

        if (event.getMessage().getText().trim().toLowerCase().replace('î', 'i').contains("shenron apparait"))
        {
            event.getConversation().sendMessage("https://giphy.com/gifs/dragon-ball-z-GCBuPi2YPNcxG");
            event.getConversation().sendMessage(mdBold("UN HUMAIN TEL QUE VOUS N\'EST PAS APTE A FAIRE APPARAITRE SHENRON !"));

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

    public static void handleCommandException(@NotNull ICommandCaller caller, @NotNull Command command, List<String> args, Exception ex)
    {
        // TODO: Send message to the admin

        if (caller instanceof MessageCommandCaller)
        {
            MessageCommandCaller commandCaller = (MessageCommandCaller) caller;
            User user = (((DiscordUser) commandCaller.getUser())).getUser(); // TODO: KROBOT: Make User#getPrivateConversation

            commandCaller.getConversation().sendMessage("Sorry ! An exception was thrown while executing command " + PREFIX + command.getCommand() + "\nI sent you a crash report, " + mdUnderline("send it to the developers asap !"));

            String report = "####################################\n" +
                            "\n" +
                            "Shenron command crash report\n" +
                            "\n" +
                            "Version : " + bot().getVersion() + "\n" +
                            "Command : /" + command.getCommand() + " (" + command.getClass().getName() + ")\n" +
                            "Args : " + String.join("; ", args) + "\n" +
                            "Caller : " + user.getUsername() + "\n" +
                            "Time : " + new Date() + "\n" +
                            "\n" +
                            ExceptionUtils.getStackTrace(ex) + "\n" +
                            "\n" +
                            "####################################";

            user.getPrivateChannel().sendMessage(report);
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
