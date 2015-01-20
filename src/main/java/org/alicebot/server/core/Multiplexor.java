// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core;

import org.alicebot.server.core.logging.Log;
import org.alicebot.server.core.node.Nodemapper;
import org.alicebot.server.core.parser.TemplateParser;
import org.alicebot.server.core.parser.TemplateParserException;
import org.alicebot.server.core.processor.ProcessorException;
import org.alicebot.server.core.responder.Responder;
import org.alicebot.server.core.util.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

// Referenced classes of package org.alicebot.server.core:
//            NoSuchPredicateException, ActiveMultiplexor, Bots, Bot, 
//            PredicateMaster, Graphmaster, Globals

public abstract class Multiplexor {

    protected static final String THAT = "that";
    protected static final String TOPIC = "topic";
    protected static final String INPUT = "input";
    protected static final String STAR = "star";
    protected static final String EMPTY_STRING = "";
    protected static final String SPACE = " ";
    protected static final String VALUE = "value";
    protected static final String ASTERISK = "*";
    protected static final String QUOTE_MARK = "\"";
    protected static final String HOST_NAME = Globals.getHostName();
    protected static final boolean SHOW_CONSOLE = Globals.showConsole();
    protected static final boolean SHOW_MATCH_TRACE = Globals.showMatchTrace();
    protected static final boolean USE_TARGETING = Globals.useTargeting();
    protected static final String PREDICATE_EMPTY_DEFAULT = Globals.getPredicateEmptyDefault();
    private static final String LABEL_MATCH = "Match: ";
    private static final String LABEL_FILENAME = "Filename: ";
    private static final String RESPONSE_SPACE = "Response ";
    private static final String SPACE_IN_SPACE = " in ";
    private static final String MS_AVERAGE = " ms. (Average: ";
    private static final String MS = " ms.)";
    private static final int TARGET_SKIP = Globals.getTargetSkip();
    protected static String SECRET_KEY;
    protected static long startTime = System.currentTimeMillis();
    protected static long responseCount = 0L;
    protected static long totalTime = 0L;
    protected static float avgResponseTime = 0.0F;
    private static Multiplexor proxy;
    private static ArrayList pulses = new ArrayList();
    public Multiplexor() {
    }

    public static synchronized String getResponse(String s, String s1, String s2, Responder responder) {
        Bot bot = Bots.getBot(s2);
        ArrayList arraylist = bot.sentenceSplit(bot.applyInputSubstitutions(responder.preprocess(s, HOST_NAME)));
        Iterator iterator = getReplies(arraylist, s1, s2).iterator();
        String s3 = "";
        for (Iterator iterator1 = arraylist.iterator(); iterator1.hasNext(); )
            s3 = responder.append((String) iterator1.next(), (String) iterator.next(), s3);

        responder.log(s, s3, HOST_NAME, s1, s2);
        s3 = responder.postprocess(s3);
        return s3;
    }

    public static String getInternalResponse(String s, String s1, String s2, TemplateParser templateparser) {
        Bot bot = Bots.getBot(s2);
        ArrayList arraylist = bot.sentenceSplit(PredicateMaster.get("that", 1, s1, s2));
        String s3 = InputNormalizer.patternFitIgnoreCase((String) arraylist.get(arraylist.size() - 1));
        if (s3.equals("") || s3.equals(PREDICATE_EMPTY_DEFAULT))
            s3 = "*";
        String s4 = InputNormalizer.patternFitIgnoreCase(PredicateMaster.get("topic", s1, s2));
        if (s4.equals("") || s4.equals(PREDICATE_EMPTY_DEFAULT))
            s4 = "*";
        return getMatchResult(s, s3, s4, s1, s2, templateparser);
    }

    private static ArrayList getReplies(ArrayList arraylist, String s, String s1) {
        ArrayList arraylist1 = new ArrayList(arraylist.size());
        Bot bot = Bots.getBot(s1);
        ArrayList arraylist2 = bot.sentenceSplit(PredicateMaster.get("that", 1, s, s1));
        String s2 = InputNormalizer.patternFitIgnoreCase((String) arraylist2.get(arraylist2.size() - 1));
        if (s2.equals("") || s2.equals(PREDICATE_EMPTY_DEFAULT))
            s2 = "*";
        String s3 = InputNormalizer.patternFitIgnoreCase(PredicateMaster.get("topic", s, s1));
        if (s3.equals("") || s3.equals(PREDICATE_EMPTY_DEFAULT))
            s3 = "*";
        Iterator iterator = arraylist.iterator();
        long l = 0L;
        if (SHOW_MATCH_TRACE)
            l = System.currentTimeMillis();
        for (; iterator.hasNext(); arraylist1.add(getReply((String) iterator.next(), s2, s3, s, s1)))
            ;
        responseCount++;
        if (SHOW_MATCH_TRACE) {
            l = System.currentTimeMillis() - l;
            totalTime += l;
            avgResponseTime = (float) totalTime / (float) responseCount;
            Trace.userinfo("Response " + responseCount + " in " + l + " ms. (Average: " + avgResponseTime + " ms.)");
        }
        if (responseCount % (long) TARGET_SKIP == 0L && USE_TARGETING)
            Graphmaster.checkpoint();
        if (arraylist1.size() == 0)
            arraylist1.add("");
        return arraylist1;
    }

    private static String getReply(String s, String s1, String s2, String s3, String s4) {
        PredicateMaster.push("input", s, s3, s4);
        TemplateParser templateparser;
        try {
            templateparser = new TemplateParser(s, s3, s4);
        } catch (TemplateParserException templateparserexception) {
            throw new DeveloperError(templateparserexception);
        }
        String s5 = null;
        try {
            s5 = getMatchResult(s, s1, s2, s3, s4, templateparser);
        } catch (DeveloperError developererror) {
            Log.devfail(developererror);
            Log.devfail("Exiting due to developer error.", Log.ERROR);
           throw new IllegalStateException(developererror);
        } catch (UserError usererror) {
            Log.userfail(usererror);
            Log.devfail("Exiting due to user error.", Log.ERROR);
            throw new IllegalStateException(usererror);
        } catch (RuntimeException runtimeexception) {
            Log.devfail(runtimeexception);
            Log.devfail("Exiting due to unforeseen runtime exception.", Log.ERROR);
            throw new IllegalStateException(runtimeexception);
        }
        if (s5 == null) {
            Log.devfail("getMatchReply generated a null reply!", Log.ERROR);
            throw new IllegalStateException("Null reply");
        }
        PredicateMaster.push("that", s5, s3, s4);
        return Toolkit.filterWhitespace(s5);
    }

    private static String getMatchResult(String s, String s1, String s2, String s3, String s4, TemplateParser templateparser) {
        if (SHOW_MATCH_TRACE)
            Trace.userinfo(PredicateMaster.get(Globals.getClientNamePredicate(), s3, s4) + '>' + " " + s + " " + ":" + " " + s1 + " " + ":" + " " + s2 + " " + ":" + " " + s4);
        String s5 = InputNormalizer.patternFitIgnoreCase(s);
        Match match = null;
        try {
            match = Graphmaster.match(InputNormalizer.patternFitIgnoreCase(s), s1, s2, s4);
        } catch (NoMatchException nomatchexception) {
            Log.userinfo(nomatchexception.getMessage(), Log.CHAT);
            return "";
        }
        if (match == null) {
            Log.userinfo("No match found for input \"" + s + "\".", Log.CHAT);
            return "";
        }
        if (SHOW_MATCH_TRACE) {
            Trace.userinfo("Match: " + match.getPath());
            Trace.userinfo("Filename: \"" + match.getFileName() + "\"");
        }
        ArrayList arraylist = match.getInputStars();
        if (arraylist.size() > 0)
            templateparser.setInputStars(arraylist);
        arraylist = match.getThatStars();
        if (arraylist.size() > 0)
            templateparser.setThatStars(arraylist);
        arraylist = match.getTopicStars();
        if (arraylist.size() > 0)
            templateparser.setTopicStars(arraylist);
        String s6 = match.getTemplate();
        String s7 = null;
        try {
            s7 = templateparser.processResponse(s6);
        } catch (ProcessorException processorexception) {
            Log.userinfo(processorexception.getMessage(), Log.ERROR);
            return "";
        }
        if (USE_TARGETING) {
            Nodemapper nodemapper = match.getNodemapper();
            if (nodemapper == null) {
                Trace.devinfo("Match nodemapper is null!");
            } else {
                Object obj = (Set) nodemapper.get("<activations>");
                if (obj == null)
                    obj = new HashSet();
                String s8 = match.getPath() + " " + ":" + " " + s5 + " " + ":" + " " + s1 + " " + ":" + " " + s2 + " " + ":" + " " + s4 + " " + ":" + " " + s7;
                if (!((Set) (obj)).contains(s8)) {
                    ((Set) (obj)).add(s8);
                    match.getNodemapper().put("<activations>", obj);
                    Graphmaster.activatedNode(match.getNodemapper());
                }
            }
        }
        return s7;
    }

    public static void addPulse(Pulse pulse1) {
        pulses.add(pulse1);
    }

    public static synchronized void pulse() {
        for (Iterator iterator = pulses.iterator(); iterator.hasNext(); ((Pulse) iterator.next()).emit())
            ;
    }

    public void initialize() {
        SECRET_KEY = (new Double(Math.random() * (double) System.currentTimeMillis())).toString();
        File file = new File("secret.key");
        file.delete();
        try {
            file.createNewFile();
        } catch (IOException ioexception) {
            throw new UserError("Error creating secret key file.");
        }
        PrintWriter printwriter;
        try {
            printwriter = new PrintWriter(new FileOutputStream(file));
        } catch (FileNotFoundException filenotfoundexception) {
            throw new UserError("Error writing secret key file.");
        }
        printwriter.print(SECRET_KEY);
        printwriter.flush();
        printwriter.close();
        proxy = ActiveMultiplexor.getInstance();
        addPulse(new IAmAlivePulse());
    }

    public float averageResponseTime() {
        return avgResponseTime;
    }

    public float queriesPerHour() {
        return (float) responseCount / ((float) (System.currentTimeMillis() - startTime) / 3600000F);
    }

    public abstract void savePredicate(String s, String s1, String s2, String s3);

    public abstract String loadPredicate(String s, String s1, String s2)
            throws NoSuchPredicateException;

    public abstract boolean checkUser(String s, String s1, String s2, String s3);

    public abstract boolean createUser(String s, String s1, String s2, String s3);

    public abstract boolean changePassword(String s, String s1, String s2, String s3);

    public abstract int useridCount(String s);

}
