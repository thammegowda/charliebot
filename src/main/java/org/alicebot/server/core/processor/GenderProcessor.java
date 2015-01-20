// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.processor;

import org.alicebot.server.core.Bots;
import org.alicebot.server.core.Globals;
import org.alicebot.server.core.parser.TemplateParser;
import org.alicebot.server.core.parser.XMLNode;
import org.alicebot.server.core.util.Substituter;

import java.util.HashMap;

// Referenced classes of package org.alicebot.server.core.processor:
//            AIMLProcessor, ProcessorException, AIMLProcessorException

public class GenderProcessor extends AIMLProcessor {

    public static final String label = "gender";
    private static HashMap substitutionMap = new HashMap();

    public GenderProcessor() {
    }

    public static String applySubstitutions(String s, String s1) {
        return Substituter.applySubstitutions(Bots.getBot(s1).getGenderSubstitutionsMap(), s);
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
        if (xmlnode.XMLType == 1) {
            if (!Globals.supportDeprecatedTags())
                return templateparser.shortcutTag(i, "gender", 0, "", "star", 1);
            else
                return templateparser.shortcutTag(i, "bot", 1, "name=\"gender\"", "", 1);
        } else {
            throw new AIMLProcessorException("Invalid gender element!");
        }
    }

}
