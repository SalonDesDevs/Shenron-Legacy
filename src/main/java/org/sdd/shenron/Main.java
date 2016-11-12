package org.sdd.shenron;

import fr.litarvan.krobot.Krobot;
import java.io.File;

public class Main
{
    public static void main(String[] args)
    {
        Krobot krobot = new Krobot(false, new File("."), new File("crashes"), new File("motors"), new File("bots"));

        krobot.getBotLoader().load(new Shenron());
        krobot.start("discord", "shenron");
    }
}
