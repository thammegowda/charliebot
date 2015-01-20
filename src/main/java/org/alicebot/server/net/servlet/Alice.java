// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.net.servlet;

import org.alicebot.server.core.responder.SmartResponder;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Alice extends HttpServlet {

    public Alice() {
    }

    public void init()
            throws ServletException {
    }

    public void init(ServletConfig servletconfig)
            throws ServletException {
    }

    public void doGet(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse)
            throws ServletException, IOException {
        SmartResponder smartresponder = new SmartResponder(httpservletrequest, httpservletresponse);
        smartresponder.doResponse();
    }

    public void doPost(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse)
            throws ServletException, IOException {
        doGet(httpservletrequest, httpservletresponse);
    }
}
