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

import com.bia.monitor.data.Job;
import com.bia.monitor.data.JobDown;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Date;
import org.apache.log4j.Logger;
import org.springframework.data.mongodb.core.MongoOperations;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * Runnable checks status of url and alerts emails if down
 * @author Intesar Mohammed
 */
class JobCheck implements Runnable {

    protected static Logger logger = Logger.getLogger(JobCheck.class);
    private MongoOperations mongoOps;
    private Job job;
    //@Autowired
    private EmailService emailService = EmailService.getInstance();

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
    @Override
    public void run() {

        // check status
        String responseCodeStr = getStatus();
        int responseCode = getIntegerVal(responseCodeStr);
        if (responseCode == 200) {
            if (logger.isDebugEnabled()) {
                logger.debug(" ping " + job.getUrl() + " successful!");
            }
            // site is up
            handleSiteUp();

        } else if (responseCode == 413) {
            if (logger.isInfoEnabled()) {
                logger.info(" ping in filter code " + job.getUrl() + " response code : " + responseCodeStr);
            }
            //do nothing
            // all other non-action codes will come here
            notifyAdmin(responseCodeStr);
        } else {

            handleSiteDown(responseCodeStr);

            if (logger.isInfoEnabled()) {
                int mins = (int) ((new Date().getTime() / 60000) - (job.getDownSince().getTime() / 60000));
                logger.info(" ping failed " + job.getUrl() + " response code : " + responseCodeStr + " Down since : " + mins);
            }
        }

    }

    private String getStatus() {
        String responseCode;
        try {
            if (logger.isDebugEnabled()) {
                logger.debug(" pinging " + job.getUrl());
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
        // handle only if site was down earlier
        if (!job.isLastUp()) {
            updateJobOnSiteUp();
            updateJobDownOnSiteUp();
            sendUpNotify();
        }
    }

    private void updateJobDownOnSiteUp() {
        JobDown jobDown = mongoOps.findOne(query(where("job_id").is(job.getId())).addCriteria(where("active").is(Boolean.TRUE)), JobDown.class);
        jobDown.setDownTill(new Date());
        jobDown.setActive(Boolean.FALSE);
        mongoOps.save(jobDown);
    }

    private void updateJobOnSiteUp() {
        job.setLastUp(true);
        job.setNotified(false);
        job.setUpSince(new Date());
        mongoOps.save(job);
    }

    private void sendUpNotify() {
        // send site up notification
        int mins = (int) ((new Date().getTime() / 60000) - (job.getDownSince().getTime() / 60000));
        StringBuilder body = new StringBuilder();
        body.append(job.getUrl()).append(" is Up after ").append(mins).append(" mins of downtime!");

        for (String email : job.getEmail()) {
            emailService.sendEmail(email, job.getUrl() + " is Up!", body.toString());
        }
    }

    private void handleSiteDown(String responseCodeStr) {
        if (logger.isTraceEnabled()) {
            logger.trace(" ping failed " + job.getUrl() + " status code : " + responseCodeStr);
        }
        // only send mail for the first time
        if (job.isLastUp()) {
            updateJobOnSiteDown();
            updateJobDownOnSiteDown();
        } else {
            if (!job.isNotified()) {
                job.setNotified(true);
                mongoOps.save(job);
                // notify after second attempt
                sendDownNotify(responseCodeStr);
            }
        }
    }

    private void updateJobOnSiteDown() {
        job.setLastUp(false);
        job.setDownSince(new Date());
        job.setStatus("Down");
        mongoOps.save(job);
    }

    private void updateJobDownOnSiteDown() {
        JobDown jobDown = new JobDown(job.getId(), Boolean.TRUE, new Date());
        mongoOps.save(jobDown);
    }

    private void sendDownNotify(String responseCodeStr) {
        // send alert email
        StringBuilder body = new StringBuilder();
        body.append(job.getUrl()).append(" <br/> Status : Down! ").append("<br/>Response Code : ").append(responseCodeStr).append("<br/>Detection Time: ").append(job.getDownSince());
        for (String email : job.getEmail()) {
            emailService.sendEmail(email, job.getUrl() + " is Down!", body.toString());
        }
    }

    private void notifyAdmin(String responseCodeStr) {
        // send alert email
        Date time = new Date();
        StringBuilder body = new StringBuilder();
        body.append(job.getUrl()).append(" is Down! ").append("<br/>Response Code : ").append(responseCodeStr).append("<br/>Detection Time : ").append(time).append("<br/>Owner : ").append(job.getEmail());
        emailService.sendEmail("mdshannan@gmail.com", job.getUrl() + " is Down!", "Detected down on : " + time);
    }
}
