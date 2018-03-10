package hvasoftware.com.thongtindoino.utils;

import android.app.Activity;
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

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }
}
