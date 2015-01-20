// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.logging;

import org.alicebot.server.core.Globals;
import org.alicebot.server.core.util.XMLResourceSpec;
import org.alicebot.server.core.util.XMLWriter;

import java.util.HashMap;

public class XMLLog {

    private static final String ENC_UTF8 = "UTF-8";
    private static final String RESOURCE_BASE = Globals.getProperty("programd.logging.xml.resource-base", "../resources/");
    private static final XMLResourceSpec GENERIC_CHAT;
    private static HashMap entryCounts = new HashMap();
    private static int ROLLOVER;
    static {
        GENERIC_CHAT = new XMLResourceSpec();
        GENERIC_CHAT.description = "Chat Log";
        GENERIC_CHAT.rolloverAtMax = true;
        GENERIC_CHAT.rolloverAtRestart = Boolean.valueOf(Globals.getProperty("programd.logging.xml.chat.rollover-at-restart", "true")).booleanValue();
        GENERIC_CHAT.root = "exchanges";
        GENERIC_CHAT.stylesheet = Globals.getProperty("programd.logging.xml.chat.stylesheet-path", RESOURCE_BASE + "logs/view-chat.xsl");
        GENERIC_CHAT.encoding = Globals.getProperty("programd.logging.xml.chat.encoding", "UTF-8");
        GENERIC_CHAT.dtd = XMLResourceSpec.HTML_ENTITIES_DTD;
        try {
            ROLLOVER = Integer.parseInt(Globals.getProperty("programd.logging.xml.rollover", "2000"));
        } catch (NumberFormatException numberformatexception) {
            ROLLOVER = 2000;
        }
    }
    public XMLLog() {
    }

    public static XMLResourceSpec getChatlogSpecClone() {
        return (XMLResourceSpec) GENERIC_CHAT.clone();
    }

    public static void log(String s, XMLResourceSpec xmlresourcespec) {
        Object obj = entryCounts.get(xmlresourcespec);
        if (obj != null) {
            int i = ((Integer) obj).intValue();
            if (xmlresourcespec.rolloverAtMax) {
                if (++i % ROLLOVER == 0)
                    XMLWriter.rollover(xmlresourcespec);
                entryCounts.put(xmlresourcespec, new Integer(i));
            }
        } else if (xmlresourcespec.rolloverAtRestart) {
            XMLWriter.rollover(xmlresourcespec);
            entryCounts.put(xmlresourcespec, new Integer(1));
        }
        XMLWriter.write(s, xmlresourcespec);
    }
}
