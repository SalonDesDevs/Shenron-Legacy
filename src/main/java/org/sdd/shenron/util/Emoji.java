package org.sdd.shenron.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public enum Emoji
{
    A("a", "\uD83C\uDD70"),
    B("b", "\uD83C\uDD71"),
    M("m", "\u24C2"),
    O("o2", "\uD83C\uDD7E"),
    P("parking", "\uD83C\uDD7F"),
    ZERO("0", "\u0030\u20E3"),
    ONE("1", "\u0031\u20E3"),
    TWO("2", "\u0032\u20E3"),
    THREE("3", "\u0033\u20E3"),
    FOUR("4", "\u0034\u20E3"),
    FIVE("5", "\u0035\u20E3"),
    SIX("6", "\u0036\u20E3"),
    SEVEN("7", "\u0037\u20E3"),
    EIGHT("8", "\u0038\u20E3"),
    NINE("9", "\u0039\u20E3"),
    RI_A("regional_indicator_a", "\uD83C\uDDE6"),
    RI_B("regional_indicator_b", "\uD83C\uDDE7"),
    RI_C("regional_indicator_c", "\uD83C\uDDE8"),
    RI_D("regional_indicator_d", "\uD83C\uDDE9"),
    RI_E("regional_indicator_e", "\uD83C\uDDEA"),
    RI_F("regional_indicator_f", "\uD83C\uDDEB"),
    RI_G("regional_indicator_g", "\uD83C\uDDEC"),
    RI_H("regional_indicator_h", "\uD83C\uDDED"),
    RI_I("regional_indicator_i", "\uD83C\uDDEE"),
    RI_J("regional_indicator_j", "\uD83C\uDDEF"),
    RI_K("regional_indicator_k", "\uD83C\uDDF0"),
    RI_L("regional_indicator_l", "\uD83C\uDDF1"),
    RI_M("regional_indicator_m", "\uD83C\uDDF2"),
    RI_N("regional_indicator_n", "\uD83C\uDDF3"),
    RI_O("regional_indicator_o", "\uD83C\uDDF4"),
    RI_P("regional_indicator_p", "\uD83C\uDDF5"),
    RI_Q("regional_indicator_q", "\uD83C\uDDF6"),
    RI_R("regional_indicator_r", "\uD83C\uDDF7"),
    RI_S("regional_indicator_s", "\uD83C\uDDF8"),
    RI_T("regional_indicator_t", "\uD83C\uDDF9"),
    RI_U("regional_indicator_u", "\uD83C\uDDFA"),
    RI_V("regional_indicator_v", "\uD83C\uDDFB"),
    RI_W("regional_indicator_w", "\uD83C\uDDFC"),
    RI_X("regional_indicator_x", "\uD83C\uDDFD"),
    RI_Y("regional_indicator_y", "\uD83C\uDDFE"),
    RI_Z("regional_indicator_z", "\uD83C\uDDFF");

    private String tag;
    private String unicode;

    public static final HashMap<Character, Emoji[]> CHAR_TO_EMOJI = new HashMap<>();

    Emoji(String tag, String unicode)
    {
        this.tag = ":" + tag + ":";
        this.unicode = unicode;
    }

    public String getTag()
    {
        return tag;
    }

    public String getUnicode()
    {
        return unicode;
    }

    public static Emoji[] textToEmoji(String text)
    {
        List<Emoji> result = new ArrayList<>();
        List<Emoji> used = new ArrayList<>();

        for (char c : text.toCharArray())
        {
            if (!CHAR_TO_EMOJI.containsKey(c))
            {
                continue;
            }

            Emoji[] emojis = CHAR_TO_EMOJI.get(c);
            Emoji emoji = null;

            for (Emoji e : emojis)
            {
                if (!used.contains(e))
                {
                    emoji = e;
                    break;
                }
            }

            if (emoji != null)
            {
                result.add(emoji);
                used.add(emoji);
            }
        }

        return result.toArray(new Emoji[result.size()]);
    }

    static
    {
        CHAR_TO_EMOJI.put('a', new Emoji[] {RI_A, A, FOUR});
        CHAR_TO_EMOJI.put('b', new Emoji[] {RI_B, B});
        CHAR_TO_EMOJI.put('c', new Emoji[] {RI_C});
        CHAR_TO_EMOJI.put('d', new Emoji[] {RI_D});
        CHAR_TO_EMOJI.put('e', new Emoji[] {RI_E, THREE});
        CHAR_TO_EMOJI.put('f', new Emoji[] {RI_F});
        CHAR_TO_EMOJI.put('g', new Emoji[] {RI_G});
        CHAR_TO_EMOJI.put('h', new Emoji[] {RI_H});
        CHAR_TO_EMOJI.put('i', new Emoji[] {RI_I, ONE});
        CHAR_TO_EMOJI.put('j', new Emoji[] {RI_J});
        CHAR_TO_EMOJI.put('k', new Emoji[] {RI_K});
        CHAR_TO_EMOJI.put('l', new Emoji[] {RI_L, SEVEN});
        CHAR_TO_EMOJI.put('m', new Emoji[] {RI_M, M});
        CHAR_TO_EMOJI.put('n', new Emoji[] {RI_N});
        CHAR_TO_EMOJI.put('o', new Emoji[] {RI_O, O, ZERO});
        CHAR_TO_EMOJI.put('p', new Emoji[] {RI_P, P});
        CHAR_TO_EMOJI.put('q', new Emoji[] {RI_Q});
        CHAR_TO_EMOJI.put('r', new Emoji[] {RI_R});
        CHAR_TO_EMOJI.put('s', new Emoji[] {RI_S});
        CHAR_TO_EMOJI.put('t', new Emoji[] {RI_T});
        CHAR_TO_EMOJI.put('u', new Emoji[] {RI_U});
        CHAR_TO_EMOJI.put('v', new Emoji[] {RI_V});
        CHAR_TO_EMOJI.put('w', new Emoji[] {RI_W});
        CHAR_TO_EMOJI.put('x', new Emoji[] {RI_X});
        CHAR_TO_EMOJI.put('y', new Emoji[] {RI_Y});
        CHAR_TO_EMOJI.put('z', new Emoji[] {RI_Z});
        CHAR_TO_EMOJI.put('1', new Emoji[] {ONE});
        CHAR_TO_EMOJI.put('2', new Emoji[] {TWO});
        CHAR_TO_EMOJI.put('3', new Emoji[] {THREE});
        CHAR_TO_EMOJI.put('4', new Emoji[] {FOUR});
        CHAR_TO_EMOJI.put('5', new Emoji[] {FIVE});
        CHAR_TO_EMOJI.put('6', new Emoji[] {SIX});
        CHAR_TO_EMOJI.put('7', new Emoji[] {SEVEN});
        CHAR_TO_EMOJI.put('8', new Emoji[] {EIGHT});
        CHAR_TO_EMOJI.put('9', new Emoji[] {NINE});
    }
}
