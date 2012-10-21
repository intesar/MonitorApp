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

import com.bia.monitor.dao.JobDownRepository;
import com.bia.monitor.dao.JobRepository;
import com.bia.monitor.service.task.AdminNotify;
import com.bia.monitor.service.task.SendWeeklyStatus;
import com.bia.monitor.service.task.VerifyDailyStatus;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Scheduler creates jobs which runs on given time intervals
 *
 * @author Intesar Mohammed
 */
@Component
public class Scheduler {

    static final Logger logger = Logger.getLogger(Scheduler.class);
    static final int UP_CHECK_INTERVAL = 15;
    static final int DOWN_CHECK_INTERVAL = 1;
    static final int WEEKLY_INTERVAL = 7;
    static final int MAX_THREADS = 5;
    
    private JobRepository jobRepository;
    private JobDownRepository jobDownRepository;
    
    private ScheduledThreadPoolExecutor executor;
    private EmailService emailService;

    @Autowired
    public Scheduler(JobRepository jobRepository, JobDownRepository jobDownRepository , EmailService emailService) {
        this.jobRepository = jobRepository;
        this.jobDownRepository = jobDownRepository;
        this.emailService = emailService;
    }

    @PostConstruct
    protected void setUpExecutor() {

        executor = new ScheduledThreadPoolExecutor(MAX_THREADS);

        // prod
        // 15 minutes check for up sites
        executor.scheduleAtFixedRate(new VerifyDailyStatus(executor, true, jobRepository, jobDownRepository, emailService), 1, UP_CHECK_INTERVAL, TimeUnit.MINUTES);
        // one minute check for only down sites
        executor.scheduleAtFixedRate(new VerifyDailyStatus(executor, false, jobRepository, jobDownRepository, emailService), 1, DOWN_CHECK_INTERVAL, TimeUnit.MINUTES);
        // weekly jobs
        executor.scheduleAtFixedRate(new SendWeeklyStatus(jobRepository, jobDownRepository, emailService), 0, WEEKLY_INTERVAL, TimeUnit.DAYS);
        // Daily Admin reports
        executor.scheduleAtFixedRate(new AdminNotify(jobRepository, emailService), 0, 1, TimeUnit.DAYS);

        // dev env
        //executor.scheduleAtFixedRate(new VerifyMethod(generciDao, false), 0, 2, TimeUnit.MINUTES);
        logger.info("setting up executor!");

    }

    @PreDestroy
    public void shutdown() {
        this.executor.shutdown();
    }
}
