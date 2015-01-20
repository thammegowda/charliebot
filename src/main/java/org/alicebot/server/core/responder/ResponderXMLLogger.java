// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.responder;

import org.alicebot.server.core.Bots;
import org.alicebot.server.core.Globals;
import org.alicebot.server.core.PredicateMaster;
import org.alicebot.server.core.logging.XMLLog;
import org.alicebot.server.core.util.Toolkit;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ResponderXMLLogger {

    private static final String TIMESTAMP_LOG_FORMAT = Globals.getProperty("programd.logging.timestamp-format", "yyyy-MM-dd H:mm:ss");
    private static final String EXCHANGE_START = "<exchange>";
    private static final String TIMESTAMP_START = "<timestamp>";
    private static final String TIMESTAMP_END = "</timestamp>";
    private static final String USERID_START = "<userid>";
    private static final String USERID_END = "</userid>";
    private static final String CLIENTNAME_START = "<clientname>";
    private static final String CLIENTNAME_END = "</clientname>";
    private static final String BOTID_START = "<botid>";
    private static final String BOTID_END = "</botid>";
    private static final String INPUT_START = "<input>";
    private static final String INPUT_END = "</input>";
    private static final String RESPONSE_START = "<response>";
    private static final String RESPONSE_END = "</response>";
    private static final String EXCHANGE_END = "</exchange>";
    private static final String LINE_SEPARATOR = System.getProperty("line.separator", "\n");
    private static final String INDENT = "    ";
    public ResponderXMLLogger() {
    }

    public static void log(String s, String s1, String s2, String s3, String s4) {
        String s5 = PredicateMaster.get(Globals.getClientNamePredicate(), s3, s4);
        XMLLog.log("    <exchange>" + LINE_SEPARATOR + "    " + "    " + "<timestamp>" + (new SimpleDateFormat(TIMESTAMP_LOG_FORMAT)).format(new Date()).trim() + "</timestamp>" + LINE_SEPARATOR + "    " + "    " + "<userid>" + s3 + "</userid>" + LINE_SEPARATOR + "    " + "    " + "<clientname>" + s5 + "</clientname>" + LINE_SEPARATOR + "    " + "    " + "<botid>" + s4 + "</botid>" + LINE_SEPARATOR + "    " + "    " + "<input>" + Toolkit.escapeXMLChars(s) + "</input>" + LINE_SEPARATOR + "    " + "    " + "<response>" + s1 + "</response>" + LINE_SEPARATOR + "    " + "</exchange>" + LINE_SEPARATOR, Bots.getBot(s4).getChatlogSpec());
    }

}
