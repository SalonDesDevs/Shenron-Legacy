package org.sdd.shenron.command;

import fr.litarvan.krobot.command.ICommandCaller;
import fr.litarvan.krobot.command.message.MessageCommandCaller;
import fr.litarvan.krobot.motor.User;
import fr.litarvan.krobot.motor.discord.DiscordConversation;
import fr.litarvan.krobot.motor.discord.DiscordMessage;
import fr.litarvan.krobot.motor.discord.DiscordUser;
import fr.litarvan.krobot.util.KrobotFunctions;
import fr.litarvan.krobot.util.Markdown;
import java.util.List;
import joptsimple.internal.Strings;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import org.jetbrains.annotations.NotNull;
import org.sdd.shenron.Shenron;


import static fr.litarvan.krobot.util.KrobotFunctions.*;

public class CommandFAQ extends ShenronCommand
{
    private String link;

    public CommandFAQ(String link)
    {
        this.link = link;
    }

    @NotNull
    @Override
    public String getCommand()
    {
        return "faq";
    }

    @NotNull
    @Override
    public String getDescription()
    {
        return "Envoi le lien de la faq / Quand un utilisateur pose une question de cette derniere";
    }

    @NotNull
    @Override
    public String getSyntax()
    {
        return "[utilisateur]";
    }

    @Override
    public boolean checkSyntax(List<String> list)
    {
        return true;
    }

    @Override
    public void handle(MessageCommandCaller caller, List<String> args) throws Exception
    {
        DiscordConversation conversation = (DiscordConversation) caller.getConversation();

        if (args.size() == 0 || !Shenron.get().getPermissionManager().hasPermission(caller.getUser(), "admin_support"))
        {
            conversation.getChannel().sendMessage("FAQ : " + link).queue();
            return;
        }

        String message = Strings.join(args, " ");

        String name = message.trim();

        if (name.startsWith("@"))
        {
            name = name.substring(1);
        }

        // TODO: KROBOT: userByName tres lent + effective name
        Member member = Shenron.getMemberOfGuild(name, caller);

        if (member == null)
        {
            return;
        }

        User user = new DiscordUser(member.getUser());

        conversation.getChannel().sendMessage(mention(user) + " ! " + Markdown.mdBold("La reponse a ton probleme se trouve dans la " + Markdown.mdUnderline("FAQ")) + "\nCherche bien ;)\n\n" + Markdown.mdBold("==> ") + link).queue();

        Guild guild = member.getGuild();

        Role moche = guild.getRolesByName("Pabo", true).get(0);
        Role hyperMoche = guild.getRolesByName("Hyper Pabo", true).get(0);
        Role ultraMoche = guild.getRolesByName("Ultra Pabo", true).get(0);

        if (guild.getMembersWithRoles(ultraMoche).contains(member))
        {
            conversation.getChannel().sendMessage("En plus t'es Ultra Pabo, t'es vraiment le pire des pabo omg").queue();
        }
        else if (guild.getMembersWithRoles(hyperMoche).contains(member))
        {
            guild.getController().addRolesToMember(member, ultraMoche).queue();
        }
        else if (guild.getMembersWithRoles(moche).contains(member))
        {
            guild.getController().addRolesToMember(member, hyperMoche).queue();
        }
        else
        {
            guild.getController().addRolesToMember(member, moche).queue();
        }
    }

    public String getServer()
    {
        return "Support-Launchers";
    }
}
