// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.util;

import org.alicebot.server.core.logging.Log;
import org.alicebot.server.core.parser.XMLNode;
import org.alicebot.server.core.parser.XMLParser;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.StringCharacterIterator;
import java.util.*;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

// Referenced classes of package org.alicebot.server.core.util:
//            DeveloperError, UserError, WildCardFilter, Substituter, 
//            Trace

public class Toolkit {

    protected static final String EQUAL_QUOTE = "=\"";
    protected static final char QUOTE_MARK = 34;
    private static final String EMPTY_STRING = "";
    private static final String EMPTY_STRING_ARRAY[] = {
            ""
    };
    private static final char TAG_START = 60;
    private static final char TAG_END = 62;
    private static final String SPACE = " ";
    private static final String CDATA_START = "<![CDATA[";
    private static final String CDATA_END = "]]>";
    private static final String TAB = (new Character('\t')).toString();
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private static final String XML_ESCAPES[][] = {
            {
                    "&amp;", "&"
            }, {
            "&lt;", "<"
    }, {
            "&gt;", ">"
    }, {
            "&apos;", "'"
    }, {
            "&quot;", "\""
    }
    };
    private static final String UTF8 = "UTF-8";
    private static final String XML_PI_START = "<?xml version=\"1.0\"";
    private static final int XML_PI_START_LENGTH = "<?xml version=\"1.0\"".length();
    private static final String ENCODING = "encoding";
    private static final String SYSTEM_ENCODING = System.getProperty("file.encoding", "UTF-8");
    private static HashMap xmlProhibited;
    private static HashMap xmlEscapes;
    private static String WORKING_DIRECTORY = System.getProperty("user.dir");

    public Toolkit() {
    }

    public static String filterWhitespace(String s)
            throws StringIndexOutOfBoundsException {
        int i = s.indexOf("<![CDATA[");
        if (i == -1)
            return filterMultipleConsecutive(Substituter.replace(TAB, " ", Substituter.replace(LINE_SEPARATOR, " ", filterXML(s))), " ").trim();
        int j = s.indexOf("]]>") + 2;
        if (j != -1) {
            if (j < s.length())
                return filterWhitespace(s.substring(0, i)) + s.substring(i, j) + filterWhitespace(s.substring(j));
            else
                return filterWhitespace(s.substring(0, i)) + s.substring(i, j);
        } else {
            return filterMultipleConsecutive(Substituter.replace(TAB, " ", Substituter.replace(LINE_SEPARATOR, " ", filterXML(s))), " ").trim();
        }
    }

    public static String[] breakLines(String s) {
        if (s == null)
            return EMPTY_STRING_ARRAY;
        s = s.trim();
        if (s.equals(""))
            return EMPTY_STRING_ARRAY;
        int i = s.indexOf('<');
        if (i == -1)
            return (new String[]{
                    s
            });
        int j = 0;
        int k = 0;
        int l = s.length();
        Vector vector = new Vector();
        for (; i > -1 && j > -1; i = s.indexOf('<', k)) {
            j = s.indexOf('>', k);
            if (i > 0)
                vector.addElement(s.substring(k, i).trim());
            k = j + 1;
        }

        if (k < l && k > 0)
            vector.addElement(s.substring(k).trim());
        return (String[]) vector.toArray(new String[0]);
    }

    public static String removeMarkup(String s) {
        if (s == null)
            return "";
        s = s.trim();
        if (s.equals(""))
            return s;
        int i = s.indexOf('<');
        if (i == -1)
            return s;
        int j = 0;
        int k = 0;
        int l = s.length();
        StringBuffer stringbuffer = new StringBuffer();
        for (; i > -1 && j > -1; i = s.indexOf('<', k)) {
            j = s.indexOf('>', k);
            if (i > 0)
                stringbuffer.append(s.substring(k, i));
            k = j + 1;
        }

        if (k < l && k > 0)
            stringbuffer.append(s.substring(k));
        return stringbuffer.toString();
    }

    public static String unescapeXMLChars(String s) {
        if (xmlEscapes == null) {
            xmlEscapes = new HashMap(XML_ESCAPES.length);
            for (int i = XML_ESCAPES.length; --i >= 0; )
                xmlEscapes.put(XML_ESCAPES[i][0], XML_ESCAPES[i][1]);

        }
        return Substituter.applySubstitutions(xmlEscapes, s);
    }

    public static String escapeXMLChars(String s) {
        if (xmlProhibited == null) {
            xmlProhibited = new HashMap(XML_ESCAPES.length);
            for (int i = XML_ESCAPES.length; --i >= 0; )
                xmlProhibited.put(XML_ESCAPES[i][1], XML_ESCAPES[i][0]);

        }
        return Substituter.applySubstitutions(xmlProhibited, s);
    }

    private static String filterMultipleConsecutive(String s, String s1) {
        if (s == null)
            return "";
        s = s.trim();
        if (s.equals(""))
            return "";
        int i = s.length();
        int j = s1.length();
        int k = (i - j) + 1;
        StringBuffer stringbuffer = new StringBuffer(i);
        if (k > -1) {
            String s2 = s.substring(0, j);
            stringbuffer.append(s2);
            for (int l = j; l < k; l++) {
                String s3 = s.substring(l, l + j);
                if (!s3.equals(s2) || !s3.equals(s1))
                    stringbuffer.append(s3);
                s2 = s3;
            }

            return stringbuffer.toString();
        } else {
            return s;
        }
    }

    private static String removeAll(String s, String s1) {
        if (s == null)
            return "";
        s = s.trim();
        if (s.equals(""))
            return "";
        if (s1 == null)
            return s;
        if (s1.equals(""))
            return s;
        int i = s.length();
        int j = s1.length() - 1;
        if (s.indexOf(s1) == -1)
            return s;
        int k = i - j;
        if (k > -1) {
            StringBuffer stringbuffer = new StringBuffer(i);
            for (int l = 0; l <= k; l++)
                if (!s.startsWith(s1, l))
                    stringbuffer.append(s.substring(l, l + 1));
                else
                    l += j;

            return stringbuffer.toString();
        } else {
            return s;
        }
    }

    private static String filterXML(String s) {
        if (s == null)
            return "";
        s = s.trim();
        if (s.equals(""))
            return "";
        StringBuffer stringbuffer = new StringBuffer(s.length());
        StringCharacterIterator stringcharacteriterator = new StringCharacterIterator(s);
        for (char c = stringcharacteriterator.first(); c != '\uFFFF'; c = stringcharacteriterator.next())
            if (c == '\t' || c == '\n' || c == '\r' || ' ' <= c && c <= '\uD7FF' || '\uE000' <= c && c <= '\uFFFD')
                stringbuffer.append(c);

        if (stringbuffer.length() > s.length())
            return stringbuffer.toString();
        else
            return s;
    }

    public static String convertXMLUnicodeEntities(String s) {
        int i = s.length();
        int j = 0;
        StringBuffer stringbuffer = new StringBuffer(i);
        while (j < s.length()) {
            if (s.charAt(j) == '&' && s.charAt(j + 1) == '#')
                if (s.charAt(j + 2) == 'x') {
                    int k = s.indexOf(';', j + 3);
                    if (k < j + 7)
                        try {
                            stringbuffer.append((char) Integer.decode(s.substring(j + 2, k)).intValue());
                            j += (k - j) + 1;
                        } catch (NumberFormatException numberformatexception) {
                        }
                } else {
                    int l = s.indexOf(';', j + 2);
                    if (l < j + 7)
                        try {
                            stringbuffer.append((char) Integer.parseInt(s.substring(j + 2, l)));
                            j += (l - j) + 1;
                            continue;
                        } catch (NumberFormatException numberformatexception1) {
                        }
                }
            stringbuffer.append(s.charAt(j));
            j++;
        }
        return stringbuffer.toString();
    }

    public static String getDeclaredXMLEncoding(InputStream inputstream)
            throws IOException {
        BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(inputstream));
        String s = bufferedreader.readLine();
        if (s == null)
            return SYSTEM_ENCODING;
        int i = s.indexOf("<?xml version=\"1.0\"");
        if (i != -1) {
            String s1 = getAttributeValue("encoding", s.substring(XML_PI_START_LENGTH));
            if (!s1.trim().equals(""))
                return s1;
        }
        return SYSTEM_ENCODING;
    }

    public static String getAttributeValue(String s, String s1) {
        String s2 = "";
        String s3 = new String(s + "=\"");
        int i = s1.indexOf(s3);
        if (i >= 0) {
            if (i + s3.length() >= s1.length())
                s1 = "";
            else
                s1 = s1.substring(i + s3.length(), s1.length());
            i = s1.indexOf('"');
            if (i >= 0)
                s2 = s1.substring(0, i);
        }
        return unescapeXMLChars(s2);
    }

    public static String formatAIML(String s) {
        XMLParser xmlparser = new XMLParser();
        LinkedList linkedlist = xmlparser.load(s);
        return formatAIML(linkedlist, 0, true);
    }

    public static String formatAIML(LinkedList linkedlist, int i, boolean flag) {
        ListIterator listiterator = linkedlist.listIterator();
        StringBuffer stringbuffer = new StringBuffer();
        while (listiterator.hasNext()) {
            XMLNode xmlnode = (XMLNode) listiterator.next();
            switch (xmlnode.XMLType) {
                default:
                    break;

                case 0: // '\0'
                    if (!flag)
                        stringbuffer.append(LINE_SEPARATOR);
                    else
                        flag = false;
                    stringbuffer.append(tab(i) + '<' + xmlnode.XMLData + xmlnode.XMLAttr + '>');
                    String s = formatAIML(xmlnode.XMLChild, i + 1, true);
                    if (s.trim().length() > 0)
                        stringbuffer.append(LINE_SEPARATOR + s);
                    stringbuffer.append(LINE_SEPARATOR + tab(i) + '<' + '/' + xmlnode.XMLData + xmlnode.XMLAttr + '>');
                    break;

                case 1: // '\001'
                    if (flag) {
                        stringbuffer.append(tab(i));
                        flag = false;
                    }
                    stringbuffer.append('<' + xmlnode.XMLData + xmlnode.XMLAttr + '/' + '>');
                    if (xmlnode.XMLData.trim().equals("br"))
                        stringbuffer.append(LINE_SEPARATOR + tab(i));
                    break;

                case 2: // '\002'
                    if (flag && xmlnode.XMLData.trim().length() > 0) {
                        stringbuffer.append(tab(i) + xmlnode.XMLData);
                        flag = false;
                    } else {
                        stringbuffer.append(xmlnode.XMLData);
                    }
                    break;

                case 3: // '\003'
                    if (flag) {
                        stringbuffer.append(tab(i));
                        flag = false;
                    }
                    stringbuffer.append(LINE_SEPARATOR + tab(i) + '<' + "![CDATA[" + xmlnode.XMLData + "]]>");
                    break;

                case 4: // '\004'
                    if (flag) {
                        stringbuffer.append(tab(i));
                        flag = false;
                    }
                    stringbuffer.append(LINE_SEPARATOR + tab(i) + '<' + "!--" + xmlnode.XMLData + "-->");
                    break;
            }
        }
        return stringbuffer.toString();
    }

    public static String tab(int i) {
        char ac[] = new char[i];
        for (int j = i; --j >= 0; )
            ac[j] = '\t';

        return new String(ac);
    }

    public static ArrayList wordSplit(String s) {
        ArrayList arraylist = new ArrayList();
        int i = s.length();
        if (i == 0) {
            arraylist.add("");
            return arraylist;
        }
        int j = 0;
        StringCharacterIterator stringcharacteriterator = new StringCharacterIterator(s);
        for (char c = stringcharacteriterator.first(); c != '\uFFFF'; c = stringcharacteriterator.next())
            if (c == ' ') {
                int k = stringcharacteriterator.getIndex();
                arraylist.add(s.substring(j, k));
                j = k + 1;
            }

        if (j < s.length())
            arraylist.add(s.substring(j));
        return arraylist;
    }

    public static Class[] getImplementorsOf(String s, boolean flag) {
        Class class1 = null;
        try {
            class1 = Class.forName(s);
        } catch (ClassNotFoundException classnotfoundexception) {
            throw new DeveloperError("Could not find class \"" + s + "\".");
        }
        StringTokenizer stringtokenizer = new StringTokenizer(System.getProperty("java.class.path", ""), System.getProperty("path.separator", ""));
        if (stringtokenizer.countTokens() == 0)
            return null;
        Vector vector = new Vector();
        while (stringtokenizer.hasMoreTokens()) {
            String s1 = stringtokenizer.nextToken();
            Vector vector1 = new Vector();
            if (s1.endsWith(".jar") || s1.endsWith(".zip")) {
                Enumeration enumeration = null;
                if (s1.endsWith(".jar")) {
                    JarFile jarfile = null;
                    try {
                        jarfile = new JarFile(s1);
                    } catch (IOException ioexception) {
                        Log.userinfo("Classpath contains invalid entry: \"" + s1 + "\".", Log.ERROR);
                        continue;
                    }
                    enumeration = jarfile.entries();
                } else {
                    ZipFile zipfile = null;
                    try {
                        zipfile = new ZipFile(s1);
                    } catch (IOException ioexception1) {
                        Log.userinfo("Classpath contains invalid entry: \"" + s1 + "\".", Log.ERROR);
                        continue;
                    }
                    enumeration = zipfile.entries();
                }
                if (enumeration != null)
                    for (; enumeration.hasMoreElements(); vector1.addElement(((ZipEntry) enumeration.nextElement()).getName()))
                        ;
            } else {
                String as[];
                try {
                    as = (new File(s1)).list();
                } catch (SecurityException securityexception) {
                    String s2 = "Security exception trying to open \"" + s1 + "\".";
                    Log.log(s2, Log.ERROR);
                    Log.devinfo(s2, Log.STARTUP);
                    continue;
                }
                for (int i = as.length; --i >= 0; )
                    vector1.addElement(as[i]);

            }
            if (vector1.size() > 0) {
                Iterator iterator = vector1.iterator();
                while (iterator.hasNext()) {
                    Class class2 = null;
                    String s3 = null;
                    try {
                        s3 = ((String) iterator.next()).replace('/', '.');
                    } catch (ClassCastException classcastexception) {
                        continue;
                    }
                    int j = s3.lastIndexOf(".class");
                    if (j <= -1)
                        continue;
                    s3 = s3.substring(0, j);
                    if (s3.equals(s))
                        continue;
                    try {
                        class2 = Class.forName(s3);
                    } catch (ClassNotFoundException classnotfoundexception1) {
                        continue;
                    } catch (NoClassDefFoundError noclassdeffounderror) {
                        continue;
                    } catch (ExceptionInInitializerError exceptionininitializererror) {
                        continue;
                    }
                    if (class1.isAssignableFrom(class2)) {
                        if (flag)
                            Log.devinfo("Found \"" + s3 + "\".", Log.STARTUP);
                        vector.add(class2);
                    }
                }
            }
        }
        Class aclass[] = new Class[0];
        aclass = (Class[]) vector.toArray(aclass);
        return aclass;
    }

    public static String[] glob(String s, String s1)
            throws FileNotFoundException {
        int i = s.indexOf('*');
        if (i < 0)
            return (new String[]{
                    s
            });
        int j = s.lastIndexOf(File.separatorChar);
        if (j == -1) {
            j = s.lastIndexOf('\\');
            if (j == -1) {
                j = s.lastIndexOf('/');
                if (j == -1)
                    j = s.lastIndexOf(':');
            }
        }
        if (j > i)
            throw new FileNotFoundException("Cannot expand " + s);
        String s2;
        String s3;
        File file;
        if (j >= 0) {
            s2 = s.substring(j + 1);
            s3 = s.substring(0, j + 1);
            file = new File(s3);
            if (!file.isDirectory())
                file = new File(s1 + File.separator + s3);
        } else {
            s2 = s;
            s3 = s1;
            file = new File(s3);
        }
        if (!file.isDirectory())
            throw new UserError("\"" + file.getPath() + "\" is not a valid directory path!");
        String as[] = file.list(new WildCardFilter(s2, '*'));
        if (as == null)
            return new String[0];
        for (int k = as.length; --k >= 0; )
            as[k] = s3 + File.separator + as[k];

        return as;
    }

    public static void checkOrCreate(String s, String s1) {
        File file = new File(s);
        if (!file.exists()) {
            if (s1 == null)
                s1 = "file";
            try {
                file.createNewFile();
            } catch (IOException ioexception) {
                File file1 = file.getParentFile();
                if (file1 != null) {
                    if (file1.mkdirs())
                        try {
                            file.createNewFile();
                        } catch (IOException ioexception1) {
                            throw new UserError("Could not create " + s1 + " \"" + file.getAbsolutePath() + "\".");
                        }
                    else
                        throw new UserError("Could not create directory \"" + file1.getAbsolutePath() + "\".");
                } else {
                    throw new UserError("Could not create " + s1 + " directory.");
                }
            }
            Trace.devinfo("Created new " + s1 + " \"" + s + "\".");
        }
    }

    public static String getFileContents(String s) {
        BufferedReader bufferedreader = null;
        if (s.indexOf("://") != -1) {
            URL url = null;
            try {
                url = new URL(s);
            } catch (MalformedURLException malformedurlexception) {
                Log.userinfo("Malformed URL: \"" + s + "\"", Log.ERROR);
            }
            try {
                String s1 = getDeclaredXMLEncoding(url.openStream());
                bufferedreader = new BufferedReader(new InputStreamReader(url.openStream(), s1));
            } catch (IOException ioexception) {
                Log.userinfo("I/O error trying to read \"" + s + "\"", Log.ERROR);
            }
        } else {
            File file = new File(s);
            if (file.isAbsolute())
                WORKING_DIRECTORY = file.getParent();
            if (file.exists() && !file.isDirectory()) {
                try {
                    String s2 = getDeclaredXMLEncoding(new FileInputStream(s));
                    bufferedreader = new BufferedReader(new InputStreamReader(new FileInputStream(s), s2));
                } catch (IOException ioexception1) {
                    Log.userinfo("I/O error trying to read \"" + s + "\"", Log.ERROR);
                    return null;
                }
            } else {
                if (!file.exists())
                    throw new UserError("\"" + s + "\" does not exist!");
                if (file.isDirectory())
                    throw new UserError("\"" + s + "\" is a directory!");
            }
        }
        StringBuffer stringbuffer = new StringBuffer();
        try {
            String s3;
            while ((s3 = bufferedreader.readLine()) != null)
                stringbuffer.append(s3);
            bufferedreader.close();
        } catch (IOException ioexception2) {
            Log.userinfo("I/O error trying to read \"" + s + "\"", Log.ERROR);
            return null;
        }
        return stringbuffer.toString();
    }

}
