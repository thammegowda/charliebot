// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.parser;

import java.util.LinkedList;
import java.util.ListIterator;

public class XMLNode {

    public static final int TAG = 0;
    public static final int EMPTY = 1;
    public static final int DATA = 2;
    public static final int CDATA = 3;
    public static final int COMMENT = 4;
    public static final int ENDTAG = 5;
    private static final String EMPTY_STRING = "";
    public int XMLType;
    public String XMLData;
    public String XMLAttr;
    public LinkedList XMLChild;
    XMLNode() {
        XMLType = 0;
        XMLData = "";
        XMLAttr = "";
        XMLChild = null;
    }

    public String toString() {
        StringBuffer stringbuffer = new StringBuffer(XMLData);
        stringbuffer.append(XMLAttr);
        if (XMLChild != null) {
            for (ListIterator listiterator = XMLChild.listIterator(); listiterator.hasNext(); stringbuffer.append(listiterator.next().toString()))
                ;
        }
        return stringbuffer.toString();
    }
}
