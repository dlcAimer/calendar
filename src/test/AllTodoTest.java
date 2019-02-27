package test;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After;

import java.util.GregorianCalendar;

import calendar.*;

import static org.junit.Assert.*;

/** 
* Todo Tester. 
* 
* @author <Authors name> 
* @since <pre>���� 14, 2018</pre> 
* @version 1.0 
*/ 
public class AllTodoTest {
    //todo 对每个子todo类进行一个test判断
    CalendarDate date,date1;
    CalendarInterval interval,interval1,interval2;
    @Before
    public void setUp() throws Exception {
        System.out.println("Class Todo tests begin! Good luck!");
        date=new CalendarDate("2018-5-15");
        date1 = new CalendarDate("2018-6-19");
        interval = new CalendarInterval(2018,4,21,12,0,2018,5,23,11,0);
        interval1 = new CalendarInterval(2018,4,22,12,0,2018,5,23,11,0);
        interval2 = new CalendarInterval(2018,4,22,12,0,2018,5,29,11,0);

    }

    @After
    public void tearDown() throws Exception {
        System.out.println("Class Todo tests end! Are you satisfied?");
    }

/**
* 
* Method: getContent() 
* 
*/ 
@Test
public void testOther() throws Exception {
//TODO: Test goes here...
    OtherTodo todo = new OtherTodo("Get up",false,false);
    OtherTodo todo1 = new OtherTodo("sleep",interval,false,false);

    Assert.assertEquals("Get up", todo.getContent());
    Assert.assertEquals(true,todo.getPersistance());
    todo.setContent("What");
    Assert.assertEquals("What",todo.getContent());
    todo.stateMaintenance(true,new GregorianCalendar(2018,4,14));
    Assert.assertEquals(2,todo.getMode());
    Assert.assertEquals("Mon May 14 00:00:00 CST 2018",todo.getCompleteTime().getTime().toString());
    Assert.assertEquals(false,todo1.getPersistance());
    todo1.stateMaintenance(true,new GregorianCalendar());
    Assert.assertEquals (1, todo1.getMode ());

} 


@Test
public void testMeet() throws Exception {
//TODO: Test goes here...
    MeetTodo meetTodo = new MeetTodo("content",date,"location","detail",false,false);
    Assert.assertEquals("location",meetTodo.getLocation());
    Assert.assertEquals("detail",meetTodo.getDetail());
}


@Test
public void testTrip() throws Exception {
//TODO: Test goes here...
    TripTodo tripTodo = new TripTodo("content",date,"traffic","location","B612",false,false);
    Assert.assertEquals("traffic",tripTodo.getTraffic());
    Assert.assertEquals("location",tripTodo.getLocation());
    Assert.assertEquals("B612",tripTodo.getTrainNum());
}


@Test
public void testDate() throws Exception {
//TODO: Test goes here...
    DateTodo dateTodo = new DateTodo("content of date",date,"location of date","xjh",false,false);
    Assert.assertEquals("content of date",dateTodo.getContent());
    Assert.assertEquals("location of date",dateTodo.getLocation());
    Assert.assertEquals("xjh",dateTodo.getPeople());
}



@Test
public void testCourse() throws Exception {
//TODO: Test goes here...  这里对countRealDay函数进行了测试，为满足显示课程除第一天之外真实日程的需求
    CourseTodo courseTodo = new CourseTodo("Math",date,12,25,14,11,
            "detail of Math","location of Math",20,"teacher of Math","tips",
            3,false,false);
    Assert.assertEquals(12,courseTodo.getStartHour());
    Assert.assertEquals(25,courseTodo.getStartMinute());
    Assert.assertEquals(14,courseTodo.getEndHour());
    Assert.assertEquals(11,courseTodo.getEndMinute());
    Assert.assertEquals("detail of Math",courseTodo.getDetail());
    Assert.assertEquals("location of Math",courseTodo.getLocation());
    Assert.assertEquals(20,courseTodo.getLastWeek());
    Assert.assertEquals(3,courseTodo.getW());
    Assert.assertEquals("tips",courseTodo.getTip());
    Assert.assertEquals("teacher of Math",courseTodo.getTeacher());
    //System.out.println(courseTodo.countRealDay().size());
}

@Test
public void testMemory() throws Exception {
    MemoryTodo memoryTodo = new MemoryTodo ("Memory content", date1, "结婚纪念日", "xjh", false, true);
    Assert.assertEquals ("结婚纪念日", memoryTodo.getType ());
    Assert.assertEquals ("xjh", memoryTodo.getName ());
}


@Test
public void testView() throws Exception {
    ViewTodo viewTodo = new ViewTodo("View content",interval1,"location of view","company","post",false,false);
    Assert.assertEquals("location of view",viewTodo.getLocation());
    Assert.assertEquals("company",viewTodo.getCompany());
    Assert.assertEquals("post",viewTodo.getPost());
}


} 
