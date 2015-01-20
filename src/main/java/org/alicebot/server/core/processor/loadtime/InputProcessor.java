// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.processor.loadtime;

import org.alicebot.server.core.parser.StartupFileParser;
import org.alicebot.server.core.parser.XMLNode;

// Referenced classes of package org.alicebot.server.core.processor.loadtime:
//            StartupElementProcessor, InvalidStartupElementException, SubstitutionsProcessor

public class InputProcessor extends StartupElementProcessor {

    public static final String label = "input";

    public InputProcessor() {
    }

    public String process(int i, XMLNode xmlnode, StartupFileParser startupfileparser)
            throws InvalidStartupElementException {
        SubstitutionsProcessor.addSubstitutions(0, xmlnode, startupfileparser);
        return "";
    }
}
