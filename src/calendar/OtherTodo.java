package calendar;

import java.util.GregorianCalendar;

public class OtherTodo extends Todo{
    //是否为持久性事件
    private boolean persistance;
    private GregorianCalendar completeTime;

    public OtherTodo(String content,boolean urgency,boolean importance) throws IntervalException {
        super(1,content,urgency,importance);
        this.persistance = true;
    }
    public OtherTodo(String content,CalendarDate calendarDate,boolean urgency,boolean importance) throws IntervalException {
        super(1,content,calendarDate,urgency,importance);
        this.persistance = false;
    }
    public OtherTodo(String content,CalendarInterval interval,boolean urgency,boolean importance) throws IntervalException {
        super(1,content,interval,urgency,importance);
        this.persistance = false;
    }

    public boolean getPersistance(){
        return this.persistance;
    }

    public GregorianCalendar getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(GregorianCalendar completeTime) {
        this.completeTime = completeTime;
    }

    @Override
    public boolean equals(Object object){
        if (object instanceof OtherTodo){
            OtherTodo todo = (OtherTodo)object;
            if(!persistance) {
                if (getContent ().equals (todo.getContent ()) && isUsingInterval () && todo.isUsingInterval () && getInterval ().equals (todo.getInterval ()))
                    return true;
                else if (getContent ().equals (todo.getContent ()) && !isUsingInterval () && !todo.isUsingInterval () && getDate ().equals (todo.getDate ()))
                    return true;
            }
            else{
                if(getContent ().equals (todo.getContent ()))
                    return true;
            }
        }
        return false;
    }

    //用于维护完成状态，未设置时间的持久时间需传入完成时间，若还未完成时状态维护则传入null即可
    public void stateMaintenance(boolean changeToCompleted,GregorianCalendar completeTime) throws IntervalException {
        if (this.persistance) {
            if(changeToCompleted){
                this.setMode(2);
                this.completeTime = completeTime;
            }else {
                this.setMode(1);
            }
        } else {
            operate(changeToCompleted);
        }
    }

    @Override
    public void stateMaintenance(boolean changeToCompleted) throws IntervalException {
        stateMaintenance(changeToCompleted, (GregorianCalendar) GregorianCalendar.getInstance());
    }

    @Override
    public String toString() {
        if (persistance) {
            return "<html><div>&nbsp;<span style='background-color:#fedcbd; font-size:8px;'>&emsp;</span>&nbsp;持久性事项，内容为" +
                    getContent() + "</div></html>";
        }
        return super.toString();
    }
}
