package com.aaddya.amita.simplycs.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aaddya.amita.simplycs.Model.Course_Model_List;
import com.aaddya.amita.simplycs.Model.UserCourse_Model_List;
import com.aaddya.amita.simplycs.R;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

public class User_Course_List_Adapter extends RecyclerView.Adapter<User_Course_List_Adapter.ViewHolder> {


    Context context;

    List<UserCourse_Model_List> dataAdapters;

    ImageLoader imageLoader;

    public User_Course_List_Adapter(List<UserCourse_Model_List> getDataAdapter, Context context){

        super();
        this.dataAdapters = getDataAdapter;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_user_courses, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder Viewholder, int position) {

        UserCourse_Model_List dataAdapterOBJ =  dataAdapters.get(position);



        Viewholder.CourseTitle.setText(dataAdapterOBJ.getUser_courseName());


    }

    @Override
    public int getItemCount() {

        return dataAdapters.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView CourseTitle;


        public ViewHolder(View itemView) {

            super(itemView);

            CourseTitle = (TextView) itemView.findViewById(R.id.user_course);

        }
    }

}
