// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.net.listener;

import org.alicebot.server.core.Bot;
import org.alicebot.server.core.Multiplexor;
import org.alicebot.server.core.logging.Log;
import org.alicebot.server.core.responder.AIMResponder;
import org.alicebot.server.core.util.Toolkit;

import java.io.*;
import java.net.Socket;
import java.text.StringCharacterIterator;
import java.util.StringTokenizer;

// Referenced classes of package org.alicebot.server.net.listener:
//            AliceChatListener

public class AliceAIM extends AliceChatListener {

    public static final String label = "AliceAIM";
    private static final int MAX_SEQ = 65535;
    private static final String HOST = "toc.oscar.aol.com";
    private static final int PORT = 21;
    private static final String ROAST = "Tic/Toc";
    private static final String ZERO = "0";
    private static final String ZERO_X = "0x";
    private static final String ONE = "1";
    private static final String TWO = "2";
    private static final String TWO_POINT_FIVE = "2.5";
    private static final String THREE = "3";
    private static final String FOUR = "4";
    private static final String FIVE = "5";
    private static final String SIX = "6";
    private static final String SEVEN = "7";
    private static final String EIGHT = "8";
    private static final String NINE = "9";
    private static final String ERROR = "ERROR";
    private static final String IM_IN = "IM_IN";
    private static final String MSG_FROM = "Message from [";
    private static final String RB_COLON = "]: ";
    private static final String SENDIM = "$SENDIM";
    private static final String _AIM = "_AIM";
    private static final String CHAT_IN = "CHAT_IN";
    private static final String _901 = "901";
    private static final String _903 = "903";
    private static final String _960 = "960";
    private static final String _961 = "961";
    private static final String _962 = "962";
    private static final String SIGNON_ERR = "Signon err";
    private static final String TOC_SEND_IM = "toc_send_im ";
    private static final String TOC_CHAT_SEND = "toc_chat_send ";
    private static final String TOC_ADD_BUDDY = "toc_add_buddy ";
    private static final String MSG_FMT_0 = "<BODY BGCOLOR=\"";
    private static final String MSG_FMT_1 = "\"><FONT SIZE=\"";
    private static final String MSG_FMT_2 = " FACE=\"";
    private static final String MSG_FMT_3 = " COLOR=\"";
    private static final String MSG_FMT_4 = "\">";
    private static final String MSG_FMT_5 = "</FONT>";
    private static final String SPACE_QUOTE = " \"";
    private static final String SPACE = " ";
    private static final String BACKSLASH = "\\";
    private static final String NULL = "\0";
    private static final String QUOTE_NULL = "\"\0";
    private static final String EMPTY_STRING = "";
    private static final String MSG = "AliceAIM: ";
    private String name;
    private String pass;
    private String bgcolor;
    private String fontface;
    private String fontsize;
    private String fontcolor;
    private String owner;
    private String buddies;
    private String message;
    private int seqNo;
    private Socket connection;
    private DataInputStream in;
    private DataOutputStream out;
    private boolean online;
    public AliceAIM(Bot bot) {
        super(bot, "AliceAIM", new String[][]{
                new String[]{
                        "owner", ""
                }, new String[]{
                "screenname", ""
        }, new String[]{
                "password", ""
        }, new String[]{
                "bgcolor", "White"
        }, new String[]{
                "fontface", "Verdana,Arial"
        }, new String[]{
                "fontsize", "2"
        }, new String[]{
                "fontcolor", "Black"
        }, new String[]{
                "buddies", ""
        }
        });
    }

    public static String imRoast(String s) {
        Object obj = null;
        StringBuffer stringbuffer = new StringBuffer("0x");
        int i = s.length();
        for (int j = 0; j < i; j++) {
            String s1 = Long.toHexString(s.charAt(j) ^ "Tic/Toc".charAt(j % 7));
            if (s1.length() < 2)
                stringbuffer.append("0");
            stringbuffer.append(s1);
        }

        return stringbuffer.toString();
    }

    public static String imNormalize(String s) {
        StringBuffer stringbuffer;
        int i;
        for (stringbuffer = new StringBuffer(s); (i = stringbuffer.toString().indexOf(" ")) >= 0 && stringbuffer.length() > 0; stringbuffer.delete(i, i + 1))
            ;
        return stringbuffer.toString();
    }

    public boolean checkParameters() {
        owner = (String) parameters.get("owner");
        name = (String) parameters.get("screenname");
        pass = (String) parameters.get("password");
        bgcolor = (String) parameters.get("bgcolor");
        fontface = (String) parameters.get("fontface");
        fontsize = (String) parameters.get("fontsize");
        fontcolor = (String) parameters.get("fontcolor");
        buddies = (String) parameters.get("buddies");
        if (owner.length() == 0) {
            logMessage("No owner specified; aborting.");
            return false;
        }
        if (name.length() == 0) {
            logMessage("No screen name specified; aborting.");
            return false;
        }
        if (pass.length() == 0) {
            logMessage("No password specified; aborting.");
            return false;
        } else {
            return true;
        }
    }

    public void run() {
        seqNo = (int) Math.floor(Math.random() * 65535D);
        logMessage("Starting for \"" + botID + "\".");
        try {
            connection = new Socket("toc.oscar.aol.com", 21);
            connection.setSoTimeout(10000);
            in = new DataInputStream(connection.getInputStream());
            out = new DataOutputStream(new BufferedOutputStream(connection.getOutputStream()));
        } catch (Exception exception) {
            signoff("1");
            return;
        }
        try {
            out.writeBytes("FLAPON\r\n\r\n");
            out.flush();
            byte abyte0[] = new byte[10];
            in.readFully(abyte0);
            out.writeByte(42);
            out.writeByte(1);
            out.writeShort(seqNo);
            seqNo = seqNo + 1 & 0xffff;
            out.writeShort(name.length() + 8);
            out.writeInt(1);
            out.writeShort(1);
            out.writeShort(name.length());
            out.writeBytes(name);
            out.flush();
            frameSend("toc_signon login.oscar.aol.com 5190 " + name + " " + imRoast(pass) + " english alicebot\0");
            in.skip(0L);
            short word0 = in.readShort();
            abyte0 = new byte[word0];
            in.readFully(abyte0);
            if (String.valueOf(abyte0).startsWith("ERROR")) {
                logMessage("Signon error.");
                signoff("2");
                return;
            }
            in.skip(0L);
            word0 = in.readShort();
            abyte0 = new byte[word0];
            in.readFully(abyte0);
            online = true;
            sendBuddies();
            frameSend("toc_init_done\0");
            logMessage("Logon complete.");
            connection.setSoTimeout(3000);
        } catch (InterruptedIOException interruptedioexception) {
            online = false;
            signoff("2.5");
        } catch (IOException ioexception) {
            online = false;
            signoff("3");
        }
        do
            try {
                do {
                    in.skip(0L);
                    short word1 = in.readShort();
                    byte abyte1[] = new byte[word1];
                    in.readFully(abyte1);
                    fromAIM(abyte1);
                } while (true);
            } catch (InterruptedIOException interruptedioexception1) {
                Log.devinfo("AliceAIM: Error*: " + interruptedioexception1, Log.LISTENERS);
            } catch (IOException ioexception1) {
                Log.devinfo("AliceAIM: Error**: " + ioexception1, Log.LISTENERS);
                signoff("4");
                return;
            }
        while (true);
    }

    public void shutdown() {
        signoff("4");
    }

    public void frameSend(String s)
            throws IOException {
        out.writeByte(42);
        out.writeByte(2);
        out.writeShort(seqNo);
        seqNo = seqNo + 1 & 0xffff;
        out.writeShort(s.length());
        out.writeBytes(s);
        out.flush();
    }

    public void fromAIM(byte abyte0[]) {
        String s = new String(abyte0);
        Log.devinfo("AliceAIM: Got: " + s, Log.LISTENERS);
        StringTokenizer stringtokenizer = new StringTokenizer(s, ":");
        String s1 = stringtokenizer.nextToken();
        if (s1.equals("IM_IN")) {
            String s2 = imNormalize(stringtokenizer.nextToken());
            stringtokenizer.nextToken();
            StringBuffer stringbuffer = new StringBuffer(stringtokenizer.nextToken());
            for (; stringtokenizer.hasMoreTokens(); stringbuffer.append(stringtokenizer.nextToken()))
                stringbuffer.append(':');

            String s6 = Toolkit.removeMarkup(stringbuffer.toString());
            logMessage("Message from [" + s2 + "]: " + s6);
            if (s6.startsWith("$SENDIM") && owner.equals(s2)) {
                StringTokenizer stringtokenizer1 = new StringTokenizer(s6);
                stringtokenizer1.nextToken();
                String s8 = stringtokenizer1.nextToken();
                String s9 = stringtokenizer1.nextToken();
                sendMesg(s8, s9);
            } else {
                String as[] = Toolkit.breakLines(Multiplexor.getResponse(s6, s2 + "_AIM", botID, new AIMResponder()));
                if (as.length > 0) {
                    for (int i = 0; i < as.length; i++)
                        sendMesg(s2, as[i]);

                }
            }
            return;
        }
        if (s1.equals("CHAT_IN")) {
            String s3 = imNormalize(stringtokenizer.nextToken());
            String s5 = imNormalize(stringtokenizer.nextToken());
            StringBuffer stringbuffer1 = new StringBuffer(stringtokenizer.nextToken());
            for (; stringtokenizer.hasMoreTokens(); stringbuffer1.append(stringtokenizer.nextToken()))
                stringbuffer1.append(':');

            String s7 = Toolkit.removeMarkup(stringbuffer1.toString());
            if (s7.indexOf(name) > 0) {
                String as1[] = Toolkit.breakLines(Multiplexor.getResponse(s7, s5 + "_AIM", botID, new AIMResponder()));
                if (as1.length > 0) {
                    for (int j = 0; j < as1.length; j++)
                        sendChatRoomMesg(s3, as1[j]);

                }
            }
            return;
        }
        if (s1.equals("ERROR")) {
            String s4 = stringtokenizer.nextToken();
            logMessage("Error: " + s4);
            if (s4.equals("901")) {
                logMessage("Not currently available.");
                return;
            }
            if (s4.equals("903")) {
                logMessage("Message dropped; sending too fast.");
                return;
            }
            if (s4.equals("960")) {
                logMessage("Sending messages too fast to " + stringtokenizer.nextToken());
                return;
            }
            if (s4.equals("961")) {
                logMessage(stringtokenizer.nextToken() + " sent you too big a message.");
                return;
            }
            if (s4.equals("962")) {
                logMessage(stringtokenizer.nextToken() + " sent you a message too fast.");
                return;
            }
            if (s4.equals("Signon err")) {
                logMessage("AIM signon failure: " + stringtokenizer.nextToken());
                signoff("5");
            }
            return;
        } else {
            return;
        }
    }

    public void sendMesg(String s, String s1) {
        String s2 = "<BODY BGCOLOR=\"" + bgcolor + "\"><FONT SIZE=\"" + fontsize + " FACE=\"" + fontface + " COLOR=\"" + fontcolor + "\">" + s1 + "</FONT>";
        String s3 = "toc_send_im " + s + " \"" + imEscape(s2);
        logMessage(s1);
        try {
            frameSend(s3);
        } catch (IOException ioexception) {
            signoff("9");
        }
    }

    public void sendChatRoomMesg(String s, String s1) {
        String s2 = "toc_chat_send " + s + " \"" + imEscape(s1);
        logMessage(s1);
        try {
            frameSend(s2);
        } catch (IOException ioexception) {
            signoff("9");
        }
    }

    private StringBuffer imEscape(String s) {
        StringBuffer stringbuffer = new StringBuffer();
        StringCharacterIterator stringcharacteriterator = new StringCharacterIterator(s);
        for (char c = stringcharacteriterator.first(); c != '\uFFFF'; c = stringcharacteriterator.next()) {
            switch (c) {
                case 34: // '"'
                case 36: // '$'
                case 40: // '('
                case 41: // ')'
                case 91: // '['
                case 92: // '\\'
                case 93: // ']'
                case 123: // '{'
                case 125: // '}'
                    stringbuffer.append("\\");
                    break;
            }
            stringbuffer.append(c);
        }

        stringbuffer.append("\"\0");
        return stringbuffer;
    }

    public void toAIM(byte abyte0[]) {
        if (abyte0.length < 2030) {
            logMessage("Got a message longer than 2030 bytes.");
            return;
        }
        try {
            out.writeByte(42);
            out.writeByte(2);
            out.writeShort(seqNo);
            seqNo = seqNo + 1 & 0xffff;
            out.writeShort(abyte0.length + 1);
            out.write(abyte0);
            out.writeByte(0);
            out.flush();
        } catch (IOException ioexception) {
            logMessage("Exception: " + ioexception);
            signoff("6");
        }
    }

    public void sendBuddies() {
        String s = "toc_add_buddy ";
        Log.devinfo("AliceAIM: " + s, Log.LISTENERS);
        try {
            frameSend(s + ' ' + name + "\0");
            for (StringTokenizer stringtokenizer = new StringTokenizer(buddies, ","); stringtokenizer.hasMoreTokens(); frameSend(s + ' ' + stringtokenizer.nextToken() + "\0"))
                ;
        } catch (IOException ioexception) {
            logMessage("Exception: " + ioexception);
            signoff("7");
        }
    }

    public void signoff(String s) {
        online = false;
        logMessage("Trying to close IM (" + s + ").....");
        try {
            out.close();
            in.close();
            connection.close();
        } catch (IOException ioexception) {
            logMessage("Exception: " + ioexception);
        }
        logMessage("Done.");
    }

    private void logMessage(String s) {
        Log.userinfo("AliceAIM: " + s, Log.LISTENERS);
    }
}
