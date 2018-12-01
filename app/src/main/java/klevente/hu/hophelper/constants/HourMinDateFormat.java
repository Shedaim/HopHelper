package klevente.hu.hophelper.constants;

import java.util.Locale;

public class HourMinDateFormat {

    public static String format(long millis) {
        int seconds = (int) ((millis / 1000) % 60);
        int minutes = (int) ((millis / 1000) / 60);

        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
    }
}
