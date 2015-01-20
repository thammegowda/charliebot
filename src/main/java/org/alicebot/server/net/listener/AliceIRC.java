// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.net.listener;

import org.alicebot.server.core.Bot;
import org.alicebot.server.core.Multiplexor;
import org.alicebot.server.core.logging.Log;
import org.alicebot.server.core.responder.TextResponder;
import org.alicebot.server.core.util.ShellCommandable;
import org.alicebot.server.core.util.Toolkit;
import org.alicebot.server.core.util.Trace;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

// Referenced classes of package org.alicebot.server.net.listener:
//            AliceChatListener

public class AliceIRC extends AliceChatListener
        implements ShellCommandable {

    public static final String label = "AliceIRC";
    private static final String VERSION = "0.86.0b";
    private static final String VERDATE = "1999.04.07";
    private static final boolean DEBUG = false;
    private static final int MAXARGC = 16;
    private static final String SERVERPREFIX = "[server]";
    private static final String SIRCMESSAGE = "[irc]";
    private static final String DEBUGPREFIX = "[debug]";
    private static final String NONE = "";
    private static final byte NOTCONNECTED = 0;
    private static final byte CONNECTING = 1;
    private static final byte CONNECTED = 2;
    private static final byte DISCONNECTING = 3;
    private static final String MSG = "AliceIRC: ";
    private byte clientStatus;
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private String host;
    private String nick;
    private String channel;
    private int port;
    public AliceIRC(Bot bot) {
        super(bot, "AliceIRC", new String[][]{
                new String[]{
                        "host", ""
                }, new String[]{
                "port", "6667"
        }, new String[]{
                "nick", ""
        }, new String[]{
                "channel", ""
        }
        });
        clientStatus = 0;
    }

    public boolean checkParameters() {
        host = (String) parameters.get("host");
        try {
            port = Integer.parseInt((String) parameters.get("port"));
        } catch (NumberFormatException numberformatexception) {
            logMessage("Invalid port specification (try a number!); aborting.");
            return false;
        }
        nick = (String) parameters.get("nick");
        channel = (String) parameters.get("channel");
        if (host.length() == 0) {
            logMessage("No host specified; aborting.");
            return false;
        }
        if (port <= 0)
            logMessage("Invalid port; aborting.");
        if (nick.length() == 0) {
            logMessage("No nick specified; aborting.");
            return false;
        }
        if (channel.length() == 0) {
            logMessage("No channel specified; aborting.");
            return false;
        } else {
            return true;
        }
    }

    public void shutdown() {
        disconnect();
    }

    public void run() {
        logMessage("Starting for \"" + botID + "\".");
        processMessageCommandClient("CONNECT", host + " " + port);
        processMessage("/NICK " + nick);
        processMessage("/JOIN " + channel);
        listen();
    }

    public String getShellID() {
        return "irc";
    }

    public String getShellDescription() {
        return "Alice IRC chat listener";
    }

    public String getShellCommands() {
        return "Not yet implemented.";
    }

    public void processShellCommand(String s) {
        int i = s.indexOf('/');
        if (i != 0) {
            logMessage("Invalid command.");
            return;
        }
        int j = s.indexOf(' ');
        if (j == -1) {
            processMessageCommand(s.substring(1), "");
            return;
        } else {
            processMessageCommand(s.substring(1, j), s.substring(j + 1));
            return;
        }
    }

    public String getVersion() {
        return "0.86.0b 1999.04.07";
    }

    private void connect() {
        if (clientStatus == 0) {
            clientStatus = 1;
            logMessage("Contacting " + host + ":" + port);
            try {
                socket = new Socket(host, port);
                logMessage("Connected to " + host + ":" + port);
            } catch (UnknownHostException unknownhostexception) {
                logMessage("Cannot connect; unknown server.");
                clientStatus = 0;
            } catch (IOException ioexception1) {
                logMessage("Cannot connect; the server is down or not responding.");
                clientStatus = 0;
            }
            if (clientStatus == 1)
                try {
                    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    writer = new PrintWriter(socket.getOutputStream(), true);
                    clientStatus = 2;
                } catch (IOException ioexception) {
                    logMessage("Cannot connect: I/O error.");
                    clientStatus = 3;
                    try {
                        socket.close();
                    } catch (IOException ioexception2) {
                    } finally {
                        socket = null;
                        clientStatus = 0;
                    }
                }
        } else {
            switch (clientStatus) {
                case 2: // '\002'
                    logMessage("Cannot connect again; already connected.");
                    break;

                case 1: // '\001'
                    logMessage("Cannot connect again; already in the process of connecting.");
                    break;

                case 3: // '\003'
                    logMessage("Cannot connect now; still trying to disconnect.");
                    break;

                default:
                    logMessage("Got unknown clientStatusCode: " + clientStatus);
                    break;
            }
        }
    }

    private void disconnect() {
        if (clientStatus == 2) {
            clientStatus = 3;
            try {
                socket.close();
            } catch (IOException ioexception) {
                logMessage("IO exception trying to disconnect.");
            } finally {
                reader = null;
                writer = null;
                logMessage("Connection closed.");
                clientStatus = 0;
            }
        } else {
            switch (clientStatus) {
                case 0: // '\0'
                    logMessage("Cannot close connection; not connected.");
                    break;

                case 1: // '\001'
                    logMessage("Cannot close connection; currently trying to connect.");
                    break;

                case 3: // '\003'
                    logMessage("Cannot close connection; currently trying to close it.");
                    break;

                case 2: // '\002'
                default:
                    logMessage("Got unknown clientStatusCode: " + clientStatus);
                    break;
            }
        }
    }

    protected void processMessage(String s) {
        if (s.length() > 0)
            if (s.charAt(0) == '/') {
                String s1;
                try {
                    s1 = s.substring(1, s.indexOf(' '));
                } catch (StringIndexOutOfBoundsException stringindexoutofboundsexception) {
                    s1 = s.substring(1);
                }
                if (!processMessageCommand(s1, s) && !processMessageCommandClient(s1, s)) {
                    if (!processMessageCommandDebug(s1, s)) ;
                    sendMessage("[irc]", "Unknown Command: " + s1);
                }
            } else if (clientStatus == 2 && channel.length() > 0) {
                sendMessage("", "[" + nick + "] " + s);
                logMessage("Got a message from [" + nick + "]: " + s);
                sendServerMessage("/MSG  " + channel + " :" + s);
                String as[] = Toolkit.breakLines(Multiplexor.getResponse(s, nick + "_IRC", botID, new TextResponder()));
                if (as.length > 0) {
                    for (int i = 0; i < as.length; i++)
                        processMessage("/PRVMSG " + nick + " " + as[i]);

                }
            }
    }

    private boolean processMessageCommand(String s, String s1) {
        boolean flag = false;
        String s2 = s1.substring(s1.indexOf(' ') + 1);
        if (s2.equals("/" + s))
            s2 = "";
        if (clientStatus == 2) {
            if (s.equalsIgnoreCase("AWAY")) {
                sendServerMessage("AWAY :" + s2);
                flag = true;
            } else if (s.equalsIgnoreCase("INVITE")) {
                if (s2.length() > 0)
                    sendServerMessage("INVITE " + s2 + " " + channel);
                flag = true;
            } else if (s.equalsIgnoreCase("KICK")) {
                if (s2.length() > 0) {
                    int i = s2.indexOf(' ');
                    int k = s2.indexOf(' ', i + 1);
                    try {
                        sendServerMessage("KICK " + channel + " " + s2.substring(0, k) + " :" + s2.substring(k + 1));
                    } catch (StringIndexOutOfBoundsException stringindexoutofboundsexception1) {
                        sendServerMessage("KICK " + channel + " " + s2);
                    }
                }
                flag = true;
            } else if (s.equalsIgnoreCase("LIST")) {
                if (s2.length() == 0)
                    sendServerMessage("LIST " + channel);
                else
                    sendServerMessage("LIST " + s2);
                flag = true;
            } else if (s.equalsIgnoreCase("JOIN")) {
                if (s2.length() == 0) {
                    if (channel.length() == 0)
                        sendMessage("[irc]", "You're not in a channel.");
                    else
                        sendMessage("[irc]", "You're currently in: " + channel + ".");
                } else if (channel.length() == 0)
                    sendServerMessage("JOIN " + s2);
                else if (s2.equals("0")) {
                    sendServerMessage("PART " + channel);
                } else {
                    sendServerMessage("PART " + channel);
                    sendServerMessage("JOIN " + s2);
                }
                flag = true;
            } else if (s.equalsIgnoreCase("MODE")) {
                if (s2.length() > 0)
                    sendServerMessage("MODE " + channel + " " + s2);
                flag = true;
            } else if (s.equalsIgnoreCase("MSG")) {
                if (s2.length() > 0) {
                    try {
                        int j = s2.indexOf(' ');
                        sendServerMessage("PRIVMSG " + s2.substring(0, j) + " :" + s2.substring(j + 1));
                    } catch (StringIndexOutOfBoundsException stringindexoutofboundsexception) {
                        sendServerMessage("PRIVMSG " + s2);
                    }
                    sendMessage("", "*" + nick + "* " + s2);
                }
                flag = true;
            } else if (s.equalsIgnoreCase("NAMES")) {
                if (s2.length() == 0)
                    sendServerMessage("NAMES " + channel);
                else
                    sendServerMessage("NAMES " + s2);
                flag = true;
            } else if (s.equalsIgnoreCase("NICK")) {
                if (s2.length() == 0)
                    sendMessage("[irc]", "You're currently known as " + nick);
                else
                    sendServerMessage("NICK " + s2);
                flag = true;
            } else if (s.equalsIgnoreCase("QUIT")) {
                if (s2.length() == 0)
                    sendServerMessage("QUIT");
                else
                    sendServerMessage("QUIT :" + s2);
                channel = "";
                disconnect();
                flag = true;
            } else if (s.equalsIgnoreCase("TOPIC")) {
                if (channel.length() == 0)
                    sendMessage("[irc]", "You must be in a channel to set the topic!");
                else if (s2.length() == 0)
                    sendServerMessage("TOPIC " + channel);
                else
                    sendServerMessage("TOPIC " + channel + " :" + s2);
                flag = true;
            }
        } else if (s.equalsIgnoreCase("NICK")) {
            if (s2.length() == 0) {
                sendMessage("[irc]", "You're currently known as " + nick);
            } else {
                nick = s2;
                sendMessage("[irc]", "You're now known as " + nick);
            }
            flag = true;
        }
        return flag;
    }

    private boolean processMessageCommandClient(String s, String s1) {
        boolean flag = false;
        int i = -1;
        int ai[] = new int[17];
        do
            if (++i == 0)
                ai[i] = s1.indexOf(" ");
            else
                ai[i] = s1.indexOf(" ", ai[i - 1] + 1);
        while (ai[i] != -1);
        String as[] = new String[16];
        for (int j = 0; j < i; j++)
            if (j + 1 >= i)
                as[j] = s1.substring(ai[j] + 1);
            else
                as[j] = s1.substring(ai[j] + 1, ai[j + 1]);

        if (s.equalsIgnoreCase("SERVER") || s.equalsIgnoreCase("CONNECT")) {
            if (nick.length() == 0)
                sendMessage("[irc]", "You cannot connect to a server unless your NICK is set.");
            else if (i == 0)
                sendMessage("[irc]", "Please specify a server to connect to.");
            else if (i == 1) {
                connect();
                if (clientStatus == 2) {
                    sendServerMessage("USER " + nick + " " + socket.getInetAddress().getHostName() + " server :" + nick);
                    sendServerMessage("NICK " + nick);
                }
            } else {
                try {
                    connect();
                    if (clientStatus == 2) {
                        sendServerMessage("USER " + nick + " " + socket.getInetAddress().getHostName() + " server :" + nick);
                        sendServerMessage("NICK " + nick);
                    }
                } catch (NumberFormatException numberformatexception) {
                    sendMessage("[irc]", "The Port you specified is invalid.");
                }
            }
            flag = true;
        } else if (s.equalsIgnoreCase("EXIT")) {
            if (clientStatus == 2)
                disconnect();
            flag = true;
        } else if (s.equalsIgnoreCase("COMMANDS") || s.equalsIgnoreCase("HELP")) {
            sendMessage("", "");
            sendMessage("[irc]", "sIRC Commands:");
            sendMessage("[irc]", "  /away [<message>]");
            sendMessage("[irc]", "  /commands");
            sendMessage("[irc]", "  /connect <server> [<this.port>]");
            sendMessage("[irc]", "  /exit");
            sendMessage("[irc]", "  /invite <this.nickname>");
            sendMessage("[irc]", "  /join [<channel> | '0']");
            sendMessage("[irc]", "  /kick <this.nickname> [<message>]");
            sendMessage("[irc]", "  /list [<channel>]");
            sendMessage("[irc]", "  /mode <(+|-)mode> [<this.nickname>]");
            sendMessage("[irc]", "  /msg <this.nickname> <message>");
            sendMessage("[irc]", "  /names [<channel>]");
            sendMessage("[irc]", "  /this.nick [<this.nickname>]");
            sendMessage("[irc]", "  /quit [<message>]");
            sendMessage("[irc]", "  /server <server> [<this.port>]");
            sendMessage("[irc]", "  /topic [<topic>]");
            sendMessage("[irc]", "  /version");
            sendMessage("", "");
            flag = true;
        }
        return flag;
    }

    private boolean processMessageCommandDebug(String s, String s1) {
        boolean flag = false;
        int i = -1;
        int ai[] = new int[17];
        do
            if (++i == 0)
                ai[i] = s1.indexOf(" ");
            else
                ai[i] = s1.indexOf(" ", ai[i - 1] + 1);
        while (ai[i] != -1);
        String as[] = new String[16];
        for (int j = 0; j < i; j++)
            if (j + 1 >= i)
                as[j] = s1.substring(ai[j] + 1);
            else
                as[j] = s1.substring(ai[j] + 1, ai[j + 1]);

        if (s.equals("testargs")) {
            StringBuffer stringbuffer = new StringBuffer("Test Arguments; argc=" + i + " command=" + s);
            for (int k = 0; k < i; k++)
                stringbuffer.append(" [" + (k + 1) + ";" + as[k] + "]");

            Trace.devinfo(stringbuffer.toString());
            flag = true;
        } else if (s.equals("debug")) {
            Trace.devinfo("- - - - - - - - - - - - - - - - - - - - ");
            Trace.devinfo("clientStatus=" + clientStatus);
            Trace.devinfo("socket=" + socket);
            Trace.devinfo("reader=" + reader);
            Trace.devinfo("writer=" + writer);
            Trace.devinfo("- - - - - - - - - - - - - - - - - - - - ");
            flag = true;
        } else if (s.equals("raw")) {
            String s2 = "";
            for (int l = 0; l < i; l++)
                s2 = s2 + as[l] + " ";

            sendServerMessage(s2);
            flag = true;
        }
        return flag;
    }

    private void processServerMessage(String s) {
        String s1 = "";
        String s2 = "";
        String s3 = "";
        String s4 = "";
        if (s == null) {
            disconnect();
            return;
        }
        if (s.length() <= 0) ;
        if (s.charAt(0) == ':') {
            int i = s.indexOf(' ');
            int i1 = s.indexOf(' ', i + 1);
            s1 = s.substring(0, i);
            s2 = s.substring(i + 1, i1);
            s3 = s.substring(i1 + 1);
        } else {
            int j = s.indexOf(' ');
            s2 = s.substring(0, j);
            s3 = s.substring(j + 1);
        }
        if (s1.length() > 0)
            try {
                s4 = s1.substring(1, s1.indexOf('!'));
            } catch (StringIndexOutOfBoundsException stringindexoutofboundsexception) {
                s4 = s1.substring(1);
            }
        try {
            switch (Integer.parseInt(s2)) {
                case 322:
                    int k = s3.indexOf(' ');
                    int j1 = s3.indexOf(' ', k + 1);
                    int i2 = s3.indexOf(':');
                    sendMessage("[server]", s3.substring(k + 1, j1) + ": " + s3.substring(j1 + 1, i2 - 1) + " " + s3.substring(i2 + 1));
                    break;

                case 353:
                    int l = s3.indexOf(':');
                    int k1 = s3.indexOf('=');
                    sendMessage("[server]", "Users on " + s3.substring(k1 + 2, l - 1) + ": " + s3.substring(l + 1));
                    break;

                case 372:
                    sendMessage("[server]", s3.substring(s3.indexOf(':') + 1));
                    break;

                default:
                    sendMessage("[server]", "(" + s2 + ") " + s3.substring(s3.indexOf(':') + 1));
                    break;

                case 1: // '\001'
                case 321:
                    break;
            }
        } catch (NumberFormatException numberformatexception) {
            if (s2.equals("INVITE")) {
                int l1 = s3.indexOf(' ');
                sendMessage("[server]", s4 + " has invited you to " + helpExtractIRCString(s3.substring(l1 + 1)) + ".");
            } else if (s2.equals("JOIN")) {
                String s5 = helpExtractIRCString(s3);
                if (s4.equals(nick)) {
                    sendMessage("[server]", "You're now on " + s5 + ".");
                    channel = s5;
                } else {
                    sendMessage("[server]", s4 + " has joined the channel.");
                }
            } else if (s2.equals("KICK")) {
                String s6 = "";
                String s11 = "";
                String s15 = "";
                int j2 = s3.indexOf(' ');
                int l2 = s3.indexOf(' ', j2 + 1);
                try {
                    String s7 = s3.substring(0, j2);
                    String s12 = s3.substring(j2 + 1, l2);
                    String s16 = helpExtractIRCString(s3.substring(l2 + 1));
                    if (s12.equals(nick)) {
                        sendMessage("[server]", "You've just been kicked off " + s7 + " by " + s4 + " (" + s16 + ").");
                        channel = "";
                    } else {
                        sendMessage("[server]", s12 + " has been kicked off " + s7 + " by " + s4 + " (" + s16 + ").");
                    }
                } catch (StringIndexOutOfBoundsException stringindexoutofboundsexception1) {
                    String s8 = s3.substring(0, j2);
                    String s13 = s3.substring(j2 + 1);
                    if (s13.equals(nick)) {
                        sendMessage("[server]", "You've just been kicked off " + s8 + " by " + s4 + ".");
                        channel = "";
                    } else {
                        sendMessage("[server]", s4 + " has been kicked off " + s8 + " by " + s4 + ".");
                    }
                }
            } else if (s2.equals("NICK")) {
                if (s4.equals(nick)) {
                    String s9 = helpExtractIRCString(s3);
                    sendMessage("[server]", "You're now known as " + s9 + ".");
                    nick = s9;
                } else {
                    sendMessage("[server]", s4 + " is now known as " + s3.substring(1) + ".");
                }
            } else if (s2.equals("PART")) {
                if (s4.equals(nick)) {
                    sendMessage("[server]", "You've just left " + s3 + ".");
                    channel = "";
                } else {
                    sendMessage("[server]", s4 + " has left the channel.");
                }
            } else if (s2.equals("PING"))
                sendServerMessage("PONG " + s3);
            else if (s2.equals("PRIVMSG")) {
                String s10 = s3.substring(0, s3.indexOf(' '));
                String s14 = s3.substring(s3.indexOf(':') + 1);
                if (s10.equals(nick)) {
                    sendMessage("", "*" + s4 + "* " + s14);
                    logMessage("Request: [" + s4 + "]: " + s14);
                    String as[] = Toolkit.breakLines(Multiplexor.getResponse(s14, s4 + "_IRC", botID, new TextResponder()));
                    if (as.length > 0) {
                        for (int k2 = 0; k2 < as.length; k2++)
                            processMessage("/MSG " + s4 + " " + as[k2]);

                    }
                } else {
                    sendMessage("", "[" + s4 + "] " + s14);
                }
            } else if (s2.equals("QUIT")) {
                if (s3.length() == 0)
                    sendMessage("[server]", s4 + " has quit.");
                else
                    sendMessage("[server]", s4 + " has quit (" + helpExtractIRCString(s3) + ").");
            } else if (s2.equals("TOPIC"))
                if (s4.equals(nick))
                    sendMessage("[server]", "The topic is now: " + s3.substring(s3.indexOf(':') + 1));
                else
                    sendMessage("[server]", s4 + " has set the topic to: " + helpExtractIRCString(s3.substring(s3.indexOf(' ') + 1)));
        }
    }

    private String helpExtractIRCString(String s) {
        try {
            if (s.charAt(0) == ':')
                return s.substring(1);
            else
                return s;
        } catch (StringIndexOutOfBoundsException stringindexoutofboundsexception) {
            return "";
        }
    }

    private void logMessage(String s) {
        Log.userinfo("AliceIRC: " + s, Log.LISTENERS);
    }

    private void sendMessage(String s, String s1) {
        Log.userinfo("AliceIRC: " + s + " " + s1, Log.LISTENERS);
    }

    private void sendServerMessage(String s) {
        if (clientStatus == 2)
            writer.println(s);
    }

    private void listen() {
        while (clientStatus == 2)
            try {
                String s = reader.readLine();
                processServerMessage(s);
            } catch (IOException ioexception) {
            }
    }
}
