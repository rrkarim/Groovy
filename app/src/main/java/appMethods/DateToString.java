package appMethods;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by YoAtom on 12/14/2016.
 */

public class DateToString {
    public static String convert(Date date) throws ParseException {
        Date currentDate = new Date();
        Calendar c1 = Calendar.getInstance(); c1.setTime(currentDate);
        Calendar c2 = Calendar.getInstance(); c2.setTime(date);

        long serverTime = c1.getTimeInMillis();
        long postTime = c2.getTimeInMillis();

        String returnValue = convertDateToString(serverTime , postTime);
        if(returnValue == null) throw new ParseException("error getting write date", 2163);
        else return returnValue;
    }

    private static String convertDateToString(long serverTime , long postTime) {
        String s;
        long SECOND_IN_MILLISECONDS = 1000;
        long MINUTE_IN_MILLISECONDS = SECOND_IN_MILLISECONDS * 60;
        long HOUR_IN_MILLISECONDS = MINUTE_IN_MILLISECONDS * 60;
        long DAY_IN_MILLISECONDS = HOUR_IN_MILLISECONDS * 24;
        long MONTH_IN_MILLISECONDS = DAY_IN_MILLISECONDS * 30;
        long YEAR_IN_MILLISECOND = MONTH_IN_MILLISECONDS * 12;
        long timeDifference = serverTime - postTime;

        if(timeDifference / YEAR_IN_MILLISECOND != 0) {
            if(timeDifference / YEAR_IN_MILLISECOND == 1) {
                s = "1 year";
            }
            else
                s = Long.toString(timeDifference / YEAR_IN_MILLISECOND) + " years";
            return s;
        }
        if(timeDifference / MONTH_IN_MILLISECONDS != 0) {
            if(timeDifference / MONTH_IN_MILLISECONDS == 1) {
                s = "1 month";
            }
            else
                s = Long.toString(timeDifference / MONTH_IN_MILLISECONDS) + " months";
            return s;
        }
        if(timeDifference / DAY_IN_MILLISECONDS != 0) {
            if(timeDifference / DAY_IN_MILLISECONDS == 1) {
                s = "1 day";
            }
            else
                s = Long.toString(timeDifference / DAY_IN_MILLISECONDS) + " days";
            return s;
        }
        if(timeDifference / HOUR_IN_MILLISECONDS != 0) {
            if(timeDifference / HOUR_IN_MILLISECONDS == 1) {
                s = "1 hour";
            }
            else
                s = Long.toString(timeDifference / HOUR_IN_MILLISECONDS) + " hours";
            return s;
        }
        if(timeDifference / MINUTE_IN_MILLISECONDS != 0) {
            if(timeDifference / MINUTE_IN_MILLISECONDS == 1) {
                s = "1 minute";
            }
            else
                s = Long.toString(timeDifference / MINUTE_IN_MILLISECONDS) + " minutes";
            return s;
        }
        if(timeDifference / SECOND_IN_MILLISECONDS != 0) {
            if(timeDifference / SECOND_IN_MILLISECONDS == 1) {
                s = "1 second";
            }
            else
                s = Long.toString(timeDifference / SECOND_IN_MILLISECONDS) + " seconds";
            return s;
        }
        return null;
    }
}
