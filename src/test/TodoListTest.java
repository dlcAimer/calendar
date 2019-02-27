package test;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import calendar.*;

import static org.junit.Assert.*;

/** 
* TodoList Tester. 
* 
* @author <Authors name> 
* @since <pre>���� 14, 2018</pre> 
* @version 1.0 
*/ 
public class TodoListTest {
    TodoList list = new TodoList ();
    @Before
    public void setUp() throws Exception {
        System.out.println("Class TodoList tests begin! Good luck!");


    }

    @After
    public void tearDown() throws Exception {
        System.out.println("Class TodoList tests end! Are you satisfied?");
    }

/** 
* 
* Method: add(Todo todo) 
* 
*/ 
@Test
public void testAddAndSearch() throws Exception {
//TODO: Test goes here...
    CalendarDate calendarDate = new CalendarDate ("2018-6-1");
    CalendarDate calendarDate1 = new CalendarDate ("2018-6-3");
    CalendarDate calendarDate2 = new CalendarDate ("2018-6-5");
    CalendarInterval interval = new CalendarInterval (2018, 5, 3, 12, 0, 2018, 5, 4, 12, 0);
    CalendarInterval interval1 = new CalendarInterval (2018, 5, 3, 11, 0, 2018, 5, 6, 23, 0);
    CalendarInterval interval2 = new CalendarInterval (2018, 5, 4, 12, 0, 2018, 5, 6, 24, 0);
    CalendarInterval interval3 = new CalendarInterval (2018, 5, 1, 19, 4, 2018, 5, 1, 19, 33);
    CalendarInterval interval4 = new CalendarInterval (2018, 5, 1, 14, 30, 2018, 5, 1, 15, 30);
    CalendarInterval interval5 = new CalendarInterval (2018, 5, 1, 14, 30, 2018, 5, 1, 17, 30);
    CalendarInterval interval6 = new CalendarInterval (2018, 5, 1, 11, 30, 2018, 5, 1, 15, 30);
    MeetTodo meetTodo = new MeetTodo ("Meet", interval3, "location", "detail", true, true);
    DateTodo dateTodo = new DateTodo ("date", interval4, "location", "13", false, false);
    TripTodo tripTodo = new TripTodo ("trip", interval5, "traffic", "location", "fa", true, false);
    OtherTodo otherTodo = new OtherTodo ("other", false, false);
    ViewTodo viewTodo = new ViewTodo ("view", interval6, "location", "company", "post", false, true);
    MemoryTodo memoryTodo = new MemoryTodo ("content", calendarDate, "type", "name", false, false);
    list.add (meetTodo);
    Assert.assertEquals (1, list.search (interval3).size ());
    Assert.assertEquals ("Meet", list.search (interval3).get (0).getContent ());
    list.add (dateTodo);
    Assert.assertEquals (2, list.search (calendarDate).size ());
    Assert.assertEquals (1, list.search (interval3).size ());
    Assert.assertEquals ("date", list.search (interval4).get (0).getContent ());
    list.add (tripTodo);
    Assert.assertEquals (2, list.search (calendarDate).size ());
    Assert.assertEquals (1, list.search (interval5).size ());
    list.add (otherTodo);
}

@Test
public void testSearch() throws Exception {
    CalendarInterval searchInterval=new CalendarInterval(new GregorianCalendar(2018, 3, 13), new GregorianCalendar(2018, 4, 15));

    TodoList list=new TodoList();
    Todo todo=new Todo(1,"Get up", new CalendarDate("2018-4-14"),false,false);
    assertTrue(list.search(searchInterval).isEmpty());
    list.add(todo);
    ArrayList array=list.search(new CalendarDate("2018-4-14"));
    if(!array.contains(todo))
        fail("Searching by date fails");

    GregorianCalendar startDate=new GregorianCalendar(2018, 3, 14, 15, 47);
    GregorianCalendar endDate=new GregorianCalendar(2018, 4, 4, 12, 3);
    CalendarInterval interval=new CalendarInterval(startDate, endDate);
    Todo todoInterval=new Todo(1,"Wake up", interval,false,false);
    list.add(todoInterval);
    array=list.search(searchInterval);
    if(!array.contains(todoInterval))
        fail("Searching by interval fails");

    array=list.search(interval);
    if(!array.contains(todo))
        fail("Searching by interval fails");

    array=list.search(new CalendarDate("2018-4-14"));
    if(!array.contains(todoInterval))
        fail("Searching by date fails");
}


/** 
* 
* Method: delete(Todo todo) 
* 
*/ 
@Test
public void testDelete() throws Exception { 
//TODO: Test goes here...
    CalendarDate calendarDate = new CalendarDate ("2018-6-1");
    CalendarDate calendarDate1 = new CalendarDate ("2018-6-3");
    CalendarDate calendarDate2 = new CalendarDate ("2018-6-5");
    CalendarInterval interval = new CalendarInterval (2018, 5, 3, 12, 0, 2018, 5, 4, 12, 0);
    CalendarInterval interval1 = new CalendarInterval (2018, 5, 3, 11, 0, 2018, 5, 6, 23, 0);
    CalendarInterval interval2 = new CalendarInterval (2018, 5, 4, 12, 0, 2018, 5, 6, 24, 0);
    CalendarInterval interval3 = new CalendarInterval (2018, 5, 1, 19, 4, 2018, 5, 1, 19, 33);
    CalendarInterval interval4 = new CalendarInterval (2018, 5, 1, 14, 30, 2018, 5, 1, 15, 30);
    CalendarInterval interval5 = new CalendarInterval (2018, 5, 1, 14, 30, 2018, 5, 1, 17, 30);
    CalendarInterval interval6 = new CalendarInterval (2018, 5, 1, 11, 30, 2018, 5, 1, 15, 30);

    MeetTodo meetTodo = new MeetTodo ("Meet", interval3, "location", "detail", true, true);
    DateTodo dateTodo = new DateTodo ("date", interval4, "location", "13", false, false);
    TripTodo tripTodo = new TripTodo ("trip", interval5, "traffic", "location", "fa", true, false);
    OtherTodo otherTodo = new OtherTodo ("other", false, false);
    ViewTodo viewTodo = new ViewTodo ("view", interval6, "location", "company", "post", false, true);
    MemoryTodo memoryTodo = new MemoryTodo ("content", calendarDate, "type", "name", false, false);
    Assert.assertEquals (0, list.search (calendarDate1).size ());
    list.add (meetTodo);
    Assert.assertEquals (1, list.search (calendarDate).size ());
    list.add (dateTodo);
    Assert.assertEquals (2, list.search (calendarDate).size ());
    Assert.assertTrue (list.delete (dateTodo));
    list.add (tripTodo);
    Assert.assertEquals (2, list.search (calendarDate).size ());
    list.add (otherTodo);
    list.delete (otherTodo);
    Assert.assertEquals (2, list.search (calendarDate).size ());
}


} 
