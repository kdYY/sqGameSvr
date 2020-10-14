package org.sq.gameDemo.svr.common;

import org.sq.gameDemo.svr.game.task.model.Progress;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DateUtil{

    private static LocalDateTime localDateTime;
    private static long DAY_MILLIS = 86400000L;
    private static long ZONE_OFFSET = TimeZone.getDefault().getOffset(System.currentTimeMillis());

    private static ThreadLocal<SimpleDateFormat> dateFormat;
    private static final Map<String, ThreadLocal<SimpleDateFormat>> dateFormats;

    private static String DEFAULT_FORMAT_MODEL = "yyyy-MM-dd HH:mm";

    static {
        dateFormat = ThreadLocal.withInitial(() -> {return new SimpleDateFormat(DEFAULT_FORMAT_MODEL);});
        dateFormats = new HashMap<>();
    }

    public static long getNow() {
        return LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
    }

    public static int  getMonthDayNum(Long millis) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(millis);
        cal.set(5,1);
        cal.roll(5, -1);
        return cal.get(5);
    }


    public static boolean isToday(long time) {
        return isSameDay(time, System.currentTimeMillis());
    }

    private static boolean isSameDay(long time1, long time2) {
        return isSameDayOfMillis(time1, time2);
    }

    private static boolean isSameDayOfMillis(long time1, long time2) {
        long interval = Math.abs(time1 - time2);
        return interval <  DAY_MILLIS&& toDay(time1) == toDay(time2);
    }

    private static int toDay(long time1) {
        return (int) ((time1 + ZONE_OFFSET) / DAY_MILLIS);
    }



    public static long getTimeStampByString(String date) {
        try {
            return parseDate(date).getTime();
        } catch (Exception e) {
            return 0L;
        }
    }
    public static String getStringByTimeStamp(long time) throws  Exception{
        return toTime(time, DEFAULT_FORMAT_MODEL);
    }


    private static String toTime(long millis, String format) {
        ThreadLocal<SimpleDateFormat> tl = dateFormats.get(format);
        if(tl == null) {
            synchronized (dateFormats) {
                tl = dateFormats.get(format);
                if(tl == null) {
                    tl = ThreadLocal.withInitial(() -> {return new SimpleDateFormat(format);});
                    dateFormats.put(format, tl);
                }
            }
        }
        return tl.get().format(new Date(millis));
    }

    public static Date parseDate(String dateStr) throws ParseException {
        return dateFormat.get().parse(dateStr);
    }


    public static void main(String[] args) {



        String a = "2019-11-20 08:00";
        String a1 = "2019-11-11 2:4";
        String a2 = "2019-11-20 09:00";
        String a3 = "2019-11-11 1:40";
        String a4 = "2019-11-13 1:4";

        List<String> strings = Arrays.asList(a, a1, a2, a3, a4);

        Optional<Long> min = strings.stream()
                .filter(Objects::nonNull)
                .map(DateUtil::getTimeStampByString)
                .min(Comparator.comparingLong(t -> t));

        min.ifPresent(m -> {
            try {
                System.out.println(DateUtil.getStringByTimeStamp(m));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });


    }
}
