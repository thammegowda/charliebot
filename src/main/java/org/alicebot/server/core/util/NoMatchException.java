// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.util;


public class NoMatchException extends Exception {

    private static String path;

    public NoMatchException(String s) {
        path = s;
    }

    public String getMessage() {
        return "No match found for path \"" + path + "\".";
    }
}
