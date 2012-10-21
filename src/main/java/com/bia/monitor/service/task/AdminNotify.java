/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bia.monitor.service.task;

import com.bia.monitor.dao.JobRepository;
import com.bia.monitor.data.Job;
import com.bia.monitor.service.EmailService;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author mdshannan
 */
public class AdminNotify implements Runnable {
    
    protected static Logger logger = Logger.getLogger(AdminNotify.class);
    private EmailService emailService;
    private JobRepository jobRepository;
    static final String P_START = "<p>";
    static final String P_END = "</p>";
    
    public AdminNotify(JobRepository jobRepository, EmailService emailService) {
        this.jobRepository = jobRepository;
        this.emailService = emailService;
    }
    
    @Override
    public void run() {
        logger.info("Started!");
        List<Job> list = jobRepository.findByLastUp(Boolean.FALSE);
        logger.info("Total sites found = " + (list != null ? list.size() : 0 ));
        StringBuilder sb = new StringBuilder();
        for (Job job : list) {
            sb.append(P_START).append("url : ").append(job.getUrl()).append(P_END);
            sb.append(P_START).append("down since : ").append(job.getDownSince()).append(P_END);
            sb.append(P_START).append("<a href=\"http://www.zytoon.me/monitor/rest/monitor/delete/")
                    .append(job.getId()).append("/").append(job.getEmail())
                    .append("\" > Stop monitoring my site! </a> ").append(P_END);
            sb.append("<br/>");
        }
        emailService.sendEmail("mdshannan@gmail.com", "Website down list", sb.toString());
    }
}
