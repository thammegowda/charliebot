// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.processor;

import org.alicebot.server.core.PredicateMaster;
import org.alicebot.server.core.parser.TemplateParser;
import org.alicebot.server.core.parser.XMLNode;
import org.alicebot.server.core.util.Toolkit;

// Referenced classes of package org.alicebot.server.core.processor:
//            AIMLProcessor, AIMLProcessorException

public class GetProcessor extends AIMLProcessor {

    public static final String label = "get";

    public GetProcessor() {
    }

    public String process(int i, XMLNode xmlnode, TemplateParser templateparser)
            throws AIMLProcessorException {
        if (xmlnode.XMLType == 1) {
            String s = Toolkit.getAttributeValue("name", xmlnode.XMLAttr);
            if (s.equals(""))
                throw new AIMLProcessorException("<get/> must have a non-empty name attribute.");
            else
                return PredicateMaster.get(s, templateparser.getUserID(), templateparser.getBotID());
        } else {
            throw new AIMLProcessorException("<get/> cannot have content!");
        }
    }
}
