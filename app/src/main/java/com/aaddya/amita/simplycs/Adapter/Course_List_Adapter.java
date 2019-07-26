package com.aaddya.amita.simplycs.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaddya.amita.simplycs.Model.Course_Model_List;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.aaddya.amita.simplycs.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Course_List_Adapter extends RecyclerView.Adapter<Course_List_Adapter.ViewHolder> {


    Context context;

    List<Course_Model_List> dataAdapters;

    public Course_List_Adapter(List<Course_Model_List> getDataAdapter, Context context){

        super();
        this.dataAdapters = getDataAdapter;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_courses, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder Viewholder, int position) {

        Course_Model_List dataAdapterOBJ =  dataAdapters.get(position);



        if (dataAdapterOBJ.getImageUrl().isEmpty()) {
            Viewholder.Item_Image.setImageResource(R.drawable.default_placeholder);
        } else{
            Picasso.with(context)
                    .load(dataAdapterOBJ.getImageUrl())
                    .placeholder(R.drawable.default_placeholder)
                    .error(R.drawable.default_placeholder)
                    .into(Viewholder.Item_Image);

        }

        Viewholder.CourseTitle.setText(dataAdapterOBJ.getCourseTitle());
        Viewholder.CourseDesc.setText(dataAdapterOBJ.getCourseDesc());
        Viewholder.CoursePrice.setText("\u20B9"+dataAdapterOBJ.getCoursePrice());
        Viewholder.CourseDiscount.setText(dataAdapterOBJ.getCourseDiscount()+"% off");
//        Viewholder.CourseHours.setText(dataAdapterOBJ.getCourseHours());

    }

    @Override
    public int getItemCount() {

        return dataAdapters.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView CourseTitle,CourseDesc,CoursePrice,CourseDiscount,CourseHours;
        public ImageView Item_Image;

        public ViewHolder(View itemView) {

            super(itemView);

            Item_Image=(ImageView)itemView.findViewById(R.id.item_image);
            CourseTitle = (TextView) itemView.findViewById(R.id.courseTitle) ;
            CourseDesc = (TextView) itemView.findViewById(R.id.courseDesc) ;
            CoursePrice = (TextView) itemView.findViewById(R.id.coursePrice) ;
            CourseDiscount = (TextView) itemView.findViewById(R.id.courseDiscount) ;
//            CourseHours = (TextView) itemView.findViewById(R.id.courseHours) ;

        }
    }

}
