package de.MCmoderSD.openai.other;

import java.util.Calendar;
import java.util.Date;

import static java.util.Calendar.*;

@SuppressWarnings("ALL")
public class Dates {

    // Knowledge Cutoff Dates
    public final static Date KNOWLEDGE_CUTOFF_2021_09_01 = createDate(2021, SEPTEMBER, 1);
    public final static Date KNOWLEDGE_CUTOFF_2023_10_01 = createDate(2023, OCTOBER, 1);
    public final static Date KNOWLEDGE_CUTOFF_2023_12_01 = createDate(2023, DECEMBER, 1);
    public final static Date KNOWLEDGE_CUTOFF_2024_06_01 = createDate(2024, JUNE, 1);

    // Create a Date object with the specified year, month, and day
    private static Date createDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, 0, 0, 0);
        calendar.set(MILLISECOND, 0);
        return calendar.getTime();
    }
}