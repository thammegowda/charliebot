// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.node;

import java.util.Set;

public interface Nodemapper {

    public abstract Object put(String s, Object obj);

    public abstract Object get(String s);

    public abstract void remove(Object obj);

    public abstract Set keySet();

    public abstract boolean containsKey(String s);

    public abstract int size();

    public abstract Nodemapper getParent();

    public abstract void setParent(Nodemapper nodemapper);

    public abstract int getHeight();

    public abstract void setTop();
}
