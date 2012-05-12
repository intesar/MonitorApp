package com.bia.monitor.service;

import static org.junit.Assert.*;
import org.junit.*;

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
    
    MonitorService instance = MonitorService.getInstance();
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
        String url = "http://www.zytoon.me";
        String email = "mdshannan@gmail.com";
        assertNotSame("Request failed!", instance.add(url, email));
        assertSame("Request failed!", instance.add(url, email));
    }

    /**
     * Test of remove method, of class MonitorService.
     */
    @Test
    public void testRemove() {
        System.out.println("remove");
        String url = "http://www.zytoon.me/";
        String email = "mdshannan@gmail.com";
        
        String id =  instance.add(url, email);
        boolean expResult = true;
        boolean result = instance.remove(id);
        assertEquals(expResult, result);
        
        result = instance.remove(id);
        assertEquals(!expResult, result);
    }

    
}
