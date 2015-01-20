// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.sql.pool;

import java.util.Enumeration;
import java.util.Hashtable;

// Referenced classes of package org.alicebot.server.sql.pool:
//            CleanUpThread

public abstract class ObjectPool {

    private long expirationTime;
    private long lastCheckOut;
    private Hashtable locked;
    private Hashtable unlocked;
    private CleanUpThread cleaner;

    protected ObjectPool() {
        expirationTime = 0x0L;
        locked = new Hashtable();
        unlocked = new Hashtable();
        lastCheckOut = System.currentTimeMillis();
        cleaner = new CleanUpThread(this, expirationTime);
        cleaner.setDaemon(true);
        cleaner.start();
    }

    protected void checkIn(Object obj) {
        if (obj != null) {
            locked.remove(obj);
            unlocked.put(obj, new Long(System.currentTimeMillis()));
        }
    }

    protected Object checkOut() {
        long l = System.currentTimeMillis();
        lastCheckOut = l;
        if (unlocked.size() > 0) {
            for (Enumeration enumeration = unlocked.keys(); enumeration.hasMoreElements(); ) {
                Object obj = enumeration.nextElement();
                if (validate(obj)) {
                    unlocked.remove(obj);
                    locked.put(obj, new Long(l));
                    return obj;
                }
                unlocked.remove(obj);
                expire(obj);
                obj = null;
            }

        }
        Object obj1 = create();
        locked.put(obj1, new Long(l));
        return obj1;
    }

    protected void cleanUp() {
        long l = System.currentTimeMillis();
        for (Enumeration enumeration = unlocked.keys(); enumeration.hasMoreElements(); ) {
            Object obj = enumeration.nextElement();
            if (l - ((Long) unlocked.get(obj)).longValue() > expirationTime) {
                unlocked.remove(obj);
                expire(obj);
                obj = null;
            }
        }

        System.gc();
    }

    protected abstract Object create();

    protected abstract void expire(Object obj);

    protected abstract boolean validate(Object obj);
}
