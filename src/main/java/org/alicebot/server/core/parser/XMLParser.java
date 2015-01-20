// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.parser;

import org.alicebot.server.core.logging.Log;

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.StringTokenizer;

// Referenced classes of package org.alicebot.server.core.parser:
//            XMLNode

public class XMLParser {

    public static final String MARKER_START = "<";
    public static final String COMMENT_START = "!--";
    public static final String COMMENT_END = "-->";
    public static final String PI_START = "?";
    public static final String CDATA_START = "![CDATA[";
    public static final String CDATA_END = "]]>";
    public static final String SLASH = "/";
    public static final String MARKER_END = ">";
    public static final String SPACE = " ";
    public static final String EMPTY_STRING = "";
    private static final String PI_END = "?";
    public XMLParser() {
    }

    public LinkedList XMLRead(String s, LinkedList linkedlist) {
        StringBuffer stringbuffer = new StringBuffer();
        boolean flag = false;
        int i1 = 0;
        int j1 = 0;
        boolean flag1 = false;
        boolean flag2 = false;
        boolean flag3 = false;
        ListIterator listiterator = linkedlist.listIterator(0);
        for (; j1 < s.length(); j1++) {
            String s12 = String.valueOf(s.charAt(j1));
            switch (i1) {
                case 0: // '\0'
                    if (s12.equals("<")) {
                        i1 = 5;
                        if (stringbuffer.length() > 0) {
                            XMLNode xmlnode = new XMLNode();
                            xmlnode.XMLType = 2;
                            xmlnode.XMLData = stringbuffer.toString();
                            xmlnode.XMLAttr = "";
                            listiterator.add(xmlnode);
                        }
                        stringbuffer = new StringBuffer();
                    } else {
                        stringbuffer.append(s12);
                    }
                    break;

                case 5: // '\005'
                    if (s.indexOf("!--", j1) - j1 == 0) {
                        i1 = 4;
                        break;
                    }
                    if (s.indexOf("?", j1) - j1 == 0) {
                        i1 = 6;
                        break;
                    }
                    if (s.indexOf("![CDATA[", j1) - j1 == 0) {
                        i1 = 3;
                        break;
                    }
                    if (s.indexOf("/", j1) - j1 == 0) {
                        i1 = 2;
                        break;
                    }
                    i1 = 1;
                    // fall through

                case 1: // '\001'
                    int i2 = j1;
                    int j3 = s.indexOf(">", i2);
                    String s6 = s.substring(i2, j3);
                    String s1 = "";
                    int k4 = s6.length() - 1;
                    int i;
                    if (s6.charAt(k4) == '/') {
                        i = 1;
                        s6 = s6.substring(0, k4);
                    } else {
                        i = 0;
                    }
                    int k1 = s6.indexOf(" ");
                    String s13;
                    if (k1 > 0) {
                        s13 = s6.substring(0, k1);
                        s1 = " " + s6.substring(k1 + 1, s6.length());
                    } else {
                        s13 = s6;
                        s1 = "";
                    }
                    j1 = j3;
                    i1 = 0;
                    stringbuffer = new StringBuffer();
                    if (s13.length() > 0) {
                        XMLNode xmlnode1 = new XMLNode();
                        xmlnode1.XMLType = i;
                        xmlnode1.XMLData = s13;
                        xmlnode1.XMLAttr = s1;
                        listiterator.add(xmlnode1);
                        String s7 = "";
                    }
                    break;

                case 2: // '\002'
                    int j2 = j1;
                    int k3 = s.indexOf(">", j2);
                    String s8 = s.substring(j2, k3);
                    int l1 = s8.indexOf(" ");
                    String s14;
                    if (l1 > 0) {
                        s14 = s8.substring(0, l1);
                        String s2 = " " + s8.substring(l1 + 1, s8.length());
                    } else {
                        s14 = s8;
                    }
                    int j = 5;
                    String s3 = "";
                    if (s14.length() > 0) {
                        XMLNode xmlnode2 = new XMLNode();
                        xmlnode2.XMLType = j;
                        xmlnode2.XMLData = s14;
                        xmlnode2.XMLAttr = s3;
                        listiterator.add(xmlnode2);
                    }
                    j1 = k3;
                    i1 = 0;
                    stringbuffer = new StringBuffer();
                    break;

                case 3: // '\003'
                    int k2 = j1 + 7;
                    int l3 = s.indexOf("]]>", k2);
                    String s9 = s.substring(k2, l3);
                    int k = 3;
                    String s15 = s9;
                    String s4 = "";
                    if (s15.length() > 0) {
                        XMLNode xmlnode3 = new XMLNode();
                        xmlnode3.XMLType = k;
                        xmlnode3.XMLData = s15;
                        xmlnode3.XMLAttr = s4;
                        listiterator.add(xmlnode3);
                    }
                    j1 = l3 + 2;
                    i1 = 0;
                    break;

                case 4: // '\004'
                    int l2 = j1 + 2;
                    int i4 = s.indexOf("-->", l2);
                    String s10 = s.substring(l2, i4);
                    int l = 4;
                    String s16 = s10;
                    String s5 = "";
                    if (s16.length() > 0) {
                        XMLNode xmlnode4 = new XMLNode();
                        xmlnode4.XMLType = l;
                        xmlnode4.XMLData = s16;
                        xmlnode4.XMLAttr = s5;
                        listiterator.add(xmlnode4);
                    }
                    j1 = i4 + 2;
                    i1 = 0;
                    break;

                case 6: // '\006'
                    int i3 = j1;
                    int j4 = s.indexOf("?", i3);
                    String s11 = s.substring(i3, j4);
                    j1 = j4 + 1;
                    i1 = 0;
                    break;

                default:
                    Log.userinfo("Invalid tag format.", Log.ERROR);
                    return null;
            }
        }

        if (stringbuffer.length() > 0 && i1 == 0) {
            XMLNode xmlnode5 = new XMLNode();
            xmlnode5.XMLType = 2;
            xmlnode5.XMLData = stringbuffer.toString();
            xmlnode5.XMLAttr = "";
            listiterator.add(xmlnode5);
        }
        return linkedlist;
    }

    public LinkedList scan(ListIterator listiterator, LinkedList linkedlist, LinkedList linkedlist1) {
        while (listiterator.hasNext()) {
            XMLNode xmlnode = (XMLNode) listiterator.next();
            if (xmlnode != null)
                switch (xmlnode.XMLType) {
                    case 0: // '\0'
                        XMLNode xmlnode1 = new XMLNode();
                        xmlnode1.XMLType = xmlnode.XMLType;
                        xmlnode1.XMLData = xmlnode.XMLData;
                        xmlnode1.XMLAttr = xmlnode.XMLAttr;
                        xmlnode1.XMLChild = new LinkedList();
                        xmlnode1.XMLChild = scan(listiterator, linkedlist, xmlnode1.XMLChild);
                        linkedlist1.add(xmlnode1);
                        break;

                    case 1: // '\001'
                        XMLNode xmlnode2 = new XMLNode();
                        xmlnode2.XMLType = xmlnode.XMLType;
                        xmlnode2.XMLData = xmlnode.XMLData;
                        xmlnode2.XMLAttr = xmlnode.XMLAttr;
                        linkedlist1.add(xmlnode2);
                        break;

                    case 2: // '\002'
                    case 3: // '\003'
                        XMLNode xmlnode3 = new XMLNode();
                        xmlnode3.XMLType = 2;
                        xmlnode3.XMLData = xmlnode.XMLData;
                        xmlnode3.XMLAttr = "";
                        linkedlist1.add(xmlnode3);
                        break;

                    case 5: // '\005'
                        return linkedlist1;
                }
            else
                Log.userinfo("XML element is null.", Log.ERROR);
        }
        return linkedlist1;
    }

    public LinkedList load(String s) {
        LinkedList linkedlist = new LinkedList();
        LinkedList linkedlist1 = new LinkedList();
        linkedlist = XMLRead(s, linkedlist);
        if (linkedlist == null) {
            Log.userinfo("Invalid XML:", Log.ERROR);
            for (StringTokenizer stringtokenizer = new StringTokenizer(s, System.getProperty("line.separator")); stringtokenizer.hasMoreTokens(); Log.userinfo(stringtokenizer.nextToken(), Log.ERROR))
                ;
            return null;
        } else {
            ListIterator listiterator = linkedlist.listIterator(0);
            linkedlist1 = scan(listiterator, linkedlist, linkedlist1);
            linkedlist.clear();
            return linkedlist1;
        }
    }
}
