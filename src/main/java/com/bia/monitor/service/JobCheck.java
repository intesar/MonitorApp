package com.bia.monitor.service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Date;
import org.apache.log4j.Logger;
import org.springframework.data.mongodb.core.MongoOperations;

/**
 *
 * @author intesar
 */
class JobCheck implements Runnable {

    private MongoOperations mongoOps;
    private Job job;
    protected static Logger logger = Logger.getLogger(JobCheck.class);

    JobCheck(MongoOperations mongoOps, Job job) {
        this.mongoOps = mongoOps;
        this.job = job;
    }

    // < 100 is undertermined.
    // 1nn is informal (shouldn't happen on a GET/HEAD)
    // 2nn is success
    // 3nn is redirect
    // 4nn is client error
    // 5nn is server error
    public void run() {

        // check status
        String responseCodeStr = getStatus();
        int responseCode = getIntegerVal(responseCodeStr);
        if (responseCode == 200) {
            if (logger.isTraceEnabled()) {
                logger.trace(" ping successful " + job.getUrl());
            }
            // site is up
            handleSiteUp();

        } else if (responseCode == 413) {
            if (logger.isTraceEnabled()) {
                logger.trace(" ping in filter code " + job.getUrl() + " response code : " + responseCodeStr);
            }
            //do nothing
            // all other non-action codes will come here
            notifyAdmin(responseCodeStr);
        } else {

            handleSiteDown(responseCodeStr);

            if (logger.isTraceEnabled()) {
                int mins = (int) ((new Date().getTime() / 60000) - (job.getDownSince().getTime() / 60000));
                logger.trace(" ping failed " + job.getUrl() + " response code : " + responseCodeStr + " Down since : " + mins);
            }
        }

    }

    private String getStatus() {
        String responseCode = null;
        try {
            if (logger.isTraceEnabled()) {
                logger.trace(" pinging " + job.getUrl());
            }

            HttpURLConnection connection = (HttpURLConnection) new URL(job.getUrl()).openConnection();
            connection.setRequestMethod("GET");
            responseCode = String.valueOf(connection.getResponseCode());
            return responseCode;

        } catch (ProtocolException pe) {
            responseCode = pe.getMessage();
        } catch (IOException io) {
            responseCode = io.getMessage();
        } catch (RuntimeException re) {
            responseCode = re.getMessage();
        }
        return responseCode;

    }

    private int getIntegerVal(String responseCodeStr) {
        int code = 0;
        try {
            code = Integer.parseInt(responseCodeStr);
        } catch (RuntimeException re) {
            // do nothing
        }
        return code;
    }

    private void handleSiteUp() {
        // OK.


        //job.setStatus("Running");

        handleIfLastDown();
    }

    private void handleIfLastDown() {
        if (!job.isLastUp()) {
            job.setLastUp(true);
            job.setUpSince(new Date());
            this.mongoOps.save(job);

            sendUpNotify();
        }
    }

    private void sendUpNotify() {
        // send site up notification
        int mins = (int) ((new Date().getTime() / 60000) - (job.getDownSince().getTime() / 60000));
        StringBuilder body = new StringBuilder();
        body.append(job.getUrl()).append(" is Up after ").append(mins).append(" mins of downtime!");

        EmailService.getInstance().sendEmail(job.getEmail(), job.getUrl() + " is Up!", body.toString());
    }

    private void handleSiteDown(String responseCodeStr) {
        if (logger.isTraceEnabled()) {
            logger.trace(" ping failed " + job.getUrl() + " status code : " + responseCodeStr);
        }
        // only send mail for the first time
        if (job.isLastUp()) {
            job.setLastUp(false);
            job.setDownSince(new Date());
            job.setStatus("Down");
            this.mongoOps.save(job);
            sendDownNotify(responseCodeStr);
        }
    }

    private void sendDownNotify(String responseCodeStr) {
        // send alert email
        Date time = new Date();
        StringBuilder body = new StringBuilder();
        body.append(job.getUrl()).append(" is Down! ").append("<br/>Response Code : ").append(responseCodeStr).append("<br/>Detection Time: ").append(time);
        EmailService.getInstance().sendEmail(job.getEmail(), job.getUrl() + " is Down!", "Detected down on : " + time);
    }

    private void notifyAdmin(String responseCodeStr) {
        // send alert email
        Date time = new Date();
        StringBuilder body = new StringBuilder();
        body.append(job.getUrl()).append(" is Down! ").append("<br/>Response Code : ").append(responseCodeStr).append("<br/>Detection Time : ").append(time).append("<br/>Owner : ").append(job.getEmail());
        EmailService.getInstance().sendEmail("mdshannan@gmail.com", job.getUrl() + " is Down!", "Detected down on : " + time);
    }
}
