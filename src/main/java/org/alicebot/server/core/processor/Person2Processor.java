// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.processor;

import org.alicebot.server.core.Bots;
import org.alicebot.server.core.parser.TemplateParser;
import org.alicebot.server.core.parser.XMLNode;
import org.alicebot.server.core.util.Substituter;

import java.util.HashMap;

// Referenced classes of package org.alicebot.server.core.processor:
//            AIMLProcessor, ProcessorException, AIMLProcessorException

public class Person2Processor extends AIMLProcessor {

    public static final String label = "person2";
    private static HashMap substitutionMap = new HashMap();

    public Person2Processor() {
    }

    public static String applySubstitutions(String s, String s1) {
        return Substituter.applySubstitutions(Bots.getBot(s1).getPerson2SubstitutionsMap(), s);
    }

    public static void addSubstitution(String s, String s1) {
        if (s != null && s1 != null)
            substitutionMap.put(s.toUpperCase(), s1);
    }

    public String process(int i, XMLNode xmlnode, TemplateParser templateparser)
            throws AIMLProcessorException {
        if (xmlnode.XMLType == 0)
            try {
                return templateparser.processResponse(applySubstitutions(templateparser.evaluate(i++, xmlnode.XMLChild), templateparser.getBotID()));
            } catch (ProcessorException processorexception) {
                throw (AIMLProcessorException) processorexception;
            }
        else
            return templateparser.shortcutTag(i, "person2", 0, "", "star", 1);
    }

}
