// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.parser;

import org.alicebot.server.core.logging.Log;
import org.alicebot.server.core.processor.Processor;
import org.alicebot.server.core.processor.ProcessorException;
import org.alicebot.server.core.util.ClassRegistry;
import org.alicebot.server.core.util.DeveloperError;
import org.alicebot.server.core.util.Toolkit;

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.StringTokenizer;

// Referenced classes of package org.alicebot.server.core.parser:
//            XMLParser, XMLNode

public abstract class GenericParser {

    protected static final String EMPTY_STRING = "";
    protected static final String OPEN_MARKER_START = "<";
    protected static final String CLOSE_MARKER_START = "</";
    protected static final String NONATOMIC_MARKER_END = ">";
    protected static final String ATOMIC_MARKER_END = "/>";
    protected static final String INDEX = "index";
    protected static final String COMMA = ",";
    protected static final String COLON = ":";
    protected ClassRegistry processorRegistry;

    public GenericParser() {
    }

    public static int[] getValid2dIndex(XMLNode xmlnode) {
        String s = Toolkit.getAttributeValue("index", xmlnode.XMLAttr);
        int ai[] = {
                1, 1
        };
        if (s.equals(""))
            return ai;
        int i = s.indexOf(",");
        if (i < 0) {
            try {
                ai[0] = Integer.parseInt(s);
            } catch (NumberFormatException numberformatexception) {
            }
            ai[1] = 1;
            return ai;
        }
        try {
            ai[0] = Integer.parseInt(s.substring(0, i));
        } catch (NumberFormatException numberformatexception1) {
        }
        try {
            ai[1] = Integer.parseInt(s.substring(i + 1));
        } catch (NumberFormatException numberformatexception2) {
        }
        return ai;
    }

    public static int getValid1dIndex(XMLNode xmlnode) {
        try {
            return Integer.parseInt(Toolkit.getAttributeValue("index", xmlnode.XMLAttr));
        } catch (NumberFormatException numberformatexception) {
            return 1;
        }
    }

    public String processResponse(String s)
            throws ProcessorException {
        XMLParser xmlparser = new XMLParser();
        LinkedList linkedlist = xmlparser.load(s);
        if (linkedlist == null) {
            Log.userinfo("Invalid content:", Log.ERROR);
            for (StringTokenizer stringtokenizer = new StringTokenizer(s, System.getProperty("line.separator")); stringtokenizer.hasMoreTokens(); Log.userinfo(stringtokenizer.nextToken(), Log.ERROR))
                ;
            throw new ProcessorException("Invalid content (see log file).");
        } else {
            String s1 = evaluate(0, linkedlist);
            linkedlist.clear();
            return s1;
        }
    }

    public String processTag(int i, XMLNode xmlnode) throws ProcessorException {
        if (xmlnode == null) {
            return "";
        }
        Class class1 = null;
        if (processorRegistry != null)
            class1 = (Class) processorRegistry.get(xmlnode.XMLData);
        else
            throw new DeveloperError("processorRegistry has not been initialized!");
        Processor processor = null;
        if (class1 != null)
            try {
                processor = (Processor) class1.newInstance();
            } catch (InstantiationException instantiationexception) {
                throw new DeveloperError(instantiationexception);
            } catch (IllegalAccessException illegalaccessexception) {
                throw new DeveloperError(illegalaccessexception);
            } catch (RuntimeException runtimeexception) {
                throw new DeveloperError(runtimeexception);
            }
        else {
            throw new ProcessorException("Could not find a processor for \"" + xmlnode.XMLData + "\"!");
        }
        if (processor != null) {
            return Toolkit.filterWhitespace(processor.process(i++, xmlnode, this));
        } else {
            throw new DeveloperError("Corrupt processor set.");
        }
    }

    public String evaluate(int i, LinkedList linkedlist) {
        String s = "";
        if (linkedlist == null)
            return "";
        for (ListIterator listiterator = linkedlist.listIterator(0); listiterator.hasNext(); ) {
            XMLNode xmlnode = (XMLNode) listiterator.next();
            if (xmlnode != null)
                switch (xmlnode.XMLType) {
                    default:
                        break;

                    case 0: // '\0'
                    case 1: // '\001'
                        try {
                            s = s + processTag(i, xmlnode);
                        } catch (ProcessorException processorexception) {
                            throw new DeveloperError(processorexception.getMessage(), processorexception);
                        }
                        break;

                    case 2: // '\002'
                    case 3: // '\003'
                        s = s + xmlnode.XMLData;
                        break;
                }
        }

        return s;
    }

    public String formatTag(int i, XMLNode xmlnode) {
        String s = "";
        switch (xmlnode.XMLType) {
            default:
                break;

            case 0: // '\0'
                s = s + "<" + xmlnode.XMLData;
                if (!xmlnode.XMLAttr.equals(""))
                    s = s + xmlnode.XMLAttr;
                s = s + ">";
                if (xmlnode.XMLChild != null)
                    s = s + evaluate(i++, xmlnode.XMLChild);
                s = s + "</" + xmlnode.XMLData + ">";
                break;

            case 1: // '\001'
                s = s + "<" + xmlnode.XMLData;
                if (!xmlnode.XMLAttr.equals(""))
                    s = s + xmlnode.XMLAttr;
                s = s + "/>";
                break;

            case 2: // '\002'
            case 3: // '\003'
                s = s + xmlnode.XMLData;
                break;
        }
        return s;
    }

    public int nodeCount(String s, LinkedList linkedlist, boolean flag) {
        int i = 0;
        if (linkedlist == null)
            return 0;
        for (ListIterator listiterator = linkedlist.listIterator(0); listiterator.hasNext(); ) {
            XMLNode xmlnode = (XMLNode) listiterator.next();
            if (xmlnode != null)
                switch (xmlnode.XMLType) {
                    default:
                        break;

                    case 0: // '\0'
                    case 1: // '\001'
                        if (xmlnode.XMLData.equals(s) || flag)
                            i++;
                        break;
                }
        }

        return i;
    }

    public XMLNode getNode(String s, LinkedList linkedlist, int i) {
        if (linkedlist == null)
            return null;
        for (ListIterator listiterator = linkedlist.listIterator(0); listiterator.hasNext(); ) {
            XMLNode xmlnode = (XMLNode) listiterator.next();
            if (xmlnode != null)
                switch (xmlnode.XMLType) {
                    case 0: // '\0'
                    case 1: // '\001'
                        if (xmlnode.XMLData.equals(s) && --i == 0)
                            return xmlnode;
                        break;
                }
        }

        return null;
    }

    public String shortcutTag(int i, String s, int j, String s1, String s2, int k) {
        String s3 = "";
        if (s.equals(""))
            return "";
        XMLNode xmlnode = new XMLNode();
        xmlnode.XMLType = j;
        xmlnode.XMLData = s;
        xmlnode.XMLAttr = s1;
        LinkedList linkedlist = new LinkedList();
        ListIterator listiterator = linkedlist.listIterator(0);
        XMLNode xmlnode1 = new XMLNode();
        LinkedList linkedlist1 = new LinkedList();
        if (j == 0 && !s2.equals("") && (k == 1 || k == 2)) {
            switch (k) {
                case 1: // '\001'
                    xmlnode1.XMLType = 1;
                    xmlnode1.XMLData = s2;
                    xmlnode1.XMLAttr = "";
                    break;

                case 2: // '\002'
                case 3: // '\003'
                    xmlnode1.XMLType = 2;
                    xmlnode1.XMLData = s2;
                    xmlnode1.XMLAttr = "";
                    break;
            }
            ListIterator listiterator1 = linkedlist1.listIterator(0);
            listiterator1.add(xmlnode1);
            xmlnode.XMLChild = linkedlist1;
        }
        listiterator.add(xmlnode);
        s3 = s3 + evaluate(i++, linkedlist);
        xmlnode1 = null;
        linkedlist1 = null;
        xmlnode = null;
        linkedlist = null;
        return s3;
    }
}
