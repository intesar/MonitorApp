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
import com.bia.monitor.service.EmailService;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;

/**
 *
 * @author mdshannan
 */
public class VerifyDailyStatus implements Runnable {

    protected final Logger logger_ = Logger.getLogger(VerifyDailyStatus.class);
    private boolean active;
    //private GenericDao genericDao;
    private ScheduledThreadPoolExecutor executor;
    private EmailService emailService;
    private JobRepository jobRepository;
    private JobDownRepository jobDownRepository;
    
    public VerifyDailyStatus(ScheduledThreadPoolExecutor executor, boolean checkDown, JobRepository jobRepository, JobDownRepository jobDownRepository, EmailService emailService) {
        this.executor = executor;
        this.active = checkDown;
        this.jobRepository = jobRepository;
        this.jobDownRepository = jobDownRepository;
        this.emailService = emailService;
    }

    @Override
    public void run() {
        List<Job> list;
        try {
            list = jobRepository.findByLastUp(active);
            logger_.info("checking " + (list != null ? list.size() : 0) + " sites active=" + active);

            for (Job job : list) {
                if (job.getEmail() != null && !job.getEmail().isEmpty()) {
                    Runnable job_ = new JobCheck(job, jobRepository, jobDownRepository, emailService);
                    executor.schedule(job_, 10, TimeUnit.MILLISECONDS);
                }
            }
        } catch (Exception e) {
            logger_.error(e.getMessage(), e);
        }
    }
}
