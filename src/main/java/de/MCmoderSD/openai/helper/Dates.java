package de.MCmoderSD.openai.helper;

import java.util.Calendar;
import java.util.Date;

@SuppressWarnings("ALL")
public class Dates {

    // Knowledge Cutoff Dates
    public final static Date KNOWLEDGE_CUTOFF_2021_09_01 = createDate(2021, Calendar.SEPTEMBER, 1);
    public final static Date KNOWLEDGE_CUTOFF_2023_10_01 = createDate(2023, Calendar.OCTOBER, 1);
    public final static Date KNOWLEDGE_CUTOFF_2023_12_01 = createDate(2023, Calendar.DECEMBER, 1);

    // Create a Date object with the specified year, month, and day
    private static Date createDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
}