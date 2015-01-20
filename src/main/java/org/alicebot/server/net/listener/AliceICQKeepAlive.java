// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.net.listener;

import java.io.IOException;

// Referenced classes of package org.alicebot.server.net.listener:
//            AliceICQ

class AliceICQKeepAlive extends Thread {

    AliceICQ parent;

    public AliceICQKeepAlive(AliceICQ aliceicq) {
        parent = aliceicq;
    }

    public void run() {
        while (true)
            try {
                parent.send(parent.header((short) 1070));
                Thread.sleep(0x0L);
            } catch (IOException ioexception) {
            } catch (InterruptedException interruptedexception) {
            }
    }
}
