package hvasoftware.com.thongtindoino.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    public static String formatCurrency(int number) {
        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.getDefault());
        String currency = format.format(number);
        System.out.println("Currency in VietNam : " + currency);
        return currency;
    }

    public static int getRandomColor() {
        return Color.rgb(new Random().nextInt(255), new Random().nextInt(255), new Random().nextInt(255));
    }

    public static void HideSoftKeyboard(Context context, View view) {
        try {
            InputMethodManager inputMethodManager =
                    (InputMethodManager) context.getSystemService(
                            Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e) {
        }
    }

    public static Date parseStringToDate(String stringDate) {
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
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
}
