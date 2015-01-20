// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.processor;

import org.alicebot.server.core.parser.TemplateParser;
import org.alicebot.server.core.parser.XMLNode;

// Referenced classes of package org.alicebot.server.core.processor:
//            IndexedPredicateProcessor, AIMLProcessorException

public class ThatProcessor extends IndexedPredicateProcessor {

    public static final String label = "that";

    public ThatProcessor() {
    }

    public String process(int i, XMLNode xmlnode, TemplateParser templateparser)
            throws AIMLProcessorException {
        if (xmlnode.XMLType == 1)
            return super.process(i, xmlnode, templateparser, "that", 2);
        else
            throw new AIMLProcessorException("Template-side <that/> cannot have element content!");
    }
}
