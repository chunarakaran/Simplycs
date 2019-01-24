package com.example.amita.simplycs;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

public class TheoryViewActivity extends AppCompatActivity {

    String Content_id,Content_Data;

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theory_view);



        Content_Data = getIntent().getStringExtra("content_Data");


        webView = (WebView)findViewById(R.id.webView);
        webView.loadData(Content_Data, "text/html", null);

        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        webView.setLongClickable(false);


    }
}
