package org.sdd.shenron.command;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.litarvan.krobot.command.message.MessageCommandCaller;
import fr.litarvan.krobot.motor.discord.DiscordMessage;
import fr.litarvan.krobot.motor.discord.DiscordUser;
import fr.litarvan.krobot.util.KrobotFunctions;
import fr.litarvan.krobot.util.Markdown;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.sdd.shenron.Group;
import org.sdd.shenron.Shenron;


import static fr.litarvan.krobot.util.KrobotFunctions.*;

public class CommandGroup extends ShenronCommand
{
    private Shenron shenron = Shenron.get();
    private File groupsFile = new File(shenron.getFolder(), "groups.json");
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private Group[] groups;

    public CommandGroup()
    {
        if (!groupsFile.exists())
        {
            setGroups(new Group[] {});
            return;
        }

        BufferedReader reader = null;

        try
        {
            this.groups = gson.fromJson(reader = new BufferedReader(new FileReader(groupsFile)), Group[].class);
        }
        catch (FileNotFoundException e)
        {
            throw new RuntimeException("Can't load groups file");
        }
        finally
        {
            if (reader != null)
            {
                try
                {
                    reader.close();
                }
                catch (IOException e)
                {
                }
            }
        }
    }

    @NotNull
    @Override
    public String getCommand()
    {
        return "group";
    }

    @NotNull
    @Override
    public String getDescription()
    {
        return "Pour gerer les groupes du SDD (en rejoindre un, en créer un (admin), etc...)";
    }

    @NotNull
    @Override
    public String getSyntax()
    {
        return "list | join <group> | create <group> [channel-name] | leave [group]";
    }

    @Override
    public boolean checkSyntax(List<String> args)
    {
        int size = args.size();

        if (size == 0)
        {
            return false;
        }

        String command = args.get(0);

        if (command.equals("list"))
        {
            return size == 1;
        }
        else if (command.equals("join"))
        {
            return size == 2;
        }
        else if (command.equals("create"))
        {
            return size == 2 || size == 3;
        }
        else if (command.equals("leave"))
        {
            return size == 1 || size == 2;
        }
        else
        {
            return false;
        }
    }

    @Override
    public void handle(MessageCommandCaller caller, List<String> args) throws Exception
    {
        String command = args.get(0);

        switch (command)
        {
            case "list":
                list(caller);
                break;
            case "join":
                join(caller, args.get(1));
                break;
            case "create":
                create(caller, args.get(1), args.size() == 3 ? args.get(2) : null);
                break;
            case "leave":
                leave(caller, args.size() == 2 ? args.get(1) : null);
                break;
        }
    }

    private void list(MessageCommandCaller caller)
    {
        String message = Markdown.mdUnderline("Liste des groupes :") + "\n\n";

        for (Group group : groups)
        {
            message += "    - " + group.getName() + (group.getChannel() != null ? " ( #" + group.getChannel() + " )" : "") + "\n";
        }

        caller.getConversation().sendMessage(message);
    }

    private void join(MessageCommandCaller caller, String group) throws RateLimitedException
    {
        group = group.trim();

        Guild guild = ((DiscordMessage) caller.getMessage()).getMessage().getGuild();
        List<Role> roles = guild.getRolesByName(group, true);

        if (roles.size() == 0)
        {
            caller.getConversation().sendMessage(mention(caller.getUser()) + " Groupe inconnu '" + group);
            return;
        }

        Role role = roles.get(0);
        guild.getController().addRolesToMember(guild.getMember(((DiscordUser) caller.getUser()).getUser()), role).block();

        caller.getConversation().sendMessage(mention(caller.getUser()) + " a été ajouté au groupe '" + group + "'");
    }

    private void create(MessageCommandCaller caller, String name, String channelName) throws RateLimitedException
    {
        shenron.getPermissionManager().hasOrFail(caller.getUser(), "admin", caller.getConversation(), "Seul un administrateur peut créer un groupe");

        channelName = channelName == null ? name.trim().toLowerCase() : channelName.trim().toLowerCase();

        if (ArrayUtils.contains(groups, name.trim()))
        {
            caller.getConversation().sendMessage(mention(caller.getUser()) + " Un groupe du même nom existe déjà");
            return;
        }

        Guild guild = ((DiscordMessage) caller.getMessage()).getMessage().getGuild();
        Role role = guild.getController().createRole().block();
        Role member = guild.getRolesByName("Membre", true).get(0);
        TextChannel channel = guild.getController().createTextChannel(channelName).block();

        role.getManager().setName(name).block();
        role.getManager().setMentionable(true).block();

        channel.createPermissionOverride(member).block().getManager().deny(Permission.MESSAGE_READ).block();
        channel.createPermissionOverride(role).block().getManager().grant(Permission.MESSAGE_READ).block();

        setGroups(ArrayUtils.add(groups, new Group(name, channelName)));

        caller.getConversation().sendMessage(mention(caller.getUser()) + " Groupe '" + name + "' créé (channel #" + channelName + " )");
    }

    private void leave(MessageCommandCaller caller, String group) throws RateLimitedException
    {
        if (group == null)
        {
            for (Group g : groups)
            {
                if (g.getChannel().trim().equalsIgnoreCase(caller.getConversation().getName().trim()))
                {
                    group = g.getName();
                    break;
                }
            }
        }

        group = group.trim();

        Guild guild = ((DiscordMessage) caller.getMessage()).getMessage().getGuild();
        List<Role> roles = guild.getRolesByName(group, true);

        if (roles.size() == 0)
        {
            caller.getConversation().sendMessage(mention(caller.getUser()) + " Groupe inconnu '" + group);
            return;
        }

        Role role = roles.get(0);
        guild.getController().removeRolesFromMember(guild.getMember(((DiscordUser) caller.getUser()).getUser()), role).block();

        caller.getConversation().sendMessage(mention(caller.getUser()) + " a été supprimé du groupe '" + group + "'");
    }

    public void setGroups(Group[] groups)
    {
        this.groups = groups;

        BufferedWriter writer = null;
        try
        {
            writer = new BufferedWriter(new FileWriter(groupsFile));
            writer.write(gson.toJson(groups));
        }
        catch (IOException e)
        {
            shenron.error("Can't save groups !");
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (writer != null)
                {
                    writer.close();
                }
            }
            catch (IOException e)
            {
            }
        }
    }

    @Override
    public String getServer()
    {
        return "Salon des développeurs";
    }
}
