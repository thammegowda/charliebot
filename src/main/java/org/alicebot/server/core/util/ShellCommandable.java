// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.util;


public interface ShellCommandable {

    public abstract String getShellID();

    public abstract String getShellDescription();

    public abstract String getShellCommands();

    public abstract void processShellCommand(String s);
}
