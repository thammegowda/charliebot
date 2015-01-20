// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core;


public class NoSuchPredicateException extends Exception {

    private static String name;
    private static int index = -1;

    public NoSuchPredicateException(String s) {
        name = s;
    }

    public NoSuchPredicateException(String s, int i) {
        name = s;
        index = i;
    }

    public String getMessage() {
        if (index != -1)
            return "No predicate with name \"" + name + "\" with a value at index " + index + ".";
        else
            return "No predicate with name \"" + name + "\".";
    }

}
