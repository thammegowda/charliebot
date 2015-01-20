// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.util;

import org.alicebot.server.core.Globals;
import org.alicebot.server.core.Multiplexor;

import java.util.Timer;
import java.util.TimerTask;

public class Heart {
    private static final Heart self = new Heart();
    private Timer timer;

    private Heart() {
    }

    public static void start() {
        int i = 0;
        try {
            i = 60000 / Integer.parseInt(Globals.getProperty("programd.heart.pulserate"));
        } catch (NumberFormatException numberformatexception) {
        }
        if (i > 0)
            self.startBeating(i);
    }

    protected Object clone()
            throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    private void startBeating(int i) {
        timer = new Timer();
        timer.schedule(new HeartBeat(), 0L, i);
    }

    class HeartBeat extends TimerTask {

        HeartBeat() {
        }

        public void run() {
            Multiplexor.pulse();
        }
    }

}
