package calendar;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by ThinkPad on 2018/5/26.
 */
public class ViewTodo extends Todo {
    //content作为备注
    private String location;
    private String company;
    private String post;

    public ViewTodo(String content, CalendarDate date, String location,String company,String post,boolean urgency,boolean importance) throws IntervalException {
        super(6,content,date,urgency,importance);
        this.location = location;
        this.company = company;
        this.post = post;
    }

    public ViewTodo(String content, CalendarInterval interval, String location, String company, String post, boolean urgency, boolean importance) throws IntervalException {
        super(6,content,interval,urgency,importance);
        this.location = location;
        this.company = company;
        this.post = post;
    }

    ViewTodo(){}

    public String getLocation() {return location;}
    public String getCompany(){return company;}
    public String getPost() {return post;}

    public String toString(){
        if (super.isUsingInterval()){
            GregorianCalendar start = super.getInterval().getStartTime();
            GregorianCalendar end = super.getInterval().getEndTime();

            boolean isStartPM=start.get(Calendar.AM_PM)==1;
            String startHour=isStartPM?(start.get(Calendar.HOUR)+12)+"":start.get(Calendar.HOUR)+"";
            boolean isEndPM=end.get(Calendar.AM_PM)==1;
            String endHour=isEndPM?(end.get(Calendar.HOUR)+12)+"":end.get(Calendar.HOUR)+"";

            return "<html><div>&nbsp;<span style='background-color:#D8BFD8; font-size:8px;'>&emsp;</span>&nbsp;" +
                    (start.get(Calendar.MONTH)+1)+"月"+start.get(Calendar.DATE)+"日"+
                    startHour+"时到"+(end.get(Calendar.MONTH)+1)+"月"+end.get(Calendar.DATE)+"日"+
                    endHour + "时 去" + getLocation() + "面试" + getCompany() + "的" + getPost() + " 备注:" + super.getContent() + "</div></html>";
        }
        else {
            CalendarDate date = super.getDate();
            return "<html><div>&nbsp;<span style='background-color:#D8BFD8; font-size:8px;'>&emsp;</span>&nbsp;" +
                    date.getMonth()+"月"+date.getDay()+"日去"+getLocation ()+"面试"+getCompany ()+"的"+getPost ()+" 备注:"+super.getContent()+"</div></html>";
        }
    }

    @Override
    public boolean equals(Object object){
        if (object instanceof ViewTodo){
            ViewTodo todo = (ViewTodo) object;
            if (getContent().equals(todo.getContent())&& isUsingInterval()&& todo.isUsingInterval()
                    && getInterval().equals(todo.getInterval()) && getLocation().equals(todo.getLocation())
                    && getCompany ().equals(todo.getCompany ()) && getPost ().equals (todo.getPost ())) return true;
            else if (getContent().equals(todo.getContent()) && !isUsingInterval() && !todo.isUsingInterval()
                    && getDate().equals(todo.getDate()) && getLocation().equals(todo.getLocation())
                    && getCompany ().equals(todo.getCompany ()) && getPost ().equals (todo.getPost ())) return true;
        }
        return false;
    }
}
