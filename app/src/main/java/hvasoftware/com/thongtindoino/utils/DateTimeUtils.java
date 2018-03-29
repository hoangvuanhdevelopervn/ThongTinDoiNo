package hvasoftware.com.thongtindoino.utils;

import android.annotation.SuppressLint;
import android.content.Context;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * Created by HoangVuAnh on 3/11/18.
 */

public class DateTimeUtils {
    private Context mContext;

    public DateTimeUtils(Context mContext) {
        this.mContext = mContext;
    }

    public static Date getDateTime() {
        Date date = new Date();
        String strDateFormat = "dd/MM/yyyy";
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat(strDateFormat);
        simpleDateFormat.format(date);
        return date;
    }

    public static String getDateToday() {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String date = df.format(Calendar.getInstance().getTime());
        return date;
    }

    public static String getDateTodayOneMonthLater() {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date dt = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.DATE, 0);
        c.add(Calendar.MONTH, 1);
        c.add(Calendar.YEAR, 0);
        dt = c.getTime();
        String date = df.format(dt);
        return date;
    }


    public static String formatDatetime(Context mContext, Date date) {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String date1 = df.format(date);
        return date1;
    }

    public static String getCurrentLocal(Context mContext) {
        Locale currentLocal = mContext.getResources().getConfiguration().locale;
        return currentLocal.getDisplayCountry() + " - " + currentLocal.getDisplayName();
    }

    @SuppressLint("SimpleDateFormat")
    public static Timestamp convertStringToTimestamp(String something) {
        SimpleDateFormat dateFormat = null;
        if (something.contains(".")) {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
        }
        if (something.contains(",")) {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss,SSS");
        }
        Timestamp timestamp = null;
        Date parsedDate;
        try {
            assert dateFormat != null;
            parsedDate = dateFormat.parse(something);
            timestamp = new java.sql.Timestamp(parsedDate.getTime());
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return timestamp;
    }
}
