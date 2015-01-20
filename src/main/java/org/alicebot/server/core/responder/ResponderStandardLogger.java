// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.responder;

import org.alicebot.server.core.Globals;
import org.alicebot.server.core.PredicateMaster;
import org.alicebot.server.core.logging.Log;
import org.alicebot.server.core.util.Toolkit;

public class ResponderStandardLogger {

    protected static final String PROMPT = "> ";

    public ResponderStandardLogger() {
    }

    public static void log(String s, String s1, String s2, String s3, String s4) {
        String as[] = Toolkit.breakLines(s);
        String s5 = PredicateMaster.get(Globals.getClientNamePredicate(), s3, s4);
        for (int i = 0; i < as.length; i++)
            Log.log(s5 + "> " + as[i], Log.CHAT);

        as = Toolkit.breakLines(s1);
        for (int j = 0; j < as.length; j++)
            Log.log(s4 + "> " + as[j], Log.CHAT);

    }
}
