/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bia.monitor.service;

import com.bia.monitor.dao.Dao;
import com.bia.monitor.data.Job;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.annotation.PreDestroy;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import org.springframework.stereotype.Component;

/**
 *
 * @author mdshannan
 */
@Component
public class Scheduler {

    private ScheduledThreadPoolExecutor executor;
    protected static final Logger logger = Logger.getLogger(Scheduler.class);
    @Autowired
    private Dao dao;

    public Scheduler() {
        executor = new ScheduledThreadPoolExecutor(10);
        setUpExecutor();
    }
    // veify method implementation begin

    private void setUpExecutor() {
        // prod
        //executor.scheduleAtFixedRate(new VerifyMethod(false), 0, 10, TimeUnit.MINUTES);
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
                list = dao.getMongoTemplate().find(query(where("lastUp").is(Boolean.FALSE)), Job.class);
                //list = mongoOps.findAll(Job.class);
            } else {
                list = dao.getMongoTemplate().find(query(where("lastUp").is(Boolean.TRUE)), Job.class);
            }
            for (Job job : list) {
                Runnable job_ = new JobCheck(dao.getMongoTemplate(), job);
                executor.schedule(job_, 10, TimeUnit.MILLISECONDS);
            }
        }
    }
    // veify method end

    @PreDestroy
    public void shutdown() {
        this.executor.shutdown();
    }
}
