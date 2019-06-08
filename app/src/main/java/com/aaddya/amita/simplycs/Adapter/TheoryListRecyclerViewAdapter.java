package com.aaddya.amita.simplycs.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aaddya.amita.simplycs.R;

import java.util.List;

public class TheoryListRecyclerViewAdapter extends RecyclerView.Adapter<TheoryListRecyclerViewAdapter.ViewHolder> {


    Context context;

    List<TheoryListDataAdapter> dataAdapters;


    public TheoryListRecyclerViewAdapter(List<TheoryListDataAdapter> getDataAdapter, Context context){

        super();
        this.dataAdapters = getDataAdapter;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_theory, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder Viewholder, int position) {

        TheoryListDataAdapter dataAdapterOBJ =  dataAdapters.get(position);


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

            TitleTextView = (TextView) itemView.findViewById(R.id.content_title) ;

        }
    }

}