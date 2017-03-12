package org.sdd.shenron.command;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.litarvan.krobot.command.message.MessageCommandCaller;
import fr.litarvan.krobot.motor.discord.DiscordConversation;
import fr.litarvan.krobot.motor.discord.DiscordMessage;
import fr.litarvan.krobot.motor.discord.DiscordUser;
import fr.litarvan.krobot.util.Markdown;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.requests.RestAction;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.sdd.shenron.Group;
import org.sdd.shenron.GroupTrigger;
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
        return "list | join <group> | create <group> [channel-name] | leave [group] | trigger <message> <emote/group...>";
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
        else if (command.equals("trigger"))
        {
            return size > 2;
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
            case "trigger":
                List<String> groups = args.subList(2, args.size());
                trigger(caller, args.get(1), groups.toArray(new String[groups.size()]));
                break;
        }
    }

    private void list(MessageCommandCaller caller)
    {
        DiscordConversation conversation = (DiscordConversation) caller.getConversation();

        String message = Markdown.mdUnderline("Liste des groupes :") + "\n\n";

        for (Group group : groups)
        {
            message += "    - " + group.getName() + (group.getChannel() != null ? " ( #" + group.getChannel() + " )" : "") + "\n";
        }

        conversation.getChannel().sendMessage(message).queue();
    }

    private void join(MessageCommandCaller caller, String group) throws RateLimitedException
    {
        DiscordConversation conversation = (DiscordConversation) caller.getConversation();

        group = group.trim();

        boolean exists = false;

        for (Group g : groups)
        {
            if (g.getName().trim().equalsIgnoreCase(group.trim()))
            {
                exists = true;
            }
        }

        if (!exists)
        {
            conversation.getChannel().sendMessage(mention(caller.getUser()) + " Ce rôle n'est pas un rôle de groupe").queue();
            return;
        }

        Guild guild = ((DiscordMessage) caller.getMessage()).getMessage().getGuild();
        List<Role> roles = guild.getRolesByName(group, true);

        if (roles.size() == 0)
        {
            conversation.getChannel().sendMessage(mention(caller.getUser()) + " Groupe inconnu '" + group).queue();
            return;
        }

        Role role = roles.get(0);
        guild.getController().addRolesToMember(guild.getMember(((DiscordUser) caller.getUser()).getUser()), role).queue();

        conversation.getChannel().sendMessage(mention(caller.getUser()) + " a été ajouté au groupe '" + group + "'").queue();
    }

    private void create(MessageCommandCaller caller, String name, String channelName) throws RateLimitedException
    {
        DiscordConversation conversation = (DiscordConversation) caller.getConversation();

        if (!shenron.getPermissionManager().hasOrFail(caller.getUser(), "admin_sdd", caller.getConversation(), "Seul un administrateur peut créer un groupe"))
        {
            return;
        }

        channelName = channelName == null ? name.trim().toLowerCase() : channelName.trim().toLowerCase();

        if (ArrayUtils.contains(groups, name.trim()))
        {
            conversation.getChannel().sendMessage(mention(caller.getUser()) + " Un groupe du même nom existe déjà").queue();
            return;
        }

        Guild guild = ((DiscordMessage) caller.getMessage()).getMessage().getGuild();
        String finalChannelName = channelName;

        guild.getController().createRole().queue((role) -> {
            Role member = guild.getRolesByName("Membre", true).get(0);

            guild.getController().createTextChannel(finalChannelName).queue((channel) -> {
                channel.createPermissionOverride(member).queue((override) -> {
                    override.getManager().deny(Permission.MESSAGE_READ).queue();
                });

                channel.createPermissionOverride(role).queue((override) -> {
                    override.getManager().grant(Permission.MESSAGE_READ).queue();
                });
            });

            role.getManager().setName(name).queue();
            role.getManager().setMentionable(true).queue();

            setGroups(ArrayUtils.add(groups, new Group(name, finalChannelName)));

            conversation.getChannel().sendMessage(mention(caller.getUser()) + " Groupe '" + name + "' créé (channel #" + finalChannelName + " )").queue();
        });
    }

    private void leave(MessageCommandCaller caller, String group) throws RateLimitedException
    {
        DiscordConversation conversation = (DiscordConversation) caller.getConversation();

        if (group == null)
        {
            for (Group g : groups)
            {
                if (g.getChannel() != null && g.getChannel().trim().equalsIgnoreCase(caller.getConversation().getName().trim()))
                {
                    group = g.getName();
                    break;
                }
            }
        }

        if (group == null)
        {
            conversation.getChannel().sendMessage(mention(caller.getUser()) + " Le channel dans lequel vous êtes n'est pas un channel de groupe").queue();
            return;
        }

        group = group.trim();

        Guild guild = ((DiscordMessage) caller.getMessage()).getMessage().getGuild();
        List<Role> roles = guild.getRolesByName(group, true);

        if (roles.size() == 0)
        {
            conversation.getChannel().sendMessage(mention(caller.getUser()) + " Groupe inconnu '" + group).queue();
            return;
        }

        Role role = roles.get(0);
        String finalGroup = group;

        guild.getController().removeRolesFromMember(guild.getMember(((DiscordUser) caller.getUser()).getUser()), role).queue((z) -> {
            conversation.getChannel().sendMessage(mention(caller.getUser()) + " a été supprimé du groupe '" + finalGroup + "'").queue();
        });
    }

    private void trigger(MessageCommandCaller caller, String message, String[] groups) throws RateLimitedException
    {
        DiscordConversation conversation = (DiscordConversation) caller.getConversation();

        conversation.getChannel().sendMessage(message).queue((msg) -> {
            List<ImmutablePair<String, String>> entries = new ArrayList<>();

            for (String group : groups)
            {
                String[] split = group.split("\\/");

                String groupName = split[0].replace("#", "/");
                String emoteName = split[1];

                List<Emote> emotes = conversation.getChannel().getJDA().getEmotesByName(emoteName, true);

                if (emotes.size() == 0)
                {
                    conversation.getChannel().sendMessage(mention(caller.getUser()) + " Impossible de trouver l'emote '" + emoteName + "'").queue();
                    msg.deleteMessage().queue();

                    return;
                }

                Group gr = null;

                for (Group g : this.groups)
                {
                    if (g.getName().trim().equalsIgnoreCase(groupName.trim()))
                    {
                        gr = g;
                        break;
                    }
                }

                if (gr == null)
                {
                    conversation.getChannel().sendMessage(mention(caller.getUser()) + " Impossible de trouver le groupe '" + groupName + "'").queue();
                    msg.deleteMessage().queue();

                    return;
                }

                Emote emote = emotes.get(0);

                msg.addReaction(emote).queue();
                entries.add(new ImmutablePair<>(emote.getId(), gr.getName()));
            }

            shenron.getGroupListener().addTrigger(new GroupTrigger(msg.getId(), entries));
        });
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

    public Group[] getGroups()
    {
        return groups;
    }

    @Override
    public String getServer()
    {
        return "Salon des développeurs";
    }
}
