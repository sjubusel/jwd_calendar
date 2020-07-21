package by.epamtc.jwd.busel.supplementary_assignment_calendar.util;

import java.util.Calendar;
import java.util.GregorianCalendar;

public final class MyCalendarFactory {
    private static final MyCalendarFactory instance = new MyCalendarFactory();

    private final Calendar calendar = new GregorianCalendar();

    private MyCalendarFactory() {
    }

    public static MyCalendarFactory getInstance() {
        return instance;
    }

    public Calendar getCalendar(int year, int month) {
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar;
    }
}
