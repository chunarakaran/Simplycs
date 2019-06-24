package com.aaddya.amita.simplycs.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaddya.amita.simplycs.Model.Category_Model_List;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.aaddya.amita.simplycs.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Category_List_Adapter extends RecyclerView.Adapter<Category_List_Adapter.ViewHolder> {


    Context context;

    List<Category_Model_List> dataAdapters;

    ImageLoader imageLoader;

    public Category_List_Adapter(List<Category_Model_List> getDataAdapter, Context context){

        super();
        this.dataAdapters = getDataAdapter;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_category, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder Viewholder, int position) {

        Category_Model_List dataAdapterOBJ =  dataAdapters.get(position);

        Picasso.with(context)
                .load(dataAdapterOBJ.getImageUrl())
                .placeholder(R.drawable.default_placeholder)
                .into(Viewholder.imageView);

        Viewholder.ImageTitleTextView.setText(dataAdapterOBJ.getImageTitle());

    }

    @Override
    public int getItemCount() {

        return dataAdapters.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView ImageTitleTextView;
        public ImageView imageView;

        public ViewHolder(View itemView) {

            super(itemView);

            ImageTitleTextView = (TextView) itemView.findViewById(R.id.textViewName) ;
            imageView=(ImageView)itemView.findViewById(R.id.item_image);

        }
    }

}
