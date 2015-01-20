// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.responder;


// Referenced classes of package org.alicebot.server.core.responder:
//            Responder, ResponderXMLLogger

public class AIMResponder
        implements Responder {

    private static final String EMPTY_STRING = "";

    public AIMResponder() {
    }

    public String preprocess(String s, String s1) {
        return s;
    }

    public String append(String s, String s1, String s2) {
        return s2 + s1;
    }

    public void log(String s, String s1, String s2, String s3, String s4) {
        ResponderXMLLogger.log(s, s1, s2, s3, s4);
    }

    public String postprocess(String s) {
        String s1 = "";
        if (s.length() > 1024)
            s1 = "Huh?";
        else
            s1 = s;
        return s1;
    }
}
