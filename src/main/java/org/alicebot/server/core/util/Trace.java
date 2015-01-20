// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.util;

import org.alicebot.server.core.Globals;

import java.io.PrintStream;
import java.util.StringTokenizer;

// Referenced classes of package org.alicebot.server.core.util:
//            StackParser, MessagePrinter

public class Trace {

    public static final String NO_FLAG = "";
    public static final String PROGRAM_MESSAGE;
    public static final String USER_INFO;
    public static final String USER_ERROR;
    public static final String DEVELOPER_INFO;
    public static final String DEVELOPER_ERROR;
    public static final String INSIST;
    public static final String COLON_SPACE = ": ";
    private static final boolean EMIT_USER_INFO;
    private static final boolean EMIT_USER_ERRORS;
    private static final boolean EMIT_DEVELOPER_INFO;
    private static final boolean METHOD_NAMES_ALWAYS;
    private static final boolean EMIT_DEVELOPER_ERRORS;
    private static final boolean SHOW_MESSAGE_FLAGS;
    private static final String THIS_PACKAGE = "org.alicebot.server.core.util.";
    private static final String FATAL_USER_UNINFORMATIVE = "Fatal user error. User error messages are turned off.";
    private static final String FATAL_DEVELOPER_UNINFORMATIVE = "Fatal developer error. Developer error messages are turned off.";
    private static final String EXITING = "Exiting.";
    private static PrintStream outStream;
    static {
        EMIT_USER_INFO = Globals.showConsole();
        EMIT_USER_ERRORS = EMIT_USER_INFO;
        EMIT_DEVELOPER_INFO = EMIT_USER_INFO ? Boolean.valueOf(Globals.getProperty("programd.console.developer", "false")).booleanValue() : false;
        METHOD_NAMES_ALWAYS = EMIT_DEVELOPER_INFO ? Boolean.valueOf(Globals.getProperty("programd.console.developer.method-names-always", "false")).booleanValue() : false;
        EMIT_DEVELOPER_ERRORS = EMIT_DEVELOPER_INFO;
        SHOW_MESSAGE_FLAGS = Boolean.valueOf(Globals.getProperty("programd.console.message-flags", "true")).booleanValue();
        PROGRAM_MESSAGE = SHOW_MESSAGE_FLAGS ? "P " : "";
        USER_INFO = SHOW_MESSAGE_FLAGS ? "u " : "";
        USER_ERROR = SHOW_MESSAGE_FLAGS ? "U " : "";
        DEVELOPER_INFO = SHOW_MESSAGE_FLAGS ? "d " : "";
        DEVELOPER_ERROR = SHOW_MESSAGE_FLAGS ? "D " : "";
        INSIST = SHOW_MESSAGE_FLAGS ? "! " : "";
        outStream = System.out;
    }
    public Trace() {
    }

    public static void setOut(PrintStream printstream) {
        outStream = printstream;
    }

    public static void userfail(String s) {
        if (EMIT_USER_ERRORS) {
            if (!METHOD_NAMES_ALWAYS)
                emit(s, USER_ERROR);
            else
                emit(StackParser.getStackMethodBefore("org.alicebot.server.core.util.", true) + ": " + s, USER_ERROR);
        } else {
            emit("Fatal user error. User error messages are turned off.", USER_ERROR);
        }
    }

    public static void userfail(Throwable throwable) {
        if (EMIT_USER_ERRORS)
            emit(throwable, USER_ERROR);
        else
            emit("Fatal user error. User error messages are turned off.", USER_ERROR);
    }

    public static void userinfo(String s) {
        if (EMIT_USER_INFO)
            if (!METHOD_NAMES_ALWAYS)
                emit(s, USER_INFO);
            else
                emit(StackParser.getStackMethodBefore("org.alicebot.server.core.util.", true) + ": " + s, USER_INFO);
    }

    public static void userinfo(String as[]) {
        if (EMIT_USER_INFO)
            if (!METHOD_NAMES_ALWAYS)
                emit(as, USER_INFO);
            else
                emit(as, StackParser.getStackMethodBefore("org.alicebot.server.core.util.", true) + ": ", USER_INFO);
    }

    public static void devfail(String s) {
        if (EMIT_DEVELOPER_ERRORS)
            emit(StackParser.getStackMethodBefore("org.alicebot.server.core.util.", true) + ": " + s, DEVELOPER_ERROR);
        else
            emit("Fatal developer error. Developer error messages are turned off.", DEVELOPER_ERROR);
    }

    public static void devfail(String s, Throwable throwable) {
        if (EMIT_DEVELOPER_ERRORS) {
            emit(s, DEVELOPER_ERROR);
            emit(throwable, DEVELOPER_ERROR);
        } else {
            emit("Fatal developer error. Developer error messages are turned off.", DEVELOPER_ERROR);
        }
    }

    public static void devfail(Throwable throwable) {
        if (EMIT_DEVELOPER_ERRORS)
            emit(throwable, DEVELOPER_ERROR);
        else
            emit("Fatal developer error. Developer error messages are turned off.", DEVELOPER_ERROR);
    }

    public static void devinfo(String s) {
        if (EMIT_DEVELOPER_INFO)
            emit(StackParser.getStackMethodBefore("org.alicebot.server.core.util.", true) + ": " + s, DEVELOPER_INFO);
    }

    public static void devinfo(Throwable throwable) {
        if (EMIT_DEVELOPER_ERRORS)
            emit(throwable, DEVELOPER_ERROR);
    }

    public static void insist(String s) {
        emit(s, INSIST);
    }

    public static void insist(String as[]) {
        emit(as, INSIST);
    }

    private static void emit(String s, String s1) {
        MessagePrinter.println(s, s1, outStream, 1);
    }

    private static void emit(String as[], String s) {
        for (int i = 0; i < as.length; i++)
            MessagePrinter.println(as[i], s, outStream, 1);

    }

    private static void emit(String as[], String s, String s1) {
        for (int i = 0; i < as.length; i++)
            MessagePrinter.println(s + as[i], s1, outStream, 1);

    }

    private static void emit(Throwable throwable, String s) {
        String s1 = throwable.getMessage();
        if (s1 != null)
            emit(s1, s);
        for (StringTokenizer stringtokenizer = StackParser.getStackTraceFor(throwable); stringtokenizer.hasMoreElements(); )
            if (!METHOD_NAMES_ALWAYS)
                emit(stringtokenizer.nextToken(), s);
            else
                emit(StackParser.getStackMethodBefore("org.alicebot.server.core.util.", true) + ": " + stringtokenizer.nextToken(), s);

    }
}
