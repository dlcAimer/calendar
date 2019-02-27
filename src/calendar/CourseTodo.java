package calendar;

import java.util.ArrayList;
import java.util.Calendar;

public class CourseTodo extends Todo {
    //content作为课程名,只能用日期创建
    private int startHour;
    private int startMinute;
    private int endHour;
    private int endMinute;
    private String detail;
    private String location;
    private int lastWeek;
    private String teacher;
    private String tip;
    private int w;

    public CourseTodo(String content, CalendarDate date,int startHour,int startMinute,
                      int endHour,int endMinute, String detail,String location,int lastWeek,String teacher,
                      String tip, int w,boolean urgency,boolean importance) throws IntervalException {
        super (7,content, date,urgency,importance);
        this.startHour = startHour;
        this.endHour = endHour;
        this.startMinute = startMinute;
        this.endMinute = endMinute;
        this.detail = detail;
        this.location = location;
        this.lastWeek = lastWeek;
        this.teacher = teacher;
        this.tip = tip;
        this.w = w;
    }

    CourseTodo(){}

    public int getStartHour() {
        return startHour;
    }
    public int getEndHour() {
        return endHour;
    }
    public int getStartMinute() {
        return startMinute;
    }
    public int getEndMinute() {
        return endMinute;
    }
    public String getDetail() {
        return detail;
    }
    public String getLocation() {
        return location;
    }
    public int getLastWeek() {
        return lastWeek;
    }
    public String getTeacher() {
        return teacher;
    }
    public String getTip() {
        return tip;
    }
    public int getW() {
        return w;
    }

    //此函数尚未完善，需要补充参数
    public String toString(CalendarDate date){
        return "<html><div>&nbsp;<span style='background-color:#DA70D6; font-size:8px;'>&emsp;</span>&nbsp;" +
                date.getMonth()+"月"+date.getDay()+"日"+getStartHour ()+":"+getStartMinute ()+"-"+
                getEndHour ()+":"+getEndMinute ()+" "+super.getContent ()+" "+getDetail ()+" "+getLocation ()+" 老师:"+
                getTeacher ()+" 备注:"+getTip ()+"</div></html>";
    }

    public String toString() {
        return "<html><div>&nbsp;<span style='background-color:#DA70D6; font-size:8px;'>&emsp;</span>&nbsp;" +
                (getInterval().getStartTime().get(Calendar.MONTH) + 1) + "月" + getInterval().getStartTime().get(Calendar.DATE) + "日" + getStartHour() + ":" + getStartMinute() + "-" +
                getEndHour() + ":" + getEndMinute() + " " + super.getContent() + " " + getDetail() + " " + getLocation() + " 老师:" +
                getTeacher() + " 备注:" + getTip() + "</div></html>";
    }

    public String remindString() {
        return "<html><div>&nbsp;<span style='background-color:#DA70D6; font-size:8px;'>&emsp;</span>&nbsp;" +
                getStartHour() + ":" + getStartMinute() + "-" +
                getEndHour() + ":" + getEndMinute() + " " + super.getContent() + " " + getDetail() + " " + getLocation() + " 老师:" +
                getTeacher() + " 备注:" + getTip() + "</div></html>";
    }

    @Override
    public boolean equals(Object object){
        if (object instanceof CourseTodo){
            CourseTodo todo = (CourseTodo) object;
            if (getContent().equals(todo.getContent()) && getDate().equals(todo.getDate())
                    && getStartHour () == todo.getStartHour () && getStartMinute () == todo.getStartMinute ()
                    && getEndHour() == todo.getEndHour() && getEndMinute() == todo.getEndMinute()
                    && getDetail ().equals(todo.getDetail ()) && getLocation ().equals (todo.getLocation ())
                    && getLastWeek () == todo.getLastWeek () && getTeacher ().equals (todo.getTeacher ())
                    && getTip ().equals (todo.getTip ()) && getW () == todo.getW ()) return true;
        }
        return false;
    }

    public ArrayList<CalendarInterval> countRealDay() throws IntervalException {
        ArrayList<CalendarInterval> course = new ArrayList<>();
        int realDay = 0;
        if(this.getW()<this.getDate().getDayOfWeek()){
            realDay = this.getDate().getDay()+(7-this.getDate().getDayOfWeek())+this.getW();
        }else{
            realDay = this.getDate().getDay()+(this.getW()-this.getDate().getDayOfWeek());
        }
        int yearCount = 0;
        int monthCount = 0;
        int dayCount = 0;
        for(int i =0;i<this.getLastWeek();i++){
            for (int j = 0; j < 2; j++) {
                if ((this.getDate().getMonth() + monthCount) - 12 * yearCount > 12) {
                    yearCount++;
                } else if ((realDay + i * 7) >
                        DateUtil.getNumOfDaysInMonth(this.getDate().getMonth() + monthCount
                                , this.getDate().getYear() + yearCount) + dayCount) {
                    dayCount += DateUtil.getNumOfDaysInMonth(this.getDate().getMonth() + monthCount
                            , this.getDate().getYear() + yearCount);
                    monthCount++;
                }
            }
            int tempYear = this.getDate().getYear() + yearCount;
            int tempMonth = this.getDate().getMonth() - 1 + monthCount - 12 * yearCount;
            int tempDay = (realDay + i * 7) - dayCount;
            CalendarInterval interval = new CalendarInterval(
                    this.getDate().getYear()+yearCount
                    , this.getDate().getMonth() - 1 + monthCount - 12 * yearCount
                    , (realDay + i * 7)-dayCount
                    , this.getStartHour()
                    , this.getStartMinute()
                    , this.getDate().getYear()+yearCount
                    , this.getDate().getMonth() - 1 + monthCount - 12 * yearCount
                    , (realDay + i * 7)-dayCount
                    , this.getEndHour()
                    , this.getEndMinute());
            course.add(interval);
        }
        return course;
    }
}
