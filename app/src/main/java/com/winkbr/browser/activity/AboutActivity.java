package com.winkbr.browser.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.SharedPreferencesKt;
import androidx.preference.PreferenceManager;

import com.winkbr.browser.R;

import java.io.File;

public class AboutActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
//        }
//    }
        //start bck button code

    }
    private void callText(String CopiedText) {

            if (CopiedText.contains("likee")) {
                if (Build.VERSION.SDK_INT >= 23) {
                    Toast.makeText(this, "ss", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "ss", Toast.LENGTH_SHORT).show();
                }
            }

        }

}








//package com.winkbr.browser.activity;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.content.SharedPreferencesKt;
//import androidx.preference.PreferenceManager;
//
//import android.app.backup.SharedPreferencesBackupHelper;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.widget.Toast;
//
//import com.winkbr.browser.R;
//
//public class AboutActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_about);
//        SharedPreferences pref  = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
//
//
//        boolean cookies = pref.getBoolean("accept_cookies", false);
//
//        if(!cookies){
//            Toast.makeText(this, "no cookies for you", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(this, "have a cookie", Toast.LENGTH_SHORT).show();
//        }
//
//        boolean location = pref.getBoolean("location", false);
//
//        if(!location){
//            Toast.makeText(this, "incognito mode", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(this, "You're under surveillance", Toast.LENGTH_SHORT).show();
//        }
//
//    }
//}