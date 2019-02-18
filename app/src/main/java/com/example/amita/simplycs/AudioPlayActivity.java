package com.example.amita.simplycs;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class AudioPlayActivity extends AppCompatActivity {

    String Content_id,audio_url;

    private MediaPlayer mediaPlayer;
    private int playbackPosition=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_play);

        audio_url = getIntent().getStringExtra("audio_url");



    }


    public void doClick(View view) {
        switch(view.getId()) {
            case R.id.startPlayerBtn:
                try {
                    playAudio(audio_url);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.pausePlayerBtn:
                if(mediaPlayer != null && mediaPlayer.isPlaying()) {
                    playbackPosition = mediaPlayer.getCurrentPosition();
                    mediaPlayer.pause();
                }
                break;
//            case R.id.restartPlayerBtn:
//                if(mediaPlayer != null && !mediaPlayer.isPlaying()) {
//                    mediaPlayer.seekTo(playbackPosition);
//                    mediaPlayer.start();
//                }
//                break;
//            case R.id.stopPlayerBtn:
//                if(mediaPlayer != null) {
//                    mediaPlayer.stop();
//                    playbackPosition = 0;
//                }
//                break;
        }
    }

    private void playAudio(String url) throws Exception
    {
        killMediaPlayer();

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setDataSource(url);
        mediaPlayer.prepare();
        mediaPlayer.start();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        killMediaPlayer();
    }

    private void killMediaPlayer() {
        if(mediaPlayer!=null) {
            try {
                mediaPlayer.release();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

}
