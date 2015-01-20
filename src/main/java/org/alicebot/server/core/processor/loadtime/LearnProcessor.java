// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.processor.loadtime;

import org.alicebot.server.core.Graphmaster;
import org.alicebot.server.core.parser.StartupFileParser;
import org.alicebot.server.core.parser.XMLNode;

// Referenced classes of package org.alicebot.server.core.processor.loadtime:
//            StartupElementProcessor, InvalidStartupElementException

public class LearnProcessor extends StartupElementProcessor {

    public static final String label = "learn";

    public LearnProcessor() {
    }

    public String process(int i, XMLNode xmlnode, StartupFileParser startupfileparser)
            throws InvalidStartupElementException {
        if (xmlnode.XMLType == 0) {
            Graphmaster.load(startupfileparser.evaluate(i++, xmlnode.XMLChild), startupfileparser.getCurrentBot().getID());
            return "";
        } else {
            throw new InvalidStartupElementException("<learn></learn> must have content!");
        }
    }
}
