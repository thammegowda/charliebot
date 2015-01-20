// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.util;


public class DeveloperError extends Error {

    private Throwable throwable;

    public DeveloperError(String s) {
        super(s);
    }

    public DeveloperError(Throwable throwable1) {
        super("Developer did not describe exception.");
        throwable = throwable1;
    }

    public DeveloperError(String s, Throwable throwable1) {
        super(s);
        throwable = throwable1;
    }

    public Throwable getEmbedded() {
        return throwable;
    }
}
