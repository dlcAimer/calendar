package calendar;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by ThinkPad on 2018/5/26.
 */
public class MemoryTodo extends Todo {
    //content作为描述出现,纪念日不能用时间间隔创建！
    private String type;
    private String name;

    public MemoryTodo(String content, CalendarDate date, String type,String name,boolean urgency,boolean importance) throws IntervalException {
        super(5,content,date,urgency,importance);
        GregorianCalendar startTime = new GregorianCalendar(date.getYear(), date.getMonth() - 1, date.getDay(), 0, 0);
        startTime.set(Calendar.AM_PM, Calendar.AM);
        GregorianCalendar endTime = (GregorianCalendar) startTime.clone();
        endTime.add(Calendar.HOUR, 24);
        setInterval(new CalendarInterval(startTime, endTime));

        this.type = type;
        this.name = name;
    }

    MemoryTodo(){}

    public String getType(){return this.type;}
    public String getName(){return this.name;}

    public String toString() {
        CalendarDate date = getDate();
        return "<html><div>&nbsp;<span style='background-color:#ff4500; font-size:8px;'>&emsp;</span>&nbsp;" +
                date.getMonth()+"月"+date.getDay()+"日"+getType ()+" "+getName ()+
                getContent()+"</div></html>";
    }

    @Override
    public boolean equals(Object object){
        if (object instanceof MemoryTodo){
            MemoryTodo todo = (MemoryTodo) object;
            if (getContent().equals(todo.getContent())
                    && getDate().equals(todo.getDate()) && getType ().equals(todo.getType ()) && getName ().equals (todo.getName ())){
                return true;
            }
        }
        return false;
    }
}

