/*
 * Copyright 2002-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bia.monitor.service;

/**
 *
 * @author Intesar Mohammed
 */
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * 
 * @author Intesar Mohammed
 */
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
        EmailService.getInstance().shutdown();
        //MonitorService.getInstance().shutdown();
    }
}
