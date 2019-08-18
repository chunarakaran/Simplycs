package com.aaddya.amita.simplycs.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaddya.amita.simplycs.Model.SubCategory_Model_List;
import com.aaddya.amita.simplycs.Model.Theory_Model_List;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.aaddya.amita.simplycs.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.shts.android.library.TriangleLabelView;

public class SubCategory_List_Adapter extends RecyclerView.Adapter<SubCategory_List_Adapter.ViewHolder> {


    Context context;

    List<SubCategory_Model_List> dataAdapters;

    ImageLoader imageLoader;

    public SubCategory_List_Adapter(List<SubCategory_Model_List> getDataAdapter, Context context){

        super();
        this.dataAdapters = getDataAdapter;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_subcategory, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder Viewholder, int position) {

        SubCategory_Model_List dataAdapterOBJ =  dataAdapters.get(position);

        int datasize=dataAdapters.size();

//        if(position==dataAdapters.size()-1){
//            // here goes some code
//            //  callback.sendMessage(Message);
//            Viewholder.triangleLabelView.setVisibility(View.VISIBLE);
//            Viewholder.triangleLabelView.setSecondaryText(Integer.toString(datasize));
//        }
//        else{
//            Viewholder.triangleLabelView.setVisibility(View.GONE);
//        }


        if (dataAdapterOBJ.getImageUrl().isEmpty()) {
            Viewholder.imageView.setImageResource(R.drawable.default_placeholder);
        } else{
            Picasso.with(context)
                    .load(dataAdapterOBJ.getImageUrl())
                    .placeholder(R.drawable.default_placeholder)
                    .error(R.drawable.default_placeholder)
                    .into(Viewholder.imageView);

        }


        Viewholder.ImageTitleTextView.setText(dataAdapterOBJ.getImageTitle());

        if (dataAdapterOBJ.getPaidStatus().equalsIgnoreCase("yes")){

            Viewholder.PremiumView.setVisibility(View.VISIBLE);
        }
        else{
            Viewholder.PremiumView.setVisibility(View.GONE);
        }

//        Viewholder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Viewholder.triangleLabelView.setVisibility(View.GONE);
//            }
//        });

    }

    @Override
    public int getItemCount() {

        return dataAdapters.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView ImageTitleTextView;
        public ImageView imageView,PremiumView;
        public TriangleLabelView triangleLabelView;

        public ViewHolder(View itemView) {

            super(itemView);

            ImageTitleTextView = (TextView) itemView.findViewById(R.id.topic_title) ;
            imageView=(ImageView)itemView.findViewById(R.id.item_image);
            triangleLabelView=(TriangleLabelView)itemView.findViewById(R.id.triangle_view);
            PremiumView=(ImageView)itemView.findViewById(R.id.premium_view);

        }
    }

}
