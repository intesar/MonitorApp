/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bia.monitor.service.task;

import com.bia.monitor.data.JobDown;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import static org.junit.Assert.*;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.powermock.api.easymock.PowerMock.*;
import org.powermock.reflect.Whitebox;

/**
 * Unit tests for the {@link SendWeeklyStatus} class. This demonstrates one
 * basic usage of PowerMock's ability for partial mocking as well as expecting a
 * private method.
 *
 * @author mdshannan
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(SendWeeklyStatus.class)
public class SendWeeklyStatusTest {

    public SendWeeklyStatusTest() {
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
     * Test of run method, of class SendWeeklyStatus.
     */
    //@Test
    public void testRun() {
        System.out.println("run");
        SendWeeklyStatus instance = null;
        instance.run();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * empty jobdown
     *
     * @throws Exception
     */
    @Test
    public void testReplaceData1() throws Exception {

        long expected = 0;
        List<JobDown> list = new ArrayList<JobDown>();

        SendWeeklyStatus sws = new SendWeeklyStatus(null, null, null);
        long actual = Whitebox.invokeMethod(sws, "getDownMins",
                list);

        assertEquals("Expected and actual did not match", expected, actual);
    }

    /**
     * null jobdown
     *
     * @throws Exception
     */
    @Test
    public void testReplaceData2() throws Exception {

        long expected = 0;
        List<JobDown> list = null;

        SendWeeklyStatus sws = new SendWeeklyStatus(null, null, null);
        long actual = Whitebox.invokeMethod(sws, "getDownMins",
                list);

        assertEquals("Expected and actual did not match", expected, actual);
    }

    /**
     * jobdown with one value start and no end- time
     *
     * @throws Exception
     */
    @Test
    public void testReplaceData3() throws Exception {

        long expected = 1;
        List<JobDown> list = new ArrayList<JobDown>();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, -1);
        JobDown jd = new JobDown("1", "1", Boolean.TRUE, cal.getTime(), null);
        list.add(jd);

        SendWeeklyStatus sws = new SendWeeklyStatus(null, null, null);
        long actual = Whitebox.invokeMethod(sws, "getDownMins",
                list);

        assertEquals("Expected and actual did not match", expected, actual);
    }

    /**
     * jobdown with one value start and no end- time
     *
     * @throws Exception
     */
    @Test
    public void testReplaceData4() throws Exception {

        long expected = 5;
        List<JobDown> list = new ArrayList<JobDown>();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, -5);
        JobDown jd = new JobDown("1", "1", Boolean.TRUE, cal.getTime(), null);
        list.add(jd);

        SendWeeklyStatus sws = new SendWeeklyStatus(null, null, null);
        long actual = Whitebox.invokeMethod(sws, "getDownMins",
                list);

        assertEquals("Expected and actual did not match", expected, actual);
    }

    /**
     * jobdown with one value start and now end- time
     *
     * @throws Exception
     */
    @Test
    public void testReplaceData5() throws Exception {

        long expected = 5;
        List<JobDown> list = new ArrayList<JobDown>();
        Calendar cal = Calendar.getInstance();
        Date dt = cal.getTime();
        cal.add(Calendar.MINUTE, -5);
        JobDown jd = new JobDown("1", "1", Boolean.TRUE, cal.getTime(), dt);
        list.add(jd);

        SendWeeklyStatus sws = new SendWeeklyStatus(null, null, null);
        long actual = Whitebox.invokeMethod(sws, "getDownMins",
                list);

        assertEquals("Expected and actual did not match", expected, actual);
    }

    /**
     * jobdown with two value start and now end- time
     *
     * @throws Exception
     */
    @Test
    public void testReplaceData6() throws Exception {

        long expected = 10;
        List<JobDown> list = new ArrayList<JobDown>();
        Calendar cal = Calendar.getInstance();
        Date dt = cal.getTime();
        cal.add(Calendar.MINUTE, -5);
        JobDown jd = new JobDown("1", "1", Boolean.TRUE, cal.getTime(), dt);
        JobDown jd1 = new JobDown("1", "1", Boolean.TRUE, cal.getTime(), dt);
        list.add(jd);

        list.add(jd1);

        SendWeeklyStatus sws = new SendWeeklyStatus(null, null, null);
        long actual = Whitebox.invokeMethod(sws, "getDownMins",
                list);

        assertEquals("Expected and actual did not match", expected, actual);
    }

    /**
     * jobdown with two value start and now end- time
     *
     * @throws Exception
     */
    @Test
    public void testReplaceData7() throws Exception {

        long expected = 10;
        List<JobDown> list = new ArrayList<JobDown>();
        Calendar cal = Calendar.getInstance();
        Date dt = cal.getTime();
        cal.add(Calendar.MINUTE, -5);
        JobDown jd = new JobDown("1", "1", Boolean.TRUE, cal.getTime(), dt);
        JobDown jd1 = new JobDown("1", "1", Boolean.TRUE, cal.getTime(), null);
        list.add(jd);

        list.add(jd1);

        SendWeeklyStatus sws = new SendWeeklyStatus(null, null, null);
        long actual = Whitebox.invokeMethod(sws, "getDownMins",
                list);

        assertEquals("Expected and actual did not match", expected, actual);
    }

    /**
     * empty jobdown
     *
     * @throws Exception
     */
    @Test
    public void testGetUpPercentage1() throws Exception {

        double expected = 100.0;
        
        SendWeeklyStatus sws = new SendWeeklyStatus(null, null, null);
        double actual = Whitebox.invokeMethod(sws, "getUpPercentage",
                0L);

        assertEquals(expected, actual, 0.1);
    }
    
    /**
     * empty jobdown
     *
     * @throws Exception
     */
    @Test
    public void testGetUpPercentage2() throws Exception {
        //10080
        double expected = 91;
        
        SendWeeklyStatus sws = new SendWeeklyStatus(null, null, null);
        double actual = Whitebox.invokeMethod(sws, "getUpPercentage",
                1000L);

        assertEquals(expected, actual, 0.1);
    }
}
