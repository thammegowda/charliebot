// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.processor.loadtime;

import org.alicebot.server.core.parser.GenericParser;
import org.alicebot.server.core.parser.StartupFileParser;
import org.alicebot.server.core.parser.XMLNode;
import org.alicebot.server.core.processor.Processor;
import org.alicebot.server.core.processor.ProcessorException;
import org.alicebot.server.core.util.Toolkit;

// Referenced classes of package org.alicebot.server.core.processor.loadtime:
//            InvalidStartupElementException

public abstract class StartupElementProcessor extends Processor {

    protected static final String HREF = "href";

    public StartupElementProcessor() {
    }

    public String process(int i, XMLNode xmlnode, GenericParser genericparser)
            throws ProcessorException {
        try {
            return process(i, xmlnode, (StartupFileParser) genericparser);
        } catch (ClassCastException classcastexception) {
            throw new ProcessorException("Tried to pass a non-StartupFileParser to a StartupElementProcessor.");
        }
    }

    public abstract String process(int i, XMLNode xmlnode, StartupFileParser startupfileparser)
            throws InvalidStartupElementException;

    protected String getHref(XMLNode xmlnode) {
        return Toolkit.getAttributeValue("href", xmlnode.XMLAttr);
    }
}
