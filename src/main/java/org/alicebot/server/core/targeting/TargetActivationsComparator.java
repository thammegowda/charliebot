// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.targeting;

import java.util.Comparator;

// Referenced classes of package org.alicebot.server.core.targeting:
//            Target

public class TargetActivationsComparator
        implements Comparator {

    public TargetActivationsComparator() {
    }

    public int compare(Object obj, Object obj1) {
        return ((Target) obj).getActivations() - ((Target) obj1).getActivations();
    }

    public boolean equals(Object obj) {
        return obj instanceof TargetActivationsComparator;
    }
}
