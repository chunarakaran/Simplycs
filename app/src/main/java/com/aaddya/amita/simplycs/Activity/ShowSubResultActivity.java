package com.aaddya.amita.simplycs.Activity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaddya.amita.simplycs.R;
import com.keenfin.audioview.AudioView;

import java.io.IOException;

public class ShowSubResultActivity extends AppCompatActivity  {

    String result_question,result_audio,result_comment,result_mark;

    AudioView audioView;

    TextView Result_comment,Result_mark,AudioSuggestion;

    public AudioViewListener mAudioViewListener;

    private ProgressDialog pDialog;

    public interface AudioViewListener {
        void onPrepared();
        void onCompletion();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showsubresult);


        result_question = getIntent().getStringExtra("result_question");
        result_audio = getIntent().getStringExtra("result_audio");
        result_comment = getIntent().getStringExtra("result_comment");
        result_mark = getIntent().getStringExtra("result_mark");

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        Initialize();

        if (result_comment.equals("null")){
            Result_comment.setText("No Comment");
        }
        else {
            Result_comment.setText(result_comment);
        }

        Result_mark.setText(result_mark);

        if(result_audio.equals("null")){

            AudioSuggestion.setVisibility(View.VISIBLE);
        }
        else {

            try {
                audioView.setDataSource(result_audio);
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
    }

    public void Initialize()
    {
        audioView=(AudioView)findViewById(R.id.audioview);
        Result_comment=(TextView)findViewById(R.id.result_comment);
        Result_mark=(TextView)findViewById(R.id.result_mark);
        AudioSuggestion=(TextView)findViewById(R.id.AudioSuggestion);

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

