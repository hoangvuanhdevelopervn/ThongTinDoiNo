package hvasoftware.com.thongtindoino.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by HoangVuAnh on 3/14/18.
 */

public class PresfUtils {
    private static final String PREF_NAME = "TDDN_PREF";
    private static final String IS_USER_ADMIN = "IsUserAdmin";
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context mContext;
    private int PRIVATE_MODE = 0;

    @SuppressLint("CommitPrefEdits")
    public PresfUtils(Context context) {
        this.mContext = context;
        pref = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public boolean isUserAdmin(String content) {
        return pref.getBoolean(content, true);
    }

    public void setUserAdmin(boolean isFirstTime, String content) {
        editor.putBoolean(content, isFirstTime);
        editor.commit();
        editor.apply();
    }
}
