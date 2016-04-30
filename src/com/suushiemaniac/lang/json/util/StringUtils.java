package com.suushiemaniac.lang.json.util;

public class StringUtils {
    public static int compare(String s1, String s2) {
        return s1.compareTo(s2);
    }

    public static String copy(String base, int times) {
        String cp = "";

        for (int i = 0; i < times; i++) cp += base;

        return cp;
    }

    public static String jsonWrap(String blank) {
        if (!blank.startsWith("\"") && !blank.endsWith("\"")) blank = "\"" + blank + "\"";
        return blank;
    }

    public static String jsonUnwrap(String wrapped) {
        if (wrapped.startsWith("\"") && wrapped.endsWith("\"")) wrapped = wrapped.substring(1, wrapped.length() - 1);
        return wrapped;
    }

    public static String reduceTab(String tabbed) {
        while (tabbed.startsWith("\t")) tabbed = tabbed.substring(1);
        return tabbed;
    }
}
