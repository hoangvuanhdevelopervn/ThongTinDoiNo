package hvasoftware.com.thongtindoino;

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
}
