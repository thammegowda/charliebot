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

public class SubstitutionsProcessor extends StartupElementProcessor {

    public static final String label = "substitutions";
    static final int INPUT = 0;
    static final int GENDER = 1;
    static final int PERSON = 2;
    static final int PERSON2 = 3;
    private static final String SUBSTITUTE = "substitute";
    private static final String FIND = "find";
    private static final String REPLACE = "replace";
    public SubstitutionsProcessor() {
    }

    static void addSubstitutions(int i, XMLNode xmlnode, StartupFileParser startupfileparser)
            throws InvalidStartupElementException {
        int j = startupfileparser.nodeCount("substitute", xmlnode.XMLChild, true);
        Bot bot = startupfileparser.getCurrentBot();
        for (int k = j; k > 0; k--) {
            XMLNode xmlnode1 = startupfileparser.getNode("substitute", xmlnode.XMLChild, k);
            if (xmlnode1.XMLType == 1) {
                String s = Toolkit.getAttributeValue("find", xmlnode1.XMLAttr);
                String s1 = Toolkit.getAttributeValue("replace", xmlnode1.XMLAttr);
                if (s != null && s1 != null)
                    switch (i) {
                        case 0: // '\0'
                            bot.addInputSubstitution(s, s1);
                            break;

                        case 1: // '\001'
                            bot.addGenderSubstitution(s, s1);
                            break;

                        case 2: // '\002'
                            bot.addPersonSubstitution(s, s1);
                            break;

                        case 3: // '\003'
                            bot.addPerson2Substitution(s, s1);
                            break;
                    }
            } else {
                throw new InvalidStartupElementException("<" + xmlnode.XMLData + "/> cannot have content!");
            }
        }

        if (Globals.showConsole())
            Log.userinfo("Loaded " + j + " " + xmlnode.XMLData + " substitutions.", Log.STARTUP);
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
