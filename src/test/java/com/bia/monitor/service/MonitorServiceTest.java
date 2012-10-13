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

import java.util.Date;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Intesar Mohammed
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
