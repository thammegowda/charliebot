// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.processor.loadtime;

import org.alicebot.server.core.util.ClassRegistry;

public class StartupElementProcessorRegistry extends ClassRegistry {

    private static final String VERSION = "4.1.8";
    private static final String PROCESSOR_LIST[] = {
            "org.alicebot.server.core.processor.loadtime.BotProcessor", "org.alicebot.server.core.processor.loadtime.BotsProcessor", "org.alicebot.server.core.processor.loadtime.GenderProcessor", "org.alicebot.server.core.processor.loadtime.InputProcessor", "org.alicebot.server.core.processor.loadtime.LearnProcessor", "org.alicebot.server.core.processor.loadtime.ListenerProcessor", "org.alicebot.server.core.processor.loadtime.ListenersProcessor", "org.alicebot.server.core.processor.loadtime.PersonProcessor", "org.alicebot.server.core.processor.loadtime.Person2Processor", "org.alicebot.server.core.processor.loadtime.PredicateProcessor",
            "org.alicebot.server.core.processor.loadtime.PredicatesProcessor", "org.alicebot.server.core.processor.loadtime.PropertiesProcessor", "org.alicebot.server.core.processor.loadtime.PropertyProcessor", "org.alicebot.server.core.processor.loadtime.SentenceSplittersProcessor", "org.alicebot.server.core.processor.loadtime.SubstitutionsProcessor"
    };
    private static final String PROCESSOR_BASE_CLASS_NAME = "org.alicebot.server.core.processor.loadtime.StartupElementProcessor";
    private static final StartupElementProcessorRegistry self = new StartupElementProcessorRegistry();
    private StartupElementProcessorRegistry() {
        super("4.1.8", PROCESSOR_LIST, "org.alicebot.server.core.processor.loadtime.StartupElementProcessor");
    }

    public static StartupElementProcessorRegistry getSelf() {
        return self;
    }

}
