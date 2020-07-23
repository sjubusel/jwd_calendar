package by.epamtc.jwd.busel.supplementary_assignment_calendar;

import by.epamtc.jwd.busel.supplementary_assignment_calendar.util.MyCalendarFactory;
import by.epamtc.jwd.busel.supplementary_assignment_calendar.util.MyCalendarPrinter;

import java.util.Calendar;

public class Main {
    public static void main(String[] args) {
        int month = 6;      // from 0 to 11 inclusively
        int year = 2020;    // any year

        MyCalendarFactory calendarFactory = MyCalendarFactory.getInstance();
        Calendar cal = calendarFactory.getCalendar(year, month);

        MyCalendarPrinter printer = new MyCalendarPrinter();
        printer.printActualMonth(cal);
        System.out.println();
        printer.printActualYearOneAfterAnother(cal);
        System.out.println();
        printer.printActualYearInColumns(cal, 3);
    }
}