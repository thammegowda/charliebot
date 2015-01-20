// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.loader;

import org.alicebot.server.core.Globals;
import org.alicebot.server.core.Graphmaster;
import org.alicebot.server.core.logging.Log;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class AIMLWatcher {
    private static AIMLWatcher myself = new AIMLWatcher();
    private Timer timer;
    private HashMap watchMaps;

    private AIMLWatcher() {
        watchMaps = new HashMap();
    }

    public static void start() {
        myself.startTimer();
    }

    public static void addWatchFile(String s, String s1) {
        File file = new File(s);
        if (file.canRead()) {
            if (!myself.watchMaps.containsKey(s1))
                myself.watchMaps.put(s1, new HashMap());
            ((HashMap) myself.watchMaps.get(s1)).put(file, new Long(file.lastModified()));
        }
    }

    private void startTimer() {
        if (timer == null) {
            timer = new Timer(true);
            timer.schedule(new CheckAIMLTask(), 0L, Integer.parseInt(Globals.getProperty("programd.watcher.timer")));
        }
    }

    protected Object clone()
            throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    private void reload(File file, String s) {
        String s1;
        try {
            s1 = file.getCanonicalPath();
        } catch (IOException ioexception) {
            return;
        }
        Log.userinfo("Reloading \"" + s1 + "\".", Log.LEARN);
        Graphmaster.load(s1, s);
    }

    private class CheckAIMLTask extends TimerTask {

        private CheckAIMLTask() {
        }

        public void run() {
            for (Iterator iterator = AIMLWatcher.myself.watchMaps.keySet().iterator(); iterator.hasNext(); ) {
                String s = (String) iterator.next();
                HashMap hashmap = (HashMap) AIMLWatcher.myself.watchMaps.get(s);
                for (Iterator iterator1 = hashmap.keySet().iterator(); iterator1.hasNext(); ) {
                    File file;
                    try {
                        file = (File) iterator1.next();
                    } catch (ConcurrentModificationException concurrentmodificationexception) {
                        return;
                    }
                    Long long1 = (Long) hashmap.get(file);
                    if (long1 == null) {
                        hashmap.put(file, new Long(file.lastModified()));
                        reload(file, s);
                    } else {
                        long l = file.lastModified();
                        if (l > long1.longValue()) {
                            hashmap.put(file, new Long(l));
                            reload(file, s);
                        }
                    }
                }

            }

            System.gc();
        }

    }


}
