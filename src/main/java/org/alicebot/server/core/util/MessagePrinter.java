// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.util;

import org.alicebot.server.core.Globals;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

// Referenced classes of package org.alicebot.server.core.util:
//            Trace

public class MessagePrinter {

    public static final int CONSOLE = 1;
    public static final int LOG = 2;
    private static final String LINE_SEPARATOR = System.getProperty("line.separator", "\n");
    private static final String SPACE = " ";
    private static final String LBRACKET = "[";
    private static final String RBRACKET = "]";
    private static final String EMPTY_STRING = "";
    private static final String TIMESTAMP_LOG_FORMAT = Globals.getProperty("programd.logging.timestamp-format", "yyyy-MM-dd H:mm:ss");
    private static final String TIMESTAMP_CONSOLE_FORMAT = Globals.getProperty("programd.console.timestamp-format", "H:mm:ss");
    private static final boolean consoleTimestamp = TIMESTAMP_CONSOLE_FORMAT.length() > 0;
    private static final boolean logTimestamp = TIMESTAMP_LOG_FORMAT.length() > 0;
    private static final String FILE_WRITER_ERROR = "Error writing to filewriter ";
    private static boolean midLine = false;
    private static String lastPrompt;
    private static String lastTypeFlag;
    private static boolean lastLongFormat = false;
    public MessagePrinter() {
    }

    public static void println(String s, String s1, PrintStream printstream, int i) {
        if (midLine)
            printstream.println();
        printstream.println(s1 + timestamp(i) + s);
        midLine = false;
    }

    public static void print(String s, String s1, PrintStream printstream, int i) {
        if (midLine)
            printstream.println();
        printstream.print(s1 + timestamp(i) + s);
        midLine = true;
    }

    public static void println(String s, String s1, FileWriter filewriter, int i) {
        try {
            filewriter.write(s1 + timestamp(i) + s + LINE_SEPARATOR);
            filewriter.flush();
        } catch (IOException ioexception) {
            Trace.devinfo("Error writing to filewriter " + filewriter);
            Trace.devinfo(ioexception);
        }
    }

    private static String timestamp(int i) {
        if (i == 1)
            if (consoleTimestamp)
                return "[" + (new SimpleDateFormat(TIMESTAMP_CONSOLE_FORMAT)).format(new Date()) + "]" + " ";
            else
                return "";
        if (i == 2) {
            if (logTimestamp)
                return "[" + (new SimpleDateFormat(TIMESTAMP_LOG_FORMAT)).format(new Date()) + "]" + " ";
            else
                return "";
        } else {
            return "";
        }
    }

    public static void gotLine() {
        midLine = false;
    }

}
