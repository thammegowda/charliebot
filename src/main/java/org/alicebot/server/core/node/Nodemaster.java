// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.node;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

// Referenced classes of package org.alicebot.server.core.node:
//            Nodemapper

public class Nodemaster
        implements Nodemapper {

    protected int size;
    protected String key;
    protected Object value;
    protected HashMap Hidden;
    protected int height;
    protected Nodemapper parent;

    public Nodemaster() {
        size = 0;
        height = 0x7fffffff;
    }

    public Object put(String s, Object obj) {
        if (size == 0) {
            key = s.toUpperCase();
            value = obj;
            size = 1;
            return obj;
        }
        if (size == 1) {
            Hidden = new HashMap();
            Hidden.put(key.toUpperCase(), value);
            size = 2;
            return Hidden.put(s.toUpperCase(), obj);
        } else {
            return Hidden.put(s.toUpperCase(), obj);
        }
    }

    public void remove(Object obj) {
        if (size > 2) {
            Hidden.remove(obj);
            size--;
        } else if (size == 2) {
            value = Hidden.remove(obj);
            size = 1;
        } else if (size == 1) {
            value = null;
            size = 0;
        }
    }

    public Object get(String s) {
        if (size == 0)
            return null;
        if (size == 1) {
            if (s.equalsIgnoreCase(key))
                return value;
            else
                return null;
        } else {
            return Hidden.get(s.toUpperCase());
        }
    }

    public Set keySet() {
        if (size <= 1) {
            HashSet hashset = new HashSet();
            if (key != null)
                hashset.add(key);
            return hashset;
        } else {
            return Hidden.keySet();
        }
    }

    public boolean containsKey(String s) {
        if (size == 0)
            return false;
        if (size <= 1)
            return s.equalsIgnoreCase(key);
        else
            return Hidden.containsKey(s.toUpperCase());
    }

    public int size() {
        return size;
    }

    public Nodemapper getParent() {
        return parent;
    }

    public void setParent(Nodemapper nodemapper) {
        parent = nodemapper;
    }

    public int getHeight() {
        return height;
    }

    public void setTop() {
        fillInHeight(0);
    }

    private void fillInHeight(int i) {
        if (height > i)
            height = i;
        if (parent != null)
            ((Nodemaster) parent).fillInHeight(i + 1);
    }
}
