package hvasoftware.com.thongtindoino.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;

import org.joda.time.Days;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

/**
 * Created by HoangVuAnh on 3/7/18.
 */

public class Utils {

    public static String getRandomUUID() {
        return UUID.randomUUID().toString();
    }

    public static Date getCurrentDateTime() {
        return new Date();
    }

    public static void setUpProgressBar(ProgressBar progressBar, boolean isGone) {
        progressBar.getIndeterminateDrawable().setColorFilter(Utils.getRandomColor(), PorterDuff.Mode.SRC_IN);
        if (isGone) {
            progressBar.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    public static String formatCurrency(long number) {
        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.getDefault());
        format.setMaximumFractionDigits(0);
        String currency = format.format(number);
        //  System.out.println("Currency in VietNam : " + currency);
        return currency;
    }

    public static int getRandomColor() {
        return Color.rgb(new Random().nextInt(255), new Random().nextInt(255), new Random().nextInt(255));
    }

    public static void HideSoftKeyboard(Context context, View view) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
            assert inputMethodManager != null;
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Date parseStringToDate(String stringDate) {
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = dateFormat.parse(stringDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String parseDateToString(Date date) {
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String stringDate = null;
        stringDate = dateFormat.format(date);
        return stringDate;
    }

    public static int daysBetween(Date d1, Date d2) {
        return (int) ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
    }
    public final static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
    /**
     *
     * @param Created_date_String
     * @param Expire_date_String
     * @return
     * public static int get_count_of_days(String Created_date_String, String Expire_date_String) {
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    Date Created_convertedDate = null;
    Date Expire_CovertedDate = null;
    Date todayWithZeroTime = null;
    try {
    Created_convertedDate = dateFormat.parse(Created_date_String);
    Expire_CovertedDate = dateFormat.parse(Expire_date_String);

    Date today = new Date();

    todayWithZeroTime = dateFormat.parse(dateFormat.format(today));
    } catch (ParseException e) {
    e.printStackTrace();
    }

    int c_year = 0, c_month = 0, c_day = 0;
    // 3550
    if (Created_convertedDate.after(todayWithZeroTime)) {
    Calendar c_cal = Calendar.getInstance();
    c_cal.setTime(Created_convertedDate);
    c_year = c_cal.get(Calendar.YEAR);
    c_month = c_cal.get(Calendar.MONTH);
    c_day = c_cal.get(Calendar.DAY_OF_MONTH);

    } else {
    Calendar c_cal = Calendar.getInstance();
    c_cal.setTime(todayWithZeroTime);
    c_year = c_cal.get(Calendar.YEAR);
    c_month = c_cal.get(Calendar.MONTH);
    c_day = c_cal.get(Calendar.DAY_OF_MONTH);
    }


    Calendar e_cal = Calendar.getInstance();
    e_cal.setTime(Expire_CovertedDate);

    int e_year = e_cal.get(Calendar.YEAR);
    int e_month = e_cal.get(Calendar.MONTH);
    int e_day = e_cal.get(Calendar.DAY_OF_MONTH);

    Calendar date1 = Calendar.getInstance();
    Calendar date2 = Calendar.getInstance();

    date1.clear();
    date1.set(c_year, c_month, c_day);
    date2.clear();
    date2.set(e_year, e_month, e_day);

    long diff = date2.getTimeInMillis() - date1.getTimeInMillis();
    float dayCount = (float) diff / (24 * 60 * 60 * 1000);
    return (int) dayCount;
    }
     */

}
