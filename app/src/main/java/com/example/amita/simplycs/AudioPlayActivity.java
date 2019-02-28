package com.example.amita.simplycs;

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
}
