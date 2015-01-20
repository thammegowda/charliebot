// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.loader;

import org.alicebot.server.core.Bot;
import org.alicebot.server.core.Bots;
import org.alicebot.server.core.Globals;
import org.alicebot.server.core.Graphmaster;
import org.alicebot.server.core.logging.Log;
import org.alicebot.server.core.node.Nodemapper;
import org.alicebot.server.core.parser.AIMLReaderListener;
import org.alicebot.server.core.util.Trace;

public class AIMLLoader
        implements AIMLReaderListener {

    protected static final boolean SHOW_CONSOLE = Globals.showConsole();
    private static final String SPACE = " ";
    private static final String EMPTY_STRING = "";
    private static final String ATOMIC_CLOSE = "/>";
    private static final String SLASH = "/";
    private static final String NAME = "name";
    private static final String LOCALHOST = "localhost";
    private static int NOTIFY_INTERVAL = Globals.getCategoryLoadNotifyInterval();
    private static String filename;
    private static String botid;
    private static Bot bot;
    private static String policy;
    public AIMLLoader(String s, String s1) {
        filename = s;
        botid = s1;
        if (s1 != null)
            bot = Bots.getBot(s1);
        policy = Globals.getMergePolicy();
    }

    public void newCategory(String s, String s1, String s2, String s3) {
        boolean flag = true;
        if (s == null)
            s = "*";
        if (s1 == null)
            s1 = "*";
        if (s2 == null)
            s2 = "*";
        if (s3 == null)
            s3 = "*";
        if (SHOW_CONSOLE && Graphmaster.getTotalCategories() % NOTIFY_INTERVAL == 0 && Graphmaster.getTotalCategories() > 0)
            Trace.userinfo(Graphmaster.getTotalCategories() + " categories loaded so far.");
        if (flag) {
            Nodemapper nodemapper = Graphmaster.add(s, s1, s2, botid);
            if (nodemapper.get("<template>") == null) {
                nodemapper.put("<filename>", filename);
                bot.addToFilenameMap(filename, nodemapper);
                nodemapper.put("<template>", s3);
                Graphmaster.incrementTotalCategories();
            } else if (!policy.equals("true")) {
                if (Globals.showConsole())
                    Log.userinfo(new String[]{
                            "Duplicate category:", s + " : " + s1 + " : " + s2, " in \"" + filename + "\"", "conflicts with category already loaded from", (String) nodemapper.get("<filename>")
                    }, Log.MERGE);
            } else {
                nodemapper.put("<filename>", filename);
                nodemapper.put("<template>", s3);
            }
        }
    }

}
