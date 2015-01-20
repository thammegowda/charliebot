// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core;

import org.alicebot.server.core.loader.AIMLLoader;
import org.alicebot.server.core.loader.AIMLWatcher;
import org.alicebot.server.core.logging.Log;
import org.alicebot.server.core.node.Nodemapper;
import org.alicebot.server.core.node.Nodemaster;
import org.alicebot.server.core.parser.AIMLReader;
import org.alicebot.server.core.targeting.TargetMaster;
import org.alicebot.server.core.util.Match;
import org.alicebot.server.core.util.NoMatchException;
import org.alicebot.server.core.util.Toolkit;
import org.alicebot.server.core.util.Trace;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

// Referenced classes of package org.alicebot.server.core:
//            Globals, Bots, PredicateMaster, Bot

public class Graphmaster {

    public static final String COPYRIGHT[] = {
            "Alicebot (c) 1995-2002 A.L.I.C.E. AI Foundation", "All Rights Reserved.", "This program is free software; you can redistribute it and/or", "modify it under the terms of the GNU General Public License", "as published by the Free Software Foundation; either version 2", "of the License, or (at your option) any later version."
    };
    public static final String VERSION = "4.1.8";
    public static final String BUILD = "00";
    public static final String TEMPLATE = "<template>";
    public static final String THAT = "<that>";
    public static final String TOPIC = "<topic>";
    public static final String BOTID = "<botid>";
    public static final String FILENAME = "<filename>";
    public static final String ASTERISK = "*";
    public static final String UNDERSCORE = "_";
    public static final String PATH_SEPARATOR = ":";
    public static final String ACTIVATIONS = "<activations>";
    private static final String EMPTY_STRING = "";
    private static final String MARKER_START = "<";
    private static final String MARKER_END = ">";
    private static final String SPACE = " ";
    private static final int S_INPUT = 0;
    private static final int S_THAT = 1;
    private static final int S_TOPIC = 2;
    private static final int S_BOTID = 3;
    protected static int RESPONSE_TIMEOUT;
    private static Stack WORKING_DIRECTORY;
    static {
        WORKING_DIRECTORY = new Stack();
        WORKING_DIRECTORY.push(System.getProperty("user.dir"));
        try {
            RESPONSE_TIMEOUT = Integer.parseInt(Globals.getProperty("programd.response-timeout", "1000"));
        } catch (NumberFormatException numberformatexception) {
            RESPONSE_TIMEOUT = 1000;
        }
    }
    private static Nodemapper ROOT = new Nodemaster();
    private static int TOTAL_CATEGORIES = 0;
    private static TreeSet patternVocabulary = new TreeSet();
    private static boolean loadtime = true;
    private static boolean startupLoaded = false;
    private static Set ACTIVATED_NODES = new HashSet();
    private Graphmaster() {
    }

    public static Nodemapper add(String s, String s1, String s2, String s3) {
        ArrayList arraylist = Toolkit.wordSplit(s);
        arraylist.add("<that>");
        arraylist.addAll(Toolkit.wordSplit(s1));
        arraylist.add("<topic>");
        arraylist.addAll(Toolkit.wordSplit(s2));
        arraylist.add("<botid>");
        arraylist.add(s3);
        Nodemapper nodemapper = add(arraylist.listIterator(), ROOT);
        return nodemapper;
    }

    private static Nodemapper add(ListIterator listiterator, Nodemapper nodemapper) {
        if (!listiterator.hasNext()) {
            nodemapper.setTop();
            return nodemapper;
        }
        String s = (String) listiterator.next();
        Object obj;
        if (nodemapper.containsKey(s)) {
            obj = (Nodemapper) nodemapper.get(s);
        } else {
            obj = new Nodemaster();
            nodemapper.put(s, obj);
            ((Nodemapper) (obj)).setParent(nodemapper);
        }
        patternVocabulary.add(s);
        return add(listiterator, ((Nodemapper) (obj)));
    }

    private static void remove(Nodemapper nodemapper) {
        Nodemapper nodemapper1 = nodemapper.getParent();
        if (nodemapper1 != null) {
            nodemapper1.remove(nodemapper);
            if (nodemapper1.size() == 0)
                remove(nodemapper1);
        }
        nodemapper = null;
    }

    public static Match match(String s, String s1, String s2, String s3)
            throws NoMatchException {
        ArrayList arraylist;
        if (s.length() > 0) {
            arraylist = Toolkit.wordSplit(s);
        } else {
            arraylist = new ArrayList();
            arraylist.add("*");
        }
        arraylist.add("<that>");
        if (s1.length() > 0)
            arraylist.addAll(Toolkit.wordSplit(s1));
        else
            arraylist.add("*");
        arraylist.add("<topic>");
        if (s2.length() > 0)
            arraylist.addAll(Toolkit.wordSplit(s2));
        else
            arraylist.add("*");
        arraylist.add("<botid>");
        arraylist.add(s3);
        Match match1 = match(ROOT, ROOT, ((List) (arraylist)), "", new StringBuffer(), 0, System.currentTimeMillis() + (long) RESPONSE_TIMEOUT);
        if (match1 != null) {
            return match1;
        } else {
            Trace.devinfo("Match is null.");
            throw new NoMatchException(s);
        }
    }

    private static Match match(Nodemapper nodemapper, Nodemapper nodemapper1, List list, String s, StringBuffer stringbuffer, int i, long l) {
        if (System.currentTimeMillis() >= l)
            return null;
        if (list.size() < nodemapper.getHeight())
            return null;
        if (list.size() == 0)
            if (nodemapper.containsKey("<template>")) {
                Match match1 = new Match();
                match1.setBotID(stringbuffer.toString());
                match1.setNodemapper(nodemapper);
                return match1;
            } else {
                return null;
            }
        String s1 = ((String) list.get(0)).trim();
        List list1 = list.subList(1, list.size());
        if (nodemapper.containsKey("_")) {
            StringBuffer stringbuffer1 = new StringBuffer();
            synchronized (stringbuffer1) {
                stringbuffer1.append(stringbuffer);
                stringbuffer1.append(' ');
                stringbuffer1.append('_');
            }
            Match match2 = match((Nodemapper) nodemapper.get("_"), nodemapper, list1, s1, stringbuffer1, i, l);
            if (match2 != null) {
                switch (i) {
                    default:
                        break;

                    case 0: // '\0'
                        if (s.length() > 0)
                            match2.pushInputStar(s);
                        break;

                    case 1: // '\001'
                        if (s.length() > 0)
                            match2.pushThatStar(s);
                        break;

                    case 2: // '\002'
                        if (s.length() > 0)
                            match2.pushTopicStar(s);
                        break;
                }
                return match2;
            }
        }
        if (nodemapper.containsKey(s1))
            if (s1.startsWith("<")) {
                if (s1.equals("<that>"))
                    i = 1;
                else if (s1.equals("<topic>"))
                    i = 2;
                else if (s1.equals("<botid>"))
                    i = 3;
                Match match3 = match((Nodemapper) nodemapper.get(s1), nodemapper, list1, "", new StringBuffer(), i, l);
                if (match3 != null) {
                    switch (i) {
                        default:
                            break;

                        case 1: // '\001'
                            if (s.length() > 0)
                                match3.pushInputStar(s);
                            match3.setPattern(stringbuffer.toString());
                            break;

                        case 2: // '\002'
                            if (s.length() > 0)
                                match3.pushThatStar(s);
                            match3.setThat(stringbuffer.toString());
                            break;

                        case 3: // '\003'
                            if (s.length() > 0)
                                match3.pushTopicStar(s);
                            match3.setTopic(stringbuffer.toString());
                            break;
                    }
                    return match3;
                }
            } else {
                StringBuffer stringbuffer2 = new StringBuffer();
                synchronized (stringbuffer2) {
                    stringbuffer2.append(stringbuffer);
                    stringbuffer2.append(' ');
                    stringbuffer2.append(s1);
                }
                Match match4 = match((Nodemapper) nodemapper.get(s1), nodemapper, list1, s, stringbuffer2, i, l);
                if (match4 != null)
                    return match4;
            }
        if (nodemapper.containsKey("*")) {
            StringBuffer stringbuffer3 = new StringBuffer();
            synchronized (stringbuffer3) {
                stringbuffer3.append(stringbuffer);
                stringbuffer3.append(' ');
                stringbuffer3.append('*');
            }
            Match match5 = match((Nodemapper) nodemapper.get("*"), nodemapper, list1, s1, stringbuffer3, i, l);
            if (match5 != null) {
                switch (i) {
                    default:
                        break;

                    case 0: // '\0'
                        if (s.length() > 0)
                            match5.pushInputStar(s);
                        break;

                    case 1: // '\001'
                        if (s.length() > 0)
                            match5.pushThatStar(s);
                        break;

                    case 2: // '\002'
                        if (s.length() > 0)
                            match5.pushTopicStar(s);
                        break;
                }
                return match5;
            }
        }
        if (nodemapper.equals(nodemapper1.get("*")) || nodemapper.equals(nodemapper1.get("_")))
            return match(nodemapper, nodemapper1, list1, s + " " + s1, stringbuffer, i, l);
        else
            return null;
    }

    public static void markReady() {
        loadtime = false;
        if (Globals.showConsole())
            Log.userinfo(Bots.getCount() + " bots thinking with " + TOTAL_CATEGORIES + " categories.", Log.STARTUP);
        Trace.insist(COPYRIGHT);
        Log.userinfo("Charliebot version 4.1.8 Build [00]", Log.STARTUP);
    }

    public static void shutdown() {
        PredicateMaster.saveAll();
    }

    public static void activatedNode(Nodemapper nodemapper) {
        ACTIVATED_NODES.add(nodemapper);
    }

    public static void checkpoint() {
        Log.log("Targeting checkpoint.", Log.TARGETING);
        for (Iterator iterator = ACTIVATED_NODES.iterator(); iterator.hasNext(); ) {
            Nodemapper nodemapper = (Nodemapper) iterator.next();
            Set set = (Set) nodemapper.get("<activations>");
            for (Iterator iterator1 = set.iterator(); iterator1.hasNext(); iterator1.remove()) {
                String s = (String) iterator1.next();
                StringTokenizer stringtokenizer = new StringTokenizer(s, ":");
                String s1 = stringtokenizer.nextToken().trim();
                String s2 = stringtokenizer.nextToken().trim();
                String s3 = stringtokenizer.nextToken().trim();
                stringtokenizer.nextToken().trim();
                String s4 = (String) nodemapper.get("<template>");
                String s5 = stringtokenizer.nextToken().trim();
                String s6 = stringtokenizer.nextToken().trim();
                String s7 = stringtokenizer.nextToken().trim();
                stringtokenizer.nextToken().trim();
                String s8 = stringtokenizer.nextToken().trim();
                TargetMaster.add(s1, s2, s3, s4, s5, s6, s7, s8);
            }

        }

    }

    public static void load(String s, String s1) {
        if (s.length() < 1)
            Log.userinfo("Cannot open a file whose name has zero length.", Log.ERROR);
        Bot bot = null;
        boolean flag = true;
        if (s.equals(Globals.getStartupFilePath())) {
            if (startupLoaded) {
                Log.userinfo("Cannot reload startup file.", Log.ERROR);
            } else {
                startupLoaded = true;
                Log.userinfo("Starting up with \"" + s + "\".", Log.STARTUP);
            }
        } else {
            bot = Bots.getBot(s1);
        }
        BufferedReader bufferedreader = null;
        if (s.indexOf("://") != -1) {
            URL url = null;
            try {
                url = new URL(s);
            } catch (MalformedURLException malformedurlexception) {
                Log.userinfo("Malformed URL: \"" + s + "\"", Log.ERROR);
            }
            try {
                String s2 = Toolkit.getDeclaredXMLEncoding(url.openStream());
                bufferedreader = new BufferedReader(new InputStreamReader(url.openStream(), s2));
            } catch (IOException ioexception) {
                Log.userinfo("I/O error trying to read \"" + s + "\"", Log.ERROR);
            }
            if (!loadCheck(url, bot))
                return;
            flag = false;
        } else {
            if (s.indexOf("*") != -1) {
                String as[] = null;
                try {
                    as = Toolkit.glob(s, (String) WORKING_DIRECTORY.peek());
                } catch (FileNotFoundException filenotfoundexception) {
                    Log.userinfo(filenotfoundexception.getMessage(), Log.ERROR);
                }
                if (as != null) {
                    for (int i = as.length; --i >= 0; )
                        load(as[i], s1);

                    return;
                }
            }
            File file = new File(s);
            if (!file.exists()) {
                file = new File(WORKING_DIRECTORY.peek() + File.separator + s);
                if (file.exists()) {
                    s = WORKING_DIRECTORY.peek() + File.separator + s;
                } else {
                    Trace.userinfo("Couldn't find \"" + s + "\".");
                    return;
                }
            }
            if (file.exists() && !file.isDirectory()) {
                if (!loadCheck(file, bot))
                    return;
                try {
                    String s3 = Toolkit.getDeclaredXMLEncoding(new FileInputStream(s));
                    bufferedreader = new BufferedReader(new InputStreamReader(new FileInputStream(s), s3));
                } catch (IOException ioexception1) {
                    Log.userinfo("I/O error trying to read \"" + s + "\"", Log.ERROR);
                    return;
                }
                if (Globals.isWatcherActive())
                    AIMLWatcher.addWatchFile(s, s1);
                WORKING_DIRECTORY.push(file.getParent());
            } else {
                if (!file.exists())
                    Log.userinfo("\"" + s + "\" does not exist!", Log.ERROR);
                if (file.isDirectory())
                    Log.userinfo("\"" + s + "\" is a directory!", Log.ERROR);
            }
        }
        (new AIMLReader(s, bufferedreader, new AIMLLoader(s, s1), Globals.warnAboutDeprecatedTags())).read();
        if (flag && WORKING_DIRECTORY.size() > 1)
            WORKING_DIRECTORY.pop();
    }

    private static boolean loadCheck(Object obj, Bot bot) {
        if (bot == null)
            return true;
        HashMap hashmap = bot.getLoadedFilesMap();
        if (hashmap.keySet().contains(obj)) {
            if (loadtime)
                return false;
            unload(obj, bot);
        } else {
            hashmap.put(obj, new HashSet());
        }
        return true;
    }

    public static void unload(Object obj, Bot bot) {
        HashSet hashset = (HashSet) bot.getLoadedFilesMap().get(obj);
        for (Iterator iterator = hashset.iterator(); iterator.hasNext(); ) {
            remove((Nodemapper) iterator.next());
            TOTAL_CATEGORIES--;
        }

    }

    public static int getTotalCategories() {
        return TOTAL_CATEGORIES;
    }

    public static int incrementTotalCategories() {
        return TOTAL_CATEGORIES++;
    }

    public static int patternVocabularySize() {
        return patternVocabulary.size();
    }

    public static String getWorkingDirectory() {
        return (String) WORKING_DIRECTORY.peek();
    }
}
