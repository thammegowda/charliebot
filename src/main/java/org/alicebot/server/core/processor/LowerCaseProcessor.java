// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.processor;

import org.alicebot.server.core.parser.TemplateParser;
import org.alicebot.server.core.parser.XMLNode;

// Referenced classes of package org.alicebot.server.core.processor:
//            AIMLProcessor, AIMLProcessorException

public class LowerCaseProcessor extends AIMLProcessor {

    public static final String label = "lowercase";

    public LowerCaseProcessor() {
    }

    public String process(int i, XMLNode xmlnode, TemplateParser templateparser)
            throws AIMLProcessorException {
        if (xmlnode.XMLType == 0)
            return templateparser.evaluate(i++, xmlnode.XMLChild).toLowerCase();
        else
            throw new AIMLProcessorException("<lowercase></lowercase> must have content!");
    }
}
