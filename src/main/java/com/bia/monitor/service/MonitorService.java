package com.bia.monitor.service;


import com.mongodb.Mongo;
import com.mongodb.MongoException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.log4j.Logger;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 *
 * @author intesar
 */
public class MonitorService {

    protected static final Logger logger = Logger.getLogger(MonitorService.class);
    private MongoOperations mongoOps;
    private EmailService emailService;
    private static final MonitorService instance = new MonitorService();
    private ScheduledThreadPoolExecutor executor;

    private MonitorService() {
        try {
            mongoOps = new MongoTemplate(new Mongo(), "monitor");
            emailService = EmailService.getInstance();
            executor = new ScheduledThreadPoolExecutor(10);
            setUpExecutor();
        } catch (UnknownHostException ex) {
            logger.error(ex);
        } catch (MongoException ex) {
            logger.error(ex);
        }
    }

    public String add(String url, String email) {
        if ((!isValidEmail(email)) || (url == null || url.trim().length() <= 4)) {
            return "Invalid data!";
        }

        String url_ = url.toLowerCase();
        if (!url_.startsWith("http://") && !url_.startsWith("https://")) {
            url = "http://" + url;
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
        return id;
    }

    public boolean remove(String id) {
        if (logger.isTraceEnabled()) {
            logger.trace(id + " trying to remove!");
        }
        List<Job> list = mongoOps.findAll(Job.class);
        for (Job j : list) {
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

    public String status(String id) {
        List<Job> list = mongoOps.findAll(Job.class);
        for (Job j : list) {
            if (logger.isTraceEnabled()) {
                logger.trace(j.getId());
                logger.trace(id);
                logger.trace(" where == ? ");

            }
            if (j.getId().equals(id)) {

                int mins = (int) ((new Date().getTime() / 60000) - (j.getUpSince().getTime() / 60000));

                StringBuilder reply = new StringBuilder();
                reply.append("{url : ").append(j.getUrl()).append(", Status : ").append(j.getStatus()).append(", Since : ").append(mins).append(" minutes}");
                return reply.toString();

            }
        }

        return "No data found, please add your site at http://www.zytoon.me/monitor";
    }

    public static MonitorService getInstance() {
        return instance;
    }

    /**
     *
     * @param emails
     * @return
     */
    private boolean isValidEmail(String email) {
        if (!EmailValidator.getInstance().isValid(email)) {
            return false;
        }
        return true;
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

    private String monitor(String url, String email) {
        // add 
        Job job = new Job();
        job.setUrl(url);
        job.setEmail(email);
        job.setUpSince(new Date());

        mongoOps.insert(job);

        StringBuilder body = new StringBuilder();
        body.append("Your submitted data! <br/>").append("<p>Url : ").append(url).append("<br/>").append("Email : ").append(email).append("<br/><br/>").append("Free Upcoming Features!").append("<br/>").append("1. Free Site/Web Service Monitor <br/>").append("2. Free Alerts <br/>").append("3. Free weekly reports <br/>").append("4. Free Social Media coverage on Twitter, G+, FB, Pinterest, Blogger, Reddit for 95+ uptime!").append("<br/><br/>").append("If you like this service please refer to your friends --  ").append("http://www.zytoon.me/monitor/ <br/><br/>").append("Important link ... <br/>").append("<a href=\"http://www.zytoon.me/monitor/rest/monitor/status/").append(job.getId()).append("\"> Show me site status </a> <br/>").append(" <a href=\"http://www.zytoon.me/monitor/rest/monitor/delete/").append(job.getId()).append("\" > Stop monitoring my site! </a> ");
        emailService.sendEmail(email, "Congratulations we are monitoring your site!", body.toString());

        return job.getId();
    }

    private boolean isUrlMonitored(String url) {
        List<Job> list = mongoOps.findAll(Job.class);
        for (Job j : list) {
            if (j.getUrl().equalsIgnoreCase(url)) {
                return true;
            }
        }
        return false;
    }

    // veify method implementation begin
    private void setUpExecutor() {
        
        executor.scheduleAtFixedRate(new VerifyMethod(false), 0, 5, TimeUnit.MINUTES);
        // one minute check for only down sites
        executor.scheduleAtFixedRate(new VerifyMethod(true), 0, 1, TimeUnit.MINUTES);
    }

    // this object will executed every 5 mins
    private class VerifyMethod implements Runnable {
        private boolean checkDown;
        VerifyMethod (boolean checkDown) {
            this.checkDown = checkDown;
        }
        public void run() {
            if (logger.isTraceEnabled()) {
                logger.trace(" started timer!");
            }
            List<Job> list = null;
            if (checkDown ) {
                list = mongoOps.find(query(where("lastUp").is(Boolean.FALSE)),Job.class);
                //list = mongoOps.findAll(Job.class);
            } else {
                list = mongoOps.find(query(where("lastUp").is(Boolean.TRUE)),Job.class);
            }
            for (Job job : list) {
                Runnable job_ = new JobCheck(mongoOps, job);
                executor.schedule(job_, 10, TimeUnit.MILLISECONDS);
            }
        }
    }
    // veify method end
}
