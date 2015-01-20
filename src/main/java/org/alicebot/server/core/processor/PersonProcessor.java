// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.processor;

import org.alicebot.server.core.Bots;
import org.alicebot.server.core.parser.TemplateParser;
import org.alicebot.server.core.parser.XMLNode;
import org.alicebot.server.core.util.Substituter;

// Referenced classes of package org.alicebot.server.core.processor:
//            AIMLProcessor, ProcessorException

public class PersonProcessor extends AIMLProcessor {

    public static final String label = "person";

    public PersonProcessor() {
    }

    public static String applySubstitutions(String s, String s1) {
        return Substituter.applySubstitutions(Bots.getBot(s1).getPersonSubstitutionsMap(), s);
    }

    public String process(int i, XMLNode xmlnode, TemplateParser templateparser) {
        if (xmlnode.XMLType == 0)
            try {
                return templateparser.processResponse(applySubstitutions(templateparser.evaluate(i++, xmlnode.XMLChild), templateparser.getBotID()));
            } catch (ProcessorException processorexception) {
                return "";
            }
        else
            return templateparser.shortcutTag(i, "person", 0, "", "star", 1);
    }
}
