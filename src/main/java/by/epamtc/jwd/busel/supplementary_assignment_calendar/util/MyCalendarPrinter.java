package by.epamtc.jwd.busel.supplementary_assignment_calendar.util;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MyCalendarPrinter {
    private static final int MONTHS_IN_YEAR = 12;
    private static final int DAYS_IN_WEEK = 7;
    private static final int LINES_IN_MONTH = 8;

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
        boolean isPrintable = true;
        String monthString = generateSeparateMonthStringView(cal, isPrintable);
        System.out.println(monthString);
    }

    public void printActualYear(Calendar cal) {
        for (int i = 0; i < MONTHS_IN_YEAR; i++) {
            cal.set(Calendar.MONTH, i);
            printActualMonth(cal);
        }
    }

    public void printActualYear(Calendar cal, int columnQuantity) {
        if (columnQuantity <= 0 || columnQuantity > MONTHS_IN_YEAR) {
            columnQuantity = 1;
        }

        String[] month = generateMonthsStringView(cal);

        int numberOfLinesOfMonths = (MONTHS_IN_YEAR % columnQuantity) != 0
                                    ? MONTHS_IN_YEAR / columnQuantity + 1
                                    : MONTHS_IN_YEAR / columnQuantity;
        int indexOfLastLine = numberOfLinesOfMonths - 1;

        for (int i = 0; i < numberOfLinesOfMonths; i++) {
            if (i != indexOfLastLine) {
                printRowOfMonths(month, i * columnQuantity, (i + 1) * columnQuantity);
            } else {
                printRowOfMonths(month, i * columnQuantity, MONTHS_IN_YEAR);
            }
        }
    }

    private String generateSeparateMonthStringView(Calendar cal, boolean isPrintable) {
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
        if (!isPrintable) {
            deleteExtraNewLineChar(sb);
        }
        return new String(sb);
    }

    private void generateAndAddHeader(StringBuilder builder, Calendar cal) {
        String month = MONTHS_NAMES.get(cal.get(Calendar.MONTH));
        int year = cal.get(Calendar.YEAR);
        String header = String.format("%" + LINE_LENGTH + "s\n%s\n",
                month + CELL_FILLER + year, MONTH_HEADER);
        builder.append(header);
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

    private void deleteExtraNewLineChar(StringBuilder sb) {
        sb.deleteCharAt(sb.length() - 1);
    }

    private String[] generateMonthsStringView(Calendar cal) {
        String[] month = new String[MONTHS_IN_YEAR];
        for (int i = 0; i < month.length; i++) {
            boolean isPrintable = false;
            cal.set(Calendar.MONTH, i);
            month[i] = generateSeparateMonthStringView(cal, isPrintable);
        }
        return month;
    }

    private void printRowOfMonths(String[] month, int startMonth, int endMonth) {
        StringBuilder[] compoundLines = initializeContainerOfCompoundLines();

        for (int i = startMonth; i < endMonth; i++) {
            updateCompoundLinesWithMonthOnes(compoundLines, month[i]);
        }

        StringBuilder strBuilder = new StringBuilder();
        for (StringBuilder compoundLine : compoundLines) {
            strBuilder.append(compoundLine).append("\n");
        }

        System.out.println(new String(strBuilder));
    }

    private void updateCompoundLinesWithMonthOnes(StringBuilder[] compoundLines,
            String strMonth) {
        String[] monthLines = strMonth.split("\n");
        for (int j = 0; j < monthLines.length; j++) {
            compoundLines[j].append(monthLines[j]).append(MONTH_DELIMITER);
        }
        int indexOfLastWeek = compoundLines.length - 1;
        if (monthLines.length < compoundLines.length) {
            compoundLines[indexOfLastWeek]
                    .append(MONTH_RECORD_FILLER)
                    .append(MONTH_DELIMITER);
        }
    }

    private StringBuilder[] initializeContainerOfCompoundLines() {
        StringBuilder[] container = new StringBuilder[LINES_IN_MONTH];
        for (int i = 0; i < container.length; i++) {
            container[i] = new StringBuilder();
        }
        return container;
    }
}
