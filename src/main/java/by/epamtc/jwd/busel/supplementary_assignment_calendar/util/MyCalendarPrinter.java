package by.epamtc.jwd.busel.supplementary_assignment_calendar.util;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MyCalendarPrinter {
    private static final int DAYS_IN_WEEK = 7;
    private static final int SUNDAY_MASK = DAYS_IN_WEEK - 1;
    private static final int NOT_SUNDAY_MASK = DAYS_IN_WEEK - 2;
    private static final String DAY_DELIMITER = "  ";
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
            int actualMaximumDay, int actualMinimumDay) {
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

    public void printActualYear(Calendar cal) {
        for (int i = 0; i < Calendar.UNDECIMBER; i++) {
            cal.set(Calendar.MONTH, i);
            printActualMonth(cal);
        }
    }
}
