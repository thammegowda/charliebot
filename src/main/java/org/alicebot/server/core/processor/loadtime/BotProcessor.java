// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.processor.loadtime;

import org.alicebot.server.core.Bot;
import org.alicebot.server.core.Bots;
import org.alicebot.server.core.logging.Log;
import org.alicebot.server.core.parser.StartupFileParser;
import org.alicebot.server.core.parser.XMLNode;
import org.alicebot.server.core.util.Toolkit;

// Referenced classes of package org.alicebot.server.core.processor.loadtime:
//            StartupElementProcessor, InvalidStartupElementException

public class BotProcessor extends StartupElementProcessor {

    public static final String label = "bot";

    public BotProcessor() {
    }

    public String process(int i, XMLNode xmlnode, StartupFileParser startupfileparser)
            throws InvalidStartupElementException {
        String s = Toolkit.getAttributeValue("id", xmlnode.XMLAttr);
        if (!s.equals("") && Boolean.valueOf(Toolkit.getAttributeValue("enabled", xmlnode.XMLAttr)).booleanValue()) {
            if (!Bots.knowsBot(s)) {
                Bot bot = new Bot(s);
                Log.userinfo("Configuring bot \"" + s + "\".", Log.STARTUP);
                startupfileparser.setCurrentBot(bot);
                Bots.addBot(s, bot);
                return startupfileparser.evaluate(i++, xmlnode.XMLChild);
            } else {
                Log.userinfo("Bot \"" + s + "\" has already been configured.", Log.STARTUP);
                return "";
            }
        } else {
            return "";
        }
    }
}
