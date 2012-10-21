/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bia.monitor.service.task;

import com.bia.monitor.dao.JobDownRepository;
import com.bia.monitor.dao.JobRepository;
import com.bia.monitor.data.Job;
import com.bia.monitor.data.JobDown;
import com.bia.monitor.service.EmailService;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.mockito.Mockito.*;

/**
 *
 * @author mdshannan
 */
public class JobCheckTest {

    public JobCheckTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

   
    /**
     * Test of run method, of class JobCheck.
     */
    @Test
    public void testRun2() {
        System.out.println("run");
        Set<String> set = new HashSet<String>();
        set.add("intesar@ymail.com");
        Date dt = new Date();
        /**
         * Site was not-up and is up now, 
         *  status = up
         *  lastup = true
         *  notified = false
         *  upSince < 1min
         *  donwsince = ignorable
         * 
         */
        boolean lastup = false;
        boolean notified = false;
        Job job = new Job("1", "http://www.zytoon.me", set, "up", lastup, notified, null, dt);
        JobRepository jobRepository = mock(JobRepository.class);
        //List mock = mock(List.class);
        when(jobRepository.save(job)).thenReturn(job);

        JobDown jd = new JobDown("1", "1", !lastup, dt, null);
        
        JobDownRepository jobDownRepository = mock(JobDownRepository.class);
        when(jobDownRepository.findByJobIdAndActive("1", true)).thenReturn(jd);

        EmailService emailService = mock(EmailService.class);
        //when(emailService.sendEmail)
        JobCheck instance = new JobCheck(job, jobRepository, jobDownRepository, emailService);
        instance.run();

        Assert.assertEquals(job.getId(), "1");
        Assert.assertEquals(job.getUrl(), "http://www.zytoon.me");
        Assert.assertEquals(job.getEmail(), set);
        Assert.assertEquals(job.getStatus(), "up");
        Assert.assertEquals(job.isLastUp(), !lastup);
        Assert.assertEquals(job.isNotified(), notified);
        Assert.assertEquals(job.getUpSince().after(dt), true);
        //Assert.assertEquals(job.getDownSince(), null);
    }
    
    /**
     * Test of run method, of class JobCheck.
     */
    @Test
    public void testRun3() {
        System.out.println("run");
        Set<String> set = new HashSet<String>();
        set.add("intesar@ymail.com");
        Date dt = new Date();
        /**
         * Site was not-up and is down too, 
         *  status = down
         *  lastup = false
         *  notified = true
         *  upSince < ignorable
         *  donwsince = dt
         * 
         */
        boolean lastup = false;
        boolean notified = false;
        String url = "http://127.0.0.1";
        String status = "down";
        Job job = new Job("1", url, set, status, lastup, notified, null, dt);
        JobRepository jobRepository = mock(JobRepository.class);
        //List mock = mock(List.class);
        when(jobRepository.save(job)).thenReturn(job);

        JobDown jd = new JobDown("1", "1", !lastup, dt, null);
        
        JobDownRepository jobDownRepository = mock(JobDownRepository.class);
        when(jobDownRepository.findByJobIdAndActive("1", true)).thenReturn(jd);

        EmailService emailService = mock(EmailService.class);
        //when(emailService.sendEmail)
        JobCheck instance = new JobCheck(job, jobRepository, jobDownRepository, emailService);
        instance.run();

        Assert.assertEquals(job.getId(), "1");
        Assert.assertEquals(job.getUrl(), url);
        Assert.assertEquals(job.getEmail(), set);
        Assert.assertEquals(job.getStatus(), status);
        Assert.assertEquals(job.isLastUp(), lastup);
        Assert.assertEquals(job.isNotified(), !notified);
        //Assert.assertEquals(job.getUpSince().after(dt), true);
        Assert.assertEquals(job.getDownSince(), dt);
    }
    
    /**
     * Test of run method, of class JobCheck.
     */
    @Test
    public void testRun4() {
        System.out.println("run");
        Set<String> set = new HashSet<String>();
        set.add("intesar@ymail.com");
        Date dt = new Date();
        /**
         * Site was not-up and is down too, but notified was true
         *  status = down
         *  lastup = false
         *  notified = true
         *  upSince < ignorable
         *  donwsince = dt
         * 
         */
        boolean lastup = false;
        boolean notified = true;
        String url = "http://127.0.0.1";
        String status = "down";
        Job job = new Job("1", url, set, status, lastup, notified, null, dt);
        JobRepository jobRepository = mock(JobRepository.class);
        //List mock = mock(List.class);
        when(jobRepository.save(job)).thenReturn(job);

        JobDown jd = new JobDown("1", "1", !lastup, dt, null);
        
        JobDownRepository jobDownRepository = mock(JobDownRepository.class);
        when(jobDownRepository.findByJobIdAndActive("1", true)).thenReturn(jd);

        EmailService emailService = mock(EmailService.class);
        //when(emailService.sendEmail)
        JobCheck instance = new JobCheck(job, jobRepository, jobDownRepository, emailService);
        instance.run();

        Assert.assertEquals(job.getId(), "1");
        Assert.assertEquals(job.getUrl(), url);
        Assert.assertEquals(job.getEmail(), set);
        Assert.assertEquals(job.getStatus(), status);
        Assert.assertEquals(job.isLastUp(), lastup);
        Assert.assertEquals(job.isNotified(), notified);
        //Assert.assertEquals(job.getUpSince().after(dt), true);
        Assert.assertEquals(job.getDownSince(), dt);
    }
    
    /**
     * Test of run method, of class JobCheck.
     */
    @Test
    public void testRun5() {
        System.out.println("run");
        Set<String> set = new HashSet<String>();
        set.add("intesar@ymail.com");
        Date dt = new Date();
        /**
         * Site was not-up and is up now, and notified was true
         *  status = up
         *  lastup = true
         *  notified = false
         *  upSince < dt
         *  donwsince = ignorable
         * 
         */
        boolean lastup = false;
        boolean notified = true;
        String url = "http://www.zytoon.me";
        String status = "down";
        Job job = new Job("1", url, set, status, lastup, notified, null, dt);
        JobRepository jobRepository = mock(JobRepository.class);
        //List mock = mock(List.class);
        when(jobRepository.save(job)).thenReturn(job);

        JobDown jd = new JobDown("1", "1", !lastup, dt, null);
        
        JobDownRepository jobDownRepository = mock(JobDownRepository.class);
        when(jobDownRepository.findByJobIdAndActive("1", true)).thenReturn(jd);

        EmailService emailService = mock(EmailService.class);
        //when(emailService.sendEmail)
        JobCheck instance = new JobCheck(job, jobRepository, jobDownRepository, emailService);
        instance.run();

        Assert.assertEquals(job.getId(), "1");
        Assert.assertEquals(job.getUrl(), url);
        Assert.assertEquals(job.getEmail(), set);
        Assert.assertEquals(job.getStatus(), "up");
        Assert.assertEquals(job.isLastUp(), !lastup);
        Assert.assertEquals(job.isNotified(), !notified);
        Assert.assertEquals(job.getUpSince().after(dt), true);
        //Assert.assertEquals(job.getDownSince(), dt);
    }
    
     /**
     * Test of run method, of class JobCheck.
     */
    @Test
    public void testRun1() {
        System.out.println("run");
        Set<String> set = new HashSet<String>();
        set.add("intesar@ymail.com");
        Date dt = new Date();
        /**
         * Site was up and is up so status should be up, unchanged values (notified, upsince, downsince, lastup)
         */
        Job job = new Job("1", "http://www.zytoon.me", set, "up", true, false, dt, null);
        JobRepository jobRepository = mock(JobRepository.class);
        //List mock = mock(List.class);
        when(jobRepository.save(job)).thenReturn(job);


        JobDownRepository jobDownRepository = mock(JobDownRepository.class);
        when(jobDownRepository.findByJobIdAndActive("1", true)).thenReturn(null);

        EmailService emailService = mock(EmailService.class);
        //when(emailService.sendEmail)
        JobCheck instance = new JobCheck(job, jobRepository, jobDownRepository, emailService);
        instance.run();

        Assert.assertEquals(job.getId(), "1");
        Assert.assertEquals(job.getUrl(), "http://www.zytoon.me");
        Assert.assertEquals(job.getEmail(), set);
        Assert.assertEquals(job.getStatus(), "up");
        Assert.assertEquals(job.isLastUp(), true);
        Assert.assertEquals(job.isNotified(), false);
        Assert.assertEquals(job.getUpSince(), dt);
        Assert.assertEquals(job.getDownSince(), null);
    }
    
    
    /**
     * Test of run method, of class JobCheck.
     */
    @Test
    public void testRun6() {
        System.out.println("run");
        Set<String> set = new HashSet<String>();
        set.add("intesar@ymail.com");
        Date dt = new Date();
        /**
         * Site was up and is down now, 
         *  status = down
         *  lastup = false
         *  notified = false
         *  upSince < ignorable
         *  donwsince < gt
         * 
         */
        boolean lastup = true;
        boolean notified = false;
        String url = "http://127.0.0.1";
        String status = "up";
        Job job = new Job("1", url, set, status, lastup, notified, dt, null);
        JobRepository jobRepository = mock(JobRepository.class);
        //List mock = mock(List.class);
        when(jobRepository.save(job)).thenReturn(job);

        JobDown jd = new JobDown("1", "1", !lastup, dt, null);
        
        JobDownRepository jobDownRepository = mock(JobDownRepository.class);
        when(jobDownRepository.findByJobIdAndActive("1", true)).thenReturn(jd);
        

        EmailService emailService = mock(EmailService.class);
        //when(emailService.sendEmail)
        JobCheck instance = new JobCheck(job, jobRepository, jobDownRepository, emailService);
        instance.run();
        
        verify(jobDownRepository).save(any(JobDown.class));
        
        Assert.assertEquals(job.getId(), "1");
        Assert.assertEquals(job.getUrl(), url);
        Assert.assertEquals(job.getEmail(), set);
        Assert.assertEquals(job.getStatus(), "down");
        Assert.assertEquals(job.isLastUp(), !lastup);
        Assert.assertEquals(job.isNotified(), notified);
        //Assert.assertEquals(job.getUpSince().after(dt), true);
        Assert.assertEquals(job.getDownSince().after(dt), true);
    }
}
