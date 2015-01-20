// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.util;

import org.alicebot.server.core.*;
import org.alicebot.server.core.logging.Log;
import org.alicebot.server.core.responder.TextResponder;
import org.alicebot.server.core.targeting.TargetMaster;

import java.io.*;
import java.util.Iterator;
import java.util.Set;

// Referenced classes of package org.alicebot.server.core.util:
//            ShellCommandable, Toolkit, MessagePrinter, XMLWriter

public class Shell {
    public static final String PROMPT = "> ";
    public static final String SHELL;
    private static final String HOSTNAME = Globals.getHostName();
    private static final String CLIENT_NAME_PREDICATE = Globals.getClientNamePredicate();
    private static final String BOT_NAME_PREDICATE = Globals.getBotNamePredicate();
    private static final boolean SHOW_MESSAGE_FLAGS;
    private static final String HELP = "/help";
    private static final String EXIT = "/exit";
    private static final String LOAD = "/load";
    private static final String UNLOAD = "/unload";
    private static final String BOTLIST = "/bots";
    private static final String TALKTO = "/talkto";
    private static final String WHO = "/who";
    private static final String BOT_FILES = "/files";
    private static final String ROLL_CHATLOG = "/roll chatlog";
    private static final String ROLL_TARGETS = "/roll targets";
    private static final String COMMANDABLES = "/commandables";
    private static final String HELP_TEXT[] = {
            "All shell commands are preceded by a forward slash (/).", "The commands available are:", "/help             - prints this help", "/exit             - shuts down the bot server", "/load filename    - loads/reloads given filename for active bot", "/unload filename  - unloads given filename for active bot", "/bots             - lists loaded bots", "/talkto botid     - switches conversation to given bot", "/who              - prints the id of the current bot", "/files            - lists the files loaded by the current bot",
            "/roll chatlog     - rolls over chat log", "/roll targets     - rolls over saved targeting data", "/commandables     - lists available \"shell commandables\" (such as listeners)"
    };
    private BufferedReader consoleIn;
    private PrintStream consoleDisplay;
    private PrintStream consolePrompt;
    private String botid;
    private String botName;
    static {
        SHOW_MESSAGE_FLAGS = Boolean.valueOf(Globals.getProperty("programd.console.message-flags", "true")).booleanValue();
        SHELL = SHOW_MESSAGE_FLAGS ? "s " : "";
    }

    public Shell() {
        consoleIn = new BufferedReader(new InputStreamReader(System.in));
        consoleDisplay = consolePrompt = System.out;
    }
    public Shell(InputStream inputstream, PrintStream printstream, PrintStream printstream1) {
        consoleIn = new BufferedReader(new InputStreamReader(inputstream));
        consoleDisplay = printstream;
        consolePrompt = printstream1;
    }

    public void run() {
        showConsole("Interactive shell: type \"/exit\" to shut down; \"/help\" for help.");
        Bot bot = Bots.getFirstBot();
        if (bot == null) {
            showConsole("No bot to talk to!");
            return;
        }
        botid = bot.getID();
        botName = bot.getPropertyValue(BOT_NAME_PREDICATE);
        showConsole(botName, Toolkit.breakLines(Multiplexor.getResponse(Globals.getProperty("programd.connect-string", "CONNECT"), HOSTNAME, botid, new TextResponder())));
        do {
            promptConsole('[' + botName + "] " + PredicateMaster.get(CLIENT_NAME_PREDICATE, HOSTNAME, botid));
            String s = null;
            try {
                s = consoleIn.readLine();
            } catch (IOException ioexception) {
                Log.userinfo("Cannot read from console!", Log.ERROR);
                return;
            }
            InterruptedException interruptedexception;
            if (s == null)
                do
                    try {
                        Thread.sleep(0x0L);
                    }
                    // Misplaced declaration of an exception variable
//                    catch(InterruptedException interruptedexception)
                    catch (InterruptedException interruptedexception2) {
                        return;
                    }
                while (true);
            MessagePrinter.gotLine();
            if (s.indexOf('/') == 0) {
                if (s.toLowerCase().equals("/exit")) {
                    printExitMessage();
                    return;
                }
                if (s.toLowerCase().equals("/help"))
                    help();
                else if (s.toLowerCase().startsWith("/load"))
                    load(s, botid);
                else if (s.toLowerCase().startsWith("/unload"))
                    unload(s, botid);
                else if (s.toLowerCase().equals("/bots"))
                    showBotList();
                else if (s.toLowerCase().startsWith("/talkto"))
                    talkto(s);
                else if (s.toLowerCase().equals("/who"))
                    who();
                else if (s.toLowerCase().equals("/files"))
                    listBotFiles();
                else if (s.toLowerCase().startsWith("/roll chatlog"))
                    rollChatLog(botid);
                else if (s.toLowerCase().equals("/roll targets"))
                    rollTargets();
                else if (s.toLowerCase().equals("/commandables"))
                    listCommandables();
                else
                    try {
                        sendCommand(s);
                    } catch (NoCommandException nocommandexception) {
                        showConsole("Please specify a command following the commandable.");
                    } catch (NoSuchCommandableException nosuchcommandableexception) {
                        showConsole("No such commandable is loaded.  Type \"/commandables\" for a list of loaded commandables.");
                    }
            } else if (s.length() > 0)
                showConsole(botName, Toolkit.breakLines(Multiplexor.getResponse(s, HOSTNAME, botid, new TextResponder())));
        } while (true);
    }

    public String getCurrentBotID() {
        return botid;
    }

    private void promptConsole(String s) {
        MessagePrinter.print(s + "> ", SHELL, consolePrompt, 1);
    }

    private void showConsole(String s) {
        MessagePrinter.println(s, SHELL, consoleDisplay, 1);
    }

    private void showConsole(String as[]) {
        for (int i = 0; i < as.length; i++)
            MessagePrinter.println(as[i], SHELL, consoleDisplay, 1);

    }

    private void showConsole(String s, String s1) {
        MessagePrinter.println(s + "> " + s1, SHELL, consoleDisplay, 1);
    }

    private void showConsole(String s, String as[]) {
        for (int i = 0; i < as.length; i++)
            MessagePrinter.println(s + "> " + as[i], SHELL, consoleDisplay, 1);

    }

    private void printExitMessage() {
        Log.userinfo("Exiting at user request.", Log.STARTUP);
    }

    public void help() {
        showConsole(HELP_TEXT);
    }

    public void load(String s, String s1) {
        int i = s.indexOf(' ');
        if (i == -1) {
            showConsole("You must specify a filename.");
        } else {
            int j = Graphmaster.getTotalCategories();
            String s2;
            try {
                s2 = (new File(s.substring(i + 1))).getCanonicalPath();
            } catch (IOException ioexception) {
                showConsole("I/O exception trying to locate file.");
                return;
            }
            Graphmaster.load(s2, s1);
            Log.userinfo((Graphmaster.getTotalCategories() - j) + " categories loaded from \"" + s2 + "\".", Log.LEARN);
        }
    }

    private void unload(String s, String s1) {
        int i = s.indexOf(' ');
        if (i == -1) {
            showConsole("You must specify a filename.");
        } else {
            int j = Graphmaster.getTotalCategories();
            String s2;
            try {
                s2 = (new File(s.substring(i + 1))).getCanonicalPath();
            } catch (IOException ioexception) {
                showConsole("I/O exception trying to locate file.");
                return;
            }
            Graphmaster.unload(s2, Bots.getBot(s1));
            Log.userinfo((j - Graphmaster.getTotalCategories()) + " categories unloaded.", Log.LEARN);
        }
    }

    public void showBotList() {
        showConsole("Active bots: " + Bots.getNiceList());
    }

    private void talkto(String s) {
        int i = s.indexOf(' ');
        if (i == -1)
            showConsole("You must specify a bot id.");
        else
            switchToBot(s.substring(i + 1));
    }

    public void switchToBot(String s) {
        if (!Bots.knowsBot(s)) {
            showConsole("That bot id is not known. Check your startup files.");
            return;
        } else {
            botid = s;
            botName = Bots.getBot(s).getPropertyValue(BOT_NAME_PREDICATE);
            showConsole("Switched to bot \"" + s + "\" (name: \"" + botName + "\").");
            showConsole(botName, Toolkit.breakLines(Multiplexor.getResponse(Globals.getProperty("programd.connect-string", "CONNECT"), HOSTNAME, botid, new TextResponder())));
            return;
        }
    }

    private void who() {
        showConsole("You are talking to \"" + botid + "\".");
    }

    public void listBotFiles() {
        Set set = Bots.getBot(botid).getLoadedFilesMap().keySet();
        Iterator iterator = set.iterator();
        int i = set.size();
        if (i > 1)
            showConsole(i + " files loaded by \"" + botid + "\":");
        else
            showConsole("1 file loaded by \"" + botid + "\":");
        for (; iterator.hasNext(); showConsole(((File) iterator.next()).getAbsolutePath()))
            ;
    }

    public void rollChatLog(String s) {
        showConsole("Rolling over chat log for \"" + s + "\".");
        XMLWriter.rollover(Bots.getBot(s).getChatlogSpec());
        showConsole("Finished rolling over chat log.");
    }

    public void rollTargets() {
        TargetMaster.rollTargetData();
    }

    private void listCommandables() {
        Iterator iterator = BotProcesses.getRegistryIterator();
        int i = 0;
        if (iterator.hasNext()) {
            showConsole("Available shell commandables:");
            while (iterator.hasNext())
                try {
                    ShellCommandable shellcommandable = (ShellCommandable) iterator.next();
                    showConsole("/" + shellcommandable.getShellID() + " - " + shellcommandable.getShellDescription());
                    i++;
                } catch (ClassCastException classcastexception) {
                }
        }
        if (i == 0) {
            showConsole("No shell commandables are loaded.");
        } else {
            showConsole("Commands after the shell commandable will be sent to the commandable.");
            showConsole("Example: \"/irc /JOIN #foo\" tells the AliceIRC listener to join channel \"#foo\".");
        }
    }

    private void sendCommand(String s)
            throws NoCommandException, NoSuchCommandableException {
        int i = s.indexOf(' ');
        if (i == -1)
            throw new NoCommandException();
        if (i == s.length())
            throw new NoCommandException();
        String s1 = s.substring(1, i);
        ShellCommandable shellcommandable = null;
        Iterator iterator = BotProcesses.getRegistryIterator();
        if (iterator.hasNext())
            while (iterator.hasNext())
                try {
                    ShellCommandable shellcommandable1 = (ShellCommandable) iterator.next();
                    if (s1.equals(shellcommandable1.getShellID()))
                        shellcommandable = shellcommandable1;
                } catch (ClassCastException classcastexception) {
                }
        if (shellcommandable == null) {
            throw new NoSuchCommandableException();
        } else {
            shellcommandable.processShellCommand(s.substring(i + 1));
            return;
        }
    }

    private class NoSuchCommandableException extends Exception {

        public NoSuchCommandableException() {
        }
    }

    private class NoCommandException extends Exception {

        public NoCommandException() {
        }
    }
}
