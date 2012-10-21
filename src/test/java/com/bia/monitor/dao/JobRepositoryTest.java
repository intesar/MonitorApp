/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bia.monitor.dao;

import com.bia.monitor.data.Job;
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
public class JobRepositoryTest {
    
    public JobRepositoryTest() {
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
    
    JobRepository instance;

    /**
     * Test of findByUrl method, of class JobRepository.
     */
    @Test
    public void testFindByUrl() {
        System.out.println("findByUrl");
        String url = "";
        
        Job expResult = null;
//        Job result = instance.findByUrl(url);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of findByLastUp method, of class JobRepository.
     */
    @Test
    public void testFindByLastUp() {
        System.out.println("findByLastUp");
        Boolean FALSE = null;
        List expResult = null;
        //List result = instance.findByLastUp(FALSE);
        //assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

   
}
