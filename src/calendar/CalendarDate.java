package calendar;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileReader;
import java.io.Serializable;

/**
 * We have finished part of this class yet, you should finish the rest.
 * 1. A constructor that can return a CalendarDate object through the given string.
 * 2. A method named getDayOfWeek() that can get the index of a day in a week.
 */

public class CalendarDate implements Serializable {
    private int year;
    private int month;
    private int day;
    private String festival = "";
    private boolean isWorkday;
    private boolean isHoliday;



    public CalendarDate(int year, int month, int day){
        this.year = year;
        this.month = month;
        this.day = day;
        JsonArray holidayArray = new JsonArray();
        JsonArray workdayArray = new JsonArray();
        try{
            JsonParser parser = new JsonParser();
            JsonObject object = (JsonObject) parser.parse(new FileReader("holiday.json"));
            holidayArray = object.get("holiday").getAsJsonArray();
            workdayArray = object.get("workday").getAsJsonArray();
        }catch (Exception e){
            //System.out.println("Exception!");
        }
        for (int j=0;j<holidayArray.size(); j++){
            if (this.toString().compareTo(holidayArray.get(j).getAsJsonObject().get("holiday_time").getAsString())==0){
                this.setFestival(holidayArray.get(j).getAsJsonObject().get("zh_name").getAsString());
                this.setIsHoliday(true);
            }
            else if (DateUtil.compareDate(holidayArray.get(j).getAsJsonObject().get("start_time").getAsString(),
                    this.toString()) && DateUtil.compareDate(this.toString(),holidayArray.get(j).getAsJsonObject()
                    .get("end_time").getAsString())){
                this.setIsHoliday(true);
            }
        }
        if (this.getFestival().length() == 0){
            for (int j=0;j<workdayArray.size();j++){
                if (this.toString().compareTo(workdayArray.get(j).getAsString()) == 0){
                    this.setIsWorkday(true);
                }
            }
        }
    }

    /**
     * a constructor that can return a CalendarDate object through the given string.
     * @param dateString format: 2018-3-18
     */
    public CalendarDate(String dateString) throws FormatException {
        if(DateUtil.isFormatted(dateString)){
            String[] date=dateString.split("-");
            year=Integer.parseInt(date[0]);
            month=Integer.parseInt(date[1]);
            day=Integer.parseInt(date[2]);
        }else{
            throw new FormatException();
        }
        JsonArray holidayArray = new JsonArray();
        JsonArray workdayArray = new JsonArray();
        try{
            JsonParser parser = new JsonParser();
            JsonObject object = (JsonObject) parser.parse(new FileReader("holiday.json"));
            holidayArray = object.get("holiday").getAsJsonArray();
            workdayArray = object.get("workday").getAsJsonArray();
        }catch (Exception e){
            //System.out.println("Exception!");
        }
        for (int j=0;j<holidayArray.size(); j++){
            if (this.toString().compareTo(holidayArray.get(j).getAsJsonObject().get("holiday_time").getAsString())==0){
                this.setFestival(holidayArray.get(j).getAsJsonObject().get("zh_name").getAsString());
                this.setIsHoliday(true);
            }
            else if (DateUtil.compareDate(holidayArray.get(j).getAsJsonObject().get("start_time").getAsString(),
                    this.toString()) && DateUtil.compareDate(this.toString(),holidayArray.get(j).getAsJsonObject()
                    .get("end_time").getAsString())){
                this.setIsHoliday(true);
            }
        }
        if (this.getFestival().length() == 0){
            for (int j=0;j<workdayArray.size();j++){
                if (this.toString().compareTo(workdayArray.get(j).getAsString()) == 0){
                    this.setIsWorkday(true);
                }
            }
        }
    }

    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    @Override
    public String toString() {
        if (month<10 && day<10) return year+"-0"+month+"-0"+day;
        else if (month<10) return year+"-0"+month+"-"+day;
        else if (day<10) return year+"-"+month+"-0"+day;
        return year+"-"+month+"-"+day;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof CalendarDate){
            if(this.year==((CalendarDate) obj).getYear() && this.month==((CalendarDate) obj).getMonth() && this.day==((CalendarDate) obj).getDay())
                return true;
        }

        return false;
    }

    /**
     * Get index of the day in a week for one date.
     *
     * Don't use the existing implement like Calendar.setTime(),
     * try to implement your own algorithm.
     * @return 1-7, 1 stands for Monday and 7 stands for Sunday
     */
    public int getDayOfWeek(){
        if(DateUtil.isValid(this)) {
            int weekdayRaw = (DateUtil.getStartDay(month, year) + day - 1) % 7;
            if (weekdayRaw == 0) {
                return 7;
            } else {
                return weekdayRaw;
            }
        }else{
            System.out.println("Date invalid");
            return -1;
        }
    }

    public int getWeek(){
        if(DateUtil.isValid(this)){
            return 1+(DateUtil.getStartDay(month, year)+day-1)/7;
        }else{
            System.out.println("Date invalid");
            return -1;
        }
    }

    public boolean before(CalendarDate date){
        if(this.year < date.getYear()){
            return true;
        }else if(this.year > date.getYear()){
            return false;
        }else {
            if(this.month < date.getMonth()){
                return true;
            }else if(this.month > date.getMonth()){
                return false;
            }else {
                if(this.day < date.getDay()){
                    return true;
                } else{
                    return false;
                }
            }
        }
    }

    public boolean after(CalendarDate date){
        if(this.year < date.getYear()){
            return false;
        }else if(this.year > date.getYear()){
            return true;
        }else {
            if(this.month < date.getMonth()){
                return false;
            }else if(this.month > date.getMonth()){
                return true;
            }else {
                if(this.day > date.getDay()){
                    return true;
                } else{
                    return false;
                }
            }
        }
    }

    public void setFestival(String res){
        festival = res;
    }
    public String getFestival(){
        return festival;
    }
    public void setIsHoliday(boolean res){
        isHoliday = res;
    }
    public boolean getIsHoliday(){
        return isHoliday;
    }
    public void setIsWorkday(boolean res){
        isWorkday = res;
    }
    public boolean getIsWorkday(){
        return isWorkday;
    }

}
