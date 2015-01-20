// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.responder;

import org.alicebot.server.core.ActiveMultiplexor;
import org.alicebot.server.core.Globals;
import org.alicebot.server.core.logging.Log;
import org.alicebot.server.core.util.SuffixFilenameFilter;
import org.alicebot.server.core.util.Trace;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;

// Referenced classes of package org.alicebot.server.core.responder:
//            AbstractMarkupResponder

public class HTMLResponder extends AbstractMarkupResponder {

    public static final String USER_COOKIE_NAME = "alicebot_user";
    public static final String PASSWORD_COOKIE_NAME = "alicebot_password";
    private static final SuffixFilenameFilter htmlFilenameFilter;
    private static final String templatesDirectoryName;
    private static final String registerTemplatePath;
    private static final String loginTemplatePath;
    private static final String changePasswordTemplatePath;
    private static final String badUserIDOrPasswordMessage = "<p>Invalid user id or password.</p>";
    private static final String passwordMismatchMessage = "<p>Password mismatch.</p>";
    private static final String userAlreadyExistsMessage = "<p>User id already exists.</p>";
    private static final String successfulRegistrationMessage = "<p>Successfully registered.</p>";
    private static final String successfulPasswordChangeMessage = "<p>Successfully changed password.</p>";
    private static final String developerErrorMessage = "<p>Developer error.  Please login.</p>";
    private static final int QUALIFY = 1;
    private static final int DO = 2;
    private static final int SEND_FORM = 4;
    private static final int LOGIN = 8;
    private static final int REGISTER = 16;
    private static final int CHANGE_PASSWORD = 32;
    private static final int GO_USER = 128;
    private static final int PROCESS_OK = 256;
    private static final int BAD_PASSWORD = 512;
    private static final int PASSWORD_MISMATCH = 1024;
    private static HashMap templates;
    private static String chatTemplatePath;
    private static boolean requirelogin = Boolean.valueOf(Globals.getProperty("programd.httpserver.requirelogin", "false")).booleanValue();
    private static boolean autocookie = Boolean.valueOf(Globals.getProperty("programd.httpserver.autocookie", "true")).booleanValue();
    private static LinkedList registerTemplate;
    private static LinkedList loginTemplate;
    private static LinkedList changePasswordTemplate;
    private static String SECRET_KEY;
    private static String user;
    private static String password;
    static {
        htmlFilenameFilter = new SuffixFilenameFilter(new String[]{
                ".html", ".htm", ".data", ".php"
        });
        templatesDirectoryName = Globals.getProperty("programd.responder.html.template.directory", "templates" + File.separator + "html");
        chatTemplatePath = templatesDirectoryName + File.separator + Globals.getProperty("programd.responder.html.template.chat-default", "chat.html");
        registerTemplatePath = templatesDirectoryName + File.separator + Globals.getProperty("programd.responder.html.template.register", "register.html");
        loginTemplatePath = templatesDirectoryName + File.separator + Globals.getProperty("programd.responder.html.template.login", "login.html");
        changePasswordTemplatePath = templatesDirectoryName + File.separator + Globals.getProperty("programd.responder.html.template.change-password", "change-password.html");
        registerTemplate = AbstractMarkupResponder.loadTemplate(registerTemplatePath);
        loginTemplate = AbstractMarkupResponder.loadTemplate(loginTemplatePath);
        changePasswordTemplate = AbstractMarkupResponder.loadTemplate(changePasswordTemplatePath);
        if (SECRET_KEY == null)
            try {
                BufferedReader bufferedreader = new BufferedReader(new FileReader("secret.key"));
                SECRET_KEY = bufferedreader.readLine();
            } catch (FileNotFoundException filenotfoundexception) {
                Trace.userinfo("Could not find secret.key file!");
                SECRET_KEY = "";
            } catch (IOException ioexception) {
                Trace.userinfo("I/O error reading secret.key file!");
                SECRET_KEY = "";
            }
        templates = AbstractMarkupResponder.registerTemplates(templatesDirectoryName, htmlFilenameFilter);
    }
    public HTMLResponder(String s, String s1)
            throws IOException {
        super(s);
        if (s1.equals("")) {
            parseTemplate(chatTemplatePath);
        } else {
            String s2 = (String) templates.get(s1);
            if (s2 != null)
                parseTemplate(s2);
            else
                parseTemplate(chatTemplatePath);
        }
    }

    public static String loginRequest() {
        return loginRequest("", "");
    }

    public static String loginRequest(String s, String s1) {
        if (loginTemplate != null) {
            StringBuffer stringbuffer = new StringBuffer();
            StringBuffer stringbuffer1;
            for (ListIterator listiterator = loginTemplate.listIterator(0); listiterator.hasNext(); stringbuffer.append(stringbuffer1.toString())) {
                String s2 = (String) listiterator.next();
                stringbuffer1 = new StringBuffer(s2);
                int i = 0;
                if ((i = s2.indexOf("name=\"user\"")) != -1 && (i = s2.indexOf("value=\"\"")) != -1)
                    stringbuffer1.replace(i + 6, i + 7, "\"" + s + "\"");
                if ((i = s2.indexOf("name=\"password\"")) != -1 && (i = s2.indexOf("value=\"\"")) != -1)
                    stringbuffer1.replace(i + 6, i + 7, "\"" + s1 + "\"");
            }

            return stringbuffer.toString();
        } else {
            return "";
        }
    }

    public static String registerRequest() {
        StringBuffer stringbuffer = new StringBuffer("");
        if (registerTemplate != null) {
            for (ListIterator listiterator = registerTemplate.listIterator(0); listiterator.hasNext(); stringbuffer.append((String) listiterator.next()))
                ;
            return stringbuffer.toString();
        } else {
            return "";
        }
    }

    public static String changePasswordRequest() {
        if (changePasswordTemplate != null) {
            StringBuffer stringbuffer = new StringBuffer("");
            for (ListIterator listiterator = changePasswordTemplate.listIterator(0); listiterator.hasNext(); stringbuffer.append((String) listiterator.next()))
                ;
            return stringbuffer.toString();
        } else {
            return "";
        }
    }

    private static boolean parameterEquals(HttpServletRequest httpservletrequest, String s, String s1) {
        String s2 = httpservletrequest.getParameter(s);
        if (s2 != null)
            return s2.equals(s1);
        else
            return false;
    }

    public String authenticate(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse, String s) {
        int i = 0;
        Cookie acookie[] = httpservletrequest.getCookies();
        boolean flag = false;
        HttpSession httpsession = httpservletrequest.getSession(true);
        int j = acookie.length;
        for (int k = 0; k < j; k++) {
            if (acookie[k].getName().equals("alicebot_user")) {
                user = acookie[k].getValue();
                flag = true;
            }
            if (acookie[k].getName().equals("alicebot_password"))
                password = acookie[k].getValue();
        }

        String s1 = httpservletrequest.getParameter("user");
        String s2 = httpservletrequest.getParameter("password");
        String s3 = httpservletrequest.getParameter("oldPassword");
        String s4 = httpservletrequest.getParameter("password1");
        if (parameterEquals(httpservletrequest, "login", "yes")) {
            i |= 8;
            i |= 4;
        } else if (parameterEquals(httpservletrequest, "change-password", "yes")) {
            i |= 0x20;
            i |= 1;
        } else if (parameterEquals(httpservletrequest, "checkauth", "auth")) {
            i |= 8;
            i |= 1;
        } else if (parameterEquals(httpservletrequest, "register", "yes")) {
            i |= 0x10;
            i |= 4;
        } else if (parameterEquals(httpservletrequest, "register", "auth")) {
            i |= 0x10;
            i |= 1;
        } else if (flag) {
            if (ActiveMultiplexor.getInstance().checkUser(user, password, SECRET_KEY, botid))
                i |= 0x80;
            else if (makeNewCookies(httpservletresponse)) {
                Trace.devinfo("Found invalid cookie but created new one because autocookie is on.");
                i |= 0x80;
            } else {
                Log.userinfo("Server error: Could not create new user using autocookie.", Log.ERROR);
                return loginRequest();
            }
        } else if (autocookie) {
            if (makeNewCookies(httpservletresponse)) {
                i |= 0x80;
            } else {
                Log.userinfo("Server error: Could not create new user using autocookie.", Log.ERROR);
                return loginRequest();
            }
        } else {
            return loginRequest();
        }
        if ((i & 1) == 1)
            if (s1 != null) {
                if (s2 != null) {
                    if ((i & 8) == 8)
                        i |= 2;
                    else if (s4 != null) {
                        if ((i & 0x10) == 16) {
                            if (s2.equals(s4))
                                i |= 2;
                            else
                                return "<p>Invalid user id or password.</p>" + registerRequest();
                        } else if ((i & 0x20) == 32)
                            if (s2.equals(s3))
                                i |= 2;
                            else
                                return "<p>Password mismatch.</p>" + changePasswordRequest();
                    } else {
                        i |= 4;
                    }
                } else {
                    i |= 4;
                }
            } else {
                i |= 4;
            }
        if ((i & 2) == 2) {
            if ((i & 8) == 8) {
                if (ActiveMultiplexor.getInstance().checkUser(s1, s2, SECRET_KEY, botid)) {
                    Cookie cookie = new Cookie("alicebot_user", s1);
                    Cookie cookie2 = new Cookie("alicebot_password", s2);
                    cookie.setMaxAge(0xf4240);
                    cookie2.setMaxAge(0xf4240);
                    httpservletresponse.addCookie(cookie);
                    httpservletresponse.addCookie(cookie2);
                    httpsession.setAttribute("alicebot_user", user);
                    i |= 0x100;
                } else {
                    return "<p>Invalid user id or password.</p>" + loginRequest();
                }
            } else if ((i & 0x20) == 32) {
                if (ActiveMultiplexor.getInstance().checkUser(user, s3, SECRET_KEY, botid)) {
                    Cookie cookie1 = new Cookie("alicebot_password", s2);
                    cookie1.setMaxAge(0xf4240);
                    httpservletresponse.addCookie(cookie1);
                    i |= 0x100;
                } else {
                    return "<p>Invalid user id or password.</p>" + loginRequest();
                }
            } else if ((i & 0x10) == 16)
                if (ActiveMultiplexor.getInstance().createUser(s1, s2, SECRET_KEY, botid))
                    i |= 0x100;
                else
                    return "<p>User id already exists.</p>" + registerRequest();
        } else if ((i & 4) == 4) {
            if ((i & 8) == 8)
                return loginRequest();
            if ((i & 0x10) == 16)
                return registerRequest();
            if ((i & 0x20) == 32)
                return changePasswordRequest();
        }
        if ((i & 0x80) == 128) {
            httpsession.setAttribute("alicebot_user", user);
            return null;
        }
        if ((i & 0x100) == 256) {
            if ((i & 8) == 8) {
                httpsession.setAttribute("alicebot_user", user);
                return null;
            }
            if ((i & 0x10) == 16)
                return "<p>Successfully registered.</p>" + loginRequest(s1, s2);
            if ((i & 0x20) == 16)
                return "<p>Successfully changed password.</p>" + loginRequest(s1, s2);
            else
                return "<p>Developer error.  Please login.</p>" + loginRequest();
        } else {
            return "<p>Developer error.  Please login.</p>" + loginRequest();
        }
    }

    private boolean makeNewCookies(HttpServletResponse httpservletresponse) {
        StringBuffer stringbuffer = new StringBuffer(17);
        StringBuffer stringbuffer1 = new StringBuffer(10);
        stringbuffer.append("webuser");
        long l = System.currentTimeMillis();
        stringbuffer.append(l);
        stringbuffer1.append(l);
        for (int j = 6; --j > 0; ) {
            int i = (int) (Math.random() * 5D);
            stringbuffer.append(i);
            stringbuffer1.append(i);
        }

        user = stringbuffer.toString();
        password = stringbuffer1.toString();
        Cookie cookie = new Cookie("alicebot_user", user);
        Cookie cookie1 = new Cookie("alicebot_password", password);
        cookie.setMaxAge(0xf4240);
        cookie1.setMaxAge(0xf4240);
        httpservletresponse.addCookie(cookie);
        httpservletresponse.addCookie(cookie1);
        return ActiveMultiplexor.getInstance().createUser(user, password, SECRET_KEY, botid);
    }
}
