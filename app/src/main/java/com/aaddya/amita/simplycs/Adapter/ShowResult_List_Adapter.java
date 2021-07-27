package com.aaddya.amita.simplycs.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aaddya.amita.simplycs.Model.SubjectiveResult_Model_List;
import com.aaddya.amita.simplycs.Model.Test_Model_List;
import com.aaddya.amita.simplycs.R;

import java.util.List;

public class ShowResult_List_Adapter extends RecyclerView.Adapter<ShowResult_List_Adapter.ViewHolder> {


    Context context;

    List<SubjectiveResult_Model_List> dataAdapters;


    public ShowResult_List_Adapter(List<SubjectiveResult_Model_List> getDataAdapter, Context context){

        super();
        this.dataAdapters = getDataAdapter;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_showresult, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder Viewholder, int position) {

        SubjectiveResult_Model_List dataAdapterOBJ =  dataAdapters.get(position);


        Viewholder.Question_title.setText(dataAdapterOBJ.getQuestion());



        if (dataAdapterOBJ.getIs_check().equalsIgnoreCase("1")){

            Viewholder.Question_status.setText("Show Result");

        }
        else{
            Viewholder.Question_status.setText("Pending");
            Viewholder.Question_status.setTextColor(Color.parseColor("#FFA500"));
        }

    }

    @Override
    public int getItemCount() {

        return dataAdapters.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView Question_title;
        public TextView Question_status;


        public ViewHolder(View itemView) {

            super(itemView);

            Question_title = (TextView) itemView.findViewById(R.id.question_title) ;
            Question_status = (TextView) itemView.findViewById(R.id.question_status) ;

        }
    }

}
