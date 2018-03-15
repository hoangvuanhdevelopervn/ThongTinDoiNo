package hvasoftware.com.thongtindoino.utils;

import android.annotation.SuppressLint;
import android.content.Context;

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

    public static String formatDatetime(Context mContext, Date date) {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String date1 = df.format(date);
        return date1;
    }

    public static String getCurrentLocal(Context mContext) {
        Locale currentLocal = mContext.getResources().getConfiguration().locale;
        return currentLocal.getDisplayCountry() + " - " + currentLocal.getDisplayName();
    }
}
