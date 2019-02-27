package test;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;

import calendar.*;

import static org.junit.Assert.*;

public class ChildListTest {

    CalendarDate calendarDate, calendarDate1;
    CalendarInterval calendarInterval, calendarInterval1, calendarInterval2, calendarInterval3, calendarInterval4, calendarInterval5, calendarInterval6, calendarInterval7, calendarInterval8, calendarInterval9, calendarInterval10;
    TodoList todoList;
    MemoryTodo memoryTodo;
    TripTodo tripTodo;
    MeetTodo meetTodo;
    CourseTodo courseTodo;
    OtherTodo otherTodo, otherTodo1;
    ViewTodo viewTodo;

    @Before
    public void setUp() throws Exception {
        System.out.println ("Class CalendarDate tests begin! Good luck!");
        calendarDate = new CalendarDate ("2018-6-1");
        calendarDate1 = new CalendarDate ("2018-6-2");
        calendarInterval = new CalendarInterval (2018, 5, 1, 0, 1, 2018, 5, 1, 2, 0);
        calendarInterval1 = new CalendarInterval (2018, 5, 1, 2, 0, 2018, 5, 1, 3, 0);
        calendarInterval2 = new CalendarInterval (2018, 5, 1, 3, 0, 2018, 5, 1, 12, 30);
        calendarInterval3 = new CalendarInterval (2018, 5, 1, 11, 30, 2018, 5, 1, 15, 0);
        calendarInterval4 = new CalendarInterval (2018, 5, 1, 16, 0, 2018, 5, 1, 17, 0);
        calendarInterval5 = new CalendarInterval (2018, 5, 1, 0, 0, 2018, 5, 1, 23, 59);
        todoList = new TodoList ();
        memoryTodo = new MemoryTodo ("memory", calendarDate1, "type", "name", false, true);
        tripTodo = new TripTodo ("trip", calendarInterval1, "car", "location", "B612", true, false);
        meetTodo = new MeetTodo ("Meet", calendarInterval2, "location", "detail", true, true);
        courseTodo = new CourseTodo ("math", calendarDate, 15, 0, 16, 0, "detail", "location", 50, "teacher", "tip", 5, false, false);
        otherTodo = new OtherTodo ("content", calendarInterval3, false, false);
        viewTodo = new ViewTodo ("View", calendarInterval4, "location", "company", "post", false, false);
        otherTodo1 = new OtherTodo ("big", calendarInterval5, false, false);

    }

    @After
    public void tearDown() throws Exception {
        System.out.println ("Class CalendarDate tests end! Are you satisfied?");
    }

    @Test
    public void ChildAdd() throws Exception {
        todoList.add (otherTodo1);
        Assert.assertTrue (todoList.search (calendarDate).contains (otherTodo1));
        todoList.addChild (otherTodo1, meetTodo);
        Assert.assertEquals (1, todoList.search (calendarDate).get (0).getChildTodo ().size ());
        todoList.addChild (otherTodo1, courseTodo);
        Assert.assertEquals (1, todoList.search (calendarDate).get (0).getChildTodo ().size ());
        todoList.addChild (otherTodo1, otherTodo);
        Assert.assertEquals (2, todoList.search (calendarDate).get (0).getChildTodo ().size ());
        todoList.addChild (otherTodo1, viewTodo);
        Assert.assertEquals (3, todoList.search (calendarDate).get (0).getChildTodo ().size ());

    }


    @Test
    public void ChildDelete() throws Exception {
        todoList.add (otherTodo1);
        todoList.addChild (otherTodo1, meetTodo);
        todoList.addChild (otherTodo1, otherTodo);
        todoList.addChild (otherTodo1, viewTodo);
        Assert.assertEquals (3, todoList.search (calendarDate).get (0).getChildTodo ().size ());
        Assert.assertTrue (todoList.search (calendarDate).contains (otherTodo1));
        todoList.search (calendarDate).get (0).getChildTodo ().remove (meetTodo);
        Assert.assertEquals (2, todoList.search (calendarDate).get (0).getChildTodo ().size ());
        Assert.assertTrue (todoList.search (calendarDate).contains (otherTodo1));
        todoList.search (calendarDate).get (0).getChildTodo ().remove (otherTodo);
        Assert.assertEquals (1, todoList.search (calendarDate).get (0).getChildTodo ().size ());
        Assert.assertTrue (todoList.search (calendarDate).contains (otherTodo1));
        todoList.search (calendarDate).get (0).getChildTodo ().remove (viewTodo);
        Assert.assertEquals (0, todoList.search (calendarDate).get (0).getChildTodo ().size ());
        Assert.assertTrue (todoList.search (calendarDate).contains (otherTodo1));
        todoList.delete (otherTodo1);
        Assert.assertFalse (todoList.search (calendarDate).contains (otherTodo1));
    }
}
