// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.util;


// Referenced classes of package org.alicebot.server.core.util:
//            Pulse, Trace

public class IAmAlivePulse
        implements Pulse {

    public IAmAlivePulse() {
    }

    public void emit() {
        Trace.userinfo("I am alive!");
    }
}
