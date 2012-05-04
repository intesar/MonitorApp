/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bia.monitor;

import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author intesar
 */
public class MonitorServiceTest {
    
    public MonitorServiceTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of add method, of class MonitorService.
     */
    @Test
    public void testAdd() {
        System.out.println("add");
        Job job = null;
        MonitorService instance = new MonitorService();
        instance.add(job);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of remove method, of class MonitorService.
     */
    @Test
    public void testRemove() {
        System.out.println("remove");
        Job job = null;
        MonitorService instance = new MonitorService();
        instance.remove(job);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of verify method, of class MonitorService.
     */
    @Test
    public void testVerify() {
        System.out.println("verify");
        MonitorService instance = new MonitorService();
        instance.verify();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
