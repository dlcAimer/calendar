package calendar;

import javax.swing.*;
import java.io.Serializable;
import java.util.*;
import java.util.Timer;

public class Todo implements Serializable {
    private int flag;
    //otherTodo 1;dateTodo 2;meetTodo 3;tripTodo 4;memoryTodo 5;viewTodo 6;courseTodo 7
    private String content;
    private boolean usingInterval;
    private CalendarDate date;
    private CalendarInterval interval;
    private boolean urgency;
    private boolean importance;
    private List<Todo> childTodo = new ArrayList<> ();
    private boolean hasParent = false;
    //这两个参数取值详情见Constants
    private Remind reminder;
    private int mode;

    public Todo(){}

    //用于创建永久事件
    public Todo(int flag,String content,boolean urgency,boolean importance) throws IntervalException {
        this.flag = flag;
        usingInterval = false;
        date = null;
        interval = null;
        reminder=null;
        mode = 1;
        this.content = content;
        this.urgency = urgency;
        this.importance = importance;
    }

    public Todo(int flag,String content, CalendarDate date,boolean urgency,boolean importance) throws IntervalException {
        this.interval = new CalendarInterval (date.getYear (), date.getMonth () - 1, date.getDay (), 0, 0, date.getYear (), date.getMonth () - 1, date.getDay (), 23, 59);
        this.flag = flag;
        usingInterval=false;
        reminder=null;
        mode = 0;
        this.content=content;
        this.date=date;
        this.urgency = urgency;
        this.importance = importance;
    }

    public Todo(int flag,String content, CalendarInterval interval,boolean urgency,boolean importance) throws IntervalException {
        this.flag = flag;
        usingInterval=true;
        date=null;
        reminder=null;
        mode = 0;
        this.content=content;
        this.interval=interval;
        this.urgency = urgency;
        this.importance = importance;
    }

    public int getFlag() {
        return flag;
    }

    public List<Todo> getChildTodo() {
        return childTodo;
    }

    public void addChildTodo(Todo child){
        childTodo.add (child);

    }

    public void deleteChildTodo(Todo child){

        if(childTodo.size () == 0){
            return;
        }
        for (Todo temp:childTodo) {
            if(temp.equals (child)){
                childTodo.remove (temp);
                return;
            }
        }
    }

    public boolean getHasParent(){
        return hasParent;
    }

    public void changeHasParent(boolean hasParent){
        this.hasParent = hasParent;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isUsingInterval() {
        return usingInterval;
    }

    public CalendarDate getDate() {
        return this.date;
    }

    public void setDate(CalendarDate date) {
        this.date = date;
    }

    public CalendarInterval getInterval() {
        return interval;
    }

    public void setInterval(CalendarInterval interval){this.interval = interval;}

    public boolean getUrgency(){
        return urgency;
    }

    public boolean getImportance(){
        return importance;
    }

    public Remind getReminder() {
        return reminder;
    }

    public void setReminder(Remind reminder) {
        this.reminder = reminder;
        //reminder.startRemind(this, disp);
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public String getTypeName() {
        switch (flag) {
            case 1:
                return "自定义";
            case 2:
                return "约会";
            case 3:
                return "会议";
            case 4:
                return "旅程";
            case 5:
                return "纪念日";
            case 6:
                return "面试";
            case 7:
                return "课程";
            default:
                return "???";
        }
    }

    public String toString() {
        if (isUsingInterval()){
            GregorianCalendar start = getInterval().getStartTime();
            GregorianCalendar end = getInterval().getEndTime();
            boolean isStartPM=start.get(Calendar.AM_PM)==1;
            String startHour=isStartPM?(start.get(Calendar.HOUR)+12)+"":start.get(Calendar.HOUR)+"";
            boolean isEndPM=end.get(Calendar.AM_PM)==1;
            String endHour=isEndPM?(end.get(Calendar.HOUR)+12)+"":end.get(Calendar.HOUR)+"";

            return  "<html><div>&nbsp;<span style='background-color:#fedcbd; font-size:8px;'>&emsp;</span>&nbsp;"+
                    (start.get(Calendar.MONTH)+1)+"月"+start.get(Calendar.DATE)+"日"+
                    startHour+"时到"+(end.get(Calendar.MONTH)+1)+"月"+end.get(Calendar.DATE)+"日"+
                    endHour+"时，内容为"+getContent()+"</div></html>";
        }
        else {
            CalendarDate date = getDate();
            return  "<html><div>&nbsp;<span style='background-color:#fedcbd; font-size:8px;'>&emsp;</span>&nbsp;"+
                    date.getMonth()+"月"+date.getDay()+"日，内容为"+
                    getContent()+"</div></html>";
        }
    }

    public void operate(boolean changeToCompleted) throws IntervalException {
        Calendar now = Calendar.getInstance();
        if (this.childTodo.size () != 0) {
            int count = 0;
            for (int i = 0; i < this.childTodo.size (); i++) {
                if (this.childTodo.get (i).getMode () == 2) {
                    count++;
                }
            }
            if (count == this.childTodo.size ()) {
                this.setMode (2);
                return;
            }
        }
        if(this instanceof OtherTodo){
            if (this.getMode() == 1 ) {
                if (childTodo.size() == 0 && changeToCompleted) {
                    this.setMode(2);
                    return;
                } else {
                    int count = 0;
                    for (Todo aChildTodo : childTodo) {
                        if (aChildTodo.getMode() == 2)
                            count++;
                    }
                    if (count == childTodo.size()) {
                        this.setMode(2);
                    }
                    return;
                }
            }
        }
        if (this.isUsingInterval() || this instanceof CourseTodo) {
            GregorianCalendar temp = new GregorianCalendar(now.get(Calendar.YEAR), now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH), now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE));
            if (this.getMode() != 3 && this.getMode() != 2) {
                if (temp.before(this.getInterval().getStartTime())) {
                    this.setMode(0);
                } else if (temp.after(this.getInterval().getEndTime())) {
                    this.setMode(3);
                } else {
                    if (this.getMode() == 0) {
                        this.setMode(1);
                    } else if (this.getMode() == 1 && changeToCompleted) {
                        if (childTodo.size() == 0) {
                            this.setMode(2);
                        } else {
                            int count = 0;
                            for (Todo aChildTodo : childTodo) {
                                if (aChildTodo.getMode() == 2)
                                    count++;
                            }
                            if (count == childTodo.size()) {
                                this.setMode(2);
                            }
                        }
                    }
                }
            }
        } else {
            CalendarDate temp = new CalendarDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1,
                    now.get(Calendar.DAY_OF_MONTH));
            if (this.getMode() != 3 && this.getMode() != 2) {
                if (temp.before(this.getDate())) {
                    this.setMode(0);
                } else if (temp.after(this.getDate())) {
                    this.setMode(3);
                } else {
                    if (this.getMode() == 0) {
                        this.setMode(1);
                    } else if (this.getMode() == 1 && changeToCompleted) {
                        if (childTodo.size() == 0) {
                            this.setMode(2);
                        } else {
                            int count = 0;
                            for (Todo aChildTodo : childTodo) {
                                if (aChildTodo.getMode() == 2)
                                    count++;
                            }
                            if (count == childTodo.size()) {
                                this.setMode(2);

                            }
                        }
                    }
                }
            }
        }
    }



    //用于维护完成状态
    public void stateMaintenance(boolean changeToCompleted) throws IntervalException {
        operate(changeToCompleted);
    }
}
