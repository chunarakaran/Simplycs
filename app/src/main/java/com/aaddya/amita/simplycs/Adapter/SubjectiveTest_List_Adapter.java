package com.aaddya.amita.simplycs.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aaddya.amita.simplycs.Model.SubjectiveTest_Model_List;
import com.aaddya.amita.simplycs.Model.Test_Model_List;
import com.aaddya.amita.simplycs.R;

import java.util.List;

public class SubjectiveTest_List_Adapter extends RecyclerView.Adapter<SubjectiveTest_List_Adapter.ViewHolder> {


    Context context;

    List<SubjectiveTest_Model_List> dataAdapters;


    public SubjectiveTest_List_Adapter(List<SubjectiveTest_Model_List> getDataAdapter, Context context){

        super();
        this.dataAdapters = getDataAdapter;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_subjectivetest, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder Viewholder, int position) {

        SubjectiveTest_Model_List dataAdapterOBJ =  dataAdapters.get(position);


        Viewholder.TestName.setText(dataAdapterOBJ.getTitle());
        Viewholder.TotalQue.setText(dataAdapterOBJ.getTotal_question());

        if (dataAdapterOBJ.getIsComplete().equalsIgnoreCase("1")){

            Viewholder.TestStart.setText("View Result");

        }
        else{
            Viewholder.TestStart.setText("Start");
        }

    }

    @Override
    public int getItemCount() {

        return dataAdapters.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView TestName;
        public TextView TestStart;
        public TextView TotalQue;


        public ViewHolder(View itemView) {

            super(itemView);

            TestName = (TextView) itemView.findViewById(R.id.test_title) ;
            TotalQue = (TextView) itemView.findViewById(R.id.total_que) ;
            TestStart = (TextView) itemView.findViewById(R.id.test_start) ;

        }
    }

}
