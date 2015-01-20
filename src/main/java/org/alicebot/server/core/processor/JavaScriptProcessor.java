// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.processor;

import org.alicebot.server.core.Globals;
import org.alicebot.server.core.interpreter.ActiveJavaScriptInterpreter;
import org.alicebot.server.core.logging.Log;
import org.alicebot.server.core.parser.TemplateParser;
import org.alicebot.server.core.parser.XMLNode;

// Referenced classes of package org.alicebot.server.core.processor:
//            AIMLProcessor, AIMLProcessorException

public class JavaScriptProcessor extends AIMLProcessor {

    public static final String label = "javascript";

    public JavaScriptProcessor() {
    }

    public String process(int i, XMLNode xmlnode, TemplateParser templateparser)
            throws AIMLProcessorException {
        if (!Globals.jsAccessAllowed()) {
            Log.userinfo("Use of <javascript> prohibited!", Log.INTERPRETER);
            return "";
        }
        if (xmlnode.XMLType == 0) {
            Log.devinfo("Calling JavaScript interpreter " + Globals.javaScriptInterpreter(), Log.INTERPRETER);
            return ActiveJavaScriptInterpreter.getInstance().evaluate(templateparser.evaluate(i++, xmlnode.XMLChild));
        } else {
            throw new AIMLProcessorException("<javascript></javascript> must have content!");
        }
    }
}
