// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.processor;

import org.alicebot.server.core.parser.TemplateParser;
import org.alicebot.server.core.parser.XMLNode;

import java.util.Date;

// Referenced classes of package org.alicebot.server.core.processor:
//            AIMLProcessor, AIMLProcessorException

public class DateProcessor extends AIMLProcessor {

    public static final String label = "date";

    public DateProcessor() {
    }

    public String process(int i, XMLNode xmlnode, TemplateParser templateparser)
            throws AIMLProcessorException {
        if (xmlnode.XMLType == 1)
            return (new Date()).toString();
        else
            throw new AIMLProcessorException("<date/> cannot have content!");
    }
}
