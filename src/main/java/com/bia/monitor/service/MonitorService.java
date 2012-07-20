package com.bia.monitor.service;

import com.bia.monitor.data.Job;
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
    private MongoOperations mongoTemplate;
    private EmailService emailService;
    private static final MonitorService instance = new MonitorService();
    private ScheduledThreadPoolExecutor executor;

    private MonitorService() {
        try {
            mongoTemplate = new MongoTemplate(new Mongo(), "monitor");
            emailService = EmailService.getInstance();
            executor = new ScheduledThreadPoolExecutor(10);
            setUpExecutor();
        } catch (UnknownHostException ex) {
            logger.error(ex.getMessage(), ex);
        } catch (MongoException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    /**
     *
     * @param url
     * @param email
     * @return
     */
    public String add(String url, String email) {

        if ((!isValidEmail(email)) || (url == null || url.trim().length() <= 4)) {
            return "Invalid data!";
        }

        String url_ = url.toLowerCase();
        if (!url_.startsWith("http://") && !url_.startsWith("https://")) {
            url = "http://" + url;
        }

        String id = monitor(url, email);
        if (logger.isTraceEnabled()) {
            logger.trace(url + ", " + email + ", " + id + " is added to the queue!");
        }
        return id;
    }

    /**
     *
     * @param id
     * @return
     */
    public boolean remove(String id, String email) {
        if (logger.isTraceEnabled()) {
            logger.trace(id + " trying to remove!");
        }

        Job j = mongoTemplate.findById(id, Job.class);

        if (j.getEmail().size() > 1) {
            j.getEmail().remove(email);
            this.mongoTemplate.save(j);
        } else {
            this.mongoTemplate.remove(j);
            if (logger.isTraceEnabled()) {
                logger.trace(id + " is removed!");
            }
        }

        StringBuilder body = new StringBuilder();
        body.append("Your submitted data! <br/>").append("<p>Url : ").append(j.getUrl()).append("<br/>").append("Email : ").append(j.getEmail()).append("<br/><br/>").append("Thanks for using http://www.zytoon.me/monitor/");

        emailService.sendEmail(email, "Your site is no longer monitored!", body.toString());

        return true;
    }

    /**
     *
     * @param id
     * @return
     */
    public String status(String id) {
        Job j = mongoTemplate.findById(id, Job.class);
        if (j != null) {

            int mins = (int) ((new Date().getTime() / 60000) - (j.getUpSince().getTime() / 60000));

            StringBuilder reply = new StringBuilder();
            reply.append("{url : ").append(j.getUrl()).append(", Status : ").append(j.getStatus()).append(", Since : ").append(mins).append(" minutes}");
            return reply.toString();

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

    private String monitor(String url, String email) {

        Job job = null;

        try {
            job = mongoTemplate.findOne(query(where("url").is(url)), Job.class);
        } catch (RuntimeException re) {
        }

        // add 
        if (job != null) {
            job.getEmail().add(email);
            mongoTemplate.save(job);
        } else {
            job = new Job();
            job.setUrl(url);
            job.getEmail().add(email);
            job.setUpSince(new Date());
            mongoTemplate.insert(job);
        }



        StringBuilder body = new StringBuilder();
        body.append("Your submitted data! <br/>").
                append("<p>Url : ").append(url).append("<br/>").
                append("Email : ").append(email).append("<br/><br/>").
                append("Free Upcoming Features!").append("<br/>").
                append("1. Free Site/Web Service Monitor <br/>").
                append("2. Free Alerts <br/>").
                append("3. Free weekly reports <br/>").
                append("4. Free Social Media coverage on Twitter, G+, FB, Pinterest, Blogger, Reddit for 95+ uptime!").append("<br/><br/>").
                append("If you like this service please refer to your friends --  ").append("http://www.zytoon.me/monitor/ <br/><br/>").
                append("Important link ... <br/>").
                append("<a href=\"http://www.zytoon.me/monitor/rest/monitor/status/").append(job.getId()).append("\"> Show me site status </a> <br/>").
                append(" <a href=\"http://www.zytoon.me/monitor/rest/monitor/delete/").append(job.getId()).append("/").append(email).append("\" > Stop monitoring my site! </a> ");

        emailService.sendEmail(email, "Congratulations we are monitoring your site!", body.toString());

        return job.getId();
    }

    // veify method implementation begin
    private void setUpExecutor() {
        // prod
        //executor.scheduleAtFixedRate(new VerifyMethod(false), 0, 5, TimeUnit.MINUTES);
        // one minute check for only down sites
        //executor.scheduleAtFixedRate(new VerifyMethod(true), 0, 1, TimeUnit.MINUTES);

        // dev env
        executor.scheduleAtFixedRate(new VerifyMethod(false), 0, 20, TimeUnit.SECONDS);
        executor.scheduleAtFixedRate(new VerifyMethod(true), 0, 10, TimeUnit.SECONDS);
    }

    // this object will executed every 5 mins
    private class VerifyMethod implements Runnable {

        private boolean checkDown;

        VerifyMethod(boolean checkDown) {
            this.checkDown = checkDown;
        }

        @Override
        public void run() {
            if (logger.isTraceEnabled()) {
                logger.trace(" started timer!");
            }
            List<Job> list;
            if (checkDown) {
                list = mongoTemplate.find(query(where("lastUp").is(Boolean.FALSE)), Job.class);
                //list = mongoOps.findAll(Job.class);
            } else {
                list = mongoTemplate.find(query(where("lastUp").is(Boolean.TRUE)), Job.class);
            }
            for (Job job : list) {
                Runnable job_ = new JobCheck(mongoTemplate, job);
                executor.schedule(job_, 10, TimeUnit.MILLISECONDS);
            }
        }
    }
    // veify method end

    public void shutdown() {
        this.executor.shutdown();
    }
}
