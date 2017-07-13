package com.cons.utils;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateUtils {
    public DateUtils() {
        super();
    }

    /**
     * Get a diff between two dates
     * @param date1 the oldest date
     * @param date2 the newest date
     * @param timeUnit the unit in which you want the diff
     * @return the diff value, in the provided unit
     */
    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

    /**
     * Add minutes in a date
     * @param date the canditate date to add specific minutes
     * @param minutesToAdd Number of minutes to add
     * @return the new date with added minutes provided
     */
    public static Date addMinutesToDate(Date date, long minutesToAdd) {
        final long ONE_MINUTE_IN_MILLIS = 60000; //millisecs

        long curTimeInMs = date.getTime();
        Date afterAddingMins = new Date(curTimeInMs + (minutesToAdd * ONE_MINUTE_IN_MILLIS));
        return afterAddingMins;
    }

    public static void main(String[] args) throws InterruptedException {
      //Check specific unit test class written for this class.

    }
}
