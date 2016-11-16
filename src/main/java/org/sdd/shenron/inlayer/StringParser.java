package org.sdd.shenron.inlayer;

public class StringParser
{
    public static final char[] SPACE_STOP_LIST = new char[] {' '};

    private char[] string;
    private boolean escaping = false;
    private boolean escapeNext = false;
    private char escapeChar;

    private int index = -1;

    public StringParser(String string, char escapeChar)
    {
        this(string);

        this.escaping = true;
        this.escapeChar = escapeChar;
    }

    public StringParser(String string)
    {
        this.string = string.toCharArray();
    }

    public char current()
    {
        return string[index];
    }

    public char nextChar()
    {
        index++;

        if (!done())
        {
            return current();
        }

        return 0;
    }

    public String nextToken(char[] stopList)
    {
        String result = "";

        main:
        while (!done())
        {
            char next = nextChar();

            if (!escapeNext || !escaping)
            {
                for (char c : stopList)
                {
                    if (next == c)
                    {
                        break main;
                    }
                }
            }

            if (escapeNext && escaping)
            {
                escapeNext = false;
            }
            else if (escaping && next == escapeChar)
            {
                escapeNext = true;
            }

            result += next;
        }

        return result;
    }

    public String waitFor(char c)
    {
        String waiting = "";

        while ((nextChar() != c || (!escaping || escapeNext)) && !done())
        {
            if (current() == escapeChar)
            {
                escapeNext = true;
                continue;
            }

            if (escapeNext)
            {
                escapeNext = false;
            }

            waiting += current();
        }

        return waiting;
    }

    public boolean done()
    {
        return index >= string.length;
    }

    public void setIndex(int pos)
    {
        this.index = pos;
    }

    public int index()
    {
        return index;
    }
}
