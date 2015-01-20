// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.parser;


// Referenced classes of package org.alicebot.server.core.parser:
//            GenericReaderListener

public interface AIMLReaderListener
        extends GenericReaderListener {

    public abstract void newCategory(String s, String s1, String s2, String s3);
}
