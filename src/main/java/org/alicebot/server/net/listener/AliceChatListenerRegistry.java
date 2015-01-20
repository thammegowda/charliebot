// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.net.listener;

import org.alicebot.server.core.util.ClassRegistry;

public class AliceChatListenerRegistry extends ClassRegistry {

    private static final String VERSION = "4.1.8";
    private static final String PROCESSOR_LIST[] = {
            "org.alicebot.server.net.listener.AliceAIM", "org.alicebot.server.net.listener.AliceICQ", "org.alicebot.server.net.listener.AliceIRC"
    };
    private static final String PROCESSOR_BASE_CLASS_NAME = "org.alicebot.server.net.listener.AliceChatListener";
    private static final AliceChatListenerRegistry self = new AliceChatListenerRegistry();
    private AliceChatListenerRegistry() {
        super("4.1.8", PROCESSOR_LIST, "org.alicebot.server.net.listener.AliceChatListener");
    }

    public static AliceChatListenerRegistry getSelf() {
        return self;
    }

}
