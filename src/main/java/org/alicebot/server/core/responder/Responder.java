// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.responder;


public interface Responder {

    public abstract String preprocess(String s, String s1);

    public abstract void log(String s, String s1, String s2, String s3, String s4);

    public abstract String append(String s, String s1, String s2);

    public abstract String postprocess(String s);
}
