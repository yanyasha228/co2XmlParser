package com.example.test.testproj.helpers;

import android.app.Service;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Simple check_internet_connection helper
 *
 *
 *
 *
 * @author Ruslan Zosimov
 * @version 1.0
 */


public class ConnectivityHelper {
    private Context context;

    public ConnectivityHelper(Context context) {
        this.context = context;
    }

    public boolean isConnected() {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Service.CONNECTIVITY_SERVICE);
        if(connectivity!=null){
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info!=null){
                if(info.getState()==NetworkInfo.State.CONNECTED) return true;
            }
        }
        return false;
    }
}
