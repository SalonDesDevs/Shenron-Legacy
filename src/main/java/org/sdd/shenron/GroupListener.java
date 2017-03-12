package org.sdd.shenron;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.litarvan.krobot.motor.discord.DiscordExtension;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageReaction;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.Pair;

public class GroupListener extends DiscordExtension
{
    private Shenron shenron;
    private File triggersFile;
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private GroupTrigger[] triggers;

    public GroupListener(Shenron shenron)
    {
        this.shenron = shenron;
        this.triggersFile = new File(shenron.getFolder(), "group_triggers.json");

        if (!triggersFile.exists())
        {
            setTriggers(new GroupTrigger[] {});
            return;
        }

        BufferedReader reader = null;

        try
        {
            this.triggers = gson.fromJson(reader = new BufferedReader(new FileReader(triggersFile)), GroupTrigger[].class);
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

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event)
    {
        super.onMessageReactionAdd(event);

        if (event.getUser().isBot())
        {
            return;
        }

        event.getChannel().getMessageById(event.getMessageId()).queue((message) -> {
            Role role = getRole(message, event.getReaction());
            Guild guild = message.getGuild();

            if (role == null)
            {
                return;
            }

            guild.getController().addRolesToMember(guild.getMember(event.getUser()), role).queue();
        });
    }

    @Override
    public void onMessageReactionRemove(MessageReactionRemoveEvent event)
    {
        super.onMessageReactionRemove(event);

        if (event.getUser().isBot())
        {
            return;
        }

        event.getChannel().getMessageById(event.getMessageId()).queue((message) -> {
            Role role = getRole(message, event.getReaction());
            Guild guild = message.getGuild();

            if (role == null)
            {
                return;
            }

            guild.getController().removeRolesFromMember(guild.getMember(event.getUser()), role).queue();
        });
    }

    private Role getRole(Message message, MessageReaction reaction)
    {
        GroupTrigger trigger = null;
        Pair<String, String> group = null;

        for (GroupTrigger tr : triggers)
        {
            if (message.getId().equals(tr.getMessageId()))
            {
                trigger = tr;
                break;
            }
        }

        if (trigger == null)
        {
            return null;
        }

        for (Pair<String, String> pair : trigger.getGroups())
        {
            if (pair.getKey().equals(reaction.getEmote().getId()))
            {
                group = pair;
                break;
            }
        }

        if (group == null)
        {
            message.deleteMessage().queue();
            return null;
        }

        List<Role> roles = message.getGuild().getRolesByName(group.getValue(), true);

        if (roles.size() == 0)
        {
            message.deleteMessage().queue();
            return null;
        }

        return roles.get(0);
    }

    public void setTriggers(GroupTrigger[] triggers)
    {
        this.triggers = triggers;

        BufferedWriter writer = null;

        try
        {
            writer = new BufferedWriter(new FileWriter(triggersFile));
            writer.write(gson.toJson(triggers));
        }
        catch (IOException e)
        {
            shenron.error("Can't save triggers !");
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

    public void addTrigger(GroupTrigger message)
    {
        setTriggers(ArrayUtils.add(triggers, message));
    }
}
