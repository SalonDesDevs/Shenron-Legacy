package org.sdd.shenron;

import fr.litarvan.krobot.motor.discord.DiscordExtension;
import net.dv8tion.jda.core.events.ReadyEvent;

public class ShenronDiscordExtension extends DiscordExtension
{
    private GroupListener groupListener;

    public ShenronDiscordExtension(GroupListener groupListener)
    {
        this.groupListener = groupListener;
    }

    @Override
    public void onReady(ReadyEvent event)
    {
        super.onReady(event);

        event.getJDA().addEventListener(groupListener);
    }
}
