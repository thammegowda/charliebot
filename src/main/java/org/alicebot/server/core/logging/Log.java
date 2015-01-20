// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.logging;

import org.alicebot.server.core.Globals;
import org.alicebot.server.core.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;

public class Log {

    public static final String CHAT = Globals.getProperty("programd.logging.chat.path", "./logs/chat.log");
    public static final String LISTENERS = Globals.getProperty("programd.logging.listeners.path", "./logs/listeners.log");
    public static final String DATABASE = Globals.getProperty("programd.logging.database.path", "./logs/database.log");
    public static final String ERROR = Globals.getProperty("programd.logging.error.path", "./logs/error.log");
    public static final String GOSSIP = Globals.getProperty("programd.logging.gossip.path", "./logs/gossip.log");
    public static final String INTERPRETER = Globals.getProperty("programd.logging.interpreter.path", "./logs/interpreter.log");
    public static final String LEARN = Globals.getProperty("programd.logging.learn.path", "./logs/learn.log");
    public static final String MERGE = Globals.getProperty("programd.logging.merge.path", "./logs/merge.log");
    public static final String STARTUP = Globals.getProperty("programd.logging.startup.path", "./logs/startup.log");
    public static final String SYSTEM = Globals.getProperty("programd.logging.system.path", "./logs/system.log");
    public static final String TARGETING = Globals.getProperty("programd.logging.targeting.path", "./logs/targeting.log");
    public static final String RUNTIME = Globals.getProperty("programd.logging.runtime.path", "./logs/runtime.log");
    private static final String LOGFILE = "log file";
    private static final Logger GENERAL_LOG = LoggerFactory.getLogger(Log.class);

    public Log() {
    }

    public static void log(String s, String s1) {
        Toolkit.checkOrCreate(s1, "log file");
        GENERAL_LOG.info(s);
        FileWriter filewriter;
        try {
            filewriter = new FileWriter(s1, true);
        } catch (IOException ioexception) {
            throw new UserError("Could not create log file \"" + s1 + "\".");
        }
        MessagePrinter.println(s, s1, filewriter, 2);
        try {
            filewriter.close();
        } catch (IOException ioexception1) {
            throw new DeveloperError("Could not close FileWriter!");
        }
    }

    public static void log(Throwable throwable, String s) {
        Toolkit.checkOrCreate(s, "log file");
        GENERAL_LOG.error(throwable.getMessage(), throwable);
        FileWriter filewriter;
        try {
            filewriter = new FileWriter(s, true);
        } catch (IOException ioexception) {
            throw new UserError("Could not create log file \"" + s + "\".");
        }
        String s1 = throwable.getMessage();
        if (s1 != null)
            MessagePrinter.println(s1, s, filewriter, 2);
        for (StringTokenizer stringtokenizer = StackParser.getStackTraceFor(throwable); stringtokenizer.hasMoreElements(); MessagePrinter.println(stringtokenizer.nextToken(), s, filewriter, 2))
            ;
        try {
            filewriter.close();
        } catch (IOException ioexception1) {
            throw new DeveloperError("Could not close FileWriter!");
        }
    }

    public static void log(String as[], String s) {
        int i = as.length;
        for (int j = 0; j < i; j++)
            log(as[j], s);

    }

    public static void userinfo(String s, String s1) {
        Trace.userinfo(s);
        log(s, s1);
    }

    public static void userinfo(String s, String as[]) {
        Trace.userinfo(s);
        int i = as.length;
        for (int j = 0; j < i; j++)
            log(s, as[j]);

    }

    public static void userinfo(String as[], String s) {
        int i = as.length;
        for (int j = 0; j < i; j++)
            userinfo(as[j], s);

    }

    public static void userinfo(String as[], String as1[]) {
        Trace.userinfo(as);
        int i = as1.length;
        for (int j = 0; j < i; j++)
            log(as, as1[j]);

    }

    public static void userfail(String s, String s1) {
        Trace.userfail(s);
        log(s, s1);
    }

    public static void userfail(String as[], String s) {
        int i = as.length;
        for (int j = 0; j < i; j++)
            userfail(as[j], s);

    }

    public static void userfail(String s, String as[]) {
        Trace.userfail(s);
        int i = as.length;
        for (int j = 0; j < i; j++)
            log(s, as[j]);

    }

    public static void userfail(String as[], String as1[]) {
        userfail(as, as1[0]);
        int i = as1.length - 1;
        for (int j = 0; j < i; j++)
            log(as, as1[j]);

    }

    public static void userfail(String s, Throwable throwable, String s1) {
        Trace.userfail(s);
        log(s, s1);
        Trace.userfail(throwable);
        log(throwable, s1);
    }

    public static void userfail(UserError usererror) {
        Exception exception = usererror.getException();
        if (exception == null)
            userfail(usererror.getMessage(), ERROR);
        else
            userfail(usererror.getMessage(), ((Throwable) (exception)), ERROR);
    }

    public static void devinfo(String s, String s1) {
        Trace.devinfo(s);
        log(s, s1);
    }

    public static void devinfo(String s, String as[]) {
        Trace.devinfo(s);
        int i = as.length;
        for (int j = 0; j < i; j++)
            log(s, as[j]);

    }

    public static void devfail(String s, String s1) {
        Trace.devfail(s);
        log(s, s1);
    }

    public static void devfail(String s, Throwable throwable, String s1) {
        Trace.devfail(s);
        log(s, s1);
        Trace.devfail(throwable);
        log(throwable, s1);
    }

    public static void devfail(String s, String as[]) {
        Trace.devfail(s);
        int i = as.length;
        for (int j = 0; j < i; j++)
            log(s, as[j]);

    }

    public static void devfail(DeveloperError developererror) {
        Throwable throwable = developererror.getEmbedded();
        if (throwable == null)
            devfail(developererror.getMessage(), ERROR);
        else
            devfail(developererror.getMessage(), throwable, ERROR);
    }

    public static void devfail(RuntimeException runtimeexception) {
        devfail("Unforeseen runtime exception.", ((Throwable) (runtimeexception)), ERROR);
    }

}
