package calendar;/*
* This class provides some utils that may help you to finish this lab.
* getToday() is finished, you can use this method to get the current date.
* The other four methods getDaysInMonth(), isValid(), isFormatted() and isLeapYear() are not finished,
* you should implement them before you use.
*
* */
import java.io.FileReader;
import java.io.Serializable;
import java.util.*;
import java.util.regex.Pattern;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class DateUtil implements Serializable {
    private DateUtil(){
    }

    /**
     * get a CalendarDate instance point to today
     * @return a CalendarDate object
     */
    public static CalendarDate getToday(){
        Calendar calendar = Calendar.getInstance();
        return new CalendarDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH));
    }

    /**
     * get all dates in the same month with given date
     * @param date the given date
     * @return a list of days in a whole month
     */
    public static List<CalendarDate> getDaysInMonth(CalendarDate date){
        if(!isValid(date))
            return null;

        int year=date.getYear();
        int month=date.getMonth();
        int num=getNumOfDaysInMonth(month, year);

        ArrayList<CalendarDate> calendarDates=new ArrayList<>();
        for(int i=0; i<num; i++){
            CalendarDate date1 = new CalendarDate(year, month, i+1);
            calendarDates.add(date1);
        }

        return calendarDates;
    }

    /**
     * Judge whether the input date is valid. For example, 2018-2-31 is not valid
     * @param date the input date
     * @return true if the date is valid, false if the date is not valid.
     */
    public static boolean isValid(CalendarDate date){
        if(date==null)
            return false;

        if(date.getMonth()<1 || date.getMonth()>12){
            return false;
        }

        if(date.getDay()>getNumOfDaysInMonth(date.getMonth(), date.getYear()) || date.getDay()<=0){
            return false;
        }

        return true;
    }

    /**
     * Count days of the month of the year
     * @param month
     * @param year
     * @return the number of days in the month
     */
    public static int getNumOfDaysInMonth(int month, int year) {
        int num = 30;
        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
            num = 31;
        } else if (month == 2) {
            num = isLeapYear(year) ? 29 : 28;
        }
        return num;
    }

    /**
     * Judge whether the input is formatted.
     * For example, 2018/2/1 is not valid and 2018-2-1 is valid.
     * @param dateString
     * @return true if the input is formatted, false if the input is not formatted.
     */
    public static boolean isFormatted(String dateString){
        if(dateString==null)
            return false;
        Pattern pattern=Pattern.compile("^(\\d{1,4})-(\\d{1,2})-(\\d{1,2})$");
        return pattern.matcher(dateString).matches();
    }

    /**
     * Judge whether the input year is a leap year or not.
     * For example, year 2000 is a leap year, and 1900 is not.
     * @param year
     * @return true if the input year is a leap year, false if the input is not.
     */
    public static boolean isLeapYear(int year){
        return year % 400 == 0 || year % 4 == 0 && year % 100 != 0;
    }

    public static int getStartDay(int month, int year) {
        int numDay = 0;
        int day = 1;
        if (year < 2016) {
            for (int i = year + 1; i < 2016; i++) {
                numDay += getTotalNumOfDays(i);
            }
            for (int i = month; i <= 12; i++) {
                numDay += DateUtil.getNumOfDaysInMonth(i, year);
            }
            for (int i = 1; i <= 9; i++) {
                numDay += DateUtil.getNumOfDaysInMonth(i, 2016);
            }
            numDay += 1;
            day = (7 - numDay % 7) % 7;
        } else if (year == 2016 && month <= 10) {
            for (int i = month; i <= 9; i++) {
                numDay += DateUtil.getNumOfDaysInMonth(i, 2016);
            }
            numDay += 1;
            day = (7 - numDay % 7) % 7;
        } else if (year == 2016 && month > 10) {
            for (int i = 10; i <= (month - 1); i++) {
                numDay += DateUtil.getNumOfDaysInMonth(i, 2016);
            }
            numDay -= 1;
            day = numDay % 7;
        } else if (year > 2016) {
            for (int i = year - 1; i > 2016; i--) {
                numDay += getTotalNumOfDays(i);
            }
            for (int i = 1; i < month; i++) {
                numDay += DateUtil.getNumOfDaysInMonth(i, year);
            }
            for (int i = 10; i <= 12; i++) {
                numDay += DateUtil.getNumOfDaysInMonth(i, 2016);
            }
            numDay -= 1;
            day = numDay % 7;
        }
        return day;
    }

    public static int getTotalNumOfDays(int year) {
        return DateUtil.isLeapYear(year) ? 366 : 365;
    }

    public static boolean isValidInterval(GregorianCalendar start, GregorianCalendar end){
        if(start.after(end))
            return false;
        return true;
    }

    public static String calendarToString(Calendar date){
        boolean isPM=date.get(Calendar.AM_PM)==1;
        boolean isMid=date.get(Calendar.HOUR)==0;
        return  date.get(Calendar.YEAR)+"-"+(date.get(Calendar.MONTH)+1)+"-"+date.get(Calendar.DATE)+
                " "+(isMid&&isPM?"12":date.get(Calendar.HOUR))+":"+date.get(Calendar.MINUTE)+
                " "+(isPM?"PM":"AM");
    }

    public static boolean compareDate(String first, String second){
        String[] firstString = first.split("-");
        String[] secondString = second.split("-");
        if (firstString.length==3 && secondString.length==3){
            int firstYear = Integer.parseInt(firstString[0]);
            int firstMonth = Integer.parseInt(firstString[1]);
            int firstDay = Integer.parseInt(firstString[2]);
            int secondYear = Integer.parseInt(secondString[0]);
            int secondMonth = Integer.parseInt(secondString[1]);
            int secondDay = Integer.parseInt(secondString[2]);
            if (firstYear > secondYear) return false;
            else if (firstYear == secondYear){
                if (firstMonth > secondMonth) return false;
                else if (firstMonth == secondMonth) {
                    if (firstDay > secondDay) return false;
                }
            }
        }
        return true;

    }
}

