package com.example.amita.simplycs;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class PDFViewActivity extends AppCompatActivity {

    String Content_id,PDF_Data;

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfview);

        PDF_Data = getIntent().getStringExtra("pdf_Data");

        webView = (WebView)findViewById(R.id.webView);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);

        webView.setWebViewClient(new Callback());

        webView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + PDF_Data);


    }

    private class Callback extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(
                WebView view, String url) {
            return(false);
        }
    }
}
