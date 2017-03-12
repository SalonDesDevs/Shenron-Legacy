package org.sdd.shenron;

import java.util.List;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class GroupTrigger
{
    private String messageId;
    private List<ImmutablePair<String, String>> groups;

    public GroupTrigger(String messageId, List<ImmutablePair<String, String>> groups)
    {
        this.messageId = messageId;
        this.groups = groups;

        // /group trigger "__Systèmes__ : Windows, Mac OS X, Linux" Windows/windows MacOS/osx Linux/linux
        // /group trigger "__Langages Web__ : HTML/CSS, Javascript, PHP" HTML#CSS/html Javascript/js PHP/php
        // /group trigger "__Langages Natifs__ : C, C++, Rust, Go, Bas-Level" C-lang/c_lang C++/cpp Rust/rust Go/go Bas-Level/baslvl
        // /group trigger "__Langages Haut-Niveau__ : Java, Lua, Ruby, Python" Java/java Lua/lua Ruby/ruby Python/python
        // /group trigger "__Langages fonctionnels__ : Haskell, Elixir" Haskell/haskell Elixir/elixir
        // /group trigger "__Autres groupes__ : Cryptomonnaie, Pervers" Cryptomonnaie/cmonnaie Pervers/kreygasm
    }

    public String getMessageId()
    {
        return messageId;
    }

    public List<ImmutablePair<String, String>> getGroups()
    {
        return groups;
    }
}
