// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.util;

import org.alicebot.server.core.Globals;

public class XMLResourceSpec
        implements Cloneable {

    private static final String RESOURCE_BASE = Globals.getProperty("programd.logging.xml.resource-base", "../resources/");
    public static final String HTML_ENTITIES_DTD = "<!DOCTYPE ALLOW_HTML_ENTITIES [ <!ENTITY % HTMLlat1 PUBLIC \"-//W3C//ENTITIES Latin1//EN//HTML\"   \"" + RESOURCE_BASE + "DTD/xhtml-lat1.ent\">" + " %HTMLlat1;" + " <!ENTITY % HTMLsymbol PUBLIC" + "   \"-//W3C//ENTITIES Symbols//EN//HTML\"" + "   \"" + RESOURCE_BASE + "DTD/xhtml-symbol.ent\">" + " %HTMLsymbol;" + " <!ENTITY % HTMLspecial PUBLIC" + "   \"-//W3C//ENTITIES Special//EN//HTML\"" + "   \"" + RESOURCE_BASE + "DTD/xhtml-special.ent\">" + " %HTMLspecial;" + " ]>";
    public String description;
    public String path;
    public String root;
    public String stylesheet;
    public String encoding;
    public String dtd;
    public String backlink;
    public String starttime;
    public boolean rolloverAtMax;
    public boolean rolloverAtRestart;
    public XMLResourceSpec() {
        rolloverAtMax = false;
        rolloverAtRestart = false;
    }

    public Object clone() {
        XMLResourceSpec xmlresourcespec = new XMLResourceSpec();
        xmlresourcespec.description = description != null ? new String(description) : null;
        xmlresourcespec.path = path != null ? new String(path) : null;
        xmlresourcespec.root = root != null ? new String(root) : null;
        xmlresourcespec.stylesheet = stylesheet != null ? new String(stylesheet) : null;
        xmlresourcespec.encoding = encoding != null ? new String(encoding) : null;
        xmlresourcespec.dtd = dtd != null ? new String(dtd) : null;
        xmlresourcespec.backlink = backlink != null ? new String(backlink) : null;
        xmlresourcespec.starttime = starttime != null ? new String(starttime) : null;
        xmlresourcespec.rolloverAtMax = rolloverAtMax;
        xmlresourcespec.rolloverAtRestart = rolloverAtRestart;
        return xmlresourcespec;
    }

}
