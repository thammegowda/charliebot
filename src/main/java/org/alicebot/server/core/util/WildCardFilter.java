// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.util;

import java.io.File;
import java.io.FilenameFilter;

public final class WildCardFilter
        implements FilenameFilter {

    private String pattern;
    private char wildCard;
    private int wildIndex[];
    private String prefix;
    private String suffix;
    public WildCardFilter(String s, char c) {
        pattern = s;
        wildCard = c;
        int i = 0;
        for (int j = 0; j < s.length(); j++)
            if (c == s.charAt(j))
                i++;

        wildIndex = new int[i];
        int k = 0;
        for (int l = 0; k < i; l++)
            if (c == s.charAt(l))
                wildIndex[k++] = l;

        if (i == 0) {
            prefix = null;
            suffix = null;
        } else {
            prefix = s.substring(0, wildIndex[0]);
            suffix = s.substring(wildIndex[i - 1] + 1);
        }
    }

    public boolean accept(File file, String s) {
        if (wildIndex.length == 0)
            return pattern.equals(s);
        if (!s.startsWith(prefix) || !s.endsWith(suffix))
            return false;
        if (wildIndex.length == 1)
            return true;
        int i = s.length() - suffix.length();
        int j = wildIndex[0];
        int k = j;
        for (int l = 1; l < wildIndex.length; l++) {
            int i1 = j + 1;
            j = wildIndex[l];
            int j1 = j - i1;
            do {
                if (j1 + k > i)
                    return false;
                if (s.regionMatches(k, pattern, i1, j1))
                    break;
                k++;
            } while (true);
            k += j1;
        }

        return true;
    }
}
