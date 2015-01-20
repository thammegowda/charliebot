// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core;

import org.alicebot.server.core.logging.Log;
import org.alicebot.server.core.util.InputNormalizer;
import org.alicebot.server.core.util.UserError;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

public class Globals {

    private static final String EMPTY_STRING = "";

    static {
        try {
            hostName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException unknownhostexception) {
            hostName = "unknown-host";
        }
    }

    private static boolean isLoaded = false;
    private static Properties properties;
    private static String botNamePredicate;
    private static String clientNamePredicate;
    private static String predicateEmptyDefault;
    private static String infiniteLoopInput;
    private static boolean osAccessAllowed;
    private static String systemDirectory;
    private static String systemPrefix;
    private static boolean jsAccessAllowed;
    private static boolean supportDeprecatedTags;
    private static boolean warnAboutDeprecatedTags;
    private static boolean nonAIMLRequireNamespaceQualification;
    private static int predicateValueCacheMax;
    private static String startupFilePath;
    private static String targetsDataPath;
    private static String targetsAIMLPath;
    private static int categoryLoadNotifyInterval;
    private static String mergePolicy;
    private static boolean showConsole;
    private static boolean showMatchTrace;
    private static boolean useShell;
    private static boolean useWatcher;
    private static boolean haveAHeart;
    private static int targetSkip;
    private static boolean useTargeting;
    private static String version = "4.1.8";
    private static String hostName;
    private static int httpPort;
    private static String javaScriptInterpreter;
    private static int responseTimeout;
    public static final String DEFAULT_SETTINGS = "etc/server.properties";

    private Globals() {
    }

    public static void load(String s) {
        properties = new Properties();
        try {
            properties.load(new FileInputStream(s));
        } catch (IOException ioexception) {
            throw new IllegalStateException(ioexception);
        }
        loadProperties();
    }

    public static void loadProperties() {
        if (properties == null) {
            System.err.println("Server properties not loaded!");
            throw new IllegalStateException("Properties not initialised");
        }
        useWatcher = Boolean.valueOf(properties.getProperty("programd.watcher", "false")).booleanValue();
        haveAHeart = Boolean.valueOf(properties.getProperty("programd.heart.enabled", "false")).booleanValue();
        useShell = Boolean.valueOf(properties.getProperty("programd.shell", "true")).booleanValue();
        showConsole = Boolean.valueOf(properties.getProperty("programd.console", "true")).booleanValue();
        showMatchTrace = showConsole ? Boolean.valueOf(properties.getProperty("programd.console.match-trace", "true")).booleanValue() : false;
        useTargeting = Boolean.valueOf(properties.getProperty("programd.targeting", "true")).booleanValue();
        targetsAIMLPath = properties.getProperty("programd.targeting.aiml.path", "./targets/targets.aiml");
        targetsDataPath = properties.getProperty("programd.targeting.data.path", "./targets/targets.xml");
        try {
            targetSkip = Integer.parseInt(properties.getProperty("programd.targeting.targetskip", "1"));
        } catch (NumberFormatException numberformatexception) {
            targetSkip = 1;
        }
        targetSkip = targetSkip >= 1 ? targetSkip : 1;
        mergePolicy = properties.getProperty("programd.merge", "true");
        botNamePredicate = properties.getProperty("programd.console.bot-name-predicate", "name");
        clientNamePredicate = properties.getProperty("programd.console.client-name-predicate", "name");
        predicateEmptyDefault = properties.getProperty("programd.emptydefault", "");
        infiniteLoopInput = InputNormalizer.patternFitIgnoreCase(properties.getProperty("programd.infinite-loop-input", "INFINITE LOOP"));
        osAccessAllowed = Boolean.valueOf(properties.getProperty("programd.os-access-allowed", "false")).booleanValue();
        jsAccessAllowed = Boolean.valueOf(properties.getProperty("programd.javascript-allowed", "false")).booleanValue();
        systemDirectory = properties.getProperty("programd.interpreter.system.directory", "./");
        systemPrefix = properties.getProperty("programd.interpreter.system.prefix", "");
        supportDeprecatedTags = Boolean.valueOf(properties.getProperty("programd.deprecated-tags-support", "false")).booleanValue();
        warnAboutDeprecatedTags = supportDeprecatedTags ? Boolean.valueOf(properties.getProperty("programd.deprecated-tags-warn", "false")).booleanValue() : false;
        nonAIMLRequireNamespaceQualification = Boolean.valueOf(properties.getProperty("programd.non-aiml-require-namespace-qualifiers", "false")).booleanValue();
        try {
            predicateValueCacheMax = Integer.parseInt(properties.getProperty("programd.predicate-cache.max", "5000"));
        } catch (NumberFormatException numberformatexception1) {
            predicateValueCacheMax = 5000;
        }
        predicateValueCacheMax = predicateValueCacheMax <= 0 ? 5000 : predicateValueCacheMax;
        javaScriptInterpreter = properties.getProperty("programd.interpreter.javascript", "");
        try {
            categoryLoadNotifyInterval = Integer.parseInt(properties.getProperty("programd.console.category-load-notify-interval", "1000"));
        } catch (NumberFormatException numberformatexception2) {
            categoryLoadNotifyInterval = 1000;
        }
        categoryLoadNotifyInterval = categoryLoadNotifyInterval <= 0 ? 1000 : categoryLoadNotifyInterval;
        try {
            responseTimeout = Integer.parseInt(properties.getProperty("programd.response-timeout", "1000"));
        } catch (NumberFormatException numberformatexception3) {
            responseTimeout = 1000;
        }
        responseTimeout = responseTimeout <= 0 ? 1000 : responseTimeout;
        try {
            startupFilePath = (new File(properties.getProperty("programd.startup", "startup.xml"))).getCanonicalPath();
        } catch (IOException ioexception) {
            String s = "Startup file does not exist (check server properties).";
            Log.log(s, Log.STARTUP);
            throw new UserError(s);
        }
        isLoaded = true;
    }

    public static boolean isLoaded() {
        return isLoaded;
    }

    public static String getVersion() {
        return version;
    }

    public static String getStartupFilePath() {
        return startupFilePath;
    }

    public static String getClientNamePredicate() {
        return clientNamePredicate;
    }

    public static String getBotNamePredicate() {
        return botNamePredicate;
    }

    public static String getPredicateEmptyDefault() {
        return predicateEmptyDefault;
    }

    public static String getInfiniteLoopInput() {
        return infiniteLoopInput;
    }

    public static boolean showConsole() {
        return showConsole;
    }

    public static boolean showMatchTrace() {
        return showMatchTrace;
    }

    public static boolean isWatcherActive() {
        return useWatcher;
    }

    public static boolean useHeart() {
        return haveAHeart;
    }

    public static boolean useShell() {
        return useShell;
    }

    public static String getMergePolicy() {
        return mergePolicy;
    }

    public static boolean useTargeting() {
        return useTargeting;
    }

    public static String getTargetsAIMLPath() {
        return targetsAIMLPath;
    }

    public static String getTargetsDataPath() {
        return targetsDataPath;
    }

    public static int getTargetSkip() {
        return targetSkip;
    }

    public static int getCategoryLoadNotifyInterval() {
        return categoryLoadNotifyInterval;
    }

    public static int getHttpPort() {
        return httpPort;
    }

    public static void setHttpPort(int i) {
        httpPort = i;
    }

    public static int getResponseTimeout() {
        return responseTimeout;
    }

    public static String getHostName() {
        return hostName;
    }

    public static boolean supportDeprecatedTags() {
        return supportDeprecatedTags;
    }

    public static boolean warnAboutDeprecatedTags() {
        return warnAboutDeprecatedTags;
    }

    public static boolean nonAIMLRequireNamespaceQualification() {
        return nonAIMLRequireNamespaceQualification;
    }

    public static int predicateValueCacheMax() {
        return predicateValueCacheMax;
    }

    public static boolean osAccessAllowed() {
        return osAccessAllowed;
    }

    public static boolean jsAccessAllowed() {
        return jsAccessAllowed;
    }

    public static String javaScriptInterpreter() {
        return javaScriptInterpreter;
    }

    public static String getSystemDirectory() {
        return systemDirectory;
    }

    public static String getSystemPrefix() {
        return systemPrefix;
    }

    public static String getProperty(String s) {
        return properties.getProperty(s);
    }

    public static String getProperty(String s, String s1) {
        return properties.getProperty(s, s1);
    }

    public static Properties getProperties() {
        return properties;
    }
}
