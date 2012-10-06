package com.bia.monitor.service;

/**
 *
 * @author intesar
 */
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class MyThreadContextListener implements ServletContextListener {

    ServletContext context;

    @Override
    public void contextInitialized(ServletContextEvent contextEvent) {
        System.out.println("Context Created");
        context = contextEvent.getServletContext();
    }

    @Override
    public void contextDestroyed(ServletContextEvent contextEvent) {
        context = contextEvent.getServletContext();
        System.out.println("Context Destroyed, shutting EmailService, MonitorService executors!");
        //EmailService.getInstance().shutdown();
        //MonitorService.getInstance().shutdown();
    }
}
