package by.epamtc.jwd.busel.supplementary_assignment_calendar.util;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MyCalendarPrinter {
    private static final int MONTHS_IN_YEAR = 12;
    private static final int DAYS_IN_WEEK = 7;
    private static final int SUNDAY_MASK = DAYS_IN_WEEK - 1;
    private static final int NOT_SUNDAY_MASK = DAYS_IN_WEEK - 2;
    private static final String DAY_DELIMITER = "  ";
    private static final String MONTH_DELIMITER = DAY_DELIMITER + DAY_DELIMITER;
    private static final int CELL_LENGTH = DAY_DELIMITER.length() + 2;
    private static final String CELL_FILLER = " ";
    private static final int LINE_LENGTH = DAYS_IN_WEEK * CELL_LENGTH;
    private static final Map<Integer, String> MONTHS_NAMES = new HashMap<>();
    private static final String MONTH_HEADER;

    static {
        MONTH_HEADER = DAY_DELIMITER + "пн" + DAY_DELIMITER + "аў" +
                DAY_DELIMITER + "ср" + DAY_DELIMITER + "чц" +
                DAY_DELIMITER + "пт" + DAY_DELIMITER + "сб" +
                DAY_DELIMITER + "нд";
    }

    public MyCalendarPrinter() {
        MONTHS_NAMES.putIfAbsent(Calendar.JANUARY, "Студзень");
        MONTHS_NAMES.putIfAbsent(Calendar.FEBRUARY, "Люты");
        MONTHS_NAMES.putIfAbsent(Calendar.MARCH, "Сакавiк");
        MONTHS_NAMES.putIfAbsent(Calendar.APRIL, "Красавiк");
        MONTHS_NAMES.putIfAbsent(Calendar.MAY, "Травень");
        MONTHS_NAMES.putIfAbsent(Calendar.JUNE, "Чэрвень");
        MONTHS_NAMES.putIfAbsent(Calendar.JULY, "Лiпень");
        MONTHS_NAMES.putIfAbsent(Calendar.AUGUST, "Жнiвень");
        MONTHS_NAMES.putIfAbsent(Calendar.SEPTEMBER, "Верасень");
        MONTHS_NAMES.putIfAbsent(Calendar.OCTOBER, "Кастрычнiк");
        MONTHS_NAMES.putIfAbsent(Calendar.NOVEMBER, "Лiстапад");
        MONTHS_NAMES.putIfAbsent(Calendar.DECEMBER, "Снежань");
    }

    public void printActualMonth(Calendar cal) {
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        int actualMaximumDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        int actualMinimumDay = cal.getActualMinimum(Calendar.DAY_OF_MONTH);

        StringBuilder sb = new StringBuilder();
        generateAndAddHeader(sb, cal);

        String offset = calculateFirstDayOfMonthOffset(dayOfWeek);
        sb.append(offset);

        generateAndAddMonthBody(sb, dayOfWeek, actualMinimumDay, actualMaximumDay);

        System.out.println(new String(sb));

    }

    public void printActualYearOneAfterAnother(Calendar cal) {
        for (int i = 0; i < Calendar.UNDECIMBER; i++) {
            cal.set(Calendar.MONTH, i);
            printActualMonth(cal);
        }
    }

    public void printActualYearInColumns(Calendar cal, int columnQuantity) {
        int linesOfMonthsNumber = (MONTHS_IN_YEAR % columnQuantity) != 0
                                  ? MONTHS_IN_YEAR / columnQuantity + 1
                                  : MONTHS_IN_YEAR / columnQuantity;

        for (int i = 0; i < linesOfMonthsNumber; i++) {
            int diff = MONTHS_IN_YEAR - (i * columnQuantity);
            if (diff > linesOfMonthsNumber) {
                printLineOfMonths(cal, i * columnQuantity, (i + 1) * columnQuantity);
            }
            else {
                printLineOfMonths(cal, i * columnQuantity, MONTHS_IN_YEAR);
            }
        }

    }

//////////////////////////////////////////////////
// #printActualMonth(Calendar) computation methods
//////////////////////////////////////////////////

    private static String calculateFirstDayOfMonthOffset(int dayOfWeek) {
        int diff = DAYS_IN_WEEK - dayOfWeek;
        int offsetMultiplicator;
        if (diff != SUNDAY_MASK) {
            offsetMultiplicator = NOT_SUNDAY_MASK - (DAYS_IN_WEEK - dayOfWeek);
        } else {
            offsetMultiplicator = diff;
        }
        int offsetLength = offsetMultiplicator * CELL_LENGTH;
        return (offsetMultiplicator != 0)
               ? String.format("%" + offsetLength + "s", CELL_FILLER)
               : "";
    }

    private boolean endsWithLineSeparator(StringBuilder sb) {
        return sb.lastIndexOf("\n") != (sb.length() - 1);
    }

    private void generateAndAddHeader(StringBuilder builder, Calendar cal) {
        String month = MONTHS_NAMES.get(cal.get(Calendar.MONTH));
        int year = cal.get(Calendar.YEAR);
        String header = String.format("%" + LINE_LENGTH + "s\n%s\n",
                month + CELL_FILLER + year, MONTH_HEADER);
        builder.append(header);
    }

    private void generateAndAddMonthBody(StringBuilder sb, int dayOfWeek,
            int actualMinimumDay, int actualMaximumDay) {
        for (int i = actualMinimumDay; i <= actualMaximumDay; i++) {
            sb.append(DAY_DELIMITER);
            sb.append(String.format("%2d", i));
            if ((dayOfWeek++ % DAYS_IN_WEEK) == 1) {
                sb.append('\n');
            }
        }
        if (endsWithLineSeparator(sb)) {
            sb.append('\n');
        }
    }

///////////////////////////////////////////////////////////////
// #printActualYearInColumns(Calendar, int) computation methods
///////////////////////////////////////////////////////////////

    private void printLineOfMonths(Calendar cal, int minMonth, int maxMonth) {
        StringBuilder sb = new StringBuilder();
        int weeksNumber = getMaxWeeksNumber(cal, minMonth, maxMonth);
        // generate 1st line "MONTH_NAME & YEAR"
        String delimiter = "";
        for (int j = minMonth; j < maxMonth; j++) {
            cal.set(Calendar.MONTH, j);
            sb.append(delimiter);
            String month = MONTHS_NAMES.get(cal.get(Calendar.MONTH));
            int year = cal.get(Calendar.YEAR);
            String header = String.format("%" + LINE_LENGTH + "s",
                    month + CELL_FILLER + year);
            sb.append(header);
            delimiter = MONTH_DELIMITER;
        }
        sb.append('\n');

        // generate 2nd line "MONTH_HEADER"
        delimiter = "";
        for (int i = minMonth; i < maxMonth; i++) {
            cal.set(Calendar.MONTH, i);
            sb.append(delimiter);
            sb.append(MONTH_HEADER);
            delimiter = MONTH_DELIMITER;
        }
        sb.append('\n');
        System.out.println(sb.toString());
//        for (int i = 0; i < weeksNumber; i++) {
//
//        }
    }

    private int getMaxWeeksNumber(Calendar cal, int minMonth, int maxMonth) {
        int maxWeeksNumber = Integer.MIN_VALUE;
        for (int i = minMonth; i < maxMonth; i++) {
            cal.set(Calendar.MONTH, minMonth);
            int weeksNumber = cal.get(Calendar.WEEK_OF_MONTH);
            if (weeksNumber > maxWeeksNumber) {
                maxWeeksNumber = weeksNumber;
            }
        }
        return maxWeeksNumber;
    }
}
