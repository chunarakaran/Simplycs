package com.example.amita.simplycs.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.amita.simplycs.R;
import com.example.amita.simplycs.utils.Constants;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import java.util.List;

public class WebinarListRecyclerViewAdapter extends RecyclerView.Adapter<WebinarListRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = WebinarListRecyclerViewAdapter.class.getSimpleName();
    Context context;

    List<WebinarListDataAdapter> dataAdapters;


    public WebinarListRecyclerViewAdapter(List<WebinarListDataAdapter> getDataAdapter, Context context){

        super();
        this.dataAdapters = getDataAdapter;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_webinar, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder Viewholder, int position) {

        WebinarListDataAdapter dataAdapterOBJ =  dataAdapters.get(position);


        Viewholder.video_title_label.setText(dataAdapterOBJ.getTitle());
        Viewholder.video_duration_label.setText(dataAdapterOBJ.getDuration());


        /*  initialize the thumbnail image view , we need to pass Developer Key */
        Viewholder.videoThumbnailImageView.initialize(Constants.DEVELOPER_KEY, new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, final YouTubeThumbnailLoader youTubeThumbnailLoader) {
                //when initialization is sucess, set the video id to thumbnail to load
                youTubeThumbnailLoader.setVideo(dataAdapterOBJ.getVideoURL());

                youTubeThumbnailLoader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
                    @Override
                    public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                        //when thumbnail loaded successfully release the thumbnail loader as we are showing thumbnail in adapter
                        youTubeThumbnailLoader.release();
                    }

                    @Override
                    public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {
                        //print or show error when thumbnail load failed
                        Log.e(TAG, "Youtube Thumbnail Error");
                    }
                });
            }

            @Override
            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
                //print or show error when initialization failed
                Log.e(TAG, "Youtube Initialization Failure");

            }
        });



    }

    @Override
    public int getItemCount() {

        return dataAdapters.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView video_title_label,video_duration_label;

        public YouTubeThumbnailView videoThumbnailImageView;

        public ViewHolder(View itemView) {

            super(itemView);

            video_title_label = (TextView) itemView.findViewById(R.id.video_title_label) ;
            video_duration_label = (TextView) itemView.findViewById(R.id.video_duration_label) ;
            videoThumbnailImageView = (YouTubeThumbnailView) itemView.findViewById(R.id.video_thumbnail_image_view) ;

        }
    }

}
