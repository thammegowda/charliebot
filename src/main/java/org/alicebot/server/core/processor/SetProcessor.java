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

public class SetProcessor extends AIMLProcessor {

    public static final String label = "set";

    public SetProcessor() {
    }

    public String process(int i, XMLNode xmlnode, TemplateParser templateparser)
            throws AIMLProcessorException {
        if (xmlnode.XMLType == 0) {
            String s = Toolkit.getAttributeValue("name", xmlnode.XMLAttr);
            if (s.equals(""))
                throw new AIMLProcessorException("<set></set> must have a name attribute!");
            else
                return PredicateMaster.set(s, templateparser.evaluate(i++, xmlnode.XMLChild), templateparser.getUserID(), templateparser.getBotID());
        } else {
            throw new AIMLProcessorException("<set></set> must have content!");
        }
    }
}
