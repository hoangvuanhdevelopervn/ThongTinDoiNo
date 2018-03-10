package hvasoftware.com.thongtindoino.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.Date;
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

    public static void HideSoftKeyboard(Context context, View view)
    {
        try
        {
            InputMethodManager inputMethodManager =
                    (InputMethodManager) context.getSystemService(
                            Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        catch (Exception e)
        {
        }
    }
}
