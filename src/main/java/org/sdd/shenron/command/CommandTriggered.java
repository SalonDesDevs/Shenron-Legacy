package org.sdd.shenron.command;

import fr.litarvan.krobot.command.Command;
import fr.litarvan.krobot.command.ICommandCaller;
import fr.litarvan.krobot.command.message.MessageCommandCaller;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.sdd.shenron.util.MessageEditor;

public class CommandTriggered extends ShenronCommand
{
    public static final String URL = "https://www.growtopiagame.com/forums/attachment.php?attachmentid=132753&d=1469397141";

    @NotNull
    @Override
    public String getCommand()
    {
        return "triggered";
    }

    @NotNull
    @Override
    public String getDescription()
    {
        return "Displays the triggered même";
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
    public void handle(MessageCommandCaller iCommandCaller, List<String> list) throws Exception
    {
        MessageEditor.edit(iCommandCaller.getUser(), iCommandCaller.getConversation(), iCommandCaller.getMessage(), URL, false);
    }
}
