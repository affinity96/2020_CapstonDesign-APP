package com.example.homekippa;

import com.example.homekippa.data.CreateDailyWorkData;
import com.example.homekippa.network.ServiceApi;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

//@RunWith(AndroidJunit4.class)
public class CreateDailyWorkActivityTest {

    private CreateDailyWorkActivity cda;
    private ServiceApi service;


    @Test
    public void onCreate() {
    }

    @Before
    public void setUp() {
        cda = new CreateDailyWorkActivity();
    }


    @After
    public void tearDown() {
    }


    @Test
    public void afterCreateDailyWorkTest() {

        cda.createDailyWork(new CreateDailyWorkData(62, 30, "유닛테스트용일과", "유닛테스트용일과설명", "11:30", "10:00"));
        int isCreated = cda.isCreatedTest;
        assertEquals(1, isCreated);
    }


}