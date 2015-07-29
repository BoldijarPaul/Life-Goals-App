package com.lifegoals.app.facebookfanpageapi.helper;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Paul on 4/19/2015.
 */
public class DateHelper {

    /**
     * @return the date format used by Facebook Graph Api
     */
    public static SimpleDateFormat getDateFormat() {
        return new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZ");
    }

    private static SimpleDateFormat toStringDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");

    /**
     * This method will parse a date to a string
     *
     * @param date the java.util.Date instance that we want to parse
     * @return a string that will tell how long ago this post was  , ex 5 minutes ago
     */
    public static String dateToString(Date date) {
        if (date == null) return "";
        try {
            long then = date.getTime();
            long now = new Date().getTime();
            long seconds = (now - then) / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            long days = hours / 24;
            String result = null;
            long difference = 0;
            if (days > 0) {
                difference = days;
                result = days + " ";
                result += difference == 1 ? GlobalStrings.MSG_DAY : GlobalStrings.MSG_DAYS;
            } else if (hours > 0) {
                difference = hours;
                result = hours + " ";
                result += difference == 1 ? GlobalStrings.MSG_HOUR : GlobalStrings.MSG_HOURS;
            } else if (minutes > 0) {
                difference = minutes;
                result = minutes + " ";
                result += difference == 1 ? GlobalStrings.MSG_MINUTE : GlobalStrings.MSG_MINUTES;
            } else {
                difference = seconds;
                result = seconds + " ";
                result += difference == 1 ? GlobalStrings.MSG_SECOND : GlobalStrings.MSG_SECONDS;
            }
            result += " " + GlobalStrings.MSG_AGO;
            return result.toUpperCase();
        } catch (Exception e) {
            return "";
        }
    }


}
