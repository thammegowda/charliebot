// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.targeting;

import org.alicebot.server.core.parser.GenericReaderListener;

import java.util.HashMap;

// Referenced classes of package org.alicebot.server.core.targeting:
//            Target, TargetingTool

public class TargetsReaderListener
        implements GenericReaderListener {

    private static HashMap set;

    public TargetsReaderListener(HashMap hashmap) {
        set = hashmap;
    }

    public void loadTarget(String s, String s1, String s2, String s3, String s4, String s5, String s6,
                           String s7) {
        TargetingTool.add(new Target(s, s1, s2, s3, s4, s5, s6, s7), set);
    }
}
