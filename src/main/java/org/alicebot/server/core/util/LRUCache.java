// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class LRUCache extends LinkedHashMap {

    private static int maxEntries;

    public LRUCache(int i) {
        maxEntries = i;
    }

    protected boolean removeEldestEntry(Map.Entry entry) {
        return size() > maxEntries;
    }
}
