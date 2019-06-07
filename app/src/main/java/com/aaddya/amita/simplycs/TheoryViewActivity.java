package com.aaddya.amita.simplycs;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.TextView;

public class TheoryViewActivity extends AppCompatActivity {

    String Content_id,content_name,Content_Data;

    TextView Contentname;

    WebView webView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theory_view);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        Content_Data = getIntent().getStringExtra("content_Data");
        content_name = getIntent().getStringExtra("content_name");

        Contentname = (TextView) findViewById(R.id.ContentTitle);
        webView = (WebView)findViewById(R.id.webView);
        webView.loadData(Content_Data, "text/html", null);

        Contentname.setText(content_name);
        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        webView.setLongClickable(false);


    }
}
