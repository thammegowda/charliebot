// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.processor;

import org.alicebot.server.core.parser.GenericParser;
import org.alicebot.server.core.parser.TemplateParser;
import org.alicebot.server.core.parser.XMLNode;

// Referenced classes of package org.alicebot.server.core.processor:
//            Processor, ProcessorException, AIMLProcessorException

public abstract class AIMLProcessor extends Processor {

    public AIMLProcessor() {
    }

    public String process(int i, XMLNode xmlnode, GenericParser genericparser)
            throws ProcessorException {
        try {
            return process(i, xmlnode, (TemplateParser) genericparser);
        } catch (ClassCastException classcastexception) {
            throw new ProcessorException("Tried to pass a non-TemplateParser to an AIMLProcessor.");
        }
    }

    public abstract String process(int i, XMLNode xmlnode, TemplateParser templateparser)
            throws AIMLProcessorException;
}
