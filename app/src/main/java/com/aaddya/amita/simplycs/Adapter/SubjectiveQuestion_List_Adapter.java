package com.aaddya.amita.simplycs.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aaddya.amita.simplycs.Model.SubCategory_Model_List;
import com.aaddya.amita.simplycs.Model.SubjectiveQues_Model_List;
import com.aaddya.amita.simplycs.R;
import com.android.volley.toolbox.ImageLoader;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import jp.shts.android.library.TriangleLabelView;

public class SubjectiveQuestion_List_Adapter extends RecyclerView.Adapter<SubjectiveQuestion_List_Adapter.ViewHolder> {


    Context context;

    List<SubjectiveQues_Model_List> dataAdapters;


    public SubjectiveQuestion_List_Adapter(List<SubjectiveQues_Model_List> getDataAdapter, Context context){

        super();
        this.dataAdapters = getDataAdapter;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_subjectivequestion, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder Viewholder, int position) {

        SubjectiveQues_Model_List dataAdapterOBJ =  dataAdapters.get(position);

        Viewholder.QuestionTitle.setText(Html.fromHtml(dataAdapterOBJ.getQuestion()));

    }

    @Override
    public int getItemCount() {

        return dataAdapters.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView QuestionTitle;

        public ViewHolder(View itemView) {

            super(itemView);

            QuestionTitle = (TextView) itemView.findViewById(R.id.Question_title) ;

        }
    }

}
