// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.responder;

import org.alicebot.server.core.Bot;
import org.alicebot.server.core.Bots;
import org.alicebot.server.core.Globals;
import org.alicebot.server.core.util.SuffixFilenameFilter;
import org.alicebot.server.core.util.Tag;
import org.alicebot.server.core.util.UserError;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.StringTokenizer;

// Referenced classes of package org.alicebot.server.core.responder:
//            Responder, ResponderDatabaseLogger, ResponderXMLLogger

public abstract class AbstractMarkupResponder
        implements Responder {

    protected static final String EMPTY_STRING = "";
    protected static final String HOSTNAME = "hostname/";
    protected static final String HNAME = "hname";
    protected static final String REPLY_START = "reply";
    protected static final String REPLY_END = "/reply";
    protected static final String USERINPUT = "userinput/";
    protected static final String ALICE_IN = "alice_in";
    protected static final String RESPONSE = "response/";
    protected static final String ALICE_OUT = "alice_out";
    protected static final String BOT_NAME_EQUALS = "bot name=\"";
    protected static final String BOT_ = "bot_";
    protected static final String UNDERSCORE = "_";
    protected static final String QUOTE_MARK = "\"";
    protected static final String MARKER_START = "<";
    protected static final String MARKER_END = ">";
    protected static final String ATOMIC_MARKER_END = ">";
    protected static final String SPACE = " ";
    private static final String PERIOD = ".";
    private static final boolean LOG_CHAT_TO_DATABASE = Boolean.valueOf(Globals.getProperty("programd.logging.to-database.chat", "false")).booleanValue();
    private static final boolean LOG_CHAT_TO_XML = Boolean.valueOf(Globals.getProperty("programd.logging.to-xml.chat", "true")).booleanValue();
    protected static String tags[] = {
            "", "reply", "/reply", "hostname/", "hname", "userinput/", "alice_in", "response/", "alice_out", "bot name=\"",
            "bot_"
    };
    protected LinkedList header;
    protected LinkedList replyPart;
    protected LinkedList footer;
    protected StringBuffer response;
    protected String hostName;
    protected String botid;
    protected Bot bot;
    public AbstractMarkupResponder(String s) {
        header = new LinkedList();
        replyPart = new LinkedList();
        footer = new LinkedList();
        response = new StringBuffer();
        botid = s;
        bot = Bots.getBot(s);
    }

    protected static LinkedList loadTemplate(String s) {
        LinkedList linkedlist = new LinkedList();
        FileReader filereader = null;
        try {
            filereader = new FileReader(s);
        } catch (FileNotFoundException filenotfoundexception) {
            return null;
        }
        BufferedReader bufferedreader = new BufferedReader(filereader);
        linkedlist.clear();
        try {
            String s1;
            while ((s1 = bufferedreader.readLine()) != null)
                linkedlist.add(s1);
            filereader.close();
            bufferedreader.close();
        } catch (IOException ioexception) {
            throw new UserError("I/O error reading \"" + s + "\".", ioexception);
        }
        return linkedlist;
    }

    protected static HashMap registerTemplates(String s, SuffixFilenameFilter suffixfilenamefilter) {
        File file = new File(s);
        HashMap hashmap = new HashMap();
        if (file.isDirectory()) {
            String as[] = file.list(suffixfilenamefilter);
            int i = as.length;
            if (i > 0) {
                for (int j = i; --j >= 0; ) {
                    String s1 = as[j];
                    hashmap.put(s1.substring(0, s1.lastIndexOf(".")), s + File.separator + s1);
                }

            }
        }
        return hashmap;
    }

    protected void parseTemplate(String s) {
        Object obj = null;
        try {
            FileReader filereader = new FileReader(s);
            parse(filereader);
            filereader.close();
        } catch (IOException ioexception) {
            throw new UserError("I/O error trying to read \"" + s + "\".", ioexception);
        }
    }

    public String preprocess(String s, String s1) {
        response.setLength(0);
        hostName = s1;
        int i = header.size();
        for (int j = 0; j < i; j++) {
            Object obj = header.get(j);
            if (obj instanceof String)
                response.append(obj);
            else if (obj instanceof Tag) {
                Tag tag = (Tag) obj;
                String s2 = tag.getName();
                if (s2.equals("hostname/"))
                    response.append(s1);
                else if (s2.equals("userinput/"))
                    response.append(s);
                else if (s2.startsWith("bot name=\"")) {
                    int k = s2.lastIndexOf("\"");
                    if (k > 10)
                        response.append(bot.getPropertyValue(s2.substring(10, k)));
                } else if (s2.equals("hname"))
                    response.append(s1);
                else if (s2.equals("alice_in"))
                    response.append(s);
                else if (s2.startsWith("bot_")) {
                    for (StringTokenizer stringtokenizer = new StringTokenizer(s2, "_"); stringtokenizer.hasMoreTokens(); response.append(bot.getPropertyValue(stringtokenizer.nextToken().toLowerCase())))
                        stringtokenizer.nextToken();

                }
            }
        }

        return s;
    }

    public String append(String s, String s1, String s2) {
        int i = replyPart.size();
        for (int j = 0; j < i; j++) {
            Object obj = replyPart.get(j);
            if (obj instanceof String)
                response.append(obj);
            else if (obj instanceof Tag) {
                Tag tag = (Tag) obj;
                String s3 = tag.getName();
                if (s3.equals("userinput/"))
                    response.append(s);
                else if (s3.equals("response/")) {
                    if (response.length() > 0)
                        response.append(' ');
                    response.append(s1);
                } else if (s3.startsWith("bot name=\"")) {
                    int k = s3.lastIndexOf("\"");
                    if (k > 10)
                        response.append(bot.getPropertyValue(s3.substring(10, k)));
                } else if (s3.equals("alice_in"))
                    response.append(s);
                else if (s3.equals("alice_out")) {
                    if (response.length() > 0)
                        response.append(' ');
                    response.append(s1);
                } else if (s3.startsWith("bot_")) {
                    for (StringTokenizer stringtokenizer = new StringTokenizer(tag.getName(), "_"); stringtokenizer.hasMoreTokens(); response.append(bot.getPropertyValue(stringtokenizer.nextToken().toLowerCase())))
                        stringtokenizer.nextToken();

                }
            }
        }

        if (s2.length() > 0)
            return s2 + ' ' + s1;
        else
            return s2 + s1;
    }

    public void log(String s, String s1, String s2, String s3, String s4) {
        if (LOG_CHAT_TO_DATABASE)
            ResponderDatabaseLogger.log(s, s1, s2, s3, s4);
        if (LOG_CHAT_TO_XML)
            ResponderXMLLogger.log(s, s1, s2, s3, s4);
    }

    public String postprocess(String s) {
        int i = footer.size();
        for (int j = 0; j < i; j++) {
            Object obj = footer.get(j);
            if (obj instanceof String)
                response.append(obj);
            else if (obj instanceof Tag) {
                Tag tag = (Tag) obj;
                String s1 = tag.getName();
                if (s1.equals("response/")) {
                    if (response.length() > 0)
                        response.append(' ');
                    response.append(s);
                } else if (s1.startsWith("bot name=\"")) {
                    int k = s1.lastIndexOf("\"");
                    if (k > 10)
                        response.append(bot.getPropertyValue(s1.substring(10, k)));
                } else if (s1.equals("hostname/"))
                    response.append(hostName);
                else if (s1.equals("alice_out")) {
                    if (response.length() > 0)
                        response.append(' ');
                    response.append(s);
                } else if (s1.startsWith("bot_")) {
                    for (StringTokenizer stringtokenizer = new StringTokenizer(s1, "_"); stringtokenizer.hasMoreTokens(); response.append(bot.getPropertyValue(stringtokenizer.nextToken().toLowerCase())))
                        stringtokenizer.nextToken();

                } else if (s1.equals("hname"))
                    response.append(hostName);
            }
        }

        return response.toString();
    }

    protected void parse(FileReader filereader)
            throws IOException {
        StringBuffer stringbuffer = new StringBuffer();
        StringBuffer stringbuffer1 = new StringBuffer();
        LinkedList linkedlist = header;
        int i;
        while ((i = filereader.read()) != -1) {
            char c = (char) i;
            if (c == '<') {
                stringbuffer1.setLength(0);
                while ((c = (char) filereader.read()) != '>')
                    stringbuffer1.append(c);
                String s = stringbuffer1.toString();
                int j;
                for (j = tags.length; --j >= 0; )
                    if (s.startsWith(tags[j])) {
                        linkedlist.add(stringbuffer.toString());
                        stringbuffer.setLength(0);
                        break;
                    }

                switch (j) {
                    case 0: // '\0'
                        stringbuffer.append("<" + stringbuffer1 + ">");
                        break;

                    case 1: // '\001'
                        linkedlist = replyPart;
                        break;

                    case 2: // '\002'
                        linkedlist = footer;
                        break;

                    default:
                        linkedlist.add(new Tag(s));
                        break;
                }
            } else {
                stringbuffer.append(c);
            }
        }
        linkedlist.add(stringbuffer.toString());
    }

}
