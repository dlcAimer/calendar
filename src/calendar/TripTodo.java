package calendar;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by ThinkPad on 2018/5/26.
 */
public class TripTodo extends Todo {
    //content作为备注出现
    private String traffic;
    private String location;
    private String trainNum;

    public TripTodo(String content, CalendarDate date, String traffic, String location,String trainNum,boolean urgency,boolean importance) throws IntervalException {
        super(4,content,date,urgency,importance);
        this.traffic = traffic;
        this.location = location;
        this.trainNum = trainNum;
    }

    public TripTodo(String content, CalendarInterval interval, String traffic, String location, String trainNum, boolean urgency, boolean importance) throws IntervalException {
        super(4,content,interval,urgency,importance);
        this.traffic = traffic;
        this.location = location;
        this.trainNum = trainNum;
    }

    TripTodo(){}

    public String getTraffic(){return this.traffic;}
    public String getLocation(){return this.location;}
    public String getTrainNum(){return this.trainNum;}

    public String toString(){
        if (super.isUsingInterval()){
            GregorianCalendar start = super.getInterval().getStartTime();
            GregorianCalendar end = super.getInterval().getEndTime();

            boolean isStartPM=start.get(Calendar.AM_PM)==1;
            String startHour=isStartPM?(start.get(Calendar.HOUR)+12)+"":start.get(Calendar.HOUR)+"";
            boolean isEndPM=end.get(Calendar.AM_PM)==1;
            String endHour=isEndPM?(end.get(Calendar.HOUR)+12)+"":end.get(Calendar.HOUR)+"";

            return  "<html><div>&nbsp;<span style='background-color:#426ab3; font-size:8px;'>&emsp;</span>&nbsp;"+
                    (start.get(Calendar.MONTH)+1)+"月"+start.get(Calendar.DATE)+"日"+
                    startHour+"时到"+(end.get(Calendar.MONTH)+1)+"月"+end.get(Calendar.DATE)+"日"+
                    endHour+"时乘坐"+getTrainNum ()+getTraffic ()+"去"+getLocation()+"旅行 备注:"+super.getContent()+"</div></html>";
        }
        else {
            CalendarDate date = super.getDate();
            return "<html><div>&nbsp;<span style='background-color:#b7ba6b; font-size:8px;'>&emsp;</span>&nbsp;" +
                    date.getMonth()+"月"+date.getDay()+"日乘坐"+getTrainNum ()+getTraffic ()+"去"+getLocation()+"旅行 备注:"+super.getContent()+"</div></html>";
        }
    }

    @Override
    public boolean equals(Object object){
        if (object instanceof TripTodo){
            TripTodo todo = (TripTodo)object;
            if (getContent().equals(todo.getContent())&& isUsingInterval()&& todo.isUsingInterval()
                    && getInterval().equals(todo.getInterval()) && getLocation().equals(todo.getLocation())
                    && getTraffic ().equals(todo.getTraffic ()) && getTrainNum ().equals (todo.getTrainNum ())) return true;
            else if (getContent().equals(todo.getContent()) && !isUsingInterval() && !todo.isUsingInterval()
                    && getDate().equals(todo.getDate()) && getLocation().equals(todo.getLocation())
                    && getTraffic ().equals(todo.getTrainNum ()) && getTrainNum ().equals (todo.getTrainNum ())) return true;
        }
        return false;
    }
}
