package grocery;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Date {

    private static final SimpleDateFormat DATE_FORMAT =
        new SimpleDateFormat("yyyyMMdd");

    private final Calendar calendar = DATE_FORMAT.getCalendar();

    public Date(String date) {
        try {
            calendar.setTime(DATE_FORMAT.parse(date));
        }
        catch (ParseException ex) {
            System.out.println(String.format(
                "Invalid date: %s", date));
        }
    }

    public boolean isWeekend() {
        final int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY;
    }

    public void nextDay() {
        calendar.roll(Calendar.DATE, true);
    }

    @Override
    public String toString() {
        return DATE_FORMAT.format(calendar.getTime());
    }
}
