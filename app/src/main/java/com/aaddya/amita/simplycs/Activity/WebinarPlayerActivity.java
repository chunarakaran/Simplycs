package com.aaddya.amita.simplycs.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.aaddya.amita.simplycs.R;
import com.aaddya.amita.simplycs.Utils.Constants;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;


public class WebinarPlayerActivity extends YouTubeBaseActivity {
    private static final String TAG = WebinarPlayerActivity.class.getSimpleName();
    private String video_url;
    private YouTubePlayerView youTubePlayerView;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webinar_player);
        //get the video id
        video_url = getIntent().getStringExtra("video_url");
        youTubePlayerView = findViewById(R.id.youtube_player_view);

//        Toast.makeText(getApplicationContext(), "You clicked " + video_url, Toast.LENGTH_SHORT).show();

        initializeYoutubePlayer();
    }

    /**
     * initialize the youtube player
     */
    private void initializeYoutubePlayer() {
        youTubePlayerView.initialize(Constants.DEVELOPER_KEY, new YouTubePlayer.OnInitializedListener() {

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer,
                                                boolean wasRestored) {

                //if initialization success then load the video id to youtube player
                if (!wasRestored) {
                    //set the player style here: like CHROMELESS, MINIMAL, DEFAULT
                    youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);

                    //load the video
                    youTubePlayer.loadVideo(video_url);

                    //OR

                    //cue the video
                    //youTubePlayer.cueVideo(videoID);

                    //if you want when activity start it should be in full screen uncomment below comment
                    //  youTubePlayer.setFullscreen(true);

                    //If you want the video should play automatically then uncomment below comment
                    //  youTubePlayer.play();

                    //If you want to control the full screen event you can uncomment the below code
                    //Tell the player you want to control the fullscreen change
                   /*player.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
                    //Tell the player how to control the change
                    player.setOnFullscreenListener(new YouTubePlayer.OnFullscreenListener() {
                        @Override
                        public void onFullscreen(boolean arg0) {
                            // do full screen stuff here, or don't.
                            Log.e(TAG,"Full screen mode");
                        }
                    });*/

                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider arg0, YouTubeInitializationResult arg1) {
                //print or show error if initialization failed
                Log.e(TAG, "Youtube Player View initialization failed");
            }
        });
    }

}
