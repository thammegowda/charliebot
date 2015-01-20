package org.alicebot.server.net.servlet;

import org.alicebot.server.core.Globals;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;

/**
 * @author Thamme Gowda N (thammegowda@simplyphi.com)
 */

public class StartupListener implements javax.servlet.ServletContextListener {
    private static final Logger LOG = LoggerFactory.getLogger(StartupListener.class);
    public void contextInitialized(ServletContextEvent sce) {
        LOG.info("Initialising the bot..");
        Globals.load(Globals.DEFAULT_SETTINGS);
    }

    public void contextDestroyed(ServletContextEvent sce) {
        LOG.info("Destroying the bot...");
    }
}

