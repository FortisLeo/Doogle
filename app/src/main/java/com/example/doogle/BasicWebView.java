package com.example.doogle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class BasicWebView extends AppCompatActivity {
    WebView webView2;
    String newsUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_web_view);

        webView2 = findViewById(R.id.web_view2);


        WebSettings webSettings = webView2.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webView2.setWebViewClient(new WebViewClient());
        webView2.setWebChromeClient(new WebChromeClient());

        Intent intent3 = getIntent();
        String newsUrl = intent3.getStringExtra("newsUrl");

        webView2.loadUrl(newsUrl);

    }
}