package com.example.amita.simplycs.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.amita.simplycs.R;

import java.util.List;

/**
 * Created by Juned on 2/8/2017.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    Context context;

    List<DataAdapter> dataAdapters;

    ImageLoader imageLoader;

    public RecyclerViewAdapter(List<DataAdapter> getDataAdapter, Context context){

        super();
        this.dataAdapters = getDataAdapter;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view, parent, false);


        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder Viewholder, int position) {

        DataAdapter dataAdapterOBJ =  dataAdapters.get(position);


        Viewholder.Topic_title.setText(dataAdapterOBJ.getImageTitle());

//        Viewholder.Question_content.setText("From "+dataAdapterOBJ.getFrom_Date()+" To "+dataAdapterOBJ.getTo_Date());
//
//        Viewholder.Status.setText(dataAdapterOBJ.getLeaveStatus());



    }

    @Override
    public int getItemCount() {

        return dataAdapters.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{




        public TextView Topic_title;
        public NetworkImageView VollyImageView;


        public ViewHolder(View itemView) {

            super(itemView);

            Topic_title = (TextView) itemView.findViewById(R.id.topic_title) ;
//            Question_content = (TextView) itemView.findViewById(R.id.Question_content) ;
//            Status = (TextView) itemView.findViewById(R.id.status) ;


        }


    }
}