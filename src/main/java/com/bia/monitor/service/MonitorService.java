/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bia.monitor.service;

import com.bia.monitor.email.EmailService;
import com.bia.monitor.email.EmailServiceImpl;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 *
 * @author intesar
 */
public class MonitorService {

    private Set<Job> list;
    private EmailService emailService;
    protected static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(MonitorService.class);
    private static final MonitorService instance = new MonitorService();

    private MonitorService() {
        list = new ConcurrentSkipListSet<Job>();
        emailService = EmailServiceImpl.getInstance();
        setUpExecutor();
    }

    private void setUpExecutor() {
        JobMonitor jobMonitor = new JobMonitor();
        jobMonitor.setRepeatable(new MonitorRunnalbe(jobMonitor, list));
    }

    public String add(String url, String email) {

        if (!isValidUrl(url, email)) {
            if (logger.isTraceEnabled()) {
                logger.trace(url + ", " + email + " is not valid!");
            }
            return "Request failed!";
        }


        String id = monitor(url, email);
        if (logger.isTraceEnabled()) {
            logger.trace(url + ", " + email + ", " + id + " is added to the queue!");
        }
        return id;
    }

    private String monitor(String url, String email) {
        // add 
        Job job = new Job();
        job.setUrl(url);
        job.setEmail(email);
        UUID uuid = UUID.randomUUID();
        job.setId(uuid.toString());

        list.add(job);

        StringBuilder body = new StringBuilder();
        body.append("Your submitted data! <br/>").append("<p>Url : ").append(url).append("<br/>").append("Email : ").append(email).append("<br/><br/>").append(" <a href=\"http://localhost:8080/rest/monitor/delete/id=").append(job.getId()).append("\" > Click here to remove monitor</a>  ");
        emailService.sendEmail(email, "Congratulations we are monitoring your site!", body.toString());

        return job.getId();
    }

    private boolean isValidUrl(String url, String email) {
        if (isUrlMonitored(url)) {
            // send email
            StringBuilder body = new StringBuilder();
            body.append("<p>").append("This url : ").append(url).append(" is already being monitored we cannot remonitor!  </p>");
            emailService.sendEmail(email, "Cannot monitor this site!", body.toString());
            return false;
        }
        return true;
    }

    private boolean isUrlMonitored(String url) {
        for (Job j : list) {
            if (j.getUrl().equalsIgnoreCase(url)) {
                return true;
            }
        }
        return false;
    }

    public boolean remove(String id) {
        if (logger.isTraceEnabled()) {
            logger.trace(id + " trying to remove!");
        }
        for (Job j : list) {
            if (logger.isTraceEnabled()) {
                logger.trace(j);
            }
            if (j.getId().equals(id)) {
                list.remove(j);
                if (logger.isTraceEnabled()) {
                    logger.trace(id + " is removed!");
                }
                return true;
            }
        }

        return false;
    }

    public static MonitorService getInstance() {
        return instance;
    }
}
