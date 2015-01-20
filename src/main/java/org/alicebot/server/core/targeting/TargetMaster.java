// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.targeting;

import org.alicebot.server.core.Globals;
import org.alicebot.server.core.logging.Log;
import org.alicebot.server.core.util.Trace;
import org.alicebot.server.core.util.XMLResourceSpec;
import org.alicebot.server.core.util.XMLWriter;

import java.io.File;
import java.util.Random;

// Referenced classes of package org.alicebot.server.core.targeting:
//            Targeting, Target, TargetWriter

public class TargetMaster extends Targeting {

    private static final Random RandomNumberGenerator = new Random();
    private static String TARGETS_DATA_PATH;
    private static XMLResourceSpec TARGETS_DATA_RESOURCE;
    private static File targetsData;
    static {
        if (Globals.useTargeting()) {
            TARGETS_DATA_PATH = Globals.getTargetsDataPath();
            TARGETS_DATA_RESOURCE = new XMLResourceSpec();
            TARGETS_DATA_RESOURCE.description = "Targeting Data";
            TARGETS_DATA_RESOURCE.path = TARGETS_DATA_PATH;
            TARGETS_DATA_RESOURCE.root = "targets";
            TARGETS_DATA_RESOURCE.dtd = XMLResourceSpec.HTML_ENTITIES_DTD;
            TARGETS_DATA_RESOURCE.encoding = Globals.getProperty("programd.targeting.data.encoding", "UTF-8");
            targetsData = new File(TARGETS_DATA_PATH);
        }
    }
    public TargetMaster() {
    }

    public static void add(String s, String s1, String s2, String s3, String s4, String s5, String s6, String s7) {
        Target target = new Target(s, s1, s2, s3, s4, s5, s6, s7);
        TargetWriter.write(target, TARGETS_DATA_RESOURCE);
    }

    public static void rollTargetData() {
        if (Globals.useTargeting()) {
            Log.userinfo("Rolling over targeting data.", Log.TARGETING);
            XMLWriter.rollover(TARGETS_DATA_RESOURCE);
            targetsData = new File(TARGETS_DATA_PATH);
            Log.userinfo("Targeting data deleted (old file rolled over).", Log.TARGETING);
        } else {
            Trace.userinfo("Targeting is switched off. Turn it on in server.properties.");
        }
    }
}
