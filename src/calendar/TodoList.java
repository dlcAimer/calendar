package calendar;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.sun.org.apache.bcel.internal.generic.Type;

import javax.swing.*;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Proxy;
import java.util.*;

class Point implements Serializable {
    int month,day;

    Point(int month,int day){
        this.month = month;
        this.day = day;
    }

    @Override
    public String toString() {
        return "" + month + "-" + day;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Point) {
            Point point = (Point) obj;
            return point.month == month && point.day == day;
        }

        return false;
    }
}

public class TodoList implements Serializable {
    private Map<Long,ArrayList<Todo>> todoList;
    private Map<String, ArrayList<Todo>> mTodoList;

    public TodoList(){
        todoList=new HashMap<Long, ArrayList<Todo>>();
        mTodoList = new HashMap<String, ArrayList<Todo>>();
    }

    private void addHandler(long startKey,long endKey,Todo todo){
        for(long i=startKey; i<=endKey; i++){
            if(todoList.containsKey(i)){
                todoList.get(i).add(todo);
            }else {
                ArrayList<Todo> array=new ArrayList<>();
                array.add(todo);
                todoList.put(i, array);
            }
        }
    }

    //在新建父级事件时使用
    public int add(Todo todo) throws IntervalException {
        for (Map.Entry<Long, ArrayList<Todo>> entry:todoList.entrySet()
                ) {
            ArrayList<Todo> arrayList = entry.getValue();
            for (Todo temp : arrayList) {
                if (temp.equals(todo) || todo.equals(temp)) {
                    //JOptionPane.showMessageDialog(null, "事件已存在");
                    return 0;
                }
            }
        }
        for (Map.Entry<Long, ArrayList<Todo>> entry : todoList.entrySet()
                ) {
            ArrayList<Todo> arrayList = entry.getValue();
            ArrayList<CalendarInterval> courseToAdd = new ArrayList<>();
            ArrayList<CalendarInterval> courseAlreadyHave = new ArrayList<>();
            if(todo.getFlag () == 2 || todo.getFlag () == 3 || todo.getFlag () == 4 ||
                    todo.getFlag () == 6 || todo.getFlag () == 7) {
                if(todo.getFlag() == 7){
                    if(todo instanceof CourseTodo){
                        CourseTodo courseTodo = (CourseTodo) todo;
                        courseToAdd = courseTodo.countRealDay();
                    }
                }
                for (Todo temp : arrayList) {
                    if(temp.getFlag () == 2 || temp.getFlag () == 3 || temp.getFlag () == 4 ||
                            temp.getFlag () == 6 || temp.getFlag () == 7){
                        if(todo.getFlag() != 7) {
                            if(temp.getFlag() != 7) {
                                if (temp.getInterval().isOverlapped(todo.getInterval())) {
                                    overlapAlert(temp.getInterval(), todo.getInterval(), temp.getTypeName() + "待办事项");
                                    //此处等待qyy修改，进行alert，提示时间重叠信息
                                    return -1;
                                }
                            } else{
                                if(temp instanceof CourseTodo){
                                    CourseTodo courseTodo = (CourseTodo) temp;
                                    courseAlreadyHave = courseTodo.countRealDay();
                                }
                                for (CalendarInterval aCourseAlreadyHave : courseAlreadyHave) {
                                    if (aCourseAlreadyHave.isOverlapped(todo.getInterval())) {
                                        overlapAlert(aCourseAlreadyHave, todo.getInterval(), temp.getTypeName() + "待办事项");
                                        //此处等待qyy修改，进行alert，提示时间重叠信息
                                        return -1;
                                    }
                                }
                            }
                        }else {
                            if(temp.getFlag() != 7) {
                                for (CalendarInterval aCourseToAdd : courseToAdd) {
                                    if (temp.getInterval().isOverlapped(aCourseToAdd)) {
                                        overlapAlert(temp.getInterval(), aCourseToAdd, temp.getTypeName() + "待办事项");
                                        //此处等待qyy修改，进行alert，提示时间重叠信息
                                        return -1;
                                    }
                                }
                            } else{
                                if(temp instanceof CourseTodo){
                                    CourseTodo courseTodo = (CourseTodo) temp;
                                    courseAlreadyHave = courseTodo.countRealDay();
                                }
                                for (CalendarInterval aCourseAlreadyHave : courseAlreadyHave) {
                                    for (CalendarInterval aCourseToAdd : courseToAdd) {
                                        if (aCourseAlreadyHave.isOverlapped(aCourseToAdd)) {
                                            overlapAlert(aCourseAlreadyHave, aCourseToAdd, temp.getTypeName() + "待办事项");
                                            //此处等待qyy修改，进行alert，提示时间重叠信息
                                            return -1;
                                        }
                                    }
                                }
                            }
                        }
                    }

                    //也要对每个事件的子事件进行判断
                    if (temp.getChildTodo ().size () != 0) {
                        for (Todo ttemp : temp.getChildTodo ()) {
                            if (ttemp.getFlag () == 2 || ttemp.getFlag () == 3 || ttemp.getFlag () == 4 || ttemp.getFlag () == 6) {
                                if (todo.getFlag () != 7) {
                                    if (ttemp.getInterval ().isOverlapped (todo.getInterval ())) {
                                        overlapAlert (ttemp.getInterval (), todo.getInterval (), ttemp.getTypeName () + "待办事项");
                                        //此处等待qyy修改，进行alert，提示时间重叠信息
                                        return -1;
                                    }
                                } else {
                                    for (CalendarInterval aCourseToAdd : courseToAdd) {
                                        if (ttemp.getInterval ().isOverlapped (aCourseToAdd)) {
                                            overlapAlert (ttemp.getInterval (), aCourseToAdd, ttemp.getTypeName () + "待办事项");
                                            //此处等待qyy修改，进行alert，提示时间重叠信息
                                            return -1;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }if(todo.getFlag () == 5) {
            for(int i =todo.getDate().getYear();i<=2300;i++){
                CalendarDate newDate = new CalendarDate(i,todo.getDate().getMonth(),todo.getDate().getDay());
                MemoryTodo memoryTodo = new MemoryTodo(todo.getContent(),newDate,((MemoryTodo)todo).getType()
                        ,((MemoryTodo)todo).getName(),todo.getUrgency(),todo.getImportance());
                CalendarDate cDate=memoryTodo.getDate();
                GregorianCalendar date=new GregorianCalendar(cDate.getYear(), cDate.getMonth()-1, cDate.getDay());
                if(todoList.containsKey((date.getTimeInMillis()-57600000L)/86400000L)){
                    todoList.get((date.getTimeInMillis()-57600000L)/86400000L).add(memoryTodo);
                }else {
                    ArrayList<Todo> array=new ArrayList<>();
                    array.add(memoryTodo);
                    todoList.put((date.getTimeInMillis()-57600000L) / 86400000L, array);
                }
            }
        }else if(todo instanceof OtherTodo){
            OtherTodo otherTodo = (OtherTodo) todo;
            if(otherTodo.getPersistance()){
                if (todoList.containsKey(-1000000000L)) {
                    todoList.get(-1000000000L).add(todo);
                }else {
                    ArrayList<Todo> array=new ArrayList<>();
                    array.add(todo);
                    todoList.put(-1000000000L, array);
                }
            }else {
                long startKey=(todo.getInterval().getStartTime().getTimeInMillis()-57600000L)/86400000L;
                long endKey=(todo.getInterval().getEndTime().getTimeInMillis()-57600000L)/86400000L;
                addHandler(startKey,endKey,todo);
            }
        }else if (todo instanceof CourseTodo){
            CourseTodo courseTodo = (CourseTodo) todo;
            ArrayList<CalendarInterval> courseToAdd = new ArrayList<>();
            courseToAdd = courseTodo.countRealDay();
            for (CalendarInterval aCourseToAdd : courseToAdd) {
                CourseTodo temp = new CourseTodo(todo.getContent(), todo.getDate(), ((CourseTodo) todo).getStartHour()
                        , ((CourseTodo) todo).getStartMinute(), ((CourseTodo) todo).getEndHour(), ((CourseTodo) todo).getEndMinute(),
                        ((CourseTodo) todo).getDetail(), ((CourseTodo) todo).getLocation(), ((CourseTodo) todo).getLastWeek(),
                        ((CourseTodo) todo).getTeacher(), ((CourseTodo) todo).getTip(), ((CourseTodo) todo).getW()
                        , todo.getUrgency(), todo.getImportance());
                temp.setInterval(aCourseToAdd);
                long startKey=(temp.getInterval().getStartTime().getTimeInMillis()-57600000L)/86400000L;
                long endKey=(temp.getInterval().getEndTime().getTimeInMillis()-57600000L)/86400000L;
                addHandler(startKey, endKey, temp);
            }
        } else if(!todo.isUsingInterval()) {
            CalendarDate cDate=todo.getDate();
            GregorianCalendar date=new GregorianCalendar(cDate.getYear(), cDate.getMonth()-1, cDate.getDay());
            if(todoList.containsKey((date.getTimeInMillis()-57600000L)/86400000L)){
                todoList.get((date.getTimeInMillis()-57600000L)/86400000L).add(todo);
            }else {
                ArrayList<Todo> array=new ArrayList<>();
                array.add(todo);
                todoList.put((date.getTimeInMillis()-57600000L) / 86400000L, array);
            }
        }
        else{
            long startKey=(todo.getInterval().getStartTime().getTimeInMillis()-57600000L)/86400000L;
            long endKey=(todo.getInterval().getEndTime().getTimeInMillis()-57600000L)/86400000L;
            addHandler(startKey,endKey,todo);
        }

        return 0;
    }

    private void overlapAlert(CalendarInterval a, CalendarInterval b, String todoName) {
        GregorianCalendar startTime = (a.getStartTime().after(b.getStartTime())) ?
                a.getStartTime() : b.getStartTime();
        GregorianCalendar endTime = (a.getEndTime().before(b.getEndTime())) ?
                a.getEndTime() : b.getEndTime();

        JOptionPane.showMessageDialog(null, "与" + todoName + "在" +
                DateUtil.calendarToString(startTime) + "和" + DateUtil.calendarToString(endTime) + "之间发生时间重叠");
    }

    public int addChild(Todo parent, Todo child) throws IntervalException {
        if (!child.isUsingInterval () || child.getHasParent ()) {
            //1代表错误类型一，父类型没有使用时间段或是子类型没有使用时间段,这里不允许一个事件有两个父事件
            JOptionPane.showMessageDialog (null, "子类型没有使用时间段");
            return 1;
        }
        if(parent.getHasParent () || child.getChildTodo ().size () != 0){
            //2代表错误类型二，事件嵌套
            JOptionPane.showMessageDialog(null, "事件嵌套");
            return 2;
        }
        if (parent.getFlag () == 1) {
            OtherTodo otherTodo = (OtherTodo) parent;
            if (!otherTodo.getPersistance ()) {
                if (!parent.getInterval ().isInclude (child.getInterval ())) {
                    //4代表错误类型四，时间不包含
                    JOptionPane.showMessageDialog (null, "时间不包含");
                    return 4;
                }
            }
        } else {
            if (!parent.getInterval ().isInclude (child.getInterval ())) {
                //4代表错误类型四，时间不包含
                JOptionPane.showMessageDialog (null, "时间不包含");
                return 4;
            }
        }
        /*long startKey = (child.getInterval ().getStartTime ().getTimeInMillis () - 57600000L) / 86400000L;
        long endKey = (child.getInterval ().getEndTime ().getTimeInMillis () - 57600000L) / 86400000L;
        for (long i = startKey; i <= endKey; i++) {
            if (todoList.containsKey (i)) {
                todoList.get (i).add (child);
            } else {
                ArrayList<Todo> array = new ArrayList<> ();
                array.add (child);
                todoList.put (i, array);
            }
        }*/
        int parentFlag = parent.getFlag ();
        int childFlag = child.getFlag ();
        if(parentFlag != childFlag &&
                (parentFlag == 2 || parentFlag == 3 || parentFlag == 6 || parentFlag == 7)
                && (childFlag == 2 || childFlag == 3 || childFlag == 6 || childFlag == 7)){
            //3代表错误类型三，事件互斥
            JOptionPane.showMessageDialog(null, "事件互斥");
            return 3;
        }
        //也要对每个事件的子事件进行判断
        if (parent.getChildTodo ().size () != 0) {
            for (Todo ttemp : parent.getChildTodo ()) {
                if (ttemp.getFlag () == 2 || ttemp.getFlag () == 3 || ttemp.getFlag () == 4 || ttemp.getFlag () == 6) {
                    if (ttemp.getInterval ().isOverlapped (child.getInterval ())) {
                        overlapAlert (ttemp.getInterval (), child.getInterval (), ttemp.getTypeName () + "待办事项");
                        //此处等待qyy修改，进行alert，提示时间重叠信息
                        return -1;
                    }
                }
            }
        }

        for (Map.Entry<Long, ArrayList<Todo>> entry : todoList.entrySet ()) {
            ArrayList<Todo> arrayList = entry.getValue ();
            for (Todo temp : arrayList) {
                if (temp.equals (child) || child.equals (temp)) {
                    JOptionPane.showMessageDialog (null, "事件已存在");
                    return -1;
                }
            }
        }
        for (Map.Entry<Long, ArrayList<Todo>> entry : todoList.entrySet ()) {
            ArrayList<Todo> arrayList = entry.getValue ();
            ArrayList<CalendarInterval> courseAlreadyHave = new ArrayList<> ();
            if (child.getFlag () == 2 || child.getFlag () == 3 || child.getFlag () == 4 || child.getFlag () == 6) {
                for (Todo temp : arrayList) {
                    if (temp.getFlag () == 2 || temp.getFlag () == 3 || temp.getFlag () == 4 || temp.getFlag () == 6 || temp.getFlag () == 7) {
                        if (temp.getFlag () != 7) {
                            if (temp.getInterval ().isOverlapped (child.getInterval ())) {
                                overlapAlert (temp.getInterval (), child.getInterval (), temp.getTypeName () + "待办事项");
                                //此处等待qyy修改，进行alert，提示时间重叠信息
                                return -1;
                            }
                        } else {
                            if (temp instanceof CourseTodo) {
                                CourseTodo courseTodo = (CourseTodo) temp;
                                courseAlreadyHave = courseTodo.countRealDay ();
                            }
                            for (CalendarInterval aCourseAlreadyHave : courseAlreadyHave) {
                                if (aCourseAlreadyHave.isOverlapped (child.getInterval ())) {
                                    overlapAlert (aCourseAlreadyHave, child.getInterval (), temp.getTypeName () + "待办事项");
                                    //此处等待qyy修改，进行alert，提示时间重叠信息
                                    return -1;
                                }
                            }
                        }
                    }
                }
            }
        }
        parent.addChildTodo (child);
        child.changeHasParent (true);
        return 0;
    }

    public ArrayList<Todo> search(CalendarInterval interval) throws IntervalException {
        ArrayList<Todo> result = new ArrayList<>();
        long startKey = (interval.getStartTime().getTimeInMillis() - 57600000L) / 86400000L;
        long endKey = (interval.getEndTime().getTimeInMillis() - 57600000L) / 86400000L;

        Calendar now = Calendar.getInstance();
        CalendarDate date = new CalendarDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH));
        if (interval.isOverlapped(date)) {
            if (todoList.containsKey(-1000000000L)) {
                for (int i = 0; i < todoList.get(-1000000000L).size(); i++) {
                    if (todoList.get(-1000000000L).get(i).getMode() == 1) {
                        result.add(todoList.get(-1000000000L).get(i));
                    }
                    if (todoList.get(-1000000000L).get(i).getMode() == 2) {
                        OtherTodo otherTodo = (OtherTodo) todoList.get(-1000000000L).get(i);
                        if (((otherTodo.getCompleteTime().getTimeInMillis() - 57600000L) / 86400000L) >= startKey &&
                                ((otherTodo.getCompleteTime().getTimeInMillis() - 57600000L) / 86400000L) <= endKey) {
                            result.add(todoList.get(-1000000000L).get(i));
                        }
                    }
                }
            }
        } else {
            for (int i = 0; i < todoList.get(-1000000000L).size(); i++) {
                OtherTodo otherTodo = (OtherTodo) this.todoList.get(-1000000000L).get(i);
                if (otherTodo.getCompleteTime() != null) {
                    CalendarDate key = new CalendarDate(otherTodo.getCompleteTime().get(Calendar.YEAR), otherTodo.getCompleteTime().get(Calendar.MONTH) + 1, otherTodo.getCompleteTime().get(Calendar.DAY_OF_MONTH));
                    if (interval.isOverlapped(key)) {
                        if (todoList.get(-1000000000L).get(i).getMode() == 2) {
                            result.add(todoList.get(-1000000000L).get(i));
                        }
                    }
                }
            }
        }
        for (long i = startKey; i <= endKey; i++) {
            if (todoList.containsKey(i)) {
                todoList.get(i).forEach(ele -> {
                    boolean dateOverlap = (!ele.isUsingInterval()) && interval.isOverlapped(ele.getDate());
                    boolean courseOverlap = (ele instanceof CourseTodo) && interval.isOverlapped(ele.getInterval());
                    boolean intervalOverlap = ele.isUsingInterval() && ele.getInterval().isOverlapped(interval);
                    if ((dateOverlap || intervalOverlap || courseOverlap) && !result.contains(ele)) {
                        result.add(ele);
                    }
                });
            }
        }
        Comparator c = new Comparator<Todo>() {
            @Override
            public int compare(Todo o1, Todo o2) {
                // TODO Auto-generated method stub
                int temp1 = 0;
                if(o1.getImportance()){
                    temp1 += 1;
                }
                if(o1.getUrgency()){
                    temp1 += 1;
                }
                int temp2 = 0;
                if(o2.getImportance()){
                    temp2 += 1;
                }
                if(o2.getUrgency()){
                    temp2 += 1;
                }
                if(temp1<temp2)
                    return 1;
                    //注意！！返回值必须是一对相反数，否则无效。jdk1.7以后就是这样。
                    //      else return 0; //无效
                else return -1;
            }
        };
        Collections.sort(result,c);
        return result;
    }

    public ArrayList<Todo> search(CalendarDate date) throws IntervalException {
        GregorianCalendar gDate = new GregorianCalendar(date.getYear(), date.getMonth() - 1, date.getDay());
        Calendar now = Calendar.getInstance();
        ArrayList<Todo> result = new ArrayList<>();

        if (date.getYear() == now.get(Calendar.YEAR) && date.getMonth() - 1 == now.get(Calendar.MONTH)
                && date.getDay() == now.get(Calendar.DAY_OF_MONTH)) {
            if (todoList.containsKey(-1000000000L)) {
                for (int i = 0; i < todoList.get(-1000000000L).size(); i++) {
                    if (todoList.get(-1000000000L).get(i).getMode() == 1) {
                        result.add(todoList.get(-1000000000L).get(i));
                    }
                }
            }
        }
        GregorianCalendar temp1 = new GregorianCalendar(date.getYear(), date.getMonth() - 1, date.getDay(), 0, 0);
        GregorianCalendar temp2 = new GregorianCalendar(date.getYear(), date.getMonth() - 1, date.getDay(), 23, 59);
        long startKey = (temp1.getTimeInMillis() - 57600000L) / 86400000L;
        long endKey = (temp2.getTimeInMillis() - 57600000L) / 86400000L;
        if (todoList.containsKey(-1000000000L)) {
            for (int i = 0; i < todoList.get(-1000000000L).size(); i++) {
                if (todoList.get(-1000000000L).get(i).getMode() == 2) {
                    OtherTodo otherTodo = (OtherTodo) todoList.get(-1000000000L).get(i);
                    if (((otherTodo.getCompleteTime().getTimeInMillis() - 57600000L) / 86400000L) >= startKey &&
                            ((otherTodo.getCompleteTime().getTimeInMillis() - 57600000L) / 86400000L) <= endKey) {
                        result.add(todoList.get(-1000000000L).get(i));
                    }
                }
            }
        }
        Comparator c = new Comparator<Todo>() {
            @Override
            public int compare(Todo o1, Todo o2) {
                // TODO Auto-generated method stub
                int temp1 = 0;
                if(o1.getImportance()){
                    temp1 += 1;
                }
                if(o1.getUrgency()){
                    temp1 += 1;
                }
                int temp2 = 0;
                if(o2.getImportance()){
                    temp2 += 1;
                }
                if(o2.getUrgency()){
                    temp2 += 1;
                }
                if(temp1<temp2)
                    return 1;
                    //注意！！返回值必须是一对相反数，否则无效。jdk1.7以后就是这样。
                    //      else return 0; //无效
                else return -1;
            }
        };
        if (todoList.containsKey((gDate.getTimeInMillis() - 57600000L) / 86400000L) && !todoList.get((gDate.getTimeInMillis() - 57600000L) / 86400000L).isEmpty()) {
            result.addAll(todoList.get((gDate.getTimeInMillis() - 57600000L) / 86400000L));
            Collections.sort(result,c);
            return result;
        }
        Collections.sort(result,c);
        return result;
    }

    public boolean delete(Todo todo) throws IntervalException {
        if(todo instanceof MemoryTodo){
            ArrayList<MemoryTodo> memoryTodos = new ArrayList<>();
            for(int i =1800;i<=2300;i++){
                CalendarDate newDate = new CalendarDate(i,todo.getDate().getMonth(),todo.getDate().getDay());
                MemoryTodo memoryTodo = new MemoryTodo(todo.getContent(),newDate,((MemoryTodo)todo).getType()
                        ,((MemoryTodo)todo).getName(),todo.getUrgency(),todo.getImportance());
                memoryTodos.add(memoryTodo);
            }
            int count =0;
            int earlistYear = 2301;
            for(int i = 0;i<memoryTodos.size();i++) {
                CalendarDate cDate = memoryTodos.get(i).getDate();
                GregorianCalendar date = new GregorianCalendar(cDate.getYear(), cDate.getMonth() - 1, cDate.getDay());
                if (todoList.containsKey((date.getTimeInMillis() - 57600000L) / 86400000L)) {
                    for(int j =todoList.get((date.getTimeInMillis() - 57600000L) / 86400000L).size()-1;j>=0;j--){
                        if(memoryTodos.get(i).equals(todoList.get((date.getTimeInMillis() - 57600000L) / 86400000L).get(j))){
                            if(memoryTodos.get(i).getDate().getYear()<earlistYear){
                                earlistYear = memoryTodos.get(i).getDate().getYear();
                            }
                            todoList.get((date.getTimeInMillis() - 57600000L) / 86400000L).remove(j);
                            count++;
                        }
                    }
                }
            }
            if(count == 2301-earlistYear){
                return true;
            } else {
                return false;
            }
        }
        if(todo instanceof OtherTodo) {
            OtherTodo otherTodo = (OtherTodo) todo;
            if (otherTodo.getPersistance()) {
                return deleteInArray(todoList.get(-1000000000L), todo);
            }
        }
        if(todo instanceof CourseTodo){
            boolean result = true;
            CourseTodo courseTodo = (CourseTodo) todo;
            ArrayList<CalendarInterval> courseToRemove = new ArrayList<>();
            courseToRemove = courseTodo.countRealDay();
            for (CalendarInterval aCourseToRemove : courseToRemove) {
                todo.setInterval(aCourseToRemove);
                long startKey=(todo.getInterval().getStartTime().getTimeInMillis()-57600000L)/86400000L;
                long endKey=(todo.getInterval().getEndTime().getTimeInMillis()-57600000L)/86400000L;
                for (long i = startKey; i <= endKey; i++) {
                    if(!deleteInArray(todoList.get(i), todo)){
                        result = false;
                    }
                }
            }
            return result;
        }else {
            if (todo.isUsingInterval()) {
                boolean result = false;
                long startKey = (todo.getInterval().getStartTime().getTimeInMillis() - 57600000L) / 86400000L;
                long endKey = (todo.getInterval().getEndTime().getTimeInMillis() - 57600000L) / 86400000L;
                for (long i = startKey; i <= endKey; i++) {
                    result = deleteInArray(todoList.get(i), todo);
                }

                return result;
            } else {
                ArrayList<Todo> array = search(todo.getDate());
                for (Todo temp1 : array) {
                    if (temp1.getChildTodo().size() != 0) {
                        for (Todo temp2 : temp1.getChildTodo()) {
                            temp2.changeHasParent(false);
                        }
                    }
                }
                return deleteInArray(array, todo);
            }
        }
    }

    public Map<Long, ArrayList<Todo>> getTodoList() {
        return todoList;
    }

    private boolean deleteInArray(ArrayList<Todo> array, Todo todo){
        if (array != null && array.contains(todo)) {
            array.remove(todo);
            return true;
        }
        else {
            System.out.println("This element doesn't exist");
            return false;
        }
    }


   /* public void loadTodoList(){
        try{
            loadMTodoList();

            JsonParser parser = new JsonParser();
            JsonArray array = (JsonArray) parser.parse(new FileReader("todoList.json"));
            for (int i=0;i<array.size();i++){
                JsonArray jsonArray = array.get(i).getAsJsonArray();
                for (int j=0;j<jsonArray.size();j++){
                    JsonObject todo = array.get(i).getAsJsonArray().get(j).getAsJsonObject();
                    if (todo.get("usingInterval").getAsBoolean()){
                        JsonObject startTime = todo.get("interval").getAsJsonObject().get("startTime").getAsJsonObject();
                        JsonObject endTime = todo.get("interval").getAsJsonObject().get("endTime").getAsJsonObject();
                        CalendarInterval calendarInterval = new CalendarInterval(startTime.get("year").getAsInt(),
                                startTime.get("month").getAsInt(),startTime.get("dayOfMonth").getAsInt(),startTime.
                                get("hourOfDay").getAsInt(),startTime.get("minute").getAsInt(),endTime.get("year").getAsInt(),
                                endTime.get("month").getAsInt(),endTime.get("dayOfMonth").getAsInt(),endTime.
                                get("hourOfDay").getAsInt(),endTime.get("minute").getAsInt());
                        Remind remind1 = null;
                        if (todo.get("reminder") != null) {
                            JsonObject remind = todo.get("reminder").getAsJsonObject();
                            JsonObject startRemindTime = remind.get("startRemindTime").getAsJsonObject();
                            remind1 = new Remind(startRemindTime.get("year").getAsInt(), startRemindTime.get("month").getAsInt(),
                                    startRemindTime.get("dayOfMonth").getAsInt(), startRemindTime.get("hourOfDay").getAsInt(),
                                    startRemindTime.get("minute").getAsInt(), remind.get("remindMode").getAsInt(), remind.get("remindStrategy").getAsInt());
                        }
                        int flag = todo.get ("flag").getAsInt ();
                        int mode = todo.get("mode").getAsInt();
                        JsonArray array1 = todo.get("childTodo").getAsJsonArray();
                        Todo todo1;
                        switch(flag){
                            case 1:
                                todo1 = new OtherTodo (todo.get ("content").getAsString (),calendarInterval,todo.get ("urgency").getAsBoolean (),todo.get ("importance").getAsBoolean ());
                                todo1.setMode(mode);
                                todo1.setReminder(remind1);
                                addChildTodos(todo1,array1);
                                add (todo1);
                                break;
                            case 2:
                                todo1 = new DateTodo (todo.get ("content").getAsString (),calendarInterval,todo.get ("location").getAsString (),
                                        todo.get ("people").getAsString (),todo.get ("urgency").getAsBoolean (),todo.get ("importance").getAsBoolean ());
                                todo1.setMode(mode);
                                todo1.setReminder(remind1);
                                addChildTodos(todo1,array1);
                                add (todo1);
                                break;
                            case 3:
                                todo1 = new MeetTodo (todo.get("content").getAsString (),calendarInterval,todo.get ("location").getAsString (),
                                        todo.get ("detail").getAsString (),todo.get ("urgency").getAsBoolean (),todo.get ("importance").getAsBoolean ());
                                todo1.setMode(mode);
                                todo1.setReminder(remind1);
                                addChildTodos(todo1,array1);
                                add (todo1);
                                break;
                            case 4:
                                todo1 = new TripTodo (todo.get ("content").getAsString (),calendarInterval,todo.get ("traffic").getAsString (),
                                        todo.get ("location").getAsString (),todo.get ("trainNum").getAsString (),todo.get ("urgency").getAsBoolean (),todo.get ("importance").getAsBoolean ());
                                todo1.setMode(mode);
                                todo1.setReminder(remind1);
                                addChildTodos(todo1,array1);
                                add(todo1);
                                break;
                            //case 5 does not exist here
                            case 6:
                                todo1 = new ViewTodo (todo.get ("content").getAsString (),calendarInterval,todo.get ("location").getAsString (),
                                        todo.get ("company").getAsString (),todo.get ("post").getAsString (),todo.get ("urgency").getAsBoolean (),todo.get ("importance").getAsBoolean ());
                                todo1.setMode(mode);
                                todo1.setReminder(remind1);
                                add(todo1);
                                break;
                            //case 7 does not exist here
                        }
                    }
                    else {
                        JsonObject date = todo.get("date").getAsJsonObject();
                        if(date == null){
                            add(new OtherTodo (todo.get ("content").getAsString (),todo.get ("urgency").getAsBoolean (), todo.get ("importance").getAsBoolean ()));
                            return;
                        }
                        CalendarDate calendarDate = new CalendarDate(date.get("year").getAsInt(),date.get("month").getAsInt(),
                                date.get("day").getAsInt());
                        int flag = todo.get ("flag").getAsInt ();
                        Remind remind1 = null;
                        if (todo.get("reminder") != null) {
                            JsonObject remind = todo.get("reminder").getAsJsonObject();
                            JsonObject startRemindTime = remind.get("startRemindTime").getAsJsonObject();
                            remind1 = new Remind(startRemindTime.get("year").getAsInt(), startRemindTime.get("month").getAsInt(),
                                    startRemindTime.get("dayOfMonth").getAsInt(), startRemindTime.get("hourOfDay").getAsInt(),
                                    startRemindTime.get("minute").getAsInt(), remind.get("remindMode").getAsInt(), remind.get("remindStrategy").getAsInt());
                        }
                        int mode = todo.get("mode").getAsInt();
                        Todo todo1;
                        switch(flag) {
                            case 1:
                                todo1 = new OtherTodo (todo.get ("content").getAsString (), calendarDate, todo.get ("urgency").getAsBoolean (), todo.get ("importance").getAsBoolean ());
                                todo1.setMode(mode);
                                todo1.setReminder(remind1);
                                add (todo1);
                                break;
                            case 2:
                                todo1 = new DateTodo (todo.get ("content").getAsString (), calendarDate, todo.get ("location").getAsString (), todo.get ("people").getAsString (), todo.get ("urgency").getAsBoolean (), todo.get ("importance").getAsBoolean ());
                                todo1.setMode(mode);
                                todo1.setReminder(remind1);
                                add (todo1);
                                break;
                            case 3:
                                todo1 = new MeetTodo (todo.get ("content").getAsString (), calendarDate, todo.get ("location").getAsString (), todo.get ("detail").getAsString (), todo.get ("urgency").getAsBoolean (), todo.get ("importance").getAsBoolean ());
                                todo1.setMode(mode);
                                todo1.setReminder(remind1);
                                add (todo1);
                                break;
                            case 4:
                                todo1 = new TripTodo (todo.get ("content").getAsString (), calendarDate, todo.get ("traffic").getAsString (), todo.get ("location").getAsString (), todo.get ("trainNum").getAsString (), todo.get ("urgency").getAsBoolean (), todo.get ("importance").getAsBoolean ());
                                todo1.setMode(mode);
                                todo1.setReminder(remind1);
                                add (todo1);
                                break;
                            case 5:
                                todo1 = new MemoryTodo (todo.get ("content").getAsString (), calendarDate, todo.get ("type").getAsString (), todo.get ("name").getAsString (), todo.get ("urgency").getAsBoolean (), todo.get ("importance").getAsBoolean ());
                                todo1.setMode(mode);
                                todo1.setReminder(remind1);
                                add (todo1);
                                break;
                            case 6:
                                todo1 = new ViewTodo (todo.get ("content").getAsString (), calendarDate, todo.get ("location").getAsString (), todo.get ("company").getAsString (), todo.get ("post").getAsString (), todo.get ("urgency").getAsBoolean (), todo.get ("importance").getAsBoolean ());
                                todo1.setMode(mode);
                                todo1.setReminder(remind1);
                                add (todo1);
                                break;
                            case 7:
                                todo1 = new CourseTodo(todo.get("content").getAsString(), calendarDate, todo.get("startHour").getAsInt(), todo.get("startMinute").getAsInt(), todo.get("endHour").getAsInt(), todo.get("endMinute").getAsInt(), todo.get("detail").getAsString(), todo.get("location").getAsString(), todo.get("lastWeek").getAsInt(), todo.get("teacher").getAsString(), todo.get("tip").getAsString(), todo.get("w").getAsInt(), todo.get("urgency").getAsBoolean(), todo.get("importance").getAsBoolean());
                                todo1.setMode(mode);
                                todo1.setReminder(remind1);
                                add (todo1);
                                break;
                        }
                    }
                }


            }

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public Todo addChildTodos(Todo father, JsonArray array){
        try {
            for (int i=0; i<array.size();i++){
                JsonObject todo = array.get(i).getAsJsonObject();
                if (todo.get("usingInterval").getAsBoolean()){
                    JsonObject startTime = todo.get("interval").getAsJsonObject().get("startTime").getAsJsonObject();
                    JsonObject endTime = todo.get("interval").getAsJsonObject().get("endTime").getAsJsonObject();
                    CalendarInterval calendarInterval = new CalendarInterval(startTime.get("year").getAsInt(),
                            startTime.get("month").getAsInt(),startTime.get("dayOfMonth").getAsInt(),startTime.
                            get("hourOfDay").getAsInt(),startTime.get("minute").getAsInt(),endTime.get("year").getAsInt(),
                            endTime.get("month").getAsInt(),endTime.get("dayOfMonth").getAsInt(),endTime.
                            get("hourOfDay").getAsInt(),endTime.get("minute").getAsInt());
                    int flag = todo.get ("flag").getAsInt ();
                    Todo todo1;
                    Remind remind1 = null;
                    if (todo.get("reminder") != null) {
                        JsonObject remind = todo.get("reminder").getAsJsonObject();
                        JsonObject startRemindTime = remind.get("startRemindTime").getAsJsonObject();
                        remind1 = new Remind(startRemindTime.get("year").getAsInt(), startRemindTime.get("month").getAsInt(),
                                startRemindTime.get("dayOfMonth").getAsInt(), startRemindTime.get("hourOfDay").getAsInt(),
                                startRemindTime.get("minute").getAsInt(), remind.get("remindMode").getAsInt(), remind.get("remindStrategy").getAsInt());
                    }
                    int mode = todo.get("mode").getAsInt();
                    switch(flag){
                        case 1:
                            todo1 = new OtherTodo (todo.get ("content").getAsString (),calendarInterval,todo.get ("urgency").getAsBoolean (),todo.get ("importance").getAsBoolean ());
                            todo1.setMode(mode);
                            todo1.setReminder(remind1);
                            father.addChildTodo(todo1);
                            break;
                        case 2:
                            todo1 = new DateTodo (todo.get ("content").getAsString (),calendarInterval,todo.get ("location").getAsString (),
                                    todo.get ("people").getAsString (),todo.get ("urgency").getAsBoolean (),todo.get ("importance").getAsBoolean ());
                            todo1.setMode(mode);
                            todo1.setReminder(remind1);
                            father.addChildTodo(todo1);
                            break;
                        case 3:
                            todo1 = new MeetTodo (todo.get("content").getAsString (),calendarInterval,todo.get ("location").getAsString (),
                                    todo.get ("detail").getAsString (),todo.get ("urgency").getAsBoolean (),todo.get ("importance").getAsBoolean ());
                            todo1.setMode(mode);
                            todo1.setReminder(remind1);
                            father.addChildTodo (todo1);
                            break;
                        case 4:
                            todo1 = new TripTodo (todo.get ("content").getAsString (),calendarInterval,todo.get ("traffic").getAsString (),
                                    todo.get ("location").getAsString (),todo.get ("trainNum").getAsString (),todo.get ("urgency").getAsBoolean (),todo.get ("importance").getAsBoolean ());
                            todo1.setMode(mode);
                            todo1.setReminder(remind1);
                            father.addChildTodo(todo1);
                            break;
                        //case 5 does not exist here
                        case 6:
                            todo1 = new ViewTodo (todo.get ("content").getAsString (),calendarInterval,todo.get ("location").getAsString (),
                                    todo.get ("company").getAsString (),todo.get ("post").getAsString (),todo.get ("urgency").getAsBoolean (),todo.get ("importance").getAsBoolean ());
                            todo1.setMode(mode);
                            todo1.setReminder(remind1);
                            father.addChildTodo(todo1);
                            break;
                        //case 7 does not exist here
                    }
                }
                else {
                    JsonObject date = todo.get("date").getAsJsonObject();
                    if(date == null){
                        father.addChildTodo(new OtherTodo (todo.get ("content").getAsString (),todo.get ("urgency").getAsBoolean (), todo.get ("importance").getAsBoolean ()));
                        return father;
                    }
                    CalendarDate calendarDate = new CalendarDate(date.get("year").getAsInt(),date.get("month").getAsInt(),
                            date.get("day").getAsInt());
                    int flag = todo.get ("flag").getAsInt ();
                    Todo todo1;
                    int mode = todo.get("mode").getAsInt();
                    Remind remind1 = null;
                    if (todo.get("reminder") != null) {
                        JsonObject remind = todo.get("reminder").getAsJsonObject();
                        JsonObject startRemindTime = remind.get("startRemindTime").getAsJsonObject();
                        remind1 = new Remind(startRemindTime.get("year").getAsInt(), startRemindTime.get("month").getAsInt(),
                                startRemindTime.get("dayOfMonth").getAsInt(), startRemindTime.get("hourOfDay").getAsInt(),
                                startRemindTime.get("minute").getAsInt(), remind.get("remindMode").getAsInt(), remind.get("remindStrategy").getAsInt());
                    }
                    switch(flag) {
                        case 1:
                            todo1 = new OtherTodo(todo.get("content").getAsString(), calendarDate, todo.get("urgency").getAsBoolean(), todo.get("importance").getAsBoolean());
                            todo1.setReminder(remind1);
                            todo1.setMode(mode);
                            father.addChildTodo(todo1);
                            break;
                        case 2:
                            todo1 = new DateTodo(todo.get("content").getAsString(), calendarDate, todo.get("location").getAsString(), todo.get("people").getAsString(), todo.get("urgency").getAsBoolean(), todo.get("importance").getAsBoolean());
                            todo1.setReminder(remind1);
                            todo1.setMode(mode);
                            father.addChildTodo(todo1);
                            break;
                        case 3:
                            todo1 = new MeetTodo(todo.get("content").getAsString(), calendarDate, todo.get("location").getAsString(), todo.get("detail").getAsString(), todo.get("urgency").getAsBoolean(), todo.get("importance").getAsBoolean());
                            todo1.setReminder(remind1);
                            todo1.setMode(mode);
                            father.addChildTodo(todo1);
                            break;
                        case 4:
                            todo1 = new TripTodo(todo.get("content").getAsString(), calendarDate, todo.get("traffic").getAsString(), todo.get("location").getAsString(), todo.get("trainNum").getAsString(), todo.get("urgency").getAsBoolean(), todo.get("importance").getAsBoolean());
                            todo1.setReminder(remind1);
                            todo1.setMode(mode);
                            father.addChildTodo(todo1);
                            break;
                        case 5:
                            todo1 = new MemoryTodo(todo.get("content").getAsString(), calendarDate, todo.get("type").getAsString(), todo.get("name").getAsString(), todo.get("urgency").getAsBoolean(), todo.get("importance").getAsBoolean());
                            todo1.setReminder(remind1);
                            todo1.setMode(mode);
                            father.addChildTodo(todo1);
                        case 6:
                            todo1 = new ViewTodo(todo.get("content").getAsString(), calendarDate, todo.get("location").getAsString(), todo.get("company").getAsString(), todo.get("post").getAsString(), todo.get("urgency").getAsBoolean(), todo.get("importance").getAsBoolean());
                            todo1.setReminder(remind1);
                            todo1.setMode(mode);
                            father.addChildTodo(todo1);
                            break;
                        case 7:
                            todo1 = new CourseTodo(todo.get("content").getAsString(), calendarDate, todo.get("startHour").getAsInt(), todo.get("startMinute").getAsInt(), todo.get("endHour").getAsInt(), todo.get("endMinute").getAsInt(), todo.get("detail").getAsString(), todo.get("location").getAsString(), todo.get("lastWeek").getAsInt(), todo.get("teacher").getAsString(), todo.get("tip").getAsString(), todo.get("w").getAsInt(), todo.get("urgency").getAsBoolean(), todo.get("importance").getAsBoolean());
                            todo1.setReminder(remind1);
                            todo1.setMode(mode);
                            father.addChildTodo(todo1);
                            break;
                    }
                }
            }
        }catch (Exception e){}

        return father;
    }

    public void saveTodoList(){
        saveMTodoList();
        ArrayList<ArrayList<Todo>> todos = new ArrayList<>();
        Collection<ArrayList<Todo>> values = todoList.values();
        todos.addAll(values);
        FileWriter writer = null;
        try {
            Gson gson = new Gson();
            writer = new FileWriter("todoList.json",false);
            writer.write(gson.toJson(todos));
        }
        catch (IOException e1){}finally {
            try {
                if(writer != null){
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void loadMTodoList(){
        try{
            JsonParser parser = new JsonParser();
            JsonArray array = (JsonArray) parser.parse(new FileReader("mtodoList.json"));
            for (int i=0;i<array.size();i++){
                JsonArray jsonArray = array.get(i).getAsJsonArray();
                for (int j=0;j<jsonArray.size();j++){
                    JsonObject todo = array.get(i).getAsJsonArray().get(j).getAsJsonObject();
                    JsonObject date = todo.get("date").getAsJsonObject();
                    CalendarDate calendarDate = new CalendarDate(date.get("year").getAsInt(),date.get("month").getAsInt(), date.get("day").getAsInt());
                    int mode = todo.get("mode").getAsInt();
                    JsonArray array1 = todo.get("childTodo").getAsJsonArray();
                    Todo todo1 = new MemoryTodo (todo.get ("content").getAsString (), calendarDate, todo.get ("type").getAsString (), todo.get ("name").getAsString (), todo.get ("urgency").getAsBoolean (), todo.get ("importance").getAsBoolean ());
                    todo1.setMode(mode);
                    addChildTodos(todo1,array1);
                    add (todo1);
                }
            }

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void saveMTodoList(){
        ArrayList<ArrayList<Todo>> todos = new ArrayList<>();
        Collection<ArrayList<Todo>> values = mTodoList.values();
        todos.addAll(values);
        FileWriter writer = null;
        try {
            Gson gson = new Gson();
            writer = new FileWriter("mtodoList.json",false);
            writer.write(gson.toJson(todos));
        }
        catch (IOException e1){}finally {
            try {
                if(writer != null){
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }*/

}
