// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core;

import org.alicebot.server.core.logging.Log;
import org.alicebot.server.core.util.DeveloperError;
import org.alicebot.server.core.util.Trace;

import java.util.*;

// Referenced classes of package org.alicebot.server.core:
//            NoSuchPredicateException, PredicateInfo, Bots, Bot, 
//            ActiveMultiplexor, Multiplexor, Globals

public class PredicateMaster {

    protected static final String PREDICATE_EMPTY_DEFAULT = Globals.getPredicateEmptyDefault();
    private static final int MAX_INDEX = 5;
    private static final int CACHE_MAX;
    private static final PredicateMaster myself = new PredicateMaster();
    private static final String EMPTY_STRING = "";
    static {
        CACHE_MAX = Globals.predicateValueCacheMax();
        cacheMin = Math.max(CACHE_MAX / 2, 1);
    }
    protected static int cacheSize = 0;
    private static int cacheMin;
    private static HashSet listeners = new HashSet();

    private PredicateMaster() {
    }

    public static String set(String s, String s1, String s2, String s3) {
        Map map = Bots.getBot(s3).predicatesFor(s2);
        map.put(s, s1);
        cacheSize++;
        checkCache();
        return nameOrValue(s, s1, s3);
    }

    public static synchronized String set(String s, int i, String s1, String s2, String s3) {
        Map map = Bots.getBot(s3).predicatesFor(s2);
        ArrayList arraylist = getLoadOrCreateValueList(s, map, s2, s3);
        try {
            arraylist.add(i - 1, s1);
        } catch (IndexOutOfBoundsException indexoutofboundsexception) {
            pushOnto(arraylist, s1);
        }
        cacheSize++;
        checkCache();
        return nameOrValue(s, s1, s3);
    }

    public static synchronized String push(String s, String s1, String s2, String s3) {
        Map map = Bots.getBot(s3).predicatesFor(s2);
        ArrayList arraylist = getLoadOrCreateValueList(s, map, s2, s3);
        pushOnto(arraylist, s1);
        cacheSize++;
        checkCache();
        return nameOrValue(s, s1, s3);
    }

    public static synchronized String get(String s, String s1, String s2) {
        Map map = Bots.getBot(s2).predicatesFor(s1);
        Object obj = map.get(s);
        String s3;
        if (obj == null) {
            try {
                s3 = ActiveMultiplexor.getInstance().loadPredicate(s, s1, s2);
            } catch (NoSuchPredicateException nosuchpredicateexception) {
                s3 = bestAvailableDefault(s, s2);
            }
            if (s3 != null)
                map.put(s, s3);
            else
                throw new DeveloperError("Null string found in user predicates cache!");
        } else if (obj instanceof String)
            s3 = (String) obj;
        else if (obj instanceof ArrayList) {
            ArrayList arraylist = (ArrayList) obj;
            s3 = (String) arraylist.get(0);
        } else {
            throw new DeveloperError("Something other than a String or a ArrayList found in user predicates cache!");
        }
        checkCache();
        return s3;
    }

    public static synchronized String get(String s, int i, String s1, String s2) {
        Map map = Bots.getBot(s2).predicatesFor(s1);
        String s3 = null;
        ArrayList arraylist = null;
        try {
            arraylist = getValueList(s, map);
        } catch (NoSuchPredicateException nosuchpredicateexception) {
            try {
                arraylist = loadValueList(s, map, s1, s2);
            } catch (NoSuchPredicateException nosuchpredicateexception1) {
                s3 = bestAvailableDefault(s, s2);
                map.put(s, s3);
            }
        }
        if (arraylist != null)
            try {
                s3 = (String) arraylist.get(i - 1);
            } catch (IndexOutOfBoundsException indexoutofboundsexception) {
                s3 = bestAvailableDefault(s, s2);
            }
        checkCache();
        return s3;
    }

    private static void pushOnto(ArrayList arraylist, Object obj) {
        arraylist.add(0, obj);
        for (; arraylist.size() > 5; arraylist.remove(arraylist.size() - 1)) ;
    }

    private static ArrayList getValueList(String s, Map map)
            throws NoSuchPredicateException {
        ArrayList arraylist;
        if (map.size() > 0 && map.containsKey(s)) {
            Object obj = map.get(s);
            if (obj instanceof String) {
                if (obj != null) {
                    arraylist = createValueList(s, map);
                    arraylist.add((String) obj);
                } else {
                    throw new DeveloperError("Null String found as value in predicates!");
                }
            } else if (obj instanceof ArrayList) {
                if (obj != null)
                    arraylist = (ArrayList) obj;
                else
                    throw new DeveloperError("Null ArrayList found as value in predicates!");
            } else {
                throw new DeveloperError("Something other than a String or ArrayList found in predicates!");
            }
        } else {
            throw new NoSuchPredicateException(s);
        }
        return arraylist;
    }

    private static ArrayList loadValueList(String s, Map map, String s1, String s2)
            throws NoSuchPredicateException, NullPointerException {
        if (map == null)
            throw new NullPointerException("Cannot call loadValueList with null userPredicates!");
        int i = 1;
        String s3;
        try {
            s3 = ActiveMultiplexor.getInstance().loadPredicate(s + '.' + i, s1, s2);
        } catch (NoSuchPredicateException nosuchpredicateexception) {
            throw new NoSuchPredicateException(s);
        }
        ArrayList arraylist = createValueList(s, map);
        arraylist.add(s3);
        try {
            while (i <= 5) {
                i++;
                arraylist.add(ActiveMultiplexor.getInstance().loadPredicate(s + '.' + i, s1, s2));
            }
        } catch (NoSuchPredicateException nosuchpredicateexception1) {
        }
        return arraylist;
    }

    private static ArrayList createValueList(String s, Map map) {
        ArrayList arraylist = new ArrayList();
        map.put(s, arraylist);
        return arraylist;
    }

    private static ArrayList getLoadOrCreateValueList(String s, Map map, String s1, String s2) {
        ArrayList arraylist;
        try {
            arraylist = getValueList(s, map);
        } catch (NoSuchPredicateException nosuchpredicateexception) {
            try {
                arraylist = loadValueList(s, map, s1, s2);
            } catch (NoSuchPredicateException nosuchpredicateexception1) {
                arraylist = createValueList(s, map);
            }
        }
        return arraylist;
    }

    private static String bestAvailableDefault(String s, String s1) {
        HashMap hashmap = Bots.getBot(s1).getPredicatesInfo();
        if (hashmap.containsKey(s)) {
            String s2 = ((PredicateInfo) hashmap.get(s)).defaultValue;
            if (s2 != null)
                return s2;
        }
        return PREDICATE_EMPTY_DEFAULT;
    }

    private static String nameOrValue(String s, String s1, String s2) {
        HashMap hashmap = Bots.getBot(s2).getPredicatesInfo();
        if (hashmap.containsKey(s) && ((PredicateInfo) hashmap.get(s)).returnNameWhenSet)
            return s;
        else
            return s1;
    }

    private static int save(int i) {
        int j = 0;
        for (Iterator iterator = Bots.keysIterator(); iterator.hasNext() && j < i; ) {
            String s = (String) iterator.next();
            Bot bot = Bots.getBot(s);
            Map map = bot.getPredicateCache();
            if (!map.isEmpty()) {
                for (Iterator iterator1 = map.keySet().iterator(); iterator1.hasNext() && j < i; ) {
                    String s1 = null;
                    try {
                        s1 = (String) iterator1.next();
                    } catch (ConcurrentModificationException concurrentmodificationexception) {
                        Log.log("Some problem with PredicateMaster design: ConcurrentModificationException in save() [1].", Log.RUNTIME);
                    }
                    Map map1 = (Map) map.get(s1);
                    for (Iterator iterator2 = map1.keySet().iterator(); iterator2.hasNext(); ) {
                        String s2 = (String) iterator2.next();
                        Object obj = map1.get(s2);
                        if (obj instanceof String) {
                            String s3 = (String) map1.get(s2);
                            if (!s3.equals(bestAvailableDefault(s2, s))) {
                                ActiveMultiplexor.getInstance().savePredicate(s2, s3, s1, s);
                                j++;
                            }
                        } else if (obj instanceof ArrayList) {
                            ArrayList arraylist = (ArrayList) map1.get(s2);
                            int k = arraylist.size();
                            for (int l = k; --l > 0; ) {
                                String s4 = (String) arraylist.get(l - 1);
                                if (!s4.equals(bestAvailableDefault(s2, s))) {
                                    ActiveMultiplexor.getInstance().savePredicate(s2 + '.' + l, s4, s1, s);
                                    j++;
                                }
                            }

                        } else {
                            throw new DeveloperError("Something other than a String or ArrayList found in predicates!");
                        }
                    }

                    try {
                        iterator1.remove();
                    } catch (ConcurrentModificationException concurrentmodificationexception1) {
                        Log.log("Some problem with PredicateMaster design: ConcurrentModificationException in save() [2].", Log.RUNTIME);
                    }
                }

            }
        }

        cacheSize -= j;
        return cacheSize;
    }

    static void saveAll() {
        Trace.userinfo("Saving all cached predicates (" + cacheSize + ")");
        save(cacheSize);
    }

    private static void checkCache() {
        if (cacheSize >= CACHE_MAX) {
            int i = save(cacheSize - cacheMin);
            if (i < cacheMin)
                cacheMin = (i + cacheMin) / 2;
        }
    }

    protected Object clone()
            throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }
}
