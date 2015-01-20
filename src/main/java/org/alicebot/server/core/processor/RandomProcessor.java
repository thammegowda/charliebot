// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.processor;

import org.alicebot.server.core.parser.TemplateParser;
import org.alicebot.server.core.parser.XMLNode;
import org.alicebot.server.core.util.LRUCache;
import org.alicebot.server.core.util.MersenneTwisterFast;

// Referenced classes of package org.alicebot.server.core.processor:
//            AIMLProcessor, AIMLProcessorException

public class RandomProcessor extends AIMLProcessor {

    public static final String label = "random";
    private static final String LI = "li";
    private static final LRUCache generators = new LRUCache(100);
    public RandomProcessor() {
    }

    public String process(int i, XMLNode xmlnode, TemplateParser templateparser)
            throws AIMLProcessorException {
        if (xmlnode.XMLType == 0) {
            if (xmlnode.XMLChild == null)
                return "";
            String s = templateparser.getBotID() + templateparser.getUserID() + xmlnode.toString();
            MersenneTwisterFast mersennetwisterfast = (MersenneTwisterFast) generators.get(s);
            if (mersennetwisterfast == null) {
                mersennetwisterfast = new MersenneTwisterFast(System.currentTimeMillis());
                generators.put(s, mersennetwisterfast);
            }
            int j = templateparser.nodeCount("li", xmlnode.XMLChild, false);
            if (j == 0)
                return "";
            if (j == 1)
                return templateparser.evaluate(i++, templateparser.getNode("li", xmlnode.XMLChild, 1).XMLChild);
            else
                return templateparser.evaluate(i++, templateparser.getNode("li", xmlnode.XMLChild, mersennetwisterfast.nextInt(j) + 1).XMLChild);
        } else {
            throw new AIMLProcessorException("<random></random> must have content!");
        }
    }

}
