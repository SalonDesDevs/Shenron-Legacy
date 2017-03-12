package org.sdd.shenron.command;

import fr.litarvan.krobot.command.Command;
import fr.litarvan.krobot.command.ICommandCaller;
import fr.litarvan.krobot.command.message.MessageCommandCaller;
import fr.litarvan.krobot.motor.discord.DiscordConversation;
import fr.litarvan.krobot.motor.discord.DiscordMessage;
import fr.litarvan.krobot.util.Markdown;
import net.dv8tion.jda.core.entities.Guild;
import org.jetbrains.annotations.NotNull;
import org.sdd.shenron.Shenron;

import java.util.List;

public class CommandHelp extends ShenronCommand
{
    @NotNull
    @Override
    public String getCommand()
    {
        return "help";
    }

    @NotNull
    @Override
    public String getDescription()
    {
        return "Prints the list of commands with their description and their syntax";
    }

    @NotNull
    @Override
    public String getSyntax()
    {
        return "";
    }

    @Override
    public boolean checkSyntax(List<String> list)
    {
        return list.size() == 0;
    }

    @Override
    public void handle(MessageCommandCaller caller, List<String> args)
    {
        DiscordConversation conversation = (DiscordConversation) caller.getConversation();
        String message = Markdown.mdUnderline("List of commands :") + "\n\n";

        for (Command c : Shenron.get().getCommandHandler().getCommandList())
        {
            if (c instanceof ShenronCommand && caller.getMessage() instanceof DiscordMessage)
            {
                ShenronCommand sc = (ShenronCommand) c;
                Guild guild = ((DiscordMessage) caller.getMessage()).getMessage().getGuild();

                if (sc.getServer() != null && !sc.getServer().trim().equalsIgnoreCase(guild.getName().trim()))
                {
                    continue;
                }
            }

            message += ("    " + Shenron.get().getCommandHandler().getPrefix() + c.getCommand() + " " + c.getSyntax() + "\n" +
                        "        " + c.getDescription() + "\n");
        }

        conversation.getChannel().sendMessage(message).queue();
    }
}
