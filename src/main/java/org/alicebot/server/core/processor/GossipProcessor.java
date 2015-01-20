// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.processor;

import org.alicebot.server.core.logging.Log;
import org.alicebot.server.core.parser.TemplateParser;
import org.alicebot.server.core.parser.XMLNode;

// Referenced classes of package org.alicebot.server.core.processor:
//            AIMLProcessor, AIMLProcessorException

public class GossipProcessor extends AIMLProcessor {

    public static final String label = "gossip";

    public GossipProcessor() {
    }

    public String process(int i, XMLNode xmlnode, TemplateParser templateparser)
            throws AIMLProcessorException {
        if (xmlnode.XMLType == 0) {
            String s = templateparser.evaluate(i++, xmlnode.XMLChild);
            Log.log(s, Log.GOSSIP);
            return s;
        } else {
            throw new AIMLProcessorException("<gossip></gossip> must have content!");
        }
    }
}
