// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.net;

import org.alicebot.server.core.BotProcess;

import java.io.IOException;

public interface AliceCompatibleHttpServer
        extends BotProcess {

    public abstract void configure(String s)
            throws IOException;

    public abstract void run();
}
