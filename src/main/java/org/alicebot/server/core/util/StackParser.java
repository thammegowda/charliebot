// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.StringTokenizer;

public class StackParser {

    private static final String AT = "at ";

    private StackParser() {
    }

    public static String getStackMethod(Throwable throwable, int i) {
        String s = null;
        String s1 = getStackString(throwable);
        int j;
        for (j = 0; i-- >= 0 && j >= 0; j = s1.indexOf("at ", j + 3)) ;
        if (j > 0) {
            int k = s1.indexOf('(', j);
            if (k > 0)
                s = s1.substring(j + 3, k);
        }
        return s;
    }

    public static String getStackMethod(int i) {
        i++;
        Throwable throwable = (new Throwable()).fillInStackTrace();
        return getStackMethod(throwable, i);
    }

    public static String getStackMethodBefore(String s, boolean flag) {
        int i = 1;
        String s1;
        for (s1 = getStackMethod(i); s1.indexOf(s) != -1 && s1 != null; s1 = getStackMethod(++i))
            ;
        if (flag) {
            int j = s1.lastIndexOf('.', s1.lastIndexOf('.') - 1);
            if (j != -1)
                return s1.substring(j + 1);
            else
                return s1;
        } else {
            return s1;
        }
    }

    public static String getStackString(Throwable throwable) {
        StringWriter stringwriter = new StringWriter();
        throwable.printStackTrace(new PrintWriter(stringwriter));
        return stringwriter.toString();
    }

    public static StringTokenizer getStackTraceFor(Throwable throwable) {
        return new StringTokenizer(getStackString(throwable), System.getProperty("line.separator"));
    }
}
