package hvasoftware.com.thongtindoino;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by HoangVuAnh on 3/7/18.
 */

public class CheckInternet {

    private static Context context;
    private static CheckInternet instance;

    static {
        instance = new CheckInternet();
    }

    private boolean connected;
    private ConnectivityManager connectivityManager;
    private NetworkInfo mobileInfo;
    private NetworkInfo wifiInfo;

    public CheckInternet() {
        this.connected = false;
    }

    public static CheckInternet getInstance(Context ctx) {
        context = ctx.getApplicationContext();
        return instance;
    }

    public void showData(Context context) {
        if (!isOnline()) {
            Toast.makeText(context, R.string.you_offline, Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isOnline() {
        try {
            this.connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            assert this.connectivityManager != null;
            NetworkInfo networkInfo = this.connectivityManager.getActiveNetworkInfo();
            boolean z = networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
            this.connected = z;
            return this.connected;
        } catch (Exception e) {
            Log.v("connectivity", e.toString());
            return this.connected;
        }
    }
}
