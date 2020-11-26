package fr.univlyon1.m1if.m1if10.gr14.util;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Utility class for input validation management.
 */
public final class StringValidator {

    /**
     * Casts a String to an Integer.
     * Returns null if the String is not valid.
     * Useful to do not handle try/catch in the code.
     * @param number String to cast
     * @return Integer or null
     */
    public static Integer stringToInteger(final String number) {
        try {
            return Integer.parseInt(number);
        }
        catch (Exception e) {
            return null;
        }
    }


    /**
     * casts a String to a Date. Uses the format 'yyyy-MM-dd HH:mm'.
     * Returns null if the date is not valid.
     * @param date Date to cast
     * @return Date or null
     */
    public static Date parseDate(final String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(date);
        }
        catch (Exception e) {
            return null;
        }
    }


    //Final class
    private StringValidator() {
    }
}
