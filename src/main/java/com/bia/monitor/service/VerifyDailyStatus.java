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

import com.bia.monitor.dao.GenericDao;
import com.bia.monitor.data.Job;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 *
 * @author mdshannan
 */
public class VerifyDailyStatus implements Runnable {

    protected final Logger logger_ = Logger.getLogger(VerifyDailyStatus.class);
    
    private boolean checkDown;
    private GenericDao genericDao;
    private ScheduledThreadPoolExecutor executor;

    public VerifyDailyStatus(ScheduledThreadPoolExecutor executor, GenericDao genericDao, boolean checkDown) {
        this.executor = executor;
        this.checkDown = checkDown;
        this.genericDao = genericDao;
    }

    @Override
    public void run() {
        List<Job> list;
        try {
            if (checkDown) {
                list = genericDao.getMongoTemplate().find(query(where("lastUp").is(Boolean.FALSE)), Job.class);
                logger_.info("checking " + (list != null ? list.size() : 0) + " down sites!");
                //list = mongoOps.findAll(Job.class);
            } else {
                list = genericDao.getMongoTemplate().find(query(where("lastUp").is(Boolean.TRUE)), Job.class);
                logger_.info("checking " + (list != null ? list.size() : 0) + " up sites!");
            }
            for (Job job : list) {
                if (job.getEmail() != null && !job.getEmail().isEmpty()) {
                    Runnable job_ = new JobCheck(genericDao.getMongoTemplate(), job);
                    executor.schedule(job_, 10, TimeUnit.MILLISECONDS);
                }
            }
        } catch (Exception e) {
            logger_.error(e.getMessage(), e);
        }
    }
}
