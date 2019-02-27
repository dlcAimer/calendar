package test;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import java.util.GregorianCalendar;
import java.util.List;

import calendar.*;

import static org.junit.Assert.*;

/**
 * Todo Tester.
 *
 * @author <Authors name>
 * @since <pre>���� 14, 2018</pre>
 * @version 1.0
 */

public class TodoTest {
    //todo todo中的关于content、childtodo与operate操作的测试
    CalendarDate date,date1;
    CalendarInterval interval,interval1,interval2;
    Todo todo,todo1,todo2,todo3,todo4;
    @Before
    public void setUp() throws Exception {
        System.out.println("Class Todo tests begin! Good luck!");
        date=new CalendarDate("2018-6-15");
        date1 = new CalendarDate("2018-6-19");
        interval = new CalendarInterval(2018,4,21,12,0,2018,4,23,11,0);
        interval1 = new CalendarInterval(2018,4,22,12,0,2018,4,23,11,0);
        interval2 = new CalendarInterval(2018,4,22,12,0,2018,5,29,11,0);
        todo = new Todo(1,"todo",date,false,false);
        todo1 = new Todo(1,"todo1",false,false);
        todo2 = new Todo(1,"todo2",date,false,false);
        todo3 = new Todo(1,"todo3",interval,false,false);
        todo4 = new Todo(1,"todo4",interval2,false,false);
    }

    @After
    public void tearDown() throws Exception {
        System.out.println("Class Todo tests end! Are you satisfied?");
    }

    @Test
    public void testContent(){
        Assert.assertEquals("todo",todo.getContent());
        Assert.assertEquals("todo1",todo1.getContent());
        Assert.assertEquals("todo2",todo2.getContent());
        todo.setContent("set todo");
        todo1.setContent("set todo1");
        todo2.setContent("set todo2");
        Assert.assertEquals("set todo",todo.getContent());
        Assert.assertEquals("set todo1",todo1.getContent());
        Assert.assertEquals("set todo2", todo2.getContent());
    }

    @Test
    public void testChildTodo(){
        todo.addChildTodo(todo1);
        todo.addChildTodo(todo2);
        List<Todo> todos = todo.getChildTodo();
        Assert.assertEquals("todo1",todos.get(0).getContent());
        Assert.assertEquals("todo2",todos.get(1).getContent());
        todo.deleteChildTodo(todo2);
        Assert.assertEquals(1,todos.size());
        Assert.assertEquals("todo1",todos.get(0).getContent());

    }

    @Test
    public void testOperate() throws Exception {
        Assert.assertEquals(3,todo3.getMode());
        todo3.operate(false);
        todo4.operate(false);
        todo.addChildTodo(todo4);
        todo1.addChildTodo(todo4);
        todo.operate(false);
        todo1.operate(false);
        Assert.assertEquals(1,todo.getMode());
        Assert.assertEquals(1,todo1.getMode());
        Assert.assertEquals(1,todo4.getMode());
        todo4.operate(true);
        todo3.operate(true);
        Assert.assertEquals(3,todo3.getMode());
        Assert.assertEquals(2,todo4.getMode());
        todo1.operate(true);
        Assert.assertEquals(2,todo1.getMode());
    }

    @Test
    public void testUrgencyImportance(){
        Assert.assertEquals(false,todo.getUrgency());
        Assert.assertEquals(false,todo1.getImportance());
    }


}
