// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.processor.loadtime;

import org.alicebot.server.core.Bot;
import org.alicebot.server.core.BotProcesses;
import org.alicebot.server.core.Globals;
import org.alicebot.server.core.logging.Log;
import org.alicebot.server.core.parser.StartupFileParser;
import org.alicebot.server.core.parser.XMLNode;
import org.alicebot.server.core.processor.ProcessorException;
import org.alicebot.server.core.util.DeveloperError;
import org.alicebot.server.core.util.Toolkit;
import org.alicebot.server.core.util.UserError;
import org.alicebot.server.net.listener.AliceChatListener;
import org.alicebot.server.net.listener.AliceChatListenerRegistry;

import java.lang.reflect.InvocationTargetException;

// Referenced classes of package org.alicebot.server.core.processor.loadtime:
//            StartupElementProcessor, InvalidStartupElementException

public class ListenerProcessor extends StartupElementProcessor {

    public static final String label = "listener";
    private static final String PARAMETER = "parameter";
    private static final String TYPE = "type";
    private static final String ENABLED = "enabled";
    private static final String NAME = "name";
    private static final String VALUE = "value";
    private static final String TRUE = "true";
    private static final String SEPARATOR = " : ";
    public ListenerProcessor() {
    }

    public String process(int i, XMLNode xmlnode, StartupFileParser startupfileparser)
            throws InvalidStartupElementException {
        String s = getHref(xmlnode);
        if (s.length() > 0)
            try {
                return startupfileparser.processResponse(Toolkit.getFileContents(s));
            } catch (ProcessorException processorexception) {
                throw new UserError(processorexception);
            }
        String s1 = Toolkit.getAttributeValue("type", xmlnode.XMLAttr);
        if (s1 == null)
            throw new UserError("You did not specify a type for a listener.");
        Class class1 = (Class) AliceChatListenerRegistry.getSelf().get(s1);
        if (class1 == null)
            throw new UserError("You specified an unknown listener \"" + s1 + "\".");
        String s2 = Toolkit.getAttributeValue("enabled", xmlnode.XMLAttr);
        if (s2 == null)
            throw new UserError("<listener type=\"" + s1 + "\"> is missing an enabled attribute.");
        if (!s2.equals("true"))
            return "";
        Bot bot = startupfileparser.getCurrentBot();
        AliceChatListener alicechatlistener;
        try {
            alicechatlistener = (AliceChatListener) class1.getConstructor(new Class[]{
                    Bot.class
            }).newInstance(new Object[]{
                    bot
            });
        } catch (IllegalAccessException illegalaccessexception) {
            throw new DeveloperError("The constructor for the \"" + s1 + "\" listener class is inaccessible.");
        } catch (IllegalArgumentException illegalargumentexception) {
            throw new DeveloperError("The constructor for the \"" + s1 + "\" listener class is incorrectly specifed.");
        } catch (InstantiationException instantiationexception) {
            throw new DeveloperError("The \"" + s1 + "\" listener class is abstract.");
        } catch (NoSuchMethodException nosuchmethodexception) {
            throw new DeveloperError("The constructor for the \"" + s1 + "\" listener class is incorrectly specifed.");
        } catch (InvocationTargetException invocationtargetexception) {
            throw new DeveloperError("The constructor for the \"" + s1 + "\" listener class threw an exception.", invocationtargetexception);
        }
        int j = startupfileparser.nodeCount("parameter", xmlnode.XMLChild, true);
        for (int k = j; k > 0; k--) {
            XMLNode xmlnode1 = startupfileparser.getNode("parameter", xmlnode.XMLChild, k);
            if (xmlnode1.XMLType == 1) {
                String s3 = Toolkit.getAttributeValue("name", xmlnode1.XMLAttr);
                String s4 = Toolkit.getAttributeValue("value", xmlnode1.XMLAttr);
                if (s3 != null && s4 != null)
                    alicechatlistener.setParameter(s3, s4);
            } else {
                throw new InvalidStartupElementException("<" + xmlnode1.XMLData + "/> cannot have content!");
            }
        }

        if (!alicechatlistener.checkParameters()) {
            Log.userinfo("Listener \"" + s1 + "\" is incorrectly configured; will not be started.", Log.STARTUP);
            return "";
        }
        BotProcesses.start(alicechatlistener, s1 + " : " + bot.getID());
        if (Globals.showConsole())
            Log.userinfo("Started \"" + s1 + "\" listener for bot \"" + bot.getID() + "\".", Log.STARTUP);
        return "";
    }
}
