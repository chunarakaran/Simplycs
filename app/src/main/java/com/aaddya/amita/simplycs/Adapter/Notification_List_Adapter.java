package com.aaddya.amita.simplycs.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aaddya.amita.simplycs.Model.Notification_Model_List;
import com.aaddya.amita.simplycs.R;

import java.util.List;

public class Notification_List_Adapter extends RecyclerView.Adapter<Notification_List_Adapter.ViewHolder> {


    Context context;

    List<Notification_Model_List> dataAdapters;


    public Notification_List_Adapter(List<Notification_Model_List> getDataAdapter, Context context){

        super();
        this.dataAdapters = getDataAdapter;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_notification, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder Viewholder, int position) {

        Notification_Model_List dataAdapterOBJ =  dataAdapters.get(position);


        Viewholder.TitleTextView.setText(dataAdapterOBJ.getTitle());

    }

    @Override
    public int getItemCount() {

        return dataAdapters.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView TitleTextView;


        public ViewHolder(View itemView) {

            super(itemView);

            TitleTextView = (TextView) itemView.findViewById(R.id.notification) ;

        }
    }

}
