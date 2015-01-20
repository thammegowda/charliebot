// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.responder;

import org.alicebot.server.core.Bots;
import org.alicebot.server.core.Globals;
import org.alicebot.server.core.Multiplexor;
import org.alicebot.server.core.util.DeveloperError;
import org.alicebot.server.core.util.UserError;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

// Referenced classes of package org.alicebot.server.core.responder:
//            TextResponder, HTMLResponder, FlashResponder, Responder

public class SmartResponder {

    private static final String EMPTY_STRING = "";
    private static final String CONNECT = Globals.getProperty("programd.connect-string", "CONNECT");
    private static final String INACTIVITY = Globals.getProperty("programd.inactivity-string", "INACTIVITY");
    private static final String TEXT_PARAM = "text";
    private static final String USERID_PARAM = "userid";
    private static final String BOTID_PARAM = "botid";
    private static final String TEXT_PLAIN = "text/plain";
    private static final String HTML_CONTENT_TYPE = "text/html; charset=UTF-8";
    private static final String ENC_8859_1 = "8859_1";
    private static final String ENC_UTF8 = "utf-8";
    private static final String USER_AGENT = "USER-AGENT";
    private static final String PLAIN_TEXT = "plain_text";
    private static final String FLASH = "flash";
    private static final String TEMPLATE = "template";
    private static final String HTML_USER_AGENTS[] = {
            "Mozilla", "MSIE", "Lynx", "Opera"
    };
    private static final int HTML_USER_AGENT_COUNT = HTML_USER_AGENTS.length;
    private String serviceAgent;
    private HttpServletResponse serviceResponse;
    private HttpServletRequest serviceRequest;
    private Responder responder;
    private String botResponse;
    private String userRequest;
    private String userid;
    private String botid;
    private String templateName;
    private int serviceType;
    private ServletOutputStream serviceOutputStream;
    public SmartResponder(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse) {
        serviceRequest = httpservletrequest;
        serviceResponse = httpservletresponse;
        userRequest = httpservletrequest.getParameter("text");
        userid = httpservletrequest.getParameter("userid");
        botid = httpservletrequest.getParameter("botid");
        if (userRequest == null)
            userRequest = CONNECT;
        else if (userRequest.equals(""))
            userRequest = INACTIVITY;
        else
            try {
                userRequest = new String(userRequest.getBytes("8859_1"), "utf-8");
            } catch (UnsupportedEncodingException unsupportedencodingexception) {
                throw new DeveloperError("Encodings are not properly supported!");
            }
        if (userid == null)
            userid = httpservletrequest.getRemoteHost();
        if (botid == null)
            botid = Bots.getFirstBot().getID();
        templateName = httpservletrequest.getParameter("template");
        if (templateName == null)
            templateName = "";
    }

    public void doResponse() {
        try {
            serviceOutputStream = serviceResponse.getOutputStream();
        } catch (IOException ioexception) {
            throw new DeveloperError("Error getting service response output stream.", ioexception);
        }
        switch (getServiceType()) {
            case 1: // '\001'
                serviceResponse.setContentType("text/plain");
                botResponse = Multiplexor.getResponse(userRequest, userid, botid, new TextResponder());
                break;

            case 2: // '\002'
                serviceResponse.setContentType("text/html; charset=UTF-8");
                HTMLResponder htmlresponder;
                try {
                    htmlresponder = new HTMLResponder(botid, templateName);
                } catch (IOException ioexception1) {
                    throw new DeveloperError("Error initializing HTMLResponder.");
                }
                botResponse = htmlresponder.authenticate(serviceRequest, serviceResponse, userid);
                if (botResponse == null)
                    botResponse = Multiplexor.getResponse(userRequest, (String) serviceRequest.getSession(true).getAttribute("alicebot_user"), botid, htmlresponder);
                break;

            case 3: // '\003'
                serviceResponse.setContentType("text/plain");
                botResponse = Multiplexor.getResponse(userRequest, userid, botid, new FlashResponder(botid, templateName));
                break;

            default:
                serviceResponse.setContentType("text/plain");
                botResponse = Multiplexor.getResponse(userRequest, userid, botid, new TextResponder());
                break;
        }
        try {
            serviceOutputStream.write(botResponse.getBytes("utf-8"));
        } catch (UnsupportedEncodingException unsupportedencodingexception) {
            throw new UserError("UTF-8 encoding is not supported on your platform!", unsupportedencodingexception);
        } catch (IOException ioexception4) {
            throw new DeveloperError("Error writing to service output stream.", ioexception4);
        }
        try {
            serviceOutputStream.flush();
        } catch (IOException ioexception2) {
            throw new DeveloperError("Error flushing service output stream.", ioexception2);
        }
        try {
            serviceOutputStream.close();
        } catch (IOException ioexception3) {
            throw new DeveloperError("Error closing service output stream.", ioexception3);
        }
    }

    public int getServiceType() {
        if (serviceRequest.getParameter("plain_text") != null)
            return 1;
        if (serviceRequest.getParameter("flash") != null)
            return 3;
        for (int i = HTML_USER_AGENT_COUNT; --i >= 0; )
            if (serviceRequest.getHeader("USER-AGENT").indexOf(HTML_USER_AGENTS[i]) != -1)
                return 2;

        return 0;
    }

}
