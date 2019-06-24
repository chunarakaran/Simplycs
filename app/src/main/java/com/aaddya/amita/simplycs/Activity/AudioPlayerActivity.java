package com.aaddya.amita.simplycs.Activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.aaddya.amita.simplycs.R;
import com.keenfin.audioview.AudioView;

import java.io.IOException;

public class AudioPlayerActivity extends AppCompatActivity  {

    String Content_id,audio_url;

    AudioView audioView;

    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_player);

        audio_url = getIntent().getStringExtra("audio_url");

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        audioView=(AudioView)findViewById(R.id.audioview);

//        pDialog.setMessage("Please Wait...");
//        showDialog();

        try {
            audioView.setDataSource(audio_url);
            audioView.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        hideDialog();
    }

//   protected void onStart(){
//        super.onStart();
//        audioView.start();
//        hideDialog();
//   }

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

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}

