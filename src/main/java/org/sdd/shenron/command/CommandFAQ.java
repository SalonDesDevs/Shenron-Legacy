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
import net.dv8tion.jda.core.entities.Member;
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
        return list.size() < 2;
    }

    @Override
    public void handle(MessageCommandCaller caller, List<String> args) throws Exception
    {
        if (args.size() == 0 || !Shenron.get().isAdmin(caller.getUser()))
        {
            caller.getConversation().sendMessage("FAQ : " + link);
            return;
        }

        String message = Strings.join(args, " ");

        String name = message.trim();

        if (name.startsWith("@"))
        {
            name = name.substring(1);
        }

        // TODO: KROBOT: userByName tres lent + effetive name
        List<Member> members = ((DiscordMessage) caller.getMessage()).getMessage().getGuild().getMembersByEffectiveName(name, true);

        if (members.size() == 0)
        {

            caller.getConversation().sendMessage(mention(caller.getUser()) + " Can't find user '" + name + "'");
            return;
        }

        User user = new DiscordUser(members.get(0).getUser());

        caller.getConversation().sendMessage(mention(user) + " ! " + Markdown.mdBold("La reponse a ton probleme se trouve dans la " + Markdown.mdUnderline("FAQ")) + "\nCherche bien ;)\n\n" + Markdown.mdBold("==> ") + link);
    }

    public String getServer()
    {
        return "Support-Launchers";
    }
}
