package org.sdd.shenron;

public class Group
{
    private String name;
    private String channel;

    public Group(String name, String channel)
    {
        this.name = name;
        this.channel = channel;
    }

    public String getName()
    {
        return name;
    }

    public String getChannel()
    {
        return channel;
    }
}
