// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.processor;

import org.alicebot.server.core.Bot;
import org.alicebot.server.core.Bots;
import org.alicebot.server.core.PredicateMaster;
import org.alicebot.server.core.parser.TemplateParser;
import org.alicebot.server.core.parser.XMLNode;
import org.alicebot.server.core.util.DeveloperError;
import org.alicebot.server.core.util.Toolkit;

import java.util.ArrayList;

// Referenced classes of package org.alicebot.server.core.processor:
//            AIMLProcessor

public abstract class IndexedPredicateProcessor extends AIMLProcessor {

    public IndexedPredicateProcessor() {
    }

    public String process(int i, XMLNode xmlnode, TemplateParser templateparser, String s, int j) {
        if (j != 1 && j != 2)
            return "";
        int ai[] = TemplateParser.getValid2dIndex(xmlnode);
        if (ai[0] <= 0)
            return "";
        String s1 = PredicateMaster.get(s, ai[0], templateparser.getUserID(), templateparser.getBotID());
        Bot bot = Bots.getBot(templateparser.getBotID());
        ArrayList arraylist = bot.sentenceSplit(s1);
        int k = arraylist.size();
        if (k == 0)
            return s1;
        if (ai[1] > k)
            return "";
        else
            return Toolkit.removeMarkup((String) arraylist.get(k - ai[1])).trim();
    }

    public String process(int i, XMLNode xmlnode, TemplateParser templateparser, ArrayList arraylist, int j) {
        if (j != 1)
            throw new DeveloperError("Wrong number of dimensions: " + j + " != 1");
        if (arraylist.isEmpty())
            return "";
        int k = TemplateParser.getValid1dIndex(xmlnode);
        if (--k >= arraylist.size())
            return "";
        else
            return Toolkit.removeMarkup((String) arraylist.get(k)).trim();
    }
}
