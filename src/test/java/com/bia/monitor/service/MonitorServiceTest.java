package com.bia.monitor.service;

import java.util.Date;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author intesar
 */
@Ignore
public class MonitorServiceTest {
    
    public MonitorServiceTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    MonitorService instance = new MonitorService();
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
        String url = "http://www.zytoon.me?" + (new Date().getTime());
        String email = "mdshannan@gmail.com";
        String result = instance.add(url, email);
        System.out.println ( "result xxxx : " + result );
        assertNotSame("Invalid data!", result);
        assertNotSame("Check email!", result);
        result = instance.add(url, email);
        System.out.println ( "result yyyy : " + result );
        //assertSame("Check email!", result);
    }

    /**
     * Test of remove method, of class MonitorService.
     */
    @Test
    public void testRemove() {
        System.out.println("remove");
        String url = "http://www.zytoon.me?" + (new Date().getTime());
        String email = "mdshannan@gmail.com";
        
        String id =  instance.add(url, email);
        System.out.println ("id zzzz : " + id );
        boolean expResult = true;
        boolean result = instance.remove(id, email);
        assertEquals(expResult, result);
        
        result = instance.remove(id, email);
        assertEquals(expResult, result);
    }

    
}
