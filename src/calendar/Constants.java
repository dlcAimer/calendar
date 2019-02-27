package calendar;

import java.io.Serializable;

public class Constants implements Serializable {
    public static final int areaReminder = 0;
    public static final int popupReminder = 1;
    public static final int bothReminder = 2;
    public static final int noReminder = 3;

    public static final int remindEvery5Minutes = 0;
    public static final int remindEvery1Hour = 1;
    public static final int remindEvery1Day = 2;

    public static final int notStart = 0;
    public static final int processing = 1;
    public static final int completed = 2;
    public static final int expired = 3;

    public static final int hrAhead = 0;
    public static final int hr5Ahead = 1;
    public static final int dayAhead = 2;
    public static final int weekAhead = 3;
}