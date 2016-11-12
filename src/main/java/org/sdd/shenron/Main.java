package org.sdd.shenron;

import fr.litarvan.krobot.Krobot;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Main
{
    public static void main(String[] args)
    {
        // Heroku start

        String token = System.getProperty("shenron.token");

        if (token != null)
        {
            FileWriter writer = null;

            try
            {
                writer = new FileWriter(new File("discord-tokens", "shenron"));
                writer.write(token);
            }
            catch (IOException e)
            {
                System.err.println("WARNING ! Failed to write token for heroku !");
                e.printStackTrace();
            }
            finally
            {
                if (writer != null)
                {
                    try
                    {
                        writer.close();
                    }
                    catch (IOException ignored)
                    {
                    }
                }
            }
        }

        // Heroku end

        Krobot krobot = new Krobot(false, new File("."), new File("crashes"), new File("motors"), new File("bots"));

        krobot.getBotLoader().load(new Shenron());
        krobot.start("discord", "shenron");
    }
}
