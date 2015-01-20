// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.processor.loadtime;

import org.alicebot.server.core.parser.StartupFileParser;
import org.alicebot.server.core.parser.XMLNode;
import org.alicebot.server.core.util.Toolkit;

// Referenced classes of package org.alicebot.server.core.processor.loadtime:
//            StartupElementProcessor, InvalidStartupElementException

public class PropertyProcessor extends StartupElementProcessor {

    public static final String label = "property";

    public PropertyProcessor() {
    }

    public String process(int i, XMLNode xmlnode, StartupFileParser startupfileparser)
            throws InvalidStartupElementException {
        if (xmlnode.XMLType == 1) {
            String s = Toolkit.getAttributeValue("name", xmlnode.XMLAttr);
            if (!s.equals("")) {
                String s1 = Toolkit.getAttributeValue("value", xmlnode.XMLAttr);
                startupfileparser.getCurrentBot().setPropertyValue(s, s1);
            }
        } else {
            throw new InvalidStartupElementException("<property/> cannot have contents!");
        }
        return "";
    }
}
