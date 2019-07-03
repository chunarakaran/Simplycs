package com.aaddya.amita.simplycs.Activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.aaddya.amita.simplycs.R;
import com.keenfin.audioview.AudioView;
import com.keenfin.audioview.AudioViewListener;

import java.io.IOException;

public class AudioPlayerActivity extends AppCompatActivity  {

    String Content_id,audio_url;

    AudioView audioView;

    public AudioViewListener mAudioViewListener;

    private ProgressDialog pDialog;

    public interface AudioViewListener {
        void onPrepared();
        void onCompletion();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_player);

        audio_url = getIntent().getStringExtra("audio_url");

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        audioView=(AudioView)findViewById(R.id.audioview);


        try {
            audioView.setDataSource(audio_url);
            audioView.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        audioView.setOnAudioViewListener(new com.keenfin.audioview.AudioViewListener() {
            @Override
            public void onPrepared() {
//                pDialog.setMessage("Please Wait...");
//                showDialog();
            }

            @Override
            public void onCompletion() {
//                hideDialog();
            }
        });

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

    public void setOnAudioViewListener(AudioViewListener audioViewListener) {
        mAudioViewListener = audioViewListener;
    }


    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}

