// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.processor.loadtime;

import org.alicebot.server.core.Graphmaster;
import org.alicebot.server.core.parser.StartupFileParser;
import org.alicebot.server.core.parser.XMLNode;
import org.alicebot.server.core.processor.ProcessorException;
import org.alicebot.server.core.util.Toolkit;
import org.alicebot.server.core.util.UserError;

import java.io.File;

// Referenced classes of package org.alicebot.server.core.processor.loadtime:
//            StartupElementProcessor, InvalidStartupElementException

public class PropertiesProcessor extends StartupElementProcessor {

    public static final String label = "properties";

    public PropertiesProcessor() {
    }

    public String process(int i, XMLNode xmlnode, StartupFileParser startupfileparser)
            throws InvalidStartupElementException {
        String s = getHref(xmlnode);
        if (s.length() > 0)
            try {
                return startupfileparser.processResponse(Toolkit.getFileContents(Graphmaster.getWorkingDirectory() + File.separator + s));
            } catch (ProcessorException processorexception) {
                throw new UserError(processorexception);
            }
        else
            return startupfileparser.evaluate(i++, xmlnode.XMLChild);
    }
}
