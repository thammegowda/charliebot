
package org.alicebot.server.net;

import org.alicebot.server.core.ActiveMultiplexor;
import org.alicebot.server.core.BotProcesses;
import org.alicebot.server.core.Globals;
import org.alicebot.server.core.Graphmaster;
import org.alicebot.server.core.loader.AIMLWatcher;
import org.alicebot.server.core.logging.Log;
import org.alicebot.server.core.util.*;

import java.io.IOException;
import java.util.Date;

public class AliceServer {

    private Shell shell;
    private String propertiesPath;

    public AliceServer(String s) {
        propertiesPath = s;
    }

    public AliceServer(String s, Shell shell1) {
        propertiesPath = s;
        shell = shell1;
    }

    private static void startHttpServer(String httpServerClassName, String s1) {
        Class httpServerClass;
        try {
            httpServerClass = Class.forName(httpServerClassName);
        } catch (ClassNotFoundException classnotfoundexception) {
            throw new UserError("Could not find http server \"" + httpServerClassName + "\".");
        }
        AliceCompatibleHttpServer alicecompatiblehttpserver;
        try {
            alicecompatiblehttpserver = (AliceCompatibleHttpServer) httpServerClass.newInstance();
        } catch (InstantiationException instantiationexception) {
            throw new UserError("Couldn't instantiate http server \"" + httpServerClassName + "\".");
        } catch (IllegalAccessException illegalaccessexception) {
            throw new DeveloperError("The constructor for \"" + httpServerClassName + "\" or the class itself is not available.");
        } catch (ClassCastException classcastexception) {
            throw new DeveloperError("\"" + httpServerClassName + "\" is not an implementation of AliceCompatibleHttpServer.");
        }
        if (s1 != null)
            try {
                alicecompatiblehttpserver.configure(s1);
            } catch (IOException ioexception) {
                throw new UserError("Could not find \"" + s1 + "\".");
            }
        BotProcesses.start(alicecompatiblehttpserver, "http server");
    }

    public static void shutdown() {
        Trace.userinfo("AliceServer is shutting down.");
        BotProcesses.shutdownAll();
        Graphmaster.shutdown();
        Trace.userinfo("Shutdown complete.");
    }

    public static void main(String args[]) {
        String s;
        if (args.length > 0) {
            s = args[0];
        } else {
            s = "etc/server.properties";
        }
        AliceServer aliceserver = new AliceServer(s);
        Runtime.getRuntime().addShutdownHook(new Thread("Shutdown Thread") {

            public void run() {
                AliceServer.shutdown();
            }

        });
        aliceserver.startup(true);
    }

    public void startup(boolean startEmbeddedServer) {
        if (propertiesPath == null)
            throw new DeveloperError("Did not specify a server properties path.");
        if (!Globals.isLoaded()) {
            Globals.load(propertiesPath);
            shell = new Shell();
        }
        try {
            String s = Globals.getProperty("programd.httpserver.classname");
            String s1 = Globals.getProperty("programd.httpserver.config");
            if (s == null) {
                throw new UserError("You must specify an http server to run AliceServer. Failing.");
            }

            if (Globals.showConsole()) {
                Log.userinfo("Starting Charliebot version " + Globals.getVersion(), Log.STARTUP);
                Log.userinfo("Using Java VM " + System.getProperty("java.vm.version") + " from " + System.getProperty("java.vendor"), Log.STARTUP);
                Log.userinfo("On " + System.getProperty("os.name") + " version " + System.getProperty("os.version") + " (" + System.getProperty("os.arch") + ")", Log.STARTUP);
                Log.userinfo("Predicates with no values defined will return: \"" + Globals.getPredicateEmptyDefault() + "\".", Log.STARTUP);
            }
            if (startEmbeddedServer) {
                startHttpServer(s, s1);
            }

            if (Globals.showConsole())
                Log.userinfo("Initializing Multiplexor.", Log.STARTUP);
            ActiveMultiplexor.getInstance().initialize();
            String s2;
            s2 = "http://" + Globals.getHostName() + ":" + Globals.getHttpPort();
            long l = (new Date()).getTime();
            if (Globals.showConsole())
                Log.userinfo("Loading Graphmaster.", Log.STARTUP);
            Graphmaster.load(Globals.getStartupFilePath(), null);
            l = (new Date()).getTime() - l;
            Graphmaster.markReady();
            if (Globals.showConsole()) {
                Log.userinfo(Graphmaster.getTotalCategories() + " categories loaded in " + (double) (float) l / 1000D + " seconds.", Log.STARTUP);
                if (Globals.isWatcherActive()) {
                    AIMLWatcher.start();
                    Log.userinfo("The AIML Watcher is active.", Log.STARTUP);
                } else {
                    Log.userinfo("The AIML Watcher is not active.", Log.STARTUP);
                }
                Log.userinfo("HTTP server listening at " + s2, Log.STARTUP);
            }
            String s3 = Globals.getProperty("programd.browser-launch");
            if (s3 != null && !s2.equals("unknown address"))
                try {
                    Runtime.getRuntime().exec(s3 + " " + s2);
                } catch (IOException ioexception) {
                    Trace.userinfo("Could not launch your web browser. Sorry.");
                }
            System.gc();
            if (Globals.useHeart()) {
                Heart.start();
                Trace.userinfo("Heart started.");
            }
            if(!startEmbeddedServer) {
                // No shell needed to run
            } else if (Globals.useShell() ) {
                shell.run();
                Trace.devinfo("Shell exited.");
            } else {
                if (Globals.showConsole())
                    Log.userinfo("Interactive shell disabled.  Awaiting interrupt to shut down.", Log.STARTUP);
                do
                    try {
                        Thread.sleep(0x0L);
                    } catch (InterruptedException interruptedexception) {
                    }
                while (true);
            }
        } catch (DeveloperError developererror) {
            Log.devfail(developererror);
            Log.userfail("Exiting abnormally due to developer error.", Log.ERROR);
            throw new IllegalStateException(developererror);
        } catch (UserError usererror) {
            Log.userfail(usererror);
            Log.userfail("Exiting abnormally due to user error.", Log.ERROR);
            throw new IllegalStateException(usererror);
        } catch (RuntimeException runtimeexception) {
            Log.userfail("Exiting abnormally due to unforeseen runtime exception.", runtimeexception, Log.ERROR);
            throw new IllegalStateException(runtimeexception);
        } catch (Exception exception) {
            Log.userfail("Exiting abnormally due to unforeseen exception.", exception, Log.ERROR);
            throw new IllegalStateException(exception);
        }
    }
}
