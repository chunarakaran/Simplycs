package com.example.amita.simplycs;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AudioPlayActivity extends AppCompatActivity {

    String Content_id,audio_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_play);

        audio_url = getIntent().getStringExtra("audio_url");


    }
}
