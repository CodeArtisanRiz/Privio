package com.winkbr.browser.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.winkbr.browser.R;
import com.winkbr.browser.activity.TabActivity;

public class WebViewActivity extends AppCompatActivity {




    WebView webView;
    SwipeRefreshLayout refreshLayout;
    String url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);



        url = getIntent().getStringExtra("url");

        refreshLayout = findViewById(R.id.swipe_refresh);

        refreshLayout.setRefreshing(true);

        refreshLayout.setColorSchemeResources(
                R.color.colorAccent,
                R.color.colorGreen,
                R.color.colorBlue,
                R.color.colorOrange);

//        webView = findViewById(R.id.webView);
//        webView.getSettings().setJavaScriptEnabled(true);
//
//        webView.getSettings().setBuiltInZoomControls(true);
//        webView.getSettings().setDisplayZoomControls(true);
//        webView.setWebViewClient(new WebViewClient());
//        webView.loadUrl(url);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                webView.loadUrl("https://www.google.in");
                Toast.makeText(WebViewActivity.this, "Refreshing", Toast.LENGTH_SHORT).show();
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress == 100) {
                    refreshLayout.setRefreshing(false);
                } else {
                    refreshLayout.setRefreshing(true);
                }
            }
        });


//        FloatingActionButton fab = findViewById(R.id.fab_share);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent shareIntent = new Intent(Intent.ACTION_SEND);
//                shareIntent.setType("text/plain");
//                shareIntent.putExtra(Intent.EXTRA_TEXT, url);
//                startActivity(Intent.createChooser(shareIntent, "Share link using"));
//            }
//        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        getCacheDir().deleteOnExit();
//        webView.clearCache(true);
    }
}
