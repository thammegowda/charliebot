// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.targeting;

import org.alicebot.server.core.Globals;
import org.alicebot.server.core.logging.Log;
import org.alicebot.server.core.targeting.gui.TargetingGUI;
import org.alicebot.server.core.util.Toolkit;
import org.alicebot.server.core.util.Trace;
import org.alicebot.server.core.util.XMLResourceSpec;
import org.alicebot.server.core.util.XMLWriter;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

// Referenced classes of package org.alicebot.server.core.targeting:
//            Targeting, TargetsReader, TargetsReaderListener, Target, 
//            TargetActivationsComparator, TargetWriter

public class TargetingTool extends Targeting
        implements Runnable {
    public static final String VERSION = "4.1.8";
    private static final String DEFAULT_TARGETS_DATA_PATH = "./targets/targets.xml";
    private static final String LIVE_CACHE_PATH = "./targets/live.cache";
    private static final String DISCARDED_CACHE_PATH = "./targets/discarded.cache";
    private static final String SAVED_CACHE_PATH = "./targets/saved.cache";
    private static String targetsDataPath;
    private static File liveCache = new File("./targets/live.cache");
    private static File discardedCache = new File("./targets/discarded.cache");
    private static File savedCache = new File("./targets/saved.cache");
    private static Timer timer;
    private static int timerFrequency = 0;
    private static TargetingGUI gui;
    private static XMLResourceSpec AIML_RESOURCE;
    private static HashMap liveTargets = new HashMap();
    private static HashMap savedTargets = new HashMap();
    private static HashMap discardedTargets = new HashMap();
    private static int nextTargetToServe = 0;
    private static Properties properties;
    private static String propertiesPath;
    private static boolean includeIncompleteThats;
    private static boolean includeIncompleteTopics;

    public TargetingTool() {
        targetsDataPath = Globals.getProperty("programd.targeting.data.path", "./targets/targets.xml");
        Trace.userinfo("Launching Targeting Tool with data path \"" + targetsDataPath + "\".");
        Trace.devinfo("Checking target cache files.");
        Toolkit.checkOrCreate("./targets/live.cache", "targeting cache file");
        Toolkit.checkOrCreate("./targets/discarded.cache", "targeting cache file");
        Toolkit.checkOrCreate("./targets/saved.cache", "targeting cache file");
        gui = new TargetingGUI(this);
        gui.start();
        try {
            gui.setStatus("Loading live targets cache....");
            load("./targets/live.cache");
            gui.setStatus("Loading saved targets cache....");
            load("./targets/saved.cache", savedTargets);
            gui.setStatus("Loading discarded targets cache....");
            load("./targets/discarded.cache", discardedTargets);
        } catch (Exception exception) {
            Log.userfail(exception.getMessage(), new String[]{
                    Log.ERROR, Log.TARGETING
            });
            throw new IllegalStateException(exception);
        }
        try {
            timerFrequency = Integer.parseInt(Globals.getProperty("programd.targeting.tool.reload-timer", "0")) * 1000;
        } catch (NumberFormatException numberformatexception) {
        }
        try {
            reload(targetsDataPath);
        } catch (IOException ioexception) {
            Log.userfail(ioexception.getMessage(), new String[]{
                    Log.ERROR, Log.TARGETING
            });
        }
        gui.targetPanel.nextTarget();
        gui.setStatus("Ready.");
    }

    private static void load(String s)
            throws IOException, MalformedURLException {
        load(s, liveTargets);
    }

    private static void load(String s, HashMap hashmap)
            throws IOException, MalformedURLException {
        BufferedReader bufferedreader = null;
        String s1;
        long l;
        if (s.indexOf("://") != -1) {
            URL url = new URL(s);
            s1 = Toolkit.getDeclaredXMLEncoding(url.openStream());
            bufferedreader = new BufferedReader(new InputStreamReader(url.openStream(), s1));
            l = url.openConnection().getContentLength();
        } else {
            File file = new File(s);
            if (!file.exists()) {
                Toolkit.checkOrCreate(s, "targets data file");
                TargetWriter.rewriteTargets(new HashMap(), file);
            }
            s1 = Toolkit.getDeclaredXMLEncoding(new FileInputStream(s));
            bufferedreader = new BufferedReader(new InputStreamReader(new FileInputStream(s), s1));
            l = file.length();
        }
        if (bufferedreader != null) {
            TargetsReader targetsreader = new TargetsReader(s, bufferedreader, new TargetsReaderListener(hashmap), s1, l, gui);
            Thread thread = new Thread(targetsreader);
            thread.setDaemon(true);
            thread.run();
            targetsreader.closeMonitor();
            bufferedreader.close();
        } else {
            throw new IOException("I/O error trying to read \"" + s + "\".");
        }
    }

    static void add(Target target, HashMap hashmap) {
        if (hashmap == liveTargets) {
            Integer integer = new Integer(target.hashCode());
            if (!discardedOrSaved(integer)) {
                Target target1 = (Target) liveTargets.get(integer);
                if (target1 != null)
                    target1.merge(target);
                else
                    liveTargets.put(new Integer(target.hashCode()), target);
            }
        } else if (hashmap == savedTargets)
            save(target);
        else if (hashmap == discardedTargets)
            discard(target);
        updatePanels();
    }

    private static boolean discardedOrSaved(Integer integer) {
        return savedTargets.containsKey(integer) || discardedTargets.containsKey(integer);
    }

    private static boolean discardedOrSaved(Target target) {
        Integer integer = new Integer(target.hashCode());
        return discardedOrSaved(integer);
    }

    public static void saveCategory(Target target) {
        String s = target.getNewTopic();
        String s1 = target.getNewThat();
        boolean flag;
        if (s == null)
            flag = false;
        else if (s.trim().length() == 0 || s.equals("*"))
            flag = false;
        else
            flag = true;
        boolean flag1;
        if (s1 == null)
            flag1 = false;
        else if (s1.trim().length() == 0 || s1.equals("*"))
            flag1 = false;
        else
            flag1 = true;
        if (!flag) {
            XMLWriter.write("    <category>" + Targeting.LINE_SEPARATOR + "    " + "    " + "<pattern>" + target.getNewPattern() + "</pattern>" + Targeting.LINE_SEPARATOR, AIML_RESOURCE);
            if (flag1)
                XMLWriter.write("        <that>" + s1 + "</that>" + Targeting.LINE_SEPARATOR, AIML_RESOURCE);
            XMLWriter.write("        <template>" + Targeting.LINE_SEPARATOR + "    " + "    " + "    " + target.getNewTemplate() + Targeting.LINE_SEPARATOR + "    " + "    " + "</template>" + Targeting.LINE_SEPARATOR + "    " + "</category>" + Targeting.LINE_SEPARATOR, AIML_RESOURCE);
        } else {
            XMLWriter.write("    <topic name=\"" + s + "\">" + Targeting.LINE_SEPARATOR + "    " + "    " + "<category>" + Targeting.LINE_SEPARATOR + "    " + "    " + "    " + "<pattern>" + target.getNewPattern() + "</pattern>" + Targeting.LINE_SEPARATOR, AIML_RESOURCE);
            if (flag1)
                XMLWriter.write("            <that>" + s1 + "</that>" + Targeting.LINE_SEPARATOR, AIML_RESOURCE);
            XMLWriter.write("            <template>" + Targeting.LINE_SEPARATOR + "    " + "    " + "    " + "    " + target.getNewTemplate() + Targeting.LINE_SEPARATOR + "    " + "    " + "    " + "</template>" + Targeting.LINE_SEPARATOR + "    " + "    " + "</category>" + Targeting.LINE_SEPARATOR + "    " + "</topic>" + Targeting.LINE_SEPARATOR, AIML_RESOURCE);
        }
        save(target);
    }

    public static Target nextTarget() {
        List list = getSortedTargets();
        int i = list.size();
        if (i > 0) {
            if (nextTargetToServe == i)
                nextTargetToServe = 0;
            int j = nextTargetToServe;
            Target target = null;
            do {
                Target target1 = (Target) list.toArray()[nextTargetToServe];
                String s = target1.getMatchPattern();
                target1.extend();
                for (Iterator iterator = target1.getExtensionPatterns().iterator(); iterator.hasNext(); ) {
                    String s1 = (String) iterator.next();
                    if (!s.equals(s1)) {
                        nextTargetToServe++;
                        return target1;
                    }
                }

                if (includeIncompleteThats) {
                    String s2 = target1.getMatchThat();
                    for (Iterator iterator1 = target1.getExtensionThats().iterator(); iterator1.hasNext(); ) {
                        String s4 = (String) iterator1.next();
                        if (!s2.equals(s4)) {
                            nextTargetToServe++;
                            return target1;
                        }
                    }

                } else if (includeIncompleteTopics) {
                    String s3 = target1.getMatchTopic();
                    for (Iterator iterator2 = target1.getExtensionTopics().iterator(); iterator2.hasNext(); ) {
                        String s5 = (String) iterator2.next();
                        if (!s3.equals(s5)) {
                            nextTargetToServe++;
                            return target1;
                        }
                    }

                }
                nextTargetToServe++;
                if (nextTargetToServe == i)
                    nextTargetToServe = 0;
            } while (nextTargetToServe != j);
            return target;
        } else {
            return null;
        }
    }

    public static void save(Target target) {
        Integer integer = new Integer(target.hashCode());
        liveTargets.remove(integer);
        savedTargets.put(integer, target);
    }

    public static void discardAll() {
        discardedTargets.putAll(liveTargets);
        liveTargets.clear();
    }

    public static void discard(Target target) {
        Integer integer = new Integer(target.hashCode());
        liveTargets.remove(integer);
        discardedTargets.put(integer, target);
    }

    public static List getSortedTargets() {
        ArrayList arraylist = new ArrayList(liveTargets.values());
        Collections.sort(arraylist, new TargetActivationsComparator());
        Collections.reverse(arraylist);
        return arraylist;
    }

    public static int countLive() {
        return liveTargets.size();
    }

    public static int countSaved() {
        return savedTargets.size();
    }

    public static int countDiscarded() {
        return discardedTargets.size();
    }

    public static void shutdown() {
        if (timer != null)
            timer.cancel();
        TargetWriter.rewriteTargets(liveTargets, liveCache);
        TargetWriter.rewriteTargets(savedTargets, savedCache);
        TargetWriter.rewriteTargets(discardedTargets, discardedCache);
    }

    private static void updatePanels() {
        gui.targetPanel.updateCountDisplay();
        gui.inputPanel.updateFromTargets();
        gui.categoryPanel.updateFromTargets();
    }

    public static void main(String args[]) {
        if (args.length == 0)
            propertiesPath = "targeting.properties";
        else
            propertiesPath = args[0];
        Globals.load(propertiesPath);
        includeIncompleteThats = Boolean.valueOf(Globals.getProperty("programd.targeting.tool.include-incomplete-thats", "false")).booleanValue();
        includeIncompleteTopics = Boolean.valueOf(Globals.getProperty("programd.targeting.tool.include-incomplete-topics", "false")).booleanValue();
        AIML_RESOURCE = new XMLResourceSpec();
        AIML_RESOURCE.description = "Targeting-Generated AIML";
        AIML_RESOURCE.path = Globals.getProperty("programd.targeting.tool.aiml.path", "./targets/targets.aiml");
        AIML_RESOURCE.root = "aiml";
        AIML_RESOURCE.dtd = XMLResourceSpec.HTML_ENTITIES_DTD;
        AIML_RESOURCE.encoding = Globals.getProperty("programd.targeting.aiml.encoding", "UTF-8");
        Runtime.getRuntime().addShutdownHook(new Thread("Shutdown Thread") {

            public void run() {
                TargetingTool.shutdown();
            }

        });
        (new Thread(new TargetingTool())).start();
    }

    public void reload()
            throws IOException, MalformedURLException {
        reload(targetsDataPath);
    }

    private void reload(String s)
            throws IOException, MalformedURLException {
        gui.setStatus("Reloading targets from " + s + "....");
        load(s);
        gui.setStatus("Rewriting live targets cache....");
        TargetWriter.rewriteTargets(liveTargets, liveCache);
        gui.setStatus("Rewriting saved targets cache....");
        TargetWriter.rewriteTargets(savedTargets, savedCache);
        gui.setStatus("Rewriting discarded targets cache....");
        TargetWriter.rewriteTargets(discardedTargets, discardedCache);
        gui.setStatus("Ready.");
    }

    public void run() {
        restartTimer(timerFrequency);
    }

    public void restartTimer(int i) {
        if (timer != null)
            try {
                timer.cancel();
            } catch (IllegalStateException illegalstateexception) {
            }
        if (i > 0) {
            timer = new Timer();
            timerFrequency = i * 1000;
            timer.schedule(new CheckTargetDataTask(), timerFrequency, timerFrequency);
        }
    }

    public int getReloadFrequency() {
        return timerFrequency / 1000;
    }

    public String getTargetsDataPath() {
        return targetsDataPath;
    }

    public void changeTargetsDataPath(String s) {
        targetsDataPath = s;
        liveTargets.clear();
        liveCache.delete();
        savedTargets.clear();
        savedCache.delete();
        discardedTargets.clear();
        discardedCache.delete();
        try {
            reload(s);
            restartTimer(timerFrequency);
        } catch (Exception exception) {
            gui.showError(exception.getMessage());
            Log.userinfo(exception.getMessage(), new String[]{
                    Log.ERROR, Log.TARGETING
            });
            timer.cancel();
        }
    }

    public boolean includeIncompleteThats() {
        return includeIncompleteThats;
    }

    public boolean includeIncompleteTopics() {
        return includeIncompleteTopics;
    }

    public void includeIncompleteThats(boolean flag) {
        includeIncompleteThats = flag;
    }

    public void includeIncompleteTopics(boolean flag) {
        includeIncompleteTopics = flag;
    }

    private class CheckTargetDataTask extends TimerTask {

        private CheckTargetDataTask() {
        }

        public void run() {
            try {
                reload(TargetingTool.targetsDataPath);
            } catch (ConcurrentModificationException concurrentmodificationexception) {
            } catch (Exception exception) {
                TargetingTool.gui.showError(exception.getMessage());
                Log.userinfo(exception.getMessage(), new String[]{
                        Log.ERROR, Log.TARGETING
                });
            }
        }

    }


}
