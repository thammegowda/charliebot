// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.processor;

import org.alicebot.server.core.PredicateMaster;
import org.alicebot.server.core.parser.TemplateParser;
import org.alicebot.server.core.parser.XMLNode;
import org.alicebot.server.core.util.NotAnAIMLPatternException;
import org.alicebot.server.core.util.PatternArbiter;
import org.alicebot.server.core.util.Toolkit;
import org.alicebot.server.core.util.Trace;

import java.util.LinkedList;
import java.util.ListIterator;

// Referenced classes of package org.alicebot.server.core.processor:
//            AIMLProcessor, AIMLProcessorException, ProcessorException

public class ConditionProcessor extends AIMLProcessor {

    public static final int NAME_VALUE_LI = 1;
    public static final int DEFAULT_LI = 2;
    public static final int VALUE_ONLY_LI = 3;
    public static final String label = "condition";
    private static final String LI = "li";
    public ConditionProcessor() {
    }

    public String process(int i, XMLNode xmlnode, TemplateParser templateparser)
            throws AIMLProcessorException {
        if (xmlnode.XMLType == 0) {
            if (xmlnode.XMLChild == null)
                return "";
            String s = Toolkit.getAttributeValue("name", xmlnode.XMLAttr);
            String s1 = Toolkit.getAttributeValue("value", xmlnode.XMLAttr);
            if (xmlnode.XMLAttr.indexOf("name=", 0) < 0 && xmlnode.XMLAttr.indexOf("value=", 0) < 0)
                return processListItem(i, templateparser, xmlnode.XMLChild, 1, s, s1);
            if (xmlnode.XMLAttr.indexOf("name=", 0) >= 0 && xmlnode.XMLAttr.indexOf("value=", 0) >= 0) {
                try {
                    if (PatternArbiter.matches(PredicateMaster.get(s, templateparser.getUserID(), templateparser.getBotID()), s1, true))
                        return processListItem(i, templateparser, xmlnode.XMLChild, 2, "", "");
                } catch (NotAnAIMLPatternException notanaimlpatternexception) {
                    Trace.devinfo(notanaimlpatternexception.getMessage());
                    return "";
                }
                return "";
            }
            if (xmlnode.XMLAttr.indexOf("name=", 0) >= 0 && xmlnode.XMLAttr.indexOf("value=", 0) < 0)
                return processListItem(i, templateparser, xmlnode.XMLChild, 3, s, "");
            else
                return "";
        } else {
            throw new AIMLProcessorException("<condition></condition> must have content!");
        }
    }

    public String processListItem(int i, TemplateParser templateparser, LinkedList linkedlist, int j, String s, String s1) {
        String s2 = "";
        if (linkedlist == null)
            return "";
        ListIterator listiterator = linkedlist.listIterator(0);
        String s3 = "";
        String s4 = "";
        String s7 = "";
        if (j == 3)
            s3 = PredicateMaster.get(s, templateparser.getUserID(), templateparser.getBotID());
        while (listiterator.hasNext()) {
            XMLNode xmlnode = (XMLNode) listiterator.next();
            if (xmlnode != null)
                label0:
                        switch (xmlnode.XMLType) {
                            default:
                                break;

                            case 2: // '\002'
                            case 3: // '\003'
                                s2 = s2 + xmlnode.XMLData;
                                break;

                            case 1: // '\001'
                                try {
                                    s2 = s2 + templateparser.processTag(i++, xmlnode);
                                } catch (ProcessorException processorexception) {
                                }
                                break;

                            case 0: // '\0'
                                if (!xmlnode.XMLData.equals("li")) {
                                    try {
                                        s2 = s2 + templateparser.processTag(i++, xmlnode);
                                    } catch (ProcessorException processorexception1) {
                                    }
                                    break;
                                }
                                switch (j) {
                                    default:
                                        break label0;

                                    case 1: // '\001'
                                        if (xmlnode.XMLAttr.indexOf("name=", 0) < 0 && xmlnode.XMLAttr.indexOf("value=", 0) < 0) {
                                            s2 = s2 + templateparser.evaluate(i++, xmlnode.XMLChild);
                                            break label0;
                                        }
                                        if (xmlnode.XMLAttr.indexOf("name=", 0) < 0 || xmlnode.XMLAttr.indexOf("value=", 0) < 0)
                                            break label0;
                                        String s8 = Toolkit.getAttributeValue("name", xmlnode.XMLAttr);
                                        String s5 = Toolkit.getAttributeValue("value", xmlnode.XMLAttr);
                                        try {
                                            if (PatternArbiter.matches(PredicateMaster.get(s8, templateparser.getUserID(), templateparser.getBotID()), s5, true))
                                                return s2 + templateparser.evaluate(i++, xmlnode.XMLChild);
                                        } catch (NotAnAIMLPatternException notanaimlpatternexception) {
                                            Trace.devinfo(notanaimlpatternexception.getMessage());
                                        }
                                        break label0;

                                    case 2: // '\002'
                                        s2 = s2 + templateparser.evaluate(i++, xmlnode.XMLChild);
                                        break label0;

                                    case 3: // '\003'
                                        break;
                                }
                                if (xmlnode.XMLAttr.indexOf("value=", 0) >= 0) {
                                    String s6 = Toolkit.getAttributeValue("value", xmlnode.XMLAttr);
                                    try {
                                        if (PatternArbiter.matches(s3, s6, true)) {
                                            s2 = s2 + templateparser.evaluate(i++, xmlnode.XMLChild);
                                            return s2;
                                        }
                                    } catch (NotAnAIMLPatternException notanaimlpatternexception1) {
                                        Trace.userinfo(notanaimlpatternexception1.getMessage());
                                    }
                                } else {
                                    s2 = s2 + templateparser.evaluate(i++, xmlnode.XMLChild);
                                    return s2;
                                }
                                break;
                        }
        }
        return s2;
    }
}
