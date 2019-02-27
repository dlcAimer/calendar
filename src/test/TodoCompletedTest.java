package test;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;

import calendar.*;

import static org.junit.Assert.*;

public class TodoCompletedTest {
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
    public void completeChild() throws Exception {
        //todo 事项随时间改变而改变，维护的状态可能发生改变，上交时间为成功的测试
        todoList.add (otherTodo1);
        todoList.addChild (otherTodo1, meetTodo);
        todoList.addChild (otherTodo1, courseTodo);
        todoList.addChild (otherTodo1, otherTodo);
        todoList.addChild (otherTodo1, viewTodo);
        otherTodo1.stateMaintenance (false);
        Assert.assertEquals (1, otherTodo1.getMode ());
        meetTodo.stateMaintenance (true);
        Assert.assertEquals (3, meetTodo.getMode ());
        courseTodo.stateMaintenance (true);
        Assert.assertEquals (1, courseTodo.getMode ());
        otherTodo.stateMaintenance (true);
        Assert.assertEquals (3, otherTodo.getMode ());
        viewTodo.stateMaintenance (true);
        Assert.assertEquals (3, viewTodo.getMode ());
        otherTodo1.stateMaintenance (true);
        Assert.assertEquals (1, otherTodo1.getMode ());
    }

    @Test
    public void completeTodo() throws Exception {
        todoList.add (otherTodo1);
        todoList.addChild (otherTodo1, meetTodo);
        todoList.addChild (otherTodo1, courseTodo);
        todoList.addChild (otherTodo1, otherTodo);
        todoList.addChild (otherTodo1, viewTodo);
        otherTodo1.stateMaintenance (false);
        Assert.assertEquals (1, otherTodo1.getMode ());
        meetTodo.stateMaintenance (true);
        Assert.assertEquals (3, meetTodo.getMode ());
        courseTodo.stateMaintenance (true);
        Assert.assertEquals (1, courseTodo.getMode ());
        otherTodo.stateMaintenance (true);
        Assert.assertEquals (3, otherTodo.getMode ());
        viewTodo.stateMaintenance (true);
        Assert.assertEquals (3, viewTodo.getMode ());
        otherTodo1.stateMaintenance (true);
        Assert.assertEquals (1, otherTodo1.getMode ());

    }
}
