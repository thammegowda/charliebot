// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.util;


public class Tag {

    private static final String LEFT_ANGLE_BRACKET = "<";
    private static final String RIGHT_ANGLE_BRACKET = ">";
    private String name;
    private String toString;
    public Tag(String s) {
        name = s;
        toString = "<" + s + ">";
    }

    public String toString() {
        return toString;
    }

    public String getName() {
        return name;
    }
}
