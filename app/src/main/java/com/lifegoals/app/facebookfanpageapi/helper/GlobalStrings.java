package com.lifegoals.app.facebookfanpageapi.helper;

import android.content.Context;

import com.lifegoals.app.R;


/**
 * Created by Paul on 4/19/2015.
 */
public class GlobalStrings {

    /* here we are going to store strings that will be used in our whole app
    * even in places that we don't have access to a Context instance */
    public static String MSG_SECOND="second";
    public static String MSG_MINUTE="minute";
    public static String MSG_HOUR="hour";
    public static String MSG_DAY="day";
    public static String MSG_SECONDS="seconds";
    public static String MSG_MINUTES="minutes";
    public static String MSG_HOURS="hours";
    public static String MSG_DAYS="days";
    public static String MSG_AGO="ago";

    public static void initialize(Context context) {
        MSG_SECOND = context.getString(R.string.msg_second);
        MSG_MINUTE = context.getString(R.string.msg_minute);
        MSG_HOUR = context.getString(R.string.msg_hour);
        MSG_DAY = context.getString(R.string.msg_day);
        MSG_SECONDS = context.getString(R.string.msg_seconds);
        MSG_MINUTES = context.getString(R.string.msg_minutes);
        MSG_HOURS = context.getString(R.string.msg_hours);
        MSG_DAYS = context.getString(R.string.msg_days);
        MSG_AGO = context.getString(R.string.msg_ago);
    }
}
