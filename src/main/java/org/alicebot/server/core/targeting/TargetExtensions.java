// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.targeting;

import org.alicebot.server.core.util.StringTripleMatrix;

import java.util.LinkedList;

public class TargetExtensions extends StringTripleMatrix {

    public TargetExtensions() {
    }

    public LinkedList getPatterns() {
        return getFirsts();
    }

    public LinkedList getThats() {
        return getSeconds();
    }

    public LinkedList getTopics() {
        return getThirds();
    }
}
