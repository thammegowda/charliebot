// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.sql.pool;


// Referenced classes of package org.alicebot.server.sql.pool:
//            ObjectPool

class CleanUpThread extends Thread {

    private ObjectPool pool;
    private long sleepTime;

    public CleanUpThread(ObjectPool objectpool, long l) {
        super("Database Pool Cleanup Thread");
        pool = objectpool;
        sleepTime = l;
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException interruptedexception) {
            }
            pool.cleanUp();
        }
    }
}
