package com.aaddya.amita.simplycs.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaddya.amita.simplycs.Model.Chepter_Model_List;
import com.aaddya.amita.simplycs.Model.SubCategory_Model_List;
import com.aaddya.amita.simplycs.R;
import com.android.volley.toolbox.ImageLoader;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.shts.android.library.TriangleLabelView;

public class Chepter_List_Adapter extends RecyclerView.Adapter<Chepter_List_Adapter.ViewHolder> {


    Context context;

    List<Chepter_Model_List> dataAdapters;

    public Chepter_List_Adapter(List<Chepter_Model_List> getDataAdapter, Context context){

        super();
        this.dataAdapters = getDataAdapter;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_chepter, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder Viewholder, int position) {

        Chepter_Model_List dataAdapterOBJ =  dataAdapters.get(position);

        Viewholder.ChapterName.setText(dataAdapterOBJ.getChapterName());
    }
    @Override
    public int getItemCount() {

        return dataAdapters.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView ChapterName;

        public ViewHolder(View itemView) {
            super(itemView);

            ChapterName = (TextView) itemView.findViewById(R.id.chepter_title);

        }
    }

}
