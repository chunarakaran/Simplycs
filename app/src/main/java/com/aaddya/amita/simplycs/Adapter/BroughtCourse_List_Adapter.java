package com.aaddya.amita.simplycs.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aaddya.amita.simplycs.Model.Audio_Model_List;
import com.aaddya.amita.simplycs.Model.BroughtCourse_Model_List;
import com.aaddya.amita.simplycs.R;

import java.util.List;

public class BroughtCourse_List_Adapter extends RecyclerView.Adapter<BroughtCourse_List_Adapter.ViewHolder> {


    Context context;

    List<BroughtCourse_Model_List> dataAdapters;


    public BroughtCourse_List_Adapter(List<BroughtCourse_Model_List> getDataAdapter, Context context){

        super();
        this.dataAdapters = getDataAdapter;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_broughtcourse, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder Viewholder, int position) {

        BroughtCourse_Model_List dataAdapterOBJ =  dataAdapters.get(position);


        Viewholder.CourseTitle.setText(dataAdapterOBJ.getTitle());
        Viewholder.FromDate.setText("End Date: " + dataAdapterOBJ.getToDate());
        Viewholder.DaysLeft.setText(dataAdapterOBJ.getDaysLeft());

    }

    @Override
    public int getItemCount() {

        return dataAdapters.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView CourseTitle;
        public TextView FromDate;
        public TextView DaysLeft;


        public ViewHolder(View itemView) {

            super(itemView);

            CourseTitle = (TextView) itemView.findViewById(R.id.Course_Title) ;
            FromDate = (TextView) itemView.findViewById(R.id.FromDate) ;
            DaysLeft = (TextView) itemView.findViewById(R.id.daysLeft) ;

        }
    }

}
