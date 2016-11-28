package org.sdd.shenron.command;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import fr.litarvan.krobot.command.ICommandCaller;
import fr.litarvan.krobot.command.message.MessageCommandCaller;
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

public class CommandChuck extends ShenronCommand
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
    public void handle(ICommandCaller caller, List<String> args) throws IOException
    {
        if (!(caller instanceof MessageCommandCaller))
        {
            return;
        }

        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(URL);
        HttpResponse response = client.execute(request);
        JsonElement json = new JsonParser().parse(new BufferedReader(new InputStreamReader(response.getEntity().getContent())));
        String result = json.getAsJsonObject().get("value").getAsJsonObject().get("joke").getAsString();
        String quote = result.replaceAll("&quot;", "\"");

        Shenron.get().sendMessage(Markdown.mdEmphasis(quote), ((MessageCommandCaller) caller).getConversation());
    }
}
