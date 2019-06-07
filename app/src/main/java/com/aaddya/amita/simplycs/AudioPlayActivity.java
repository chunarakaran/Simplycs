package com.aaddya.amita.simplycs;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.keenfin.audioview.AudioView;

import java.io.IOException;

public class AudioPlayActivity extends AppCompatActivity  {

    String Content_id,audio_url;

    AudioView audioView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_play);

        audio_url = getIntent().getStringExtra("audio_url");

        audioView=(AudioView)findViewById(R.id.audioview);

        try {
            audioView.setDataSource(audio_url);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onStop() {
        super.onPause();
        audioView.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        audioView.pause();
    }

}

