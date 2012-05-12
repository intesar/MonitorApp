package com.bia.monitor.service;

import com.bia.monitor.email.EmailService;
import com.bia.monitor.email.EmailServiceImpl;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author intesar
 */
public class MonitorService {

    protected static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(MonitorService.class);
    private Set<Job> list;
    private EmailService emailService;
    private static final MonitorService instance = new MonitorService();
    private ScheduledThreadPoolExecutor executor;

    private MonitorService() {
        list = new ConcurrentSkipListSet<Job>();
        emailService = EmailServiceImpl.getInstance();
        executor = new ScheduledThreadPoolExecutor(10);
        setUpExecutor();
    }

    public String add(String url, String email) {
        if (url == null || url.trim().length() <= 4 || email == null || email.trim().length() <= 4) {
            return "Invalid data!";
        }
        if (!isValidUrl(url, email)) {
            if (logger.isTraceEnabled()) {
                logger.trace(url + ", " + email + " is not valid!");
            }
            return "Check email!";
        }


        String id = monitor(url, email);
        if (logger.isTraceEnabled()) {
            logger.trace(url + ", " + email + ", " + id + " is added to the queue!");
        }
        return "Check email!";
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
        body.append("Your submitted data! <br/>")
                .append("<p>Url : ").append(url).append("<br/>")
                .append("Email : ").append(email).append("<br/><br/>")
                .append("Free Upcoming Features!").append("<br/>")
                .append("1. Free Site/Web Service Monitor <br/>")
                .append("2. Free Alerts <br/>")
                .append("3. Free weekly reports <br/>")
                .append("4. Free Social Media coverage on Twitter, G+, FB, Pinterest, Blogger, Reddit for 95+ uptime!").append("<br/><br/>")
                .append("If you like please refer to your friends --  ").append("http://www.zytoon.me/monitor/ <br/><br/>")
                .append("Unsusbcribe link ...")
                .append(" <a href=\"http://www.zytoon.me/monitor/rest/monitor/delete/").append(job.getId()).append("\" > Stop monitoring my site! </a>  ");
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
                logger.trace(j.getId());
                logger.trace(id);
                logger.trace(" where == ? ");

            }
            if (j.getId().equals(id)) {
                list.remove(j);
                StringBuilder body = new StringBuilder();
                body.append("Your submitted data! <br/>").append("<p>Url : ").append(j.getUrl()).append("<br/>").append("Email : ").append(j.getEmail()).append("<br/><br/>").append("Thanks for using http://www.zytoon.me/monitor/");
                        
                emailService.sendEmail(j.getEmail(), "Your site is no longer monitored!", body.toString());

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

    // veify method implementation begin
    private void setUpExecutor() {
        executor.scheduleAtFixedRate(new VerifyMethod(), 0, 1, TimeUnit.MINUTES);
        //JobMonitor jobMonitor = new JobMonitor();
        //jobMonitor.setRepeatable(new MonitorRunnalbe(jobMonitor, list));
    }

    // this object will executed every 5 mins
    private class VerifyMethod implements Runnable {

        public void run() {
            if (logger.isTraceEnabled()) {
                logger.trace(" started timer!");
            }
            for (Job job : list) {
                Runnable job_ = new JobCheck(job);
                executor.schedule(job_, 10, TimeUnit.MILLISECONDS);
            }
        }
    }
    // veify method end
}
