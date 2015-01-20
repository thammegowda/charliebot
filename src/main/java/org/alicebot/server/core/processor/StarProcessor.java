// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.processor;

import org.alicebot.server.core.parser.TemplateParser;
import org.alicebot.server.core.parser.XMLNode;

// Referenced classes of package org.alicebot.server.core.processor:
//            IndexedPredicateProcessor, AIMLProcessorException

public class StarProcessor extends IndexedPredicateProcessor {

    public static final String label = "star";

    public StarProcessor() {
    }

    public String process(int i, XMLNode xmlnode, TemplateParser templateparser)
            throws AIMLProcessorException {
        if (xmlnode.XMLType == 1)
            return super.process(i, xmlnode, templateparser, templateparser.getInputStars(), 1);
        else
            throw new AIMLProcessorException("<star/> cannot have element content!");
    }
}
