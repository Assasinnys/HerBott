package herbott.utils;

public class TimeParser {

    private static final int TO_SEC = 1000;
    private static final int TO_MIN = 60;
    private static final int TO_HOURS = 60;
    private static final int TO_DAYS = 24;

    public static String parse(long lastTime, long currentTime) {
        if ((currentTime - lastTime) < 0) {
            throw new IllegalArgumentException("wrong arguments: difference between vars < 0");
        }
        return parse( currentTime - lastTime);
    }

    public static String parse(long difTime) {
        String sTime = "";
        long sec, min, hours, days, temp = 0;
        sec = difTime / TO_SEC;
        min = sec / TO_MIN;
        hours = min / TO_HOURS;
        days = hours / TO_DAYS;
        if (days > 0) {
            sTime = days + "d ";
            temp = difTime - days * TO_DAYS * TO_HOURS * TO_MIN * TO_SEC;
        }
        if (hours > 0) {
            if (temp > 0) {
                sec = temp / TO_SEC;
                min = sec / TO_MIN;
                hours = min / TO_HOURS;
                sTime += hours + "h ";
                temp = temp - hours * TO_HOURS * TO_MIN * TO_SEC;
            } else {
                sTime = hours + "h ";
                temp = difTime - hours * TO_HOURS * TO_MIN * TO_SEC;
            }
        }
        if (min > 0) {
            if (temp > 0) {
                sec = temp / TO_SEC;
                min = sec / TO_MIN;
                sTime += min + "m ";
                temp = temp - min * TO_MIN * TO_SEC;
            } else {
                sTime = min + "m ";
                temp = difTime - min * TO_MIN * TO_SEC;
            }
        }
        if (sec > 0) {
            if (temp > 0) {
                sec = temp / TO_SEC;
                sTime += sec + "s";
            } else {
                sTime = sec + "s";
            }
        }
        return sTime;
    }
}
