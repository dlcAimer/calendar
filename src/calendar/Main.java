package calendar;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import sun.security.jca.GetInstance;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


/*
* Start here!
*
* */
public class Main {
    public static void main(String[] args) throws IntervalException {
        /*
        System.out.println(DateUtil.getCurrentYear());
        System.out.println(DateUtil.getCurrentMonth());
        System.out.println(DateUtil.getCurrentDay());
        */
        //todo  We will run this class to test your codes.
        Display display = new Display();
//        FileWriter writer = null;
        /*try {
            TodoList todoList1 = new TodoList();
            CalendarDate calendarDate = new CalendarDate("2018-10-2");
            CalendarDate calendarDate1 = new CalendarDate("2018-10-4");
            GregorianCalendar calendar = new GregorianCalendar(2018,9,1,12,30);
            GregorianCalendar calendar1 = new GregorianCalendar(2018,9,3,14,30);
            CalendarInterval calendarInterval = new CalendarInterval(calendar,calendar1);
            Todo todo = new MeetTodo("meeting",calendarInterval,"company","detail",false,false);
            Todo todo1 = new DateTodo("Date",calendarDate,"home","xjh",false,false);
            Todo todo2 = new MeetTodo("small meeting",calendarDate,"company","detail",false,false);
            Todo todo3 = new MemoryTodo("Memory",calendarDate,"home?","xxx",false,false);
            todo.addChildTodo(todo2);
            todo3.addChildTodo(todo1);
//            todoList1.add(todo);
//            todoList1.add(todo3);
//            todoList1.saveTodoList();
            //todoList1.saveMTodoList();
            todoList1.loadTodoList();
            todoList1.loadMTodoList();
            //System.out.println(todoList1.search(new CalendarInterval(calendar,calendar1)).size());
            //System.out.println(todoList1.search(new CalendarDate("2018-10-1")).size());
        }catch (FormatException e){
            System.out.println(e);
        }
        catch (IntervalException e2){
            System.out.println(e2);
        }*/


    }
}
