package test;

import calendar.*;
import org.junit.*;
import org.junit.rules.ExpectedException;

import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.Assert.*;

/** 
* CalendarInterval Tester. 
* 
* @author <Authors name> 
* @since <pre>���� 14, 2018</pre> 
* @version 1.0 
*/ 
public class CalendarIntervalTest {

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        System.out.println("Class CalendarInterval tests begin! Good luck!");
    }

    @After
    public void tearDown() throws Exception {
        System.out.println("Class CalendarInterval tests end! Are you satisfied?");
    }


    @Test
    public void testValidInterval() throws IntervalException{
        for (int i = 0; i <= 11; i++) {
            new CalendarInterval(2018, i, 1, 13, 30,
                    2018, i, 2, 13, 30);
        }

        for(int i=1; i<=30; i++){
            new CalendarInterval(2018, 4, i, 13, 30,
                    2018, 5, i, 13, 30);
        }

        for(int i=0; i<=24; i++){
            new CalendarInterval(2018, 4, 1, i, 30,
                    2018, 5, 1, i, 30);
        }

        for(int i=0; i<=59; i++){
            new CalendarInterval(2018, 4, 1, 13, i,
                    2018, 5, 1, 13, i);
        }
    }



/** 
* 
* Method: getStartTime() 
* 
*/ 
@Test
public void testGetStartTime() throws Exception { 
//TODO: Test goes here...
    GregorianCalendar startDate=new GregorianCalendar(2018, 3, 14, 15, 47);
    GregorianCalendar endDate=new GregorianCalendar(2018, 4, 4, 12, 3);
    CalendarInterval interval=new CalendarInterval(startDate, endDate);
    Assert.assertEquals(interval.getStartTime(), startDate);

} 

/** 
* 
* Method: setStartTime(Date startTime) 
* 
*/ 
@Test
public void testSetStartTime() throws IntervalException {
//TODO: Test goes here...
    GregorianCalendar startDate=new GregorianCalendar(2018, 3, 14, 15, 47);
    GregorianCalendar endDate=new GregorianCalendar(2018, 4, 4, 12, 3);
    CalendarInterval interval=new CalendarInterval(startDate, endDate);
    startDate=new GregorianCalendar(2017, 9, 24, 12, 33);
    interval.setStartTime(startDate);
    Assert.assertEquals(interval.getStartTime(), startDate);
} 

/** 
* 
* Method: getEndTime() 
* 
*/ 
@Test
public void testGetEndTime() throws Exception { 
//TODO: Test goes here...
    GregorianCalendar startDate=new GregorianCalendar(2018, 3, 14, 15, 47);
    GregorianCalendar endDate=new GregorianCalendar(2018, 4, 4, 12, 3);
    CalendarInterval interval=new CalendarInterval(startDate, endDate);
    Assert.assertEquals(interval.getEndTime(), endDate);
} 

/** 
* 
* Method: setEndTime(Date endTime) 
* 
*/ 
@Test
public void testSetEndTime() throws Exception { 
//TODO: Test goes here...
    GregorianCalendar startDate=new GregorianCalendar(2018, 3, 14, 15, 47);
    GregorianCalendar endDate=new GregorianCalendar(2018, 4, 4, 12, 3);
    CalendarInterval interval=new CalendarInterval(startDate, endDate);
    endDate=new GregorianCalendar(2019, 9, 24, 12, 33);
    interval.setEndTime(endDate);
    Assert.assertEquals(interval.getEndTime(), endDate);
}

/**
 *
 * Test competence to handle IntervalException of methods
 *
 */
@Test
public void testIntervalException(){
    GregorianCalendar rightStartDate=new GregorianCalendar(2018, 3, 14, 15, 47);
    GregorianCalendar wrongStartDate=new GregorianCalendar(2019, 4, 4, 12, 3);
    GregorianCalendar rightEndDate=new GregorianCalendar(2018, 4, 4, 12, 3);
    GregorianCalendar wrongEndDate=new GregorianCalendar(2017, 4, 4, 12, 3);
    try {
        CalendarInterval interval = new CalendarInterval(rightStartDate, wrongEndDate);
        fail("Constructor is not able to handle IntervalException");
    }catch (Exception ex){
        assertTrue(ex instanceof IntervalException);
    }

    try {
        CalendarInterval interval = new CalendarInterval(rightStartDate, rightEndDate);
        interval.setEndTime(wrongEndDate);
        fail("EndTime setter is not able to handle IntervalException");
    }catch (Exception ex){
        assertTrue(ex instanceof IntervalException);
    }

    try {
        CalendarInterval interval = new CalendarInterval(rightStartDate, rightEndDate);
        interval.setStartTime(wrongStartDate);
        fail("StartTime setter is not able to handle IntervalException");
    }catch (Exception ex){
        assertTrue(ex instanceof IntervalException);
    }
}

/**
* 
* Method: isOverlapped(CalendarDate date) 
* 
*/ 
@Test
public void testIsOverlappedDate() throws Exception { 
//TODO: Test goes here...
    GregorianCalendar start=new GregorianCalendar(2018, 3, 14, 15, 30);
    GregorianCalendar end=new GregorianCalendar(2018, 3, 16, 15, 30);
    CalendarInterval interval=new CalendarInterval(start, end);

    assertTrue(interval.isOverlapped(new CalendarDate(2018, 4, 14)));
    assertTrue(interval.isOverlapped(new CalendarDate(2018, 4, 15)));
    assertTrue(interval.isOverlapped(new CalendarDate(2018, 4, 16)));
    assertFalse(interval.isOverlapped(new CalendarDate(2018, 4, 17)));
} 

/** 
* 
* Method: isOverlapped(CalendarInterval interval) 
* 
*/ 
@Test
public void testIsOverlappedInterval() throws Exception { 
//TODO: Test goes here...
    GregorianCalendar start=new GregorianCalendar(2018, 3, 14, 15, 30);
    GregorianCalendar end=new GregorianCalendar(2018, 3, 16, 15, 30);
    CalendarInterval interval=new CalendarInterval(start, end);
    GregorianCalendar startAble=new GregorianCalendar(2018, 3, 15, 15, 30);
    GregorianCalendar endAble=new GregorianCalendar(2018, 3, 17, 15,30);
    GregorianCalendar startDisable=new GregorianCalendar(2018, 3, 17, 15, 30);
    GregorianCalendar endDisable=new GregorianCalendar(2018, 3, 20, 15, 30);
    CalendarInterval intervalAble=new CalendarInterval(startAble, endAble);
    CalendarInterval intervalDisable=new CalendarInterval(startDisable, endDisable);

    assertTrue(interval.isOverlapped(intervalAble));
    assertFalse(interval.isOverlapped(intervalDisable));
} 


} 
