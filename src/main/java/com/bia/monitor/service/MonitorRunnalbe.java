/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bia.monitor.service;

import java.util.Set;

/**
 *
 * @author intesar
 */
// this object will executed every 5 mins
class MonitorRunnalbe implements Runnable {

    private Set<Job> set;
    private JobMonitor jobMonitor;
    protected static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(MonitorRunnalbe.class);

    MonitorRunnalbe(JobMonitor jobMonitor, Set<Job> set) {
        this.jobMonitor = jobMonitor;
        this.set = set;
    }

    public void run() {
        if (logger.isTraceEnabled()) {
            logger.trace(" started timer!");
        }
        for (Job job : set) {
            jobMonitor.check(new JobCheck(job));
        }
    }
}
