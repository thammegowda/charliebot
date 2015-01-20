// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.processor;

import org.alicebot.server.core.parser.TemplateParser;
import org.alicebot.server.core.parser.XMLNode;

// Referenced classes of package org.alicebot.server.core.processor:
//            AIMLProcessor, AIMLProcessorException

public class SentenceProcessor extends AIMLProcessor {

    public static final String label = "sentence";

    public SentenceProcessor() {
    }

    public String process(int i, XMLNode xmlnode, TemplateParser templateparser)
            throws AIMLProcessorException {
        if (xmlnode.XMLType == 0) {
            String s = templateparser.evaluate(i++, xmlnode.XMLChild);
            if (s.equals(""))
                return s;
            if (s.trim().length() > 1)
                return s.substring(0, 1).toUpperCase() + s.substring(1);
            else
                return s;
        } else {
            throw new AIMLProcessorException("<sentence></sentence> must have content!");
        }
    }
}
