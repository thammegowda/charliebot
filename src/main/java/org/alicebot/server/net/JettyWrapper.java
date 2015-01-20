package org.alicebot.server.net;

import org.alicebot.server.core.Globals;
import org.alicebot.server.net.servlet.Alice;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;

public class JettyWrapper implements AliceCompatibleHttpServer {

    private static final Logger LOG = LoggerFactory.getLogger(JettyWrapper.class);
    private static Server jetty;

    public JettyWrapper() {
    }

    public void configure(String s)
            throws IOException {
        int port = 8080;
        jetty = new Server(port);
        //jetty.configure(s);
        //jetty.setStatsOn(true);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        jetty.setHandler(context);
        context.addServlet(new ServletHolder(new Alice()),"/*");
        Globals.setHttpPort(port);
    }

    public void run() {

        try {
            jetty.start();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

    }

    public void shutdown() {
        try {
            jetty.stop();
        } catch (InterruptedException interruptedexception) {
            org.alicebot.server.core.logging.Log.devinfo("Jetty was interrupted while stopping.", org.alicebot.server.core.logging.Log.ERROR);
            throw new IllegalStateException(interruptedexception);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public Server getServer() {
        return jetty;
    }
}
