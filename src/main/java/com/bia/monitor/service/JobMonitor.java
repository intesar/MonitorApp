/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bia.monitor.service;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author intesar
 */
public class JobMonitor {

    private ScheduledThreadPoolExecutor executor;
    
    public JobMonitor() {
        executor = new ScheduledThreadPoolExecutor(10);
    }
    
    public void setRepeatable(Runnable repeatable) {
        executor.scheduleAtFixedRate(repeatable, 0, 1, TimeUnit.MINUTES);
    }
    public void check(Runnable job) {
        executor.schedule(job, 10, TimeUnit.MILLISECONDS);
    }
}
