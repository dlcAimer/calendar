package calendar;

import javax.swing.*;
import java.io.Serializable;
import java.util.*;
import java.util.Timer;

public class Remind implements Serializable {
    private GregorianCalendar startRemindTime;

    public int getRemindAhead() {
        return remindAhead;
    }

    public void setRemindAhead(int remindAhead) {
        this.remindAhead = remindAhead;
    }

    private int remindAhead;
    private int remindMode;
    private int remindStrategy;
    private boolean isStopped;
    private Todo todo;
    //private Timer timer;

    public Remind() {
    }

    public Remind(int year, int month, int day, int hour, int min, int mode, int strategy, Todo todo) throws IntervalException {
        if (!DateUtil.isValid(new CalendarDate(year, month, day)))
            throw new IntervalException();
        if (!DateUtil.isValid(new CalendarDate(year, month, day)))
            throw new IntervalException();

        if (hour > 24 || hour < 0 || min >= 60 || min < 0)
            throw new IntervalException();

        GregorianCalendar startTime = new GregorianCalendar(year, month, day, hour, min);
        this.startRemindTime = startTime;
        this.remindMode = mode;
        this.remindStrategy = strategy;
        this.todo = todo;
    }

    public Remind(GregorianCalendar startTime, int mode, int strategy, int remindAhead, Todo todo) {
        //timer = new Timer();
        this.startRemindTime = startTime;
        this.remindMode = mode;
        this.remindStrategy = strategy;
        this.remindAhead = remindAhead;
        this.todo = todo;
    }

    public int getRemindStrategy() {
        return remindStrategy;
    }

    public void setRemindStrategy(int remindStrategy) {
        this.remindStrategy = remindStrategy;
    }

    public int getRemindMode() {
        return remindMode;
    }

    public void setRemindMode(int remindMode) {
        this.remindMode = remindMode;
    }

    public GregorianCalendar getStartRemindTime() {
        return startRemindTime;
    }

    public void setStartRemindTime(GregorianCalendar startRemindTime) {
        this.startRemindTime = startRemindTime;
    }

    public void startRemind(Display disp) {
        //if (todo.getFlag() != 5 && todo.getFlag() != 7) {
            long millis = 0;
            switch (getRemindStrategy()) {
                case Constants.remindEvery5Minutes:
                    millis = 5 * 60 * 1000;
                    break;
                case Constants.remindEvery1Hour:
                    millis = 60 * 60 * 1000;
                    break;
                case Constants.remindEvery1Day:
                    millis = 24 * 60 * 60 * 1000;
                    break;
            }

            long millisDiff = todo.getInterval().getStartTime().getTimeInMillis() - GregorianCalendar.getInstance().getTimeInMillis();
            Date startDate = null;
            if (GregorianCalendar.getInstance().before(getStartRemindTime())) {
                startDate = getStartRemindTime().getTime();
            } else {
                long millisStart = -1 + GregorianCalendar.getInstance().getTimeInMillis() + millisDiff % millis;
                startDate = new Date(millisStart);
            }
            if (millisDiff >= 0) {
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        long millisDiff = todo.getInterval().getStartTime().getTimeInMillis() - GregorianCalendar.getInstance().getTimeInMillis();
                        if (millisDiff < 0 || isStopped) {
                            timer.cancel();
                            return;
                        }
                        long[] dayHrMin = convertMillisToDay(millisDiff);
                        String info = todo.toString().substring(0, todo.toString().length() - 7) + "<div>马上就要开始了，" + dayHrMin[0] + "天" + dayHrMin[1] + "时" + dayHrMin[2] + "分后开始</div></html>";
                        if (getRemindMode() == Constants.areaReminder) {
                            disp.paneAlert(info);
                            disp.todoReminding = todo;
                        } else if (getRemindMode() == Constants.popupReminder) {
                            JOptionPane.showMessageDialog(null, info);
                        } else if (getRemindMode() == Constants.bothReminder) {
                            disp.paneAlert(info);
                            disp.todoReminding = todo;
                            JOptionPane.showMessageDialog(null, info);
                        }
                    }
                }, startDate, millis);
            }
       /* } else if (todo.getFlag() == 7) {
            try {
                Timer timer = new Timer();
                ((CourseTodo) todo).countRealDay().forEach(inter -> {
                    long millis = 0;
                    switch (getRemindStrategy()) {
                        case Constants.remindEvery5Minutes:
                            millis = 5 * 60 * 1000;
                            break;
                        case Constants.remindEvery1Hour:
                            millis = 60 * 60 * 1000;
                            break;
                        case Constants.remindEvery1Day:
                            millis = 24 * 60 * 60 * 1000;
                            break;
                    }

                    GregorianCalendar interStartTime = (GregorianCalendar) inter.getStartTime().clone();
                    switch (remindAhead) {
                        case Constants.hrAhead:
                            interStartTime.add(Calendar.HOUR, -1);
                            break;
                        case Constants.hr5Ahead:
                            interStartTime.add(Calendar.HOUR, -5);
                            break;
                        case Constants.dayAhead:
                            interStartTime.add(Calendar.DATE, -1);
                            break;
                        case Constants.weekAhead:
                            interStartTime.add(Calendar.DATE, -7);
                            break;
                        default:
                            JOptionPane.showMessageDialog(null, "未选择最早提醒时间");
                    }

                    long millisDiff = inter.getStartTime().getTimeInMillis() - GregorianCalendar.getInstance().getTimeInMillis();
                    Date startDate = null;
                    if (GregorianCalendar.getInstance().before(interStartTime)) {
                        startDate = interStartTime.getTime();
                    } else {
                        long millisStart = -1 + GregorianCalendar.getInstance().getTimeInMillis() + millisDiff % millis;
                        startDate = new Date(millisStart);
                    }
                    if (millisDiff >= 0) {
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                long millisDiff = inter.getStartTime().getTimeInMillis() - GregorianCalendar.getInstance().getTimeInMillis();
                                if (millisDiff < 0 || isStopped) {
                                    this.cancel();
                                    timer.purge();
                                }
                                long[] dayHrMin = convertMillisToDay(millisDiff);
                                String info = ((CourseTodo) todo).remindString().substring(0, ((CourseTodo) todo).remindString().length() - 7) + "<div>马上就要开始了，" + dayHrMin[0] + "天" + dayHrMin[1] + "时" + dayHrMin[2] + "分后开始</div></html>";
                                if (getRemindMode() == Constants.areaReminder) {
                                    disp.paneAlert(info);
                                    disp.todoReminding = todo;
                                } else if (getRemindMode() == Constants.popupReminder) {
                                    JOptionPane.showMessageDialog(null, info);
                                } else if (getRemindMode() == Constants.bothReminder) {
                                    disp.paneAlert(info);
                                    disp.todoReminding = todo;
                                    JOptionPane.showMessageDialog(null, info);
                                }
                            }
                        }, startDate, millis);
                    }
                });
            } catch (IntervalException e) {
                System.out.println(e.getMessage());
            }
        }*/
    }

    public void cancelRemind() {
        isStopped = true;
    }

    private long[] convertMillisToDay(long millis) {
        long[] dayHrMin = new long[3];
        dayHrMin[0] = millis / (1000 * 60 * 60 * 24);
        dayHrMin[1] = (millis - dayHrMin[0] * 24 * 60 * 60 * 1000) / (1000 * 60 * 60);
        dayHrMin[2] = (millis - dayHrMin[0] * 24 * 60 * 60 * 1000 - dayHrMin[1] * 1000 * 60 * 60) / (1000 * 60);

        return dayHrMin;
    }
}