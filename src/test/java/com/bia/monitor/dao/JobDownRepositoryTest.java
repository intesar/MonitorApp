/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bia.monitor.dao;

import com.bia.monitor.data.JobDown;
import java.util.Date;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author mdshannan
 */
public class JobDownRepositoryTest {
    
    public JobDownRepositoryTest() {
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

    JobDownRepository instance;
    
    /**
     * Test of findByJobIdAndDownFromGreaterThan method, of class JobDownRepository.
     */
    @Test
    public void testFindByJobIdAndDownFromGreaterThan() {
        System.out.println("findByJobIdAndDownFromGreaterThan");
        String job_id = "";
        Date downFrom = null;
//        JobDownRepository instance = new JobDownRepositoryImpl();
//        List expResult = null;
//        List result = instance.findByJobIdAndDownFromGreaterThan(job_id, downFrom);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of findByJobIdAndActive method, of class JobDownRepository.
     */
    @Test
    public void testFindByJobIdAndActive() {
        System.out.println("findByJobIdAndActive");
        String jobId = "";
        Boolean active = null;
//         = new JobDownRepositoryImpl();
//        JobDown expResult = null;
//        JobDown result = instance.findByJobIdAndActive(jobId, active);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

}
