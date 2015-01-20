// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.util;


public class UserError extends Error {

    private Exception exception;

    public UserError(String s) {
        super(s);
    }

    public UserError(Exception exception1) {
        super("Developer did not describe exception.");
        exception = exception1;
    }

    public UserError(String s, Exception exception1) {
        super(s);
        exception = exception1;
    }

    public Exception getException() {
        return exception;
    }
}
