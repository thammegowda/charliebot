// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.processor.loadtime;

import org.alicebot.server.core.parser.StartupFileParser;
import org.alicebot.server.core.parser.XMLNode;
import org.alicebot.server.core.util.Toolkit;

// Referenced classes of package org.alicebot.server.core.processor.loadtime:
//            StartupElementProcessor, InvalidStartupElementException

public class PredicateProcessor extends StartupElementProcessor {

    public static final String label = "predicate";
    private static final String NAME = "name";
    private static final String DEFAULT = "default";
    private static final String SET_RETURN = "set-return";
    private static final String VALUE = "value";
    public PredicateProcessor() {
    }

    public String process(int i, XMLNode xmlnode, StartupFileParser startupfileparser)
            throws InvalidStartupElementException {
        if (xmlnode.XMLType == 1) {
            String s = Toolkit.getAttributeValue("name", xmlnode.XMLAttr);
            if (s.equals(""))
                throw new InvalidStartupElementException("<predicate/> must specify a name!");
            String s1 = Toolkit.getAttributeValue("default", xmlnode.XMLAttr);
            if (s1.equals(""))
                s1 = null;
            String s2 = Toolkit.getAttributeValue("set-return", xmlnode.XMLAttr);
            boolean flag;
            if (s2.equals("name"))
                flag = true;
            else if (s2.equals("value"))
                flag = false;
            else
                throw new InvalidStartupElementException("Invalid value for set-return attribute on <predicate/>.");
            startupfileparser.getCurrentBot().addPredicateInfo(s, s1, flag);
            return "";
        } else {
            throw new InvalidStartupElementException("<predicate/> cannot have element content!");
        }
    }
}
