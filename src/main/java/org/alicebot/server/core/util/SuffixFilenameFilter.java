// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.util;

import java.io.File;
import java.io.FilenameFilter;

public class SuffixFilenameFilter
        implements FilenameFilter {

    private static String SUFFIXES[];

    public SuffixFilenameFilter(String as[]) {
        SUFFIXES = as;
    }

    public boolean accept(File file, String s) {
        if (s == null)
            return false;
        if (s.length() == 0)
            return false;
        for (int i = SUFFIXES.length; --i >= 0; )
            if (s.endsWith(SUFFIXES[i]))
                return true;

        return false;
    }
}
