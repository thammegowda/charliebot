// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.processor;

import org.alicebot.server.core.Bots;
import org.alicebot.server.core.parser.TemplateParser;
import org.alicebot.server.core.parser.XMLNode;
import org.alicebot.server.core.util.Toolkit;

// Referenced classes of package org.alicebot.server.core.processor:
//            AIMLProcessor, AIMLProcessorException

public class BotProcessor extends AIMLProcessor {

    public static final String label = "bot";

    public BotProcessor() {
    }

    public String process(int i, XMLNode xmlnode, TemplateParser templateparser)
            throws AIMLProcessorException {
        if (xmlnode.XMLType == 1) {
            String s = Toolkit.getAttributeValue("name", xmlnode.XMLAttr);
            if (s.equals(""))
                return s;
            else
                return Bots.getBot(templateparser.getBotID()).getPropertyValue(s);
        } else {
            throw new AIMLProcessorException("<bot/> cannot contain element content!");
        }
    }
}
