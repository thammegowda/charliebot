// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.processor;

import org.alicebot.server.core.parser.GenericParser;
import org.alicebot.server.core.parser.XMLNode;

// Referenced classes of package org.alicebot.server.core.processor:
//            ProcessorException

public abstract class Processor {

    public static final String label = null;
    protected static final String EMPTY_STRING = "";
    protected static final String NAME = "name";
    protected static final String VALUE = "value";
    protected static final String NAME_EQUALS = "name=";
    protected static final String VALUE_EQUALS = "value=";
    protected static final String ID = "id";
    protected static final String ENABLED = "enabled";
    public Processor() {
    }

    public abstract String process(int i, XMLNode xmlnode, GenericParser genericparser)
            throws ProcessorException;

}
