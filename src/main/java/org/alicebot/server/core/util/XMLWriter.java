// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.util;

import org.alicebot.server.core.logging.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;

// Referenced classes of package org.alicebot.server.core.util:
//            XMLResourceSpec, Toolkit, Trace

public class XMLWriter {

    private static final String ENC_UTF8 = "UTF-8";
    private static final String XML_PI_START = "<?xml version=\"1.0\" encoding=\"";
    private static final String STYLESHEET_PI_START = "<?xml-stylesheet type=\"text/xsl\" href=\"";
    private static final String PI_END = "\"?>";
    private static final String LINE_SEPARATOR = System.getProperty("line.separator", "\n");
    private static final String OPEN_MARKER_START = "<";
    private static final String CLOSE_MARKER_START = "</";
    private static final String NONATOMIC_MARKER_END = ">";
    private static final String RW = "rw";
    private static final String XMLFILE = "xml file";
    private static final String BACKLINK_EQUALS = " backlink=\"";
    private static final String STARTTIME_EQUALS = " starttime=\"";
    private static final String QUOTE_MARK = "\"";
    private static final String LINK_DATE_FORMAT = "yyyy-MM-dd-HHmmss";
    private static final String STARTTIME_DATE_FORMAT = "yyyy-MM-dd H:mm:ss";
    public XMLWriter() {
    }

    public static void write(String s, XMLResourceSpec xmlresourcespec) {
        RandomAccessFile randomaccessfile = null;
        Toolkit.checkOrCreate(xmlresourcespec.path, "xml file");
        try {
            randomaccessfile = new RandomAccessFile(xmlresourcespec.path, "rw");
        } catch (FileNotFoundException filenotfoundexception) {
            Log.userinfo("Can't write to file \"" + xmlresourcespec.path + "\".", Log.ERROR);
            return;
        }
        long l = 0L;
        try {
            l = randomaccessfile.length();
        } catch (IOException ioexception) {
            Log.userinfo("Error reading file \"" + xmlresourcespec.path + "\".", Log.ERROR);
        }
        String s1 = "</" + xmlresourcespec.root + ">" + LINE_SEPARATOR;
        if (l == 0L) {
            try {
                randomaccessfile.writeBytes("<?xml version=\"1.0\" encoding=\"");
                if (xmlresourcespec.encoding != null)
                    randomaccessfile.writeBytes(xmlresourcespec.encoding);
                else
                    randomaccessfile.writeBytes("UTF-8");
                randomaccessfile.writeBytes("\"?>" + LINE_SEPARATOR);
                if (xmlresourcespec.stylesheet != null)
                    randomaccessfile.writeBytes("<?xml-stylesheet type=\"text/xsl\" href=\"" + xmlresourcespec.stylesheet + "\"?>" + LINE_SEPARATOR);
                if (xmlresourcespec.dtd != null)
                    randomaccessfile.writeBytes(xmlresourcespec.dtd + LINE_SEPARATOR);
                randomaccessfile.writeBytes("<" + xmlresourcespec.root);
                if (xmlresourcespec.backlink != null)
                    randomaccessfile.writeBytes(" backlink=\"" + xmlresourcespec.backlink + "\"");
                if (xmlresourcespec.starttime != null)
                    randomaccessfile.writeBytes(" starttime=\"" + xmlresourcespec.starttime + "\"");
                randomaccessfile.writeBytes(">" + LINE_SEPARATOR);
            } catch (IOException ioexception1) {
                Log.userinfo("Error writing to \"" + xmlresourcespec.path + "\".", Log.ERROR);
                return;
            }
        } else {
            long l1 = l - (long) s1.length();
            try {
                randomaccessfile.seek(l1);
            } catch (IOException ioexception3) {
                Log.userinfo("Error reading \"" + xmlresourcespec.path + "\".", Log.ERROR);
                return;
            }
        }
        try {
            if (xmlresourcespec.encoding == null)
                randomaccessfile.write(s.getBytes("UTF-8"));
            else
                randomaccessfile.write(s.getBytes(xmlresourcespec.encoding));
            randomaccessfile.writeBytes(s1);
            randomaccessfile.close();
        } catch (IOException ioexception2) {
            Log.userinfo("Error writing to \"" + xmlresourcespec.encoding + "\".", Log.ERROR);
            return;
        }
    }

    public static void rollover(XMLResourceSpec xmlresourcespec) {
        if (xmlresourcespec == null) {
            Trace.userinfo("No resource found to roll over.");
            return;
        }
        if (xmlresourcespec.description != null)
            Trace.devinfo("Rolling over " + xmlresourcespec.description + ".");
        Date date = new Date();
        File file = new File(xmlresourcespec.path);
        String s = file.getParent();
        String s1 = file.getName();
        int i = s1.lastIndexOf('.');
        String s2 = (new SimpleDateFormat("yyyy-MM-dd-HHmmss")).format(date);
        if (i > 0)
            xmlresourcespec.backlink = s1.substring(0, i) + "-end-" + s2 + s1.substring(i);
        else
            xmlresourcespec.backlink = s1 + "-end-" + s2;
        xmlresourcespec.starttime = (new SimpleDateFormat("yyyy-MM-dd H:mm:ss")).format(date);
        File file1 = new File(s + File.separator + xmlresourcespec.backlink);
        (new File(xmlresourcespec.path)).renameTo(file1);
    }

}
