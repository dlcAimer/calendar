package calendar;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class DateTodo extends Todo{
    private String location;
    private String people;

    public DateTodo(String content, CalendarDate date, String location, String people,boolean urgency,boolean importance) throws IntervalException {
        super(2,content,date,urgency,importance);
        this.location = location;
        this.people = people;
    }

    public DateTodo(String content, CalendarInterval interval, String location, String people,boolean urgency,boolean importance) throws IntervalException {
        super(2,content,interval,urgency,importance);
        this.location = location;
        this.people = people;
    }

    DateTodo(){}

    public String getLocation(){
        return  location;
    }
    public String getPeople(){
        return people;
    }

    public String toString(){
        if (super.isUsingInterval()){
            GregorianCalendar start = super.getInterval().getStartTime();
            GregorianCalendar end = super.getInterval().getEndTime();

            boolean isStartPM=start.get(Calendar.AM_PM)==1;
            String startHour=isStartPM?(start.get(Calendar.HOUR)+12)+"":start.get(Calendar.HOUR)+"";
            boolean isEndPM=end.get(Calendar.AM_PM)==1;
            String endHour=isEndPM?(end.get(Calendar.HOUR)+12)+"":end.get(Calendar.HOUR)+"";

            return  "<html><div>&nbsp;<span style='background-color:#faa755; font-size:8px;'>&emsp;</span>&nbsp;"+
                    (start.get(Calendar.MONTH)+1)+"月"+start.get(Calendar.DATE)+"日"+
            startHour+"时到"+(end.get(Calendar.MONTH)+1)+"月"+end.get(Calendar.DATE)+"日"+
                    endHour + "时 与" + getPeople() + "约会，内容为" + super.getContent() + "</div></html>";
        }
        else {
            CalendarDate date = super.getDate();
            return  "<html><div>&nbsp;<span style='background-color:#faa755; font-size:8px;'>&emsp;</span>&nbsp;"+
                    date.getMonth()+"月"+date.getDay()+"日与"+getPeople()+"约会，内容为"+
                    super.getContent()+"</div></html>";
        }
    }

    @Override
    public boolean equals(Object object){
        if (object instanceof DateTodo){
            DateTodo todo = (DateTodo)object;
            if (getContent().equals(todo.getContent())&& isUsingInterval()&& todo.isUsingInterval()
                    && getInterval().equals(todo.getInterval()) && getLocation().equals(todo.getLocation())
                    && getPeople().equals(todo.getPeople())) return true;
            else if (getContent().equals(todo.getContent()) && !isUsingInterval() && !todo.isUsingInterval()
                    && getDate().equals(todo.getDate()) && getLocation().equals(todo.getLocation())
                    && getPeople().equals(todo.getPeople())) return true;
        }
        return false;
    }
}
