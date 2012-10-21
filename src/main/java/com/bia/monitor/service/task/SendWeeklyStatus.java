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
package com.bia.monitor.service.task;

import com.bia.monitor.dao.JobDownRepository;
import com.bia.monitor.dao.JobRepository;
import com.bia.monitor.data.Job;
import com.bia.monitor.data.JobDown;
import com.bia.monitor.service.EmailService;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * Sends weekly summary emails
 * @author Intesar Mohammed
 */
public class SendWeeklyStatus implements Runnable {
    
    final static long TOTAL_WEEK_MINS = 10080;
    final static long HUNDERED = 100;
    final static long ZERO = 0;
    final static int MINUS_SEVEN = -7;
    final static int MS_MINS = 60000;
    final static String NEW_LINE = "<br/>";
    final static String MONITOR_SITE = "http://www.zytoon.me/monitor/";
    final static String REPORT_TITLE = "Past 7 days status report";
    final static String SITE = "Site : ";
    final static String TOTAL_UPTIME = "Uptime : ";
    final static String TOTAL_MINS_DOWN = "Total Mins down : ";
    final static String THANKS_MSG = "Thanks for using ";
    final static String REPORT_SUBJECT = "Your site weekly uptime report";
    final static String PERCENTAGE_SYMBOL = "%";

    protected final Logger logger = Logger.getLogger(SendWeeklyStatus.class);
    private EmailService emailService;
    private JobRepository jobRepository;
    private JobDownRepository jobDownRepository;
    
    public SendWeeklyStatus(JobRepository jobRepository, JobDownRepository jobDownRepository, EmailService emailService) {
        this.jobRepository = jobRepository;
        this.jobDownRepository = jobDownRepository;
        this.emailService = emailService;
    }

    @Override
    public void run() {
        logger.info(" started ... ");
        Iterable<Job> list;
        try {

            list = jobRepository.findAll();
            logger.info(" processing jobs... ");
            for (Job job : list) {
                if (job.getEmail() != null && !job.getEmail().isEmpty()) {

                    List<JobDown> jobDowns = getJobDowns(job);

                    long downMins = getDownMins(jobDowns);

                    double upPercentage = getUpPercentage(downMins);

                    notifyUser(job, upPercentage, downMins);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * past 7 days of records
     * @param job
     * @return 
     */
    private List<JobDown> getJobDowns(Job job) {
        Calendar todayMinus7 = Calendar.getInstance();
        todayMinus7.add(Calendar.DAY_OF_MONTH, MINUS_SEVEN);
        //Criteria creteria = where("job_id").is(job.getId()).andOperator(where("downFrom").gte(todayMinus7.getTime()));
        List<JobDown> jobDowns = jobDownRepository.findByJobIdAndDownFromGreaterThan(job.getId(), todayMinus7.getTime());
        return jobDowns;
    }

    /**
     * calculates total mins site was down
     * @param jobDowns
     * @return 
     */
    private long getDownMins(List<JobDown> jobDowns) {
        long downMins = ZERO;
        if (jobDowns != null && !jobDowns.isEmpty()) {
            for (JobDown jobDown : jobDowns) {
                Date till = jobDown.getDownTill();
                if (till == null) {
                    till = Calendar.getInstance().getTime();
                }

                downMins += (till.getTime() - jobDown.getDownFrom().getTime()) / (MS_MINS);
            }
        }
        return downMins;
    }

    /**
     * calculates percentage down time
     * @param downMins
     * @return 
     */
    private double getUpPercentage(long downMins) {
        double upPercentage = HUNDERED;
        if (downMins > ZERO) {
            upPercentage = (HUNDERED - ((downMins * HUNDERED) / TOTAL_WEEK_MINS));
        }
        return upPercentage;
    }

    /**
     * emails users with weekly summary
     * @param job
     * @param upPercentage
     * @param downMins 
     */
    private void notifyUser(Job job, double upPercentage, long downMins) {
        for (String email : job.getEmail()) {
            StringBuilder body = new StringBuilder();
            body.append(REPORT_TITLE).append(NEW_LINE).append(NEW_LINE);
            body.append(SITE).append(job.getUrl()).append(NEW_LINE);
            body.append(TOTAL_UPTIME).append(upPercentage).append(PERCENTAGE_SYMBOL).append(NEW_LINE);
            body.append(TOTAL_MINS_DOWN).append(downMins).append(NEW_LINE).append(NEW_LINE);
            body.append(THANKS_MSG).append(MONITOR_SITE);
            emailService.sendEmail(email, REPORT_SUBJECT, body.toString());
        }
    }
}
