package net.kisslogo.util;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/**
 * Created by yz1309 on 2015-07-30.
 */
public class NetUtils {
    /**
     * 得到Mac地址
     * @param context
     * @return
     */
    public static String getMac(Context context){
        String res = "未打开Wifi";
        WifiManager wifi = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        if (info.getMacAddress() != null)
            res =info.getMacAddress();

        return res;
    }

}
