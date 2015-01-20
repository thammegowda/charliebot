// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.processor;

import org.alicebot.server.core.parser.TemplateParser;
import org.alicebot.server.core.parser.XMLNode;

import java.util.StringTokenizer;

// Referenced classes of package org.alicebot.server.core.processor:
//            AIMLProcessor, AIMLProcessorException

public class FormalProcessor extends AIMLProcessor {

    public static final String label = "formal";
    private static final String SPACE = " ";

    public FormalProcessor() {
    }

    public String process(int i, XMLNode xmlnode, TemplateParser templateparser)
            throws AIMLProcessorException {
        if (xmlnode.XMLType == 0) {
            String s = templateparser.evaluate(i++, xmlnode.XMLChild);
            if (s.equals(""))
                return s;
            StringTokenizer stringtokenizer = new StringTokenizer(s, " ");
            StringBuffer stringbuffer = new StringBuffer(s.length());
            String s1;
            for (; stringtokenizer.hasMoreTokens(); stringbuffer.append(s1.substring(0, 1).toUpperCase() + s1.substring(1).toLowerCase())) {
                s1 = stringtokenizer.nextToken();
                if (stringbuffer.length() > 0)
                    stringbuffer.append(" ");
            }

            return stringbuffer.toString();
        } else {
            throw new AIMLProcessorException("<formal></formal> must have content!");
        }
    }
}
