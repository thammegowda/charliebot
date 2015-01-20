// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.net.listener;

import org.alicebot.server.core.Bot;
import org.alicebot.server.core.Multiplexor;
import org.alicebot.server.core.logging.Log;
import org.alicebot.server.core.responder.TextResponder;
import org.alicebot.server.core.util.Trace;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.*;

// Referenced classes of package org.alicebot.server.net.listener:
//            AliceChatListener, AliceICQKeepAlive

public class AliceICQ extends AliceChatListener {

    public static final String label = "AliceICQ";
    private static final int SERVERPORT = 4000;
    private static final String SERVER = "icq.mirabilis.com";
    private static final String _ICQ = "_ICQ";
    private static final short VERSION = 2;
    private static final String MSG = "AliceICQ: ";
    private static InetAddress serverAddy;
    protected DatagramSocket socket;
    private String pass;
    private int uin;
    private DatagramPacket packet;
    private byte buffer[];
    private short seqNo;
    private boolean online;
    private int clientport;
    public AliceICQ(Bot bot) {
        super(bot, "AliceICQ", new String[][]{
                new String[]{
                        "number", ""
                }, new String[]{
                "password", ""
        }
        });
        seqNo = 1;
        online = false;
    }

    public static byte[] toBytes(short word0) {
        byte abyte0[] = new byte[2];
        abyte0[0] = (byte) (word0 & 0xff);
        abyte0[1] = (byte) (word0 >> 8 & 0xff);
        return abyte0;
    }

    public static byte[] toBytes(int i) {
        byte abyte0[] = new byte[4];
        abyte0[0] = (byte) (i & 0xff);
        abyte0[1] = (byte) (i >> 8 & 0xff);
        abyte0[2] = (byte) (i >> 16 & 0xff);
        abyte0[3] = (byte) (i >> 24 & 0xff);
        return abyte0;
    }

    public boolean checkParameters() {
        try {
            uin = Integer.parseInt((String) parameters.get("number"));
        } catch (NumberFormatException numberformatexception) {
            logMessage("Invalid user number (try a number!); aborting.");
            return false;
        }
        pass = (String) parameters.get("password");
        if (uin <= 0)
            logMessage("Invalid user number; aborting.");
        if (pass.length() == 0) {
            logMessage("Invalid empty password; aborting.");
            return false;
        } else {
            clientport = 0;
            return true;
        }
    }

    public void run() {
        try {
            socket = new DatagramSocket();
            serverAddy = InetAddress.getByName("icq.mirabilis.com");
            logMessage("logging in to " + serverAddy);
            ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
            bytearrayoutputstream.write(header((short) 1000));
            bytearrayoutputstream.write(toBytes(clientport));
            bytearrayoutputstream.write(toBytes((short) (pass.length() + 1)));
            bytearrayoutputstream.write((new String(pass + "\0")).getBytes());
            bytearrayoutputstream.write(toBytes(120));
            bytearrayoutputstream.write(InetAddress.getByName("localhost").getAddress());
            bytearrayoutputstream.write(4);
            bytearrayoutputstream.write(toBytes(0));
            bytearrayoutputstream.write(toBytes(2));
            bytearrayoutputstream.write(toBytes((short) 62));
            bytearrayoutputstream.write(toBytes(0));
            bytearrayoutputstream.write(toBytes(0x780000));
            socket.send(new DatagramPacket(bytearrayoutputstream.toByteArray(), bytearrayoutputstream.size(), serverAddy, 4000));
            bytearrayoutputstream.close();
            buffer = new byte[10];
            packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            buffer = packet.getData();
            if (buffer[2] != 10 || buffer[3] != 0)
                if (buffer[2] == 90 && buffer[3] == 0) {
                    ack(buffer[4], buffer[5]);
                } else {
                    logMessage("No acknowledgement from server; aborting.");
                    return;
                }
            socket.receive(packet);
            buffer = packet.getData();
            if (buffer[2] == 90 && buffer[3] == 0)
                ack(buffer[4], buffer[5]);
            else if (buffer[2] != 10 || buffer[3] != 0) {
                logMessage("No Login Reply: " + buffer[2] + " " + buffer[3]);
                return;
            }
            online = true;
            logMessage("Successfully logged on.");
            toICQ(new byte[]{
                    76, 4
            });
            AliceICQKeepAlive aliceicqkeepalive = new AliceICQKeepAlive(this);
            aliceicqkeepalive.setDaemon(true);
            logMessage("Starting for \"" + botID + "\".");
            aliceicqkeepalive.start();
            socket.setSoTimeout(2000);
            do
                try {
                    do {
                        buffer = new byte[512];
                        packet = new DatagramPacket(buffer, buffer.length);
                        socket.receive(packet);
                        buffer = packet.getData();
                    }
                    while (buffer[0] != 2 || buffer[1] != 0 || buffer[2] == 10 && buffer[3] == 0);
                    fromICQ(buffer);
                    ack(buffer[4], buffer[5]);
                    Log.devinfo("AliceICQ: ICQ Command in: " + Integer.toHexString(buffer[2]) + " " + Integer.toHexString(buffer[3]), Log.LISTENERS);
                } catch (InterruptedIOException interruptedioexception) {
                }
            while (true);
        } catch (UnknownHostException unknownhostexception) {
            logMessage("Unknown host!");
        } catch (SocketException socketexception) {
            logMessage("Socket exception!");
        } catch (IOException ioexception) {
            logMessage("IO Exception!");
        }
        signoff();
    }

    public void shutdown() {
        signoff();
    }

    public void toICQ(byte abyte0[])
            throws IOException {
        ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
        bytearrayoutputstream.write(toBytes((short) 2));
        bytearrayoutputstream.write(abyte0[0]);
        bytearrayoutputstream.write(abyte0[1]);
        bytearrayoutputstream.write(toBytes(seqNo));
        bytearrayoutputstream.write(toBytes(uin));
        seqNo = (short) (seqNo + 1 & 0xffff);
        Log.devinfo("AliceICQ: ICQ Command out: " + Integer.toHexString(abyte0[0]) + " " + Integer.toHexString(abyte0[1]), Log.LISTENERS);
        Trace.devinfo("Buffer length: " + abyte0.length);
        if (abyte0.length > 2)
            bytearrayoutputstream.write(abyte0, 2, abyte0.length - 2);
        socket.send(new DatagramPacket(bytearrayoutputstream.toByteArray(), bytearrayoutputstream.size(), serverAddy, 4000));
    }

    private void fromICQ(byte abyte0[]) {
        if (abyte0[2] == 110 && abyte0[3] == 0) {
            int i = (abyte0[6] & 0xff) + (abyte0[7] << 8 & 0xff00) + (abyte0[8] << 16 & 0xff0000) + (abyte0[9] << 24 & 0xff000000);
            return;
        }
        if (abyte0[2] == 120 && abyte0[3] == 0) {
            int j = (abyte0[6] & 0xff) + (abyte0[7] << 8 & 0xff00) + (abyte0[8] << 16 & 0xff0000) + (abyte0[9] << 24 & 0xff000000);
            return;
        }
        if (abyte0[2] == -26 && abyte0[3] == 0) {
            try {
                toICQ(new byte[]{
                        66, 4
                });
            } catch (IOException ioexception) {
                logMessage("IO Exception: " + ioexception.getMessage());
            }
            return;
        }
        if (abyte0[2] == -36 && abyte0[3] == 0) {
            int k = (abyte0[6] & 0xff) + (abyte0[7] << 8 & 0xff00) + (abyte0[8] << 16 & 0xff0000) + (abyte0[9] << 24 & 0xff000000);
            short word0 = (short) ((abyte0[18] & 0xff) + (abyte0[19] << 8 & 0xff00));
            String s = new String(abyte0, 20, word0 - 1);
            logMessage("Message from [" + k + "]: " + s);
            return;
        }
        if (abyte0[2] == 4 && abyte0[3] == 1) {
            int l = (abyte0[6] & 0xff) + (abyte0[7] << 8 & 0xff00) + (abyte0[8] << 16 & 0xff0000) + (abyte0[9] << 24 & 0xff000000);
            short word1 = (short) ((abyte0[12] & 0xff) + (abyte0[13] << 8 & 0xff00));
            String s1 = new String(abyte0, 14, word1 - 1);
            logMessage("Message from [" + l + "]: " + s1);
            if (s1 != null) {
                String s2 = Multiplexor.getResponse(s1, l + "_ICQ", botID, new TextResponder());
                sendMesg(l, s2);
            }
            return;
        } else {
            return;
        }
    }

    public byte[] header(short word0)
            throws IOException {
        ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
        bytearrayoutputstream.write(toBytes((short) 2));
        bytearrayoutputstream.write(toBytes(word0));
        bytearrayoutputstream.write(toBytes(seqNo));
        bytearrayoutputstream.write(toBytes(uin));
        seqNo = (short) (seqNo + 1 & 0xffff);
        return bytearrayoutputstream.toByteArray();
    }

    public void ack(byte byte0, byte byte1)
            throws IOException {
        Log.devinfo("AliceICQ: Acknowledgement from server!", Log.LISTENERS);
        ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
        bytearrayoutputstream.write(toBytes((short) 2));
        bytearrayoutputstream.write(toBytes((short) 10));
        bytearrayoutputstream.write(byte0);
        bytearrayoutputstream.write(byte1);
        bytearrayoutputstream.write(toBytes(uin));
        socket.send(new DatagramPacket(bytearrayoutputstream.toByteArray(), bytearrayoutputstream.size(), serverAddy, 4000));
    }

    public void send(byte abyte0[])
            throws IOException {
        socket.send(new DatagramPacket(abyte0, abyte0.length, serverAddy, 4000));
    }

    public void sendMesg(int i, String s) {
        logMessage("Response to [" + i + "]: " + s);
        try {
            ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
            bytearrayoutputstream.write(14);
            bytearrayoutputstream.write(1);
            bytearrayoutputstream.write(toBytes(i));
            bytearrayoutputstream.write(1);
            bytearrayoutputstream.write(0);
            bytearrayoutputstream.write(toBytes((short) s.length()));
            bytearrayoutputstream.write(s.getBytes());
            bytearrayoutputstream.write(0);
            toICQ(bytearrayoutputstream.toByteArray());
        } catch (IOException ioexception) {
            logMessage("IO exception!");
        }
    }

    public void signoff() {
        online = false;
        logMessage("Signing off.");
        try {
            ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
            bytearrayoutputstream.write(header((short) 1080));
            bytearrayoutputstream.write(toBytes((short) 32));
            bytearrayoutputstream.write((new String("B_USER_DISCONNECTED\0")).getBytes());
            bytearrayoutputstream.write(toBytes((short) 5));
            socket.send(new DatagramPacket(bytearrayoutputstream.toByteArray(), bytearrayoutputstream.size(), serverAddy, 4000));
            bytearrayoutputstream.close();
        } catch (IOException ioexception) {
            logMessage("IO exception while trying to sign off!");
        }
        socket.close();
        logMessage("Signed off.");
    }

    private void logMessage(String s) {
        Log.userinfo("AliceICQ: " + s, Log.LISTENERS);
    }
}
