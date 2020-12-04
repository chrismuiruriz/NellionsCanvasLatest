package com.nellions.nellionscanvas.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by CHRIS on 10/29/2015.
 * class for internet connection detection
 */

/**
 * This is how to use it
 * <p/>
 * if((new ConnectionDetector(context)).isConnectingToInternet()){
 * Log.d("internet status","Internet Access");
 * }else{
 * Log.d("internet status","no Internet Access");
 * }
 */
public class ConnectionDetector {

    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;
    private final Context _context;

    public ConnectionDetector(Context context) {
        this._context = context;
    }

    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

    public boolean networkConnectivity() {
        int conn = ConnectionDetector.getConnectivityStatus(_context);
        boolean status = false;
        if (conn == ConnectionDetector.TYPE_WIFI) {
            status = true;
        } else if (conn == ConnectionDetector.TYPE_MOBILE) {
            status = true;
        } else if (conn == ConnectionDetector.TYPE_NOT_CONNECTED) {
            status = false;
        }
        return status;
    }
}

