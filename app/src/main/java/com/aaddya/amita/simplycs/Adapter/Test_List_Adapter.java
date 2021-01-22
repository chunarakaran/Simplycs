package com.aaddya.amita.simplycs.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aaddya.amita.simplycs.Model.Test_Model_List;
import com.aaddya.amita.simplycs.R;

import java.util.List;

public class Test_List_Adapter extends RecyclerView.Adapter<Test_List_Adapter.ViewHolder> {


    Context context;

    List<Test_Model_List> dataAdapters;


    public Test_List_Adapter(List<Test_Model_List> getDataAdapter, Context context){

        super();
        this.dataAdapters = getDataAdapter;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_test, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder Viewholder, int position) {

        Test_Model_List dataAdapterOBJ =  dataAdapters.get(position);


        Viewholder.TestName.setText(dataAdapterOBJ.getTitle());
        Viewholder.TestDuration.setText(dataAdapterOBJ.getDuration());
        Viewholder.TestMarks.setText(dataAdapterOBJ.getMarks());


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
        public TextView TestDuration;
        public TextView TestMarks;
        public TextView TestStart;


        public ViewHolder(View itemView) {

            super(itemView);

            TestName = (TextView) itemView.findViewById(R.id.test_title) ;
            TestDuration = (TextView) itemView.findViewById(R.id.test_duration) ;
            TestMarks = (TextView) itemView.findViewById(R.id.test_marks) ;
            TestStart = (TextView) itemView.findViewById(R.id.test_start) ;

        }
    }

}
