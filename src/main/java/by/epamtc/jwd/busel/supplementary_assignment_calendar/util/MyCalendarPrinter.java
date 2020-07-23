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

    private static final String MONTH_RECORD_FILLER;

    static {
        MONTH_RECORD_FILLER = String.format("%" + MONTH_HEADER.length() + "s",
                CELL_FILLER);
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
        int actualMinimumDay = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
        int actualMaximumDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        StringBuilder sb = new StringBuilder();
        generateAndAddHeader(sb, cal);

        String firstOffset = calculateFirstDayOfMonthOffset(dayOfWeek);
        sb.append(firstOffset);

        generateAndAddMonthBody(sb, dayOfWeek, actualMinimumDay, actualMaximumDay);

        String lastOffset = calculateLastDayOfMonthOffset(cal, actualMaximumDay);
        sb.append(lastOffset);

        if (!endsWithLineSeparator(sb)) {
            sb.append('\n');
        }
        System.out.println(new String(sb));

    }

    public void printActualYearOneAfterAnother(Calendar cal) {
        for (int i = 0; i < MONTHS_IN_YEAR; i++) {
            cal.set(Calendar.MONTH, i);
            printActualMonth(cal);
        }
    }

    public void printActualYearInColumns(Calendar cal, int columnQuantity) {
        if (columnQuantity <= 0 || columnQuantity > MONTHS_IN_YEAR) {
            columnQuantity = 1;
        }
        int linesOfMonthsNumber = (MONTHS_IN_YEAR % columnQuantity) != 0
                                  ? MONTHS_IN_YEAR / columnQuantity + 1
                                  : MONTHS_IN_YEAR / columnQuantity;
        int indexOfLastLine = linesOfMonthsNumber - 1;
        for (int i = 0; i < linesOfMonthsNumber; i++) {
            if (i != indexOfLastLine) {
                printLineOfMonths(cal, i * columnQuantity, (i + 1) * columnQuantity);
            } else {
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

    private String calculateLastDayOfMonthOffset(Calendar cal,
            int actualMaximumDay) {
        cal.set(Calendar.DAY_OF_MONTH, actualMaximumDay);
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        int offsetMultiplicator = 0;
        if (--dayOfWeek != 0) {
            offsetMultiplicator = DAYS_IN_WEEK - dayOfWeek;
        }
        int offsetLength = offsetMultiplicator * CELL_LENGTH;
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return (offsetMultiplicator != 0)
               ? String.format("%" + offsetLength + "s", CELL_FILLER)
               : "";
    }

    private boolean endsWithLineSeparator(StringBuilder sb) {
        return sb.lastIndexOf("\n") == (sb.length() - 1);
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
    }

///////////////////////////////////////////////////////////////
// #printActualYearInColumns(Calendar, int) computation methods
///////////////////////////////////////////////////////////////

    private void printLineOfMonths(Calendar cal, int minMonth, int maxMonth) {
        StringBuilder sb = new StringBuilder();

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

        // generate months
        int weeksNumber = 6;
//        int weeksNumber = getMaxWeeksNumber(cal, minMonth, maxMonth);
        StringBuilder[] builders = new StringBuilder[weeksNumber];
        for (int i = 0; i < builders.length; i++) {
            builders[i] = new StringBuilder("\n");
        }

        for (int i = maxMonth - 1; i >= minMonth; i--) {
            cal.set(Calendar.MONTH, i);
            int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
            int actualMinimumDay = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
            int actualMaximumDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

            int pointerToBuildsIndex = 0;

            StringBuilder weekBuilder = new StringBuilder();
            for (int j = actualMinimumDay; j <= actualMaximumDay; j++) {
                if (j == 1) {
                    String firstOffset = calculateFirstDayOfMonthOffset(dayOfWeek);
                    weekBuilder.append(firstOffset);
                }
                weekBuilder.append(DAY_DELIMITER);
                weekBuilder.append(String.format("%2d", j));

                if (j == actualMaximumDay) {
                    String lastOffset = calculateLastDayOfMonthOffset(cal, actualMaximumDay);
                    weekBuilder.append(lastOffset);

                    builders[pointerToBuildsIndex].insert(0, weekBuilder);
                    if (i != minMonth) {
                        builders[pointerToBuildsIndex].insert(0, MONTH_DELIMITER);
                    }

                    if ((pointerToBuildsIndex != (builders.length - 1))
                            && (builders[pointerToBuildsIndex + 1].length() > 1)) {
                        builders[pointerToBuildsIndex + 1].insert(0, MONTH_RECORD_FILLER);
                        if (i != minMonth && builders[pointerToBuildsIndex + 1].length() > 1) {
                            builders[pointerToBuildsIndex + 1].insert(0, MONTH_DELIMITER);
                        }
                    }
                    weekBuilder.delete(0, weekBuilder.length());
                    pointerToBuildsIndex++;
                }

                if ((dayOfWeek++ % DAYS_IN_WEEK) == Calendar.SUNDAY) {
                    builders[pointerToBuildsIndex].insert(0, weekBuilder);
                    if (i != minMonth) {
                        builders[pointerToBuildsIndex].insert(0, MONTH_DELIMITER);
                    }
                    weekBuilder.delete(0, weekBuilder.length());
                    pointerToBuildsIndex++;
                }
            }
        }
        for (StringBuilder builder : builders) {
            sb.append(builder);
        }
        System.out.println(sb.toString());
    }

//    private int getMaxWeeksNumber(Calendar cal, int minMonth, int maxMonth) {
//        int maxWeeksNumber = Integer.MIN_VALUE;
//        for (int i = minMonth; i < maxMonth; i++) {
//            cal.set(Calendar.MONTH, minMonth);
//            int weeksNumber = cal.getActualMaximum(Calendar.WEEK_OF_MONTH);
//            if (weeksNumber > maxWeeksNumber) {
//                maxWeeksNumber = weeksNumber;
//            }
//        }
//        return maxWeeksNumber;
//    }
}
