// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.processor.loadtime;

import org.alicebot.server.core.Bot;
import org.alicebot.server.core.Globals;
import org.alicebot.server.core.Graphmaster;
import org.alicebot.server.core.logging.Log;
import org.alicebot.server.core.parser.StartupFileParser;
import org.alicebot.server.core.parser.XMLNode;
import org.alicebot.server.core.processor.ProcessorException;
import org.alicebot.server.core.util.Toolkit;
import org.alicebot.server.core.util.UserError;

import java.io.File;

// Referenced classes of package org.alicebot.server.core.processor.loadtime:
//            StartupElementProcessor, InvalidStartupElementException

public class SentenceSplittersProcessor extends StartupElementProcessor {

    public static final String label = "sentence-splitters";
    private static final String SPLITTER = "splitter";

    public SentenceSplittersProcessor() {
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
        Bot bot = startupfileparser.getCurrentBot();
        int j = startupfileparser.nodeCount("splitter", xmlnode.XMLChild, true);
        for (int k = j; --k > 0; ) {
            XMLNode xmlnode1 = startupfileparser.getNode("splitter", xmlnode.XMLChild, k);
            if (xmlnode1.XMLType == 1) {
                String s1 = Toolkit.getAttributeValue("value", xmlnode1.XMLAttr);
                if (s1 != null)
                    bot.addSentenceSplitter(s1);
            } else {
                throw new InvalidStartupElementException("<splitter/> cannot have content!");
            }
        }

        if (Globals.showConsole())
            Log.userinfo("Loaded " + j + " " + xmlnode.XMLData + ".", Log.STARTUP);
        return "";
    }
}
