package com.winkbr.browser.util;

import android.content.Context;
import android.net.ConnectivityManager;
import androidx.annotation.NonNull;

/**
 * Created by pBrowser on 05-03-2018.
 */

public class Internet {

    /**
     * CHECK WHETHER INTERNET CONNECTION IS AVAILABLE OR NOT
     */
    public static boolean checkConnection(@NonNull Context context) {
        return ((ConnectivityManager) context.getSystemService
                (Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }
}
