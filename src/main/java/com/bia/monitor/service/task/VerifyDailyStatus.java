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

import com.bia.monitor.dao.JobRepository;
import com.bia.monitor.data.Job;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *
 * @author mdshannan
 */
@Component
public class VerifyDailyStatus {

    protected final Logger logger_ = Logger.getLogger(getClass());
    private JobRepository jobRepository;
    private JobCheck jobCheck;
    
    public VerifyDailyStatus(){}
    
    @Autowired
    public VerifyDailyStatus(JobRepository jobRepository, JobCheck jobCheck) {
        this.jobRepository = jobRepository;
        this.jobCheck = jobCheck;
    }

    // 15 mins
    @Scheduled(fixedRate=ScheduleConstants.DAILY_UPSITE_SCHEDULE)
    public void verifyUpSites() {
        boolean active = true;
        verify(active);    
    }
    
    // 1 min
    @Scheduled(fixedRate=ScheduleConstants.DAILY_DOWNSITE_SCHEDULE)
    public void verifyDownSites() {
        boolean active = false;
        verify(active);    
    }

    private void verify(boolean active) {
        List<Job> list;
        try {
            list = jobRepository.findByLastUp(active);
            logger_.info("checking " + (list != null ? list.size() : 0) + " sites active=" + active);

            for (Job job : list) {
                if (job.getEmail() != null && !job.getEmail().isEmpty()) {
                    jobCheck.run(job);
                }
            }
        } catch (Exception e) {
            logger_.error(e.getMessage(), e);
        }
    }
}
