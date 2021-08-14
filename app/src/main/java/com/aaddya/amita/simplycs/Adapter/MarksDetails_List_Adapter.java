package com.aaddya.amita.simplycs.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaddya.amita.simplycs.Model.SubCategory_Model_List;
import com.aaddya.amita.simplycs.Model.SubjectiveMarksDetail_Model_List;
import com.aaddya.amita.simplycs.R;
import com.android.volley.toolbox.ImageLoader;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.shts.android.library.TriangleLabelView;

public class MarksDetails_List_Adapter extends RecyclerView.Adapter<MarksDetails_List_Adapter.ViewHolder> {


    Context context;

    List<SubjectiveMarksDetail_Model_List> dataAdapters;


    public MarksDetails_List_Adapter(List<SubjectiveMarksDetail_Model_List> getDataAdapter, Context context){

        super();
        this.dataAdapters = getDataAdapter;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_marksdetails, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder Viewholder, int position) {

        SubjectiveMarksDetail_Model_List dataAdapterOBJ =  dataAdapters.get(position);

        Viewholder.marks_title.setText(dataAdapterOBJ.getMark_title());
        Viewholder.given_marks.setText(dataAdapterOBJ.getGiven_mark());

    }

    @Override
    public int getItemCount() {

        return dataAdapters.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView marks_title,given_marks;

        public ViewHolder(View itemView) {

            super(itemView);

            marks_title = (TextView) itemView.findViewById(R.id.marks_title) ;
            given_marks = (TextView) itemView.findViewById(R.id.given_marks) ;


        }
    }

}
