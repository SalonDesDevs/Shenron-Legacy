package org.sdd.shenron.command;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import fr.litarvan.krobot.command.Command;
import fr.litarvan.krobot.command.ICommandCaller;
import fr.litarvan.krobot.command.message.MessageCommandCaller;
import fr.litarvan.krobot.console.ConsoleCommandCaller;
import fr.litarvan.krobot.util.Markdown;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jetbrains.annotations.NotNull;
import org.sdd.shenron.Shenron;

public class CommandChuck extends Command
{
    public static final String URL = "http://api.icndb.com/jokes/random?limitTo=[nerdy]";

    @NotNull
    @Override
    public String getCommand()
    {
        return "chuck";
    }

    @NotNull
    @Override
    public String getDescription()
    {
        return "Show Chuck Norris quote from icndb";
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
    public void handleCall(ICommandCaller caller, List<String> args)
    {
        if (!(caller instanceof MessageCommandCaller))
        {
            return;
        }

        try
        {
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(URL);
            HttpResponse response = client.execute(request);
            Gson gson = new Gson();
            JsonReader reader = gson.newJsonReader(new BufferedReader(new InputStreamReader(response.getEntity().getContent())));
            String result = reader.nextString();
            String quote = result.replaceAll("&quot;", "\"");

            ((MessageCommandCaller) caller).getConversation().sendMessage(Markdown.mdEmphasis(quote));
        }
        catch (IOException e)
        {
            Shenron.handleCommandException(caller, this, args, e);
        }
    }
}
