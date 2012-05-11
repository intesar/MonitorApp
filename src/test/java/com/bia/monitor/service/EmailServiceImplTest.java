/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bia.monitor.service;

import com.bia.monitor.email.EmailServiceImpl;
import com.bia.monitor.email.EmailService;
import java.util.Date;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author intesar
 */
public class EmailServiceImplTest {

    public EmailServiceImplTest() {
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
     * Test of sendEmail method, of class EmailServiceImpl.
     */
    @Test
    public void testSendEmail() {
        System.out.println("sendEmail");
        String toAddress = "intesar@ymail.com";
        String subject = "Monitor App Test!";
        String body = "Test!";
        EmailService instance = EmailServiceImpl.getInstance();
        try {
            long st = (new Date()).getTime();
            instance.sendEmail(toAddress, subject, body);
            System.out.println ( "Total Time ms : " + ((new Date()).getTime() - (st)));
        } catch (RuntimeException ex) {
            fail("The test case is a prototype.");
        }
        // TODO review the generated test code and remove the default call to fail.
        //
    }
}
