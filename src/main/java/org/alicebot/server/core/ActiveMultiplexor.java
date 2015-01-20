// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core;

import org.alicebot.server.core.util.DeveloperError;

// Referenced classes of package org.alicebot.server.core:
//            Multiplexor, Globals

public class ActiveMultiplexor {

    private static final ActiveMultiplexor myself = new ActiveMultiplexor(Globals.getProperty("programd.multiplexor", "org.alicebot.server.core.FlatFileMultiplexor"));
    private static Multiplexor multiplexor;

    private ActiveMultiplexor(String s) {
        try {
            multiplexor = (Multiplexor) Class.forName(s).newInstance();
        } catch (Exception exception) {
            throw new DeveloperError(exception);
        }
    }

    public static Multiplexor getInstance() {
        return multiplexor;
    }

    protected Object clone()
            throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

}
