package calendar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class CalendarInterval implements Serializable {
    private GregorianCalendar startTime;
    private GregorianCalendar endTime;

    public CalendarInterval(){}

    public CalendarInterval(int startYear, int startMonth, int startDay, int startHr, int startMin,
                            int endYear, int endMonth, int endDay, int endHr, int endMin) throws IntervalException {
        if (!DateUtil.isValid(new CalendarDate(startYear, startMonth + 1, startDay)))
            throw new IntervalException();
        if (!DateUtil.isValid(new CalendarDate(endYear, endMonth + 1, endDay)))
            throw new IntervalException();

        if(startHr>24||startHr<0||endHr>24||endHr<0)
            throw new IntervalException();
        if(startMin>=60||startMin<0||endMin>=60||endMin<0)
            throw new IntervalException();

        GregorianCalendar startTime=new GregorianCalendar(startYear, startMonth, startDay, startHr, startMin);
        GregorianCalendar endTime=new GregorianCalendar(endYear, endMonth, endDay, endHr, endMin);
        if(!DateUtil.isValidInterval(startTime, endTime))
            throw new IntervalException();
        this.startTime=startTime;
        this.endTime=endTime;
    }

    public CalendarInterval(GregorianCalendar startTime, GregorianCalendar endTime) throws IntervalException {
        if(!DateUtil.isValidInterval(startTime, endTime))
            throw new IntervalException();
        this.startTime=startTime;
        this.endTime=endTime;
    }

    public GregorianCalendar getStartTime() {
        return startTime;
    }

    public void setStartTime(GregorianCalendar startTime) throws IntervalException {
        if(!DateUtil.isValidInterval(startTime, this.endTime))
            throw new IntervalException();
        this.startTime = startTime;
    }

    public GregorianCalendar getEndTime() {
        return endTime;
    }

    public void setEndTime(GregorianCalendar endTime) throws IntervalException {
        if(!DateUtil.isValidInterval(this.startTime, endTime))
            throw new IntervalException();
        this.endTime = endTime;
    }

    public boolean isInclude(CalendarInterval interval){
        return DateUtil.isValidInterval (this.startTime, interval.startTime) && DateUtil.isValidInterval (interval.endTime, this.endTime);
    }

    public boolean isOverlapped(CalendarDate date){
        GregorianCalendar dateStartTime=new GregorianCalendar(date.getYear(), date.getMonth()-1, date.getDay(), 0, 0);
        GregorianCalendar dateEndTime=new GregorianCalendar(date.getYear(), date.getMonth()-1, date.getDay(), 24, 0);
        try {
            CalendarInterval interval=new CalendarInterval(dateStartTime, dateEndTime);
            return isOverlapped(interval);
        } catch (IntervalException e) {
            System.out.println(e.getMessage());;
        }

        return false;
    }

    public boolean isOverlapped(CalendarInterval interval){
        if(interval.startTime.after(endTime) || interval.endTime.before(startTime))
            return false;
        return true;
    }
    @Override
    public boolean equals(Object object){
        if (object instanceof CalendarInterval){
            CalendarInterval obj = (CalendarInterval)object;
            if (startTime.equals(obj.getStartTime()) && endTime.equals(obj.getEndTime())) return true;
        }
        return false;
    }
}
