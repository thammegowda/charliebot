// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core;

import org.alicebot.server.core.util.Trace;

import java.util.HashMap;
import java.util.Iterator;

// Referenced classes of package org.alicebot.server.core:
//            BotProcess

public class BotProcesses {

    private static HashMap registry = new HashMap();

    private BotProcesses() {
    }

    public static void start(BotProcess botprocess, String s) {
        registry.put(s, botprocess);
        Thread thread = new Thread(botprocess, s);
        thread.setDaemon(true);
        thread.start();
    }

    public static Iterator getRegistryIterator() {
        return registry.values().iterator();
    }

    public static BotProcess get(String s) {
        return (BotProcess) registry.get(s);
    }

    public static void shutdownAll() {
        Trace.userinfo("Shutting down all BotProcesses.");
        BotProcess botprocess;
        for (Iterator iterator = registry.values().iterator(); iterator.hasNext(); botprocess.shutdown()) {
            botprocess = (BotProcess) iterator.next();
            Trace.userinfo("Shutting down " + botprocess);
        }

        Trace.userinfo("Finished shutting down BotProcesses.");
    }

}
