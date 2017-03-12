package org.sdd.shenron;

import fr.litarvan.krobot.bot.*;
import fr.litarvan.krobot.command.ICommandCaller;
import fr.litarvan.krobot.command.message.MessageCommandCaller;
import fr.litarvan.krobot.motor.IConversation;
import fr.litarvan.krobot.motor.IMotorExtension;
import fr.litarvan.krobot.motor.User;
import fr.litarvan.krobot.motor.discord.DiscordMessage;
import fr.litarvan.krobot.motor.discord.DiscordMotor;
import fr.litarvan.krobot.motor.discord.DiscordStartEvent;
import fr.litarvan.krobot.motor.discord.DiscordUser;
import fr.litarvan.krobot.util.Config;
import fr.litarvan.krobot.util.Configurator;
import fr.litarvan.krobot.util.Markdown;
import fr.litarvan.krobot.util.MessageQueue;
import fr.litarvan.krobot.util.PermissionManager;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import kotlin.Unit;
import net.dv8tion.jda.core.entities.Member;
import org.jetbrains.annotations.NotNull;
import org.sdd.shenron.command.*;


import static fr.litarvan.krobot.util.KrobotFunctions.*;

public class Shenron extends Bot
{
    public static final String VERSION = "2.0.0";
    public static final String INVOKER = "Shenron apparait";
    public static final String FAQ = "http://support.manercraft.fr/";
    public static final long MESSAGE_INTERVAL = 2000L;

    public static final Map<String, String> CONFIG = new HashMap<String, String>() {{
        put("admins", "Who are the admins ? (Use mentions, separated each by a comma (,))");
        put("prefix", "Command prefix ?");
    }};

    private File folder = new File(krobot().getFolder(), "shenron");
    private File configFile = new File(folder, "config.json");
    private File permissionsFile = new File(folder, "permissions.json");

    private String prefix;

    private ShenronCommandHandler commandHandler;
    private Configurator configurator;

    private Config config = new Config(configFile);
    private PermissionManager permissionManager = new PermissionManager();
    private SummonListener summon = new SummonListener(INVOKER);
    private GroupListener groupListener;
    private CommandGroup commandGroup;

    @Override
    public void onStart(StartEvent startEvent)
    {
        info("Starting Shenron v" + VERSION);

        groupListener = new GroupListener(this);

        if (configFile.exists())
        {
            setup();
            permissionManager.load(permissionsFile);
        }
        else
        {
            configurator = new Configurator(this, CONFIG);
            configurator.init();

            configurator.onFinish((config, event) -> {
                event.getConversation().sendMessage("Please wait...");

                String[] admins = config.values().get("admins").split(",");
                for (String admin : admins)
                {
                    String name = admin.trim();

                    if (name.startsWith("@"))
                    {
                        name = name.substring(1);
                    }

                    User user = event.getConversation().userByName(name);

                    if (user == null)
                    {
                        event.getConversation().sendMessage("Unable to find user '" + name + "'");
                        continue;
                    }

                    event.getConversation().sendMessage(mention(user) + " is now admin");
                    permissionManager.addPermission(user, "admin");
                }

                permissionManager.addPermission(event.getUser(), "owner", "admin");
                permissionManager.save(permissionsFile);

                this.config.set("prefix", prefix = config.values().get("prefix"));
                this.config.save();

                setup();

                event.getConversation().sendMessage(Markdown.mdBold("Shenron v" + VERSION + " set up, thanks for using it !"));

                return Unit.INSTANCE;
            });
        }

        info("Shenron started");
    }

    private void setup()
    {
        if (prefix == null)
        {
            prefix = (String) config.get("prefix");
        }

        commandHandler = new ShenronCommandHandler(prefix);

        commandHandler.register(new CommandHelp(),
                                new CommandVersion(),
                                new CommandWordReact(),
                                new CommandTextToEmoji(),
                                new CommandOsef(),
                                new CommandFAQ(FAQ),
                                commandGroup = new CommandGroup(),
                                new CommandAddAdmin(),
                                new CommandTriggered(),
                                new CommandRole());



        addMessageListener(summon);
        addMessageListener(commandHandler);
    }

    @Override
    public void onStop(StopEvent event)
    {
        super.onStop(event);

        info("Stopping Shenron");

        permissionManager.save(permissionsFile);

        info("Shenron stopped");
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

    public File getFolder()
    {
        return folder;
    }

    public Config getConfig()
    {
        return config;
    }

    public boolean isAdmin(User user)
    {
        return permissionManager.hasPermission(user, "admin");
    }

    public PermissionManager getPermissionManager()
    {
        return permissionManager;
    }

    public static Member getMemberOfGuild(String name, MessageCommandCaller caller)
    {
        List<Member> members = ((DiscordMessage) caller.getMessage()).getMessage().getGuild().getMembersByEffectiveName(name, true);

        if (members.size() == 0)
        {
            caller.getConversation().sendMessage(mention(caller.getUser()) + " Can't find user '" + name + "'");
            return null;
        }

        return members.get(0);
    }

    public static User getUserOfGuild(String name, MessageCommandCaller caller)
    {
        Member member = getMemberOfGuild(name, caller);
        return member == null ? null : new DiscordUser(member.getUser());
    }

    @NotNull
    @Override
    public IMotorExtension[] getExtensions()
    {
        return new IMotorExtension[] {
            groupListener
        };
    }

    public ShenronCommandHandler getCommandHandler()
    {
        return commandHandler;
    }

    public GroupListener getGroupListener()
    {
        return groupListener;
    }

    public CommandGroup getCommandGroup()
    {
        return commandGroup;
    }

    @NotNull
    public static Shenron get()
    {
        return (Shenron) bot();
    }
}
