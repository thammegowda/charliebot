// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.processor;

import org.alicebot.server.core.util.ClassRegistry;

public class AIMLProcessorRegistry extends ClassRegistry {

    private static final String VERSION = "1.0.1";
    private static final String PROCESSOR_LIST[] = {
            "org.alicebot.server.core.processor.BotProcessor", "org.alicebot.server.core.processor.ConditionProcessor", "org.alicebot.server.core.processor.DateProcessor", "org.alicebot.server.core.processor.FormalProcessor", "org.alicebot.server.core.processor.GenderProcessor", "org.alicebot.server.core.processor.GetProcessor", "org.alicebot.server.core.processor.GossipProcessor", "org.alicebot.server.core.processor.IDProcessor", "org.alicebot.server.core.processor.InputProcessor", "org.alicebot.server.core.processor.JavaScriptProcessor",
            "org.alicebot.server.core.processor.LearnProcessor", "org.alicebot.server.core.processor.LowerCaseProcessor", "org.alicebot.server.core.processor.Person2Processor", "org.alicebot.server.core.processor.PersonProcessor", "org.alicebot.server.core.processor.RandomProcessor", "org.alicebot.server.core.processor.SentenceProcessor", "org.alicebot.server.core.processor.SetProcessor", "org.alicebot.server.core.processor.SizeProcessor", "org.alicebot.server.core.processor.SRAIProcessor", "org.alicebot.server.core.processor.SRProcessor",
            "org.alicebot.server.core.processor.StarProcessor", "org.alicebot.server.core.processor.SystemProcessor", "org.alicebot.server.core.processor.ThatProcessor", "org.alicebot.server.core.processor.ThatStarProcessor", "org.alicebot.server.core.processor.ThinkProcessor", "org.alicebot.server.core.processor.TopicStarProcessor", "org.alicebot.server.core.processor.UpperCaseProcessor", "org.alicebot.server.core.processor.VersionProcessor"
    };
    private static final String PROCESSOR_BASE_CLASS_NAME = "org.alicebot.server.core.processor.AIMLProcessor";
    private static final AIMLProcessorRegistry self = new AIMLProcessorRegistry();
    public AIMLProcessorRegistry() {
        super("1.0.1", PROCESSOR_LIST, "org.alicebot.server.core.processor.AIMLProcessor");
    }

    public static AIMLProcessorRegistry getSelf() {
        return self;
    }

}
